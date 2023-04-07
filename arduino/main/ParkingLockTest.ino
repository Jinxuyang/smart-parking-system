#include "ParkingLock.h"
#include <ESP8266WiFi.h>

const byte servoPin = D4;
ParkingLock parkingLock(servoPin);

void setup(){
    Serial.begin(115200);
    parkingLock.turnLock();
    Serial.println("Turn lock");
    delay(1000);
    parkingLock.closeLock();
    Serial.println("Close lock");
    delay(1000);

    Serial.println("Turn lock with delay");
    parkingLock.turnLockWithDelay(5000);

    delay(1000);
    parkingLock.turnLock();

    parkingLock.turnLockWithDelay(2000);
}

void loop(){
    parkingLock.loop();
}