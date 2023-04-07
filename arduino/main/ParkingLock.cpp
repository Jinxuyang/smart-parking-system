#include "ParkingLock.h"

ParkingLock::ParkingLock(int servoPin, int echoPin, int triggerPin, String macAddress) : distanceSensor(echoPin, triggerPin){
    servo.attach(servoPin);
    this->macAddress = macAddress;
    this->lockStatus = false;
    this->parkingStatus = false;
    this->prevParkingStatus = false;
}

bool ParkingLock::hasCar() {
    if(distanceSensor.measureDistanceCm() < 20) {
        if (!parkingStatus) {
            parkingTick = millis();
        }
        parkingStatus = true;
        return true;
    } else {
        parkingStatus = false;
        return false;
    }
}

unsigned long ParkingLock::getParkingTime() {
    if (!parkingStatus) {
        return 0;
    }
    return millis() - parkingTick;
}

String ParkingLock::getParkingStatusJSON() {
    DynamicJsonDocument parkingStatus(1024);
    parkingStatus["macAddress"] = macAddress;
    parkingStatus["hasCar"] = hasCar();
    parkingStatus["parkingTime"] = getParkingTime();

    String jsonStr;
    serializeJson(parkingStatus, jsonStr);
    return jsonStr;
}

bool ParkingLock::parkingStatusChanged() {
    if (parkingStatus != prevParkingStatus) {
        prevParkingStatus = parkingStatus;
        return true;
    } else {
        return false;
    }
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
    if (parkingStatusChanged()) {
        if (!parkingStatus) {
            turnLockWithDelay(10000);
        }
    }
    if (millis() - lockTick > delayTime) {
        turnLock();
    }
}