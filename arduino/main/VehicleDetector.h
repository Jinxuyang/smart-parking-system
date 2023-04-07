#ifndef VehicleDetector_H
#define VehicleDetector_H

#include <Arduino.h>
#include <HCSR04.h>
#include <ArduinoJson.h>

class VehicleDetector {
    private:
        UltraSonicDistanceSensor distanceSensor;

        bool parkingStatus;
        bool prevParkingStatus;
        unsigned long parkingTick;
        String macAddress;

    public:
        VehicleDetector(int echoPin, int triggerPin, String macAddress);

        bool hasCar();
        unsigned long getParkingTime();
        String getParkingStatusJSON();
        bool parkingStatusChanged();

        void loop();
};

#endif