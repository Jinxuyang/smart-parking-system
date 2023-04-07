#ifndef ParkingLock_h
#define ParkingLock_h

#include <Arduino.h>
#include <Servo.h>
#include <HCSR04.h>
#include <ArduinoJson.h>

class ParkingLock {
    private:
        Servo servo;
        UltraSonicDistanceSensor distanceSensor;
        String lockKey;
        bool lockStatus;

        bool parkingStatus;
        bool prevParkingStatus;
        
        unsigned long lockTick;
        unsigned long delayTime;

        unsigned long parkingTick;
        String macAddress;

    public:
        ParkingLock(int servoPin, int echoPin, int triggerPin, String macAddress);

        bool hasCar();
        unsigned long getParkingTime();
        String getParkingStatusJSON();
        bool parkingStatusChanged();

        void turnLock();
        void closeLock();
        void turnLockWithDelay(int delayTime);
        void setLockKey(String key);
        void closeLockWithKey(String key);
        
        void loop();
};
#endif



