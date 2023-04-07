// #include <SoftwareSerial.h>
// #include <ESP8266WiFi.h>
// #include <PubSubClient.h>
// #include "ParkingLock.h"

// const byte triggerPin = D0;
// const byte echoPin = D1;
// const byte servoPin = D4;
// ParkingLock parkingLock(servoPin, echoPin, triggerPin, WiFi.macAddress());

// const byte RX = D6;
// const byte TX = D5;
// SoftwareSerial EEBlue(RX, TX);

// WiFiClient espClient; 
// PubSubClient client(espClient);

// const char* ssid = "SEXBABY";
// const char* password = "204204204";

// const char* mqtt_server = "43.138.172.185";
// const int mqtt_port = 1883;
// const char *mqtt_username = "verge";
// const char *mqtt_password = "123456";

// const char* parkingStatusTopic = "ParkingStatus";
// const char* unlockKeyTopic = "UnlockKey";

// unsigned long detectTick = 0;

// void connectToWifi() {
//     WiFi.begin(ssid, password);
//     while (!WiFi.isConnected()) {
//         delay(1000);
//         Serial.println("Connecting to WiFi...");
//     }
//     Serial.println("WiFi connected");
// }

// void connectToMQTT() {
//     String client_id = "esp8266-client-";
//     client_id += String(WiFi.macAddress());
//     if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {
//         Serial.println("MQTT connected");
//         client.subscribe(unlockKeyTopic);
//     }
//     else {
//         Serial.print("MQTT failed, rc=");
//         Serial.println(client.state());
//         delay(1000);
//         return;
//     }
// }

// // void recvBlueMsg() {
// //     if (EEBlue.available()) {
// //         String command = EEBlue.readString();
// //         Serial.println(command);
// //         if (command.startsWith("unlock:")) {
// //             command = command.replace("unlock:", "");
// //             if (command == key) {
// //                 turnServo();
// //                 key = "";
// //             }
// //         }
// //         else if (command.startsWith("lock")) {
// //             closeServo();
// //         }
// //     }
// // }

// void callback(char* topic, byte* payload, unsigned int length) {
//   Serial.print("Message arrived [");
//   Serial.print(topic);
//   Serial.print("] ");
//   String mqtt_buff = "";
//   for (int i = 0; i < length; i++) {
//     mqtt_buff += (char)payload[i];
//   }
//   Serial.print(mqtt_buff);
//   Serial.println();
//   if (mqtt_buff.startsWith(WiFi.macAddress())) {
//     String key = mqtt_buff.substring(18);
//     Serial.print("Get key: ");
//     Serial.println(key);
//     parkingLock.setLockKey(key);
//   }
  
//   mqtt_buff = "";
// }

// void uploadParkingStatus() {
//     if (millis() - detectTick > 10000) {
//         detectTick = millis();
//         Serial.println("Detect car ...");

//         if (parkingLock.parkingStatusChanged()) {
//             client.publish(parkingStatusTopic, parkingLock.getParkingStatusJSON().c_str());
//         }
//     }
// }


// void setup () {
//     Serial.begin(115200);  
//     EEBlue.begin(9600); 

//     client.setServer(mqtt_server, mqtt_port);
//     client.setCallback(callback);
// }

// void loop () {
//     if (!client.connected()) {
//         connectToMQTT();
//     }
//     if (!WiFi.isConnected()) {
//         connectToWifi();
//     }

//     uploadParkingStatus();

//     client.loop();
//     parkingLock.loop();      
// }