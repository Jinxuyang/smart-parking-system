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
    Serial.printf("Set lock key: %s\n", lockKey.c_str());
}

bool ParkingLock::closeLockWithKey(String key) {
    Serial.printf("Compare key: '%s' with lock key: '%s'\n", key.c_str(), lockKey.c_str());
    if (lockKey == key) {
        closeLock();
        return true;
    }
    return false;
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