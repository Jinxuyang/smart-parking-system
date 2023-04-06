#ifndef ParkingLock_h
#define ParkingLock_h

#include <Servo.h>
#include <HCSR04.h>
#include <Arduino.h>
#include <ArduinoJson.h>

class ParkingLock {

public:
    ParkingLock(int servoPin, int echoPin, int triggerPin, String macAddress);
    ~ParkingLock();

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
private:
    Servo *servo;
    UltraSonicDistanceSensor *distanceSensor;
    String lockKey;
    bool lockStatus;

    bool parkingStatus;
    bool prevParkingStatus;
    
    unsigned long lockTick;
    unsigned long delayTime;

    unsigned long parkingTick;
    String macAddress;
};


#endif



