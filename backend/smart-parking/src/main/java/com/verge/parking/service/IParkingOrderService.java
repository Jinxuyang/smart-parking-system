package com.verge.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.verge.parking.entity.ParkingOrder;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Verge
 * @since 2023-04-12
 */
public interface IParkingOrderService extends IService<ParkingOrder> {
    boolean reserve(String macAddress, Integer userId);
    boolean carIn(Integer id, Long timestamp);
    boolean carOut(Integer id, Long timestamp);

    ParkingOrder getLatestOrder(String macAddress);

    String getUnlockKey(Integer orderId);

    boolean unlockPlace(String key);

    boolean unlockPlace(Integer orderId);

    boolean lockPlace(Integer orderId);

    int[] getPopularHours();

    void payOrder(Integer orderId);

}
