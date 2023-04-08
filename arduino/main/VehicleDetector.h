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
        unsigned long detectTick;

        String macAddress;
    public:
        VehicleDetector(int echoPin, int triggerPin, String macAddress);

        bool hasCar();
        unsigned long getParkingTime();
        String getParkingStatusJSON();
        DynamicJsonDocument getParkingStatus();
        bool parkingStatusChanged();

        void loop();
};

#endif