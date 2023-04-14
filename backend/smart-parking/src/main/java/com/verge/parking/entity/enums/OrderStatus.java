package com.verge.parking.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum OrderStatus implements IEnum<Integer> {
    WAITING(0),
    PARKING(1),
    WAITING_PAY(2),
    FINISHED(3),
    CANCELED(4);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
