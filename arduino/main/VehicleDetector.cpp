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
        } else {
            noParkingTick = millis();
        }
        parkingStatus = true;
        return true;
    } else {
        parkingStatus = false;
        return false;
    }
}
//when there is no car for 1 min, return true
bool VehicleDetector::isParkingTimeOut() {
    if (parkingStatus) {
        return true;
    }
    return getNoParkingTime() > 60000;
}

unsigned long VehicleDetector::getParkingTime() {
    // when there is a car, return 0
    if (parkingStatus) {
        return 0;
    }
    return millis() - parkingTick;
}

unsigned long VehicleDetector::getNoParkingTime() {
    // when there is a car, return 0
    if (!parkingStatus) {
        return 0;
    }
    return millis() - noParkingTick;
}

String VehicleDetector::getParkingStatusJSON() {
    String jsonStr;
    serializeJson(getParkingStatus(), jsonStr);
    return jsonStr;
}

DynamicJsonDocument VehicleDetector::getParkingStatus() {
    DynamicJsonDocument parkingStatus(1024);
    parkingStatus["macAddress"] = macAddress;
    parkingStatus["hasCar"] = hasCar();
    //parkingStatus["parkingTime"] = getParkingTime();

    return parkingStatus;
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
    if (millis() - detectTick > 1000) {
        hasCar();
        detectTick = millis();
    }
}
