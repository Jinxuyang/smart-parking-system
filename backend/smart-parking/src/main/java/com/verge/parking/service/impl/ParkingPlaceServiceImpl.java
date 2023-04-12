package com.verge.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.entity.ParkingPlaceStatus;
import com.verge.parking.mapper.ParkingPlaceMapper;
import com.verge.parking.service.IParkingPlaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public boolean updateLockStatus(String macAddress, boolean isLock) {
        ParkingPlace place = this.getOne(new QueryWrapper<ParkingPlace>().eq("device_mac_address", macAddress));
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
    public boolean updatePlaceStatusById(String macAddress, ParkingPlaceStatus status) {
        ParkingPlace place = this.getById(macAddress);
        place.setStatus(status);
        return this.updateById(place);
    }
}
