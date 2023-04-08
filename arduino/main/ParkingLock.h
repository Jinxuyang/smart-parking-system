#ifndef ParkingLock_h
#define ParkingLock_h

#include <Arduino.h>
#include <Servo.h>

class ParkingLock {
    private:
        Servo servo;

        String lockKey;
        bool lockStatus;
        bool prevLockStatus;
        unsigned long lockTick;
        unsigned long delayTime;
    public:
        ParkingLock(int servoPin);

        void turnLock();
        void closeLock();
        bool getLockStatus();
        bool lockStatusChanged();
        void turnLockWithDelay(int delayTime);
        void setLockKey(String key);
        bool closeLockWithKey(String key);
        
        void loop();
};
#endif



