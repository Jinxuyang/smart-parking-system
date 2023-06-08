package com.verge.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.verge.parking.common.CommonResponse;
import com.verge.parking.entity.ParkingOrder;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.entity.enums.OrderStatus;
import com.verge.parking.entity.enums.ParkingPlaceStatus;
import com.verge.parking.mapper.ParkingPlaceMapper;
import com.verge.parking.service.IParkingOrderService;
import com.verge.parking.service.IParkingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Verge
 * @since 2023-04-12
 */
@Service
public class ParkingPlaceServiceImpl extends ServiceImpl<ParkingPlaceMapper, ParkingPlace> implements IParkingPlaceService {

    @Autowired
    private IParkingOrderService parkingOrderService;
    private ParkingPlaceMapper parkingPlaceMapper;

    @Override
    public boolean updateLockStatus(String macAddress, boolean isLock) {
        ParkingPlace place = this.getById(macAddress);
        if (place == null) {
            return false;
        }
        place.setLockStatus(isLock);
        return this.updateById(place);
    }

    @Override
    public ParkingPlaceStatus getPlaceStatusById(String macAddress) {
        ParkingPlace place = this.getById(macAddress);
        return place.getStatus();
    }

    @Override
    public void updatePlaceStatusById(String macAddress, ParkingPlaceStatus status) {
        this.update(
            new UpdateWrapper<>(new ParkingPlace())
                    .eq("id", macAddress.toUpperCase())
                    .set("status", status.getValue())
        );
    }

    @Override
    public Map<String, Double> getUsage(int hour) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusHours(hour);
        List<ParkingPlace> places = this .list(new QueryWrapper<>(new ParkingPlace())
                .select("id", "area", "number")
        );
        Map<String, Double> res = new HashMap<>();
        for (ParkingPlace place : places) {
            List<ParkingOrder> orders = parkingOrderService.list(new QueryWrapper<>(new ParkingOrder())
                    .eq("parking_place_id", place.getId())
                    .between("start_time", oneDayAgo, now)
                    .and(i -> i.eq("order_status", OrderStatus.FINISHED.getValue()).or().eq("order_status", OrderStatus.WAITING_PAY.getValue()))
            );
            int cnt = 0;
            for (ParkingOrder order : orders) {
                Duration duration = Duration.between(order.getStartTime(), order.getStopTime());
                cnt += duration.toMinutes();
            }
            Double rate = cnt/(hour * 60.0);

            res.put(place.getId(), rate);
        }

        return res;
    }

    @Override
    public Map<String, Integer> getPopularPlace() {
        List<ParkingOrder> orders = parkingOrderService.list();
        Map<String, Integer> map = new HashMap<>();
        for (ParkingOrder order : orders) {
            String id = order.getParkingPlaceId();
            if (map.containsKey(id)) {
                Integer val = map.get(id);
                map.put(id, val + 1);
            } else {
                map.put(id, 1);
            }
        }
        return map;
    }

    @Override
    public List<String> getRecommend(double weight1, double weight2) {
        Map<String, Double> usage = getUsage(24);
        Map<String, Double> usageScore = new HashMap<>();
        double usageMax = Double.NEGATIVE_INFINITY;
        for (Double value : usage.values()) {
            usageMax = Math.max(usageMax, value);
        }
        for (Map.Entry<String, Double> entry : usage.entrySet()) {
            String id = entry.getKey();
            Double usageRate = entry.getValue();
            Double score = usageRate / usageMax;
            usageScore.put(id, score);
        }

        Map<String, Integer> cnt = getPopularPlace();
        Map<String, Double> cntScore = new HashMap<>();
        int cntMax = Integer.MIN_VALUE;
        for (Integer value : cnt.values()) {
            cntMax = Math.max(cntMax, value);
        }
        for (Map.Entry<String, Integer> entry : cnt.entrySet()) {
            String id = entry.getKey();
            Integer cntVal = entry.getValue();
            Double score = (double) cntVal / cntMax;
            cntScore.put(id, score);
        }

        Map<String, Double> scores = new HashMap<>();
        for (String key : cntScore.keySet()) {
            double val1 = usageScore.get(key);
            double val2 = cntScore.get(key);
            double score = val1 * weight1 + val2 * weight2;
            scores.put(key, score);
        }

        List<Map.Entry<String, Double>> entryList = new ArrayList<>(scores.entrySet());
        entryList.sort((o1, o2) -> {
            Double v1 = o1.getValue();
            Double v2 = o2.getValue();
            if (v2 - v1 > 0) return 1;
            else if (v2.equals(v1)) return 0;
            else return -1;
        });

        List<String> res = new ArrayList<>();
        int index = 0;
        while (res.size() < 3) {
            Map.Entry<String, Double> entry = entryList.get(index++);
            String id = entry.getKey();
            ParkingPlace place = this.getOne(new QueryWrapper<>(new ParkingPlace())
                    .eq("status", ParkingPlaceStatus.AVAILABLE.getValue())
                    .eq("id", id)
            );
            if (place != null) {
                String placeNum = place.getArea() + String.format("%03d", place.getNumber());
                res.add(placeNum);
            }
        }

        return res;
    }


}
