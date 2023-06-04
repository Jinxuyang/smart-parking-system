package com.verge.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.verge.parking.entity.ParkingOrder;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.entity.enums.ParkingPlaceStatus;
import com.verge.parking.mapper.ParkingPlaceMapper;
import com.verge.parking.service.IParkingOrderService;
import com.verge.parking.service.IParkingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<ParkingPlace> getPopularPlace() {
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

        List<Map.Entry<String, Integer>> sortedlist = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> o2 - o1))
                .toList();

        List<ParkingPlace> res = new ArrayList<>();
        for(int i = 0;i < sortedlist.size() && res.size() < 3;i++) {
            Map.Entry<String, Integer> entry = sortedlist.get(i);
            ParkingPlace place = this.getOne(new QueryWrapper<>(new ParkingPlace())
                    .eq("id", entry.getKey())
                    .eq("status", 0)
            );
            if (place != null) res.add(place);
        }
        return res;
    }
}
