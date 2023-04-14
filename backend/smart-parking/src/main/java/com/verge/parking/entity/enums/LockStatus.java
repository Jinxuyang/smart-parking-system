package com.verge.parking.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;


public enum LockStatus implements IEnum<Boolean> {
    UNLOCK(false),
    LOCK(true);

    final boolean lockStatus;

    LockStatus(boolean lockStatus) {
        this.lockStatus = lockStatus;
    }


    @Override
    public Boolean getValue() {
        return lockStatus;
    }
}
