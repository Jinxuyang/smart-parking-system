package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum LockStatus {
    UNLOCK(false),
    LOCK(true);
    @EnumValue
    final boolean lockStatus;

    LockStatus(boolean lockStatus) {
        this.lockStatus = lockStatus;
    }


}
