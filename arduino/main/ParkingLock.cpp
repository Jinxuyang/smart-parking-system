#include "ParkingLock.h"

ParkingLock::ParkingLock(int servopin) {
    servo.attach(servopin);
    this->lockStatus = false;

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

bool ParkingLock::getLockStatus() {
    return lockStatus;
}

bool ParkingLock::lockStatusChanged() {
    if (lockStatus != prevLockStatus) {
        prevLockStatus = lockStatus;
        return true;
    }
    return false;
}