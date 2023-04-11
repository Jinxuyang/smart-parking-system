package com.verge.parking.service.impl;

import com.verge.parking.entity.OrderStatus;
import com.verge.parking.entity.ParkingOrder;
import com.verge.parking.mapper.ParkingOrderMapper;
import com.verge.parking.service.IParkingOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    @Override
    public boolean book(String macAddress, Integer userId) {
        ParkingOrder order = new ParkingOrder();
        order.setId(macAddress);
        order.setUserId(userId);
        order.setStatus(OrderStatus.WAITING);
        return this.save(order);
    }

    @Override
    public boolean carIn(String macAddress, Long timestamp) {
        ParkingOrder order = this.getById(macAddress);
        order.setStatus(OrderStatus.PARKING);
        //convert timestamp to LocalDateTim
        order.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                TimeZone.getDefault().toZoneId()));
        return this.updateById(order);
    }

    @Override
    public boolean carOut(String macAddress, Long timestamp) {
        ParkingOrder order = this.getById(macAddress);
        order.setStatus(OrderStatus.FINISHED);
        //convert timestamp to LocalDateTim
        order.setStopTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                TimeZone.getDefault().toZoneId()));
        order.setFee(getFee(order.getStopTime().toEpochSecond(ZoneOffset.UTC) - order.getStartTime().toEpochSecond(ZoneOffset.UTC)));
        return this.updateById(order);
    }

    private int getFee(long time) {
        return (int) ((time / 1000) * (5.0 / 3600.0));
    }
}
