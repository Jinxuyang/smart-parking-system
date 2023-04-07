// #include "ParkingLock.h"
// #include <ESP8266WiFi.h>

// const byte servoPin = D4;
// ParkingLock parkingLock(servoPin);

// void setup(){
//     Serial.begin(115200);
//     Serial.println("Start");
//     delay(2000);

//     parkingLock.closeLock();
//     Serial.println("Close lock");
//     delay(2000);

//     Serial.println("Turn lock");
//     parkingLock.turnLock();
//     delay(2000);

//     Serial.println("Set key");
//     parkingLock.setLockKey("1234");
//     delay(2000);

//     Serial.println("turn lock with key");
//     parkingLock.closeLockWithKey("1234");
//     delay(2000);

//     Serial.println("Turn lock with delay");
//     parkingLock.turnLockWithDelay(5000);
// }

// void loop(){
//     parkingLock.loop();
// }