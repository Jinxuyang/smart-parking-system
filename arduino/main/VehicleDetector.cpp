#include "VehicleDetector.h"

VehicleDetector::VehicleDetector(int echoPin, int triggerPin, String macAddress) : distanceSensor(triggerPin, echoPin) {
    this->macAddress = macAddress;
    this->parkingStatus = false;
    this->prevParkingStatus = false;
    this->parkingTick = 0;
}

bool VehicleDetector::hasCar() {
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

unsigned long VehicleDetector::getParkingTime() {
    // when there is a car, return 0
    if (parkingStatus) {
        return 0;
    }
    return millis() - parkingTick;
}

String VehicleDetector::getParkingStatusJSON() {
    DynamicJsonDocument parkingStatus(1024);
    parkingStatus["macAddress"] = macAddress;
    parkingStatus["hasCar"] = hasCar();
    parkingStatus["parkingTime"] = getParkingTime();

    String jsonStr;
    serializeJson(parkingStatus, jsonStr);
    return jsonStr;
}

bool VehicleDetector::parkingStatusChanged() {
    if (parkingStatus != prevParkingStatus) {
        prevParkingStatus = parkingStatus;
        return true;
    } else {
        return false;
    }
}

void VehicleDetector::loop() {
    if (millis() - detectTick > 5000) {
        hasCar();
        detectTick = millis();
    }
}
