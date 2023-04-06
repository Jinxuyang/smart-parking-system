#include "ParkingLock.h"

ParkingLock::ParkingLock(int servoPin, int echoPin, int triggerPin, String macAddress) {
    this->servo = new Servo();
    this->servo->attach(servoPin);
    this->distanceSensor = new UltraSonicDistanceSensor(triggerPin, echoPin);
    this->macAddress = macAddress;
    this->lockStatus = false;
    this->parkingState = false;
    this->prevParkingStatus = false;
}

ParkingLock::hasCar() {
    if(distanceSensor->measureDistanceCm() < 20) {
        if (!parkingState) {
            parkingTick = millis();
        }
        parkingState = true;
        return true;
    } else {
        parkingState = false;
        return false;
    }
}

ParkingLock::getParkingTime() {
    if (!parkingState) {
        return 0;
    }
    return millis() - parkingTick;
}

ParkingLock::getParkingStatusJSON() {
    DynamicJsonDocument parkingStatus(1024);
    parkingState["macAddress"] = macAddress;
    parkingState["hasCar"] = hasCar();
    parkingState["parkingTime"] = getParkingTime();

    String jsonStr;
    serializeJson(parkingStatus, jsonStr);
    return jsonStr;
}

ParkingLock::parkingStatusChanged() {
    if (parkingState != prevParkingStatus) {
        prevParkingStatus = parkingState;
        return true;
    } else {
        return false;
    }
}

ParkingLock::turnLock() {
    if (lockStatus){
        return;
    }
    servo.write(10);
    lockStatus = true;
}

ParkingLock::closeLock() {
    if (!lockStatus) {
        return;
    }
    servo.write(170);
    lockStatus = false;
}

ParkingLock::setLockKey(String key) {
    this->lockKey = key;
}

ParkingLock::closeLockWithKey(String key) {
    if (key == lockKey) {
        closeLock();
    }
}

ParkingLock::turnLockWithDelay(int delayTime) {
    if (lockStatus) {
        return;
    }
    this->delayTime = delayTime;
    lockTick = millis();
}

ParkingLock::loop() {
    if (parkingStatusChanged) {
        if (parkingState) {
            turnLockWithDelay(10000);
        }
    }
    if (lockStatus) {
        return;
    }
    if (millis() - lockTick > delayTime) {
        turnLock();
    }
}