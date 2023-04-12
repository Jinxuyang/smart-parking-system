package com.verge.parking.service;

import com.verge.parking.entity.ParkingPlace;
import com.baomidou.mybatisplus.extension.service.IService;
import com.verge.parking.entity.ParkingPlaceStatus;

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

    boolean updatePlaceStatusById(String macAddress, ParkingPlaceStatus status);
}
