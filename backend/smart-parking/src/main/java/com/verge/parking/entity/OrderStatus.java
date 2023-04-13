package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum OrderStatus {
    WAITING(0),
    PARKING(1),
    WAITING_PAY(2),
    FINISHED(3),
    CANCELED(4);

    @EnumValue
    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }
}
