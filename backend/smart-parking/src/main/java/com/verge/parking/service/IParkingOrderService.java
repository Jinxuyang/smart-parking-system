package com.verge.parking.service;

import com.verge.parking.entity.ParkingOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Verge
 * @since 2023-04-12
 */
public interface IParkingOrderService extends IService<ParkingOrder> {
    boolean book(String macAddress, Integer userId);
    boolean carIn(String macAddress, Long timestamp);
    boolean carOut(String macAddress, Long timestamp);

}
