#include "VehicleDetector.h"

VehicleDetector::VehicleDetector(int echoPin, int triggerPin) : distanceSensor(triggerPin, echoPin) {
    this->parkingStatus = false;
    this->prevParkingStatus = false;
}

bool VehicleDetector::hasCar() {
    bool flag = distanceSensor.measureDistanceCm() < 25;
    bool breakFlag = false;
    for(int i = 0;i< 10;i++) {
      if ((distanceSensor.measureDistanceCm() < 25) != flag) {
        breakFlag = true;
        break;
      }
      delay(100);
    }
    if (!breakFlag) {
      parkingStatus = flag;
    }
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

bool VehicleDetector::getParkingStatus() {
    return parkingStatus;
}

void VehicleDetector::loop() {
    if (millis() - detectTick > 5000) {
        hasCar();
        detectTick = millis();
    }
}
