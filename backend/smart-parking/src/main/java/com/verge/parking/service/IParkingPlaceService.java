package com.verge.parking.service;

import com.verge.parking.entity.ParkingPlace;
import com.baomidou.mybatisplus.extension.service.IService;
import com.verge.parking.entity.enums.ParkingPlaceStatus;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Verge
 * @since 2023-04-12
 */
public interface IParkingPlaceService extends IService<ParkingPlace> {
    boolean updateLockStatus(String macAddress, boolean isLock);

    ParkingPlaceStatus getPlaceStatusById(String macAddress);

    void updatePlaceStatusById(String macAddress, ParkingPlaceStatus status);

    Map<String, Double> getUsage(int hour);

    Map<String, Integer> getPopularPlace();

    List<String> getRecommend(double weight1, double weight2);
}
