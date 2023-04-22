package com.verge.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.verge.parking.common.KeyGenerator;
import com.verge.parking.entity.ParkingOrder;
import com.verge.parking.entity.enums.OrderStatus;
import com.verge.parking.entity.enums.ParkingPlaceStatus;
import com.verge.parking.mapper.ParkingOrderMapper;
import com.verge.parking.service.IParkingOrderService;
import com.verge.parking.service.IParkingPlaceService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Verge
 * @since 2023-04-12
 */
@Service
public class ParkingOrderServiceImpl extends ServiceImpl<ParkingOrderMapper, ParkingOrder> implements IParkingOrderService {
    @Autowired
    private IParkingPlaceService parkingPlaceService;
    @Autowired
    private MqttClient mqttClient;

    @Override
    @Transactional
    public boolean reserve(String macAddress, Integer userId) {
        ParkingPlaceStatus placeStatus = parkingPlaceService.getPlaceStatusById(macAddress);
        if (placeStatus == ParkingPlaceStatus.OCCUPIED) {
            return false;
        }

        parkingPlaceService.updatePlaceStatusById(macAddress, ParkingPlaceStatus.OCCUPIED);

        String key = KeyGenerator.generate();
        try {
            // TODO TEST
            mqttClient.publish("UnlockKey", new MqttMessage((macAddress + ":" + "1234567890").getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ParkingOrder order = new ParkingOrder();
        order.setUnlockKey(key);
        order.setParkingPlaceId(macAddress);
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.WAITING);
        return this.save(order);
    }

    @Override
    public boolean carIn(Integer id, Long timestamp) {
        ParkingOrder order = new ParkingOrder();
        order.setId(id);
        //convert timestamp to LocalDateTim
        order.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp * 1000),
                TimeZone.getDefault().toZoneId()));
        order.setOrderStatus(OrderStatus.PARKING);
        return this.updateById(order);
    }

    @Override
    @Transactional
    public boolean carOut(Integer id, Long timestamp) {
        ParkingOrder order = new ParkingOrder();
        order.setId(id);
        order.setOrderStatus(OrderStatus.WAITING_PAY);
        //convert timestamp to LocalDateTim
        order.setStopTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp * 1000),
                TimeZone.getDefault().toZoneId()));
        // TODO: 费用计算报错
        //order.setFee(getFee(order.getStopTime().toEpochSecond(ZoneOffset.UTC) - order.getStartTime().toEpochSecond(ZoneOffset.UTC)));
        order.setFee(1);
        // TODO: 状态更新失败
        parkingPlaceService.updatePlaceStatusById(order.getParkingPlaceId(), ParkingPlaceStatus.AVAILABLE);
        return this.updateById(order);
    }

    @Override
    public ParkingOrder getLatestOrder(String macAddress) {
        return this.getOne(
                new QueryWrapper<ParkingOrder>()
                        .eq("parking_place_id", macAddress)
                        .orderByDesc("create_time")
        );
    }

    @Override
    public String getUnlockKey(Integer orderId) {
        return this.getById(orderId).getUnlockKey();
    }

    @Override
    public int[] getPopularHours() {
        int[] timePeriod = new int[24];
        List<ParkingOrder> list = this.list(new QueryWrapper<>(new ParkingOrder())
                .eq("order_status", "3")
        );
        for (ParkingOrder order : list) {
            LocalDateTime time = order.getStartTime();
            timePeriod[time.getHour()]++;
        }
        return timePeriod;
    }

    private int getFee(long time) {
        return (int) ((time / 1000) * (5.0 / 3600.0));
    }
}
