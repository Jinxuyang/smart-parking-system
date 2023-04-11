package com.verge.parking.entity;

public enum LockStatus {
    UNLOCK(false),
    LOCK(true);
    final boolean lockStatus;

    LockStatus(boolean lockStatus) {
        this.lockStatus = lockStatus;
    }


}
