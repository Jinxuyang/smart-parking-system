#include "ParkingLock.h"

ParkingLock::ParkingLock(int servopin) {
    servo.attach(servopin);
    this->lockStatus = false;
    this->delayTime = 0;
    this->lockTick = 0;

    turnLock();
}

void ParkingLock::turnLock() {
    Serial.println("Turn lock");
    servo.write(10);
    lockStatus = true;
    Serial.println("Lock turned");
}

void ParkingLock::closeLock() {
    Serial.println("Close lock");
    servo.write(170);
    lockStatus = false;
    Serial.println("Lock closed");
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

bool ParkingLock::getLockStatus() {
    return lockStatus;
}

void ParkingLock::loop() {
    // if (!lockStatus && millis() - lockTick > delayTime) {
    //     turnLock();
    // }
}