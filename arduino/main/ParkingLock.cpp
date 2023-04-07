#include "ParkingLock.h"

ParkingLock::ParkingLock(int servopin) {
    servo.attach(servopin);
    this->lockStatus = false;
    this->delayTime = 0;
    this->lockTick = 0;

    turnLock();
}

void ParkingLock::turnLock() {
    if (lockStatus){
        return;
    }
    servo.write(10);
    lockStatus = true;
}

void ParkingLock::closeLock() {
    if (!lockStatus) {
        return;
    }
    servo.write(170);
    lockStatus = false;
}

void ParkingLock::setLockKey(String key) {
    this->lockKey = key;
}

void ParkingLock::closeLockWithKey(String key) {
    if (key == lockKey) {
        closeLock();
    }
}

void ParkingLock::turnLockWithDelay(int delayTime) {
    if (lockStatus) {
        return;
    }
    this->delayTime = delayTime;
    lockTick = millis();
}

void ParkingLock::loop() {
    if (!lockStatus && millis() - lockTick > delayTime) {
        turnLock();
    }
}