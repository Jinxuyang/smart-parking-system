package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ParkingPlaceStatus {

    AVAILABLE(0),
    OCCUPIED(1),
    DISABLED(2);

    @EnumValue
    private final int value;

    ParkingPlaceStatus(int value) {
        this.value = value;
    }
}
