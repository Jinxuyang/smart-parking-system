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
        unsigned long noParkingTick;
        unsigned long detectTick;

        String macAddress;
    public:
        VehicleDetector(int echoPin, int triggerPin);

        bool hasCar();
        bool isParkingTimeOut();
        unsigned long getParkingTime();
        unsigned long getNoParkingTime();
        String getParkingStatusJSON();
        DynamicJsonDocument getParkingStatus();
        bool parkingStatusChanged();

        void loop();
};

#endif