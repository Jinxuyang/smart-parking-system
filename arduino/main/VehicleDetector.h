#ifndef VehicleDetector_H
#define VehicleDetector_H

#include <Arduino.h>
#include <HCSR04.h>

class VehicleDetector {
    private:
        UltraSonicDistanceSensor distanceSensor;

        bool parkingStatus;
        bool prevParkingStatus;

        unsigned long detectTick;

        String macAddress;
    public:
        VehicleDetector(int echoPin, int triggerPin);

        bool getParkingStatus();
        bool hasCar();
        bool parkingStatusChanged();
        void loop();
};

#endif