package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;


public enum ParkingPlaceStatus implements IEnum<Integer> {

    AVAILABLE(0),
    OCCUPIED(1),
    DISABLED(2);

    private final int value;

    ParkingPlaceStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
