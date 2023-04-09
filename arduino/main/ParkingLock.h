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
    public:
        ParkingLock(int servoPin);

        void turnLock();
        void closeLock();
        bool getLockStatus();
        bool lockStatusChanged();
        void setLockKey(String key);
        bool closeLockWithKey(String key);
};
#endif



