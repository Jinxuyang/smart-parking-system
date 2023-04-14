#include <SoftwareSerial.h>
#include <PubSubClient.h>
#include <ESP8266WiFi.h>
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <ArduinoJson.h>
#include "ParkingLock.h"
#include "VehicleDetector.h"

// init parking lock and vehicle detector
const byte triggerPin = D0;
const byte echoPin = D1;
const byte servoPin = D4;
ParkingLock parkingLock(servoPin);
VehicleDetector detector(echoPin, triggerPin);

// init bluetooth
const byte RX = D6;
const byte TX = D5;
SoftwareSerial EEBlue(RX, TX);

// innit wifi and mqtt
WiFiClient espClient;
PubSubClient client(espClient);

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, 8 * 3600);


const char* ssid = "SEXBABY";
const char* password = "204204204";

const char* mqtt_server = "121.43.147.234";
const int mqtt_port = 1883;
const char *mqtt_username = "verge";
const char *mqtt_password = "123456";

const char* parkingStatusTopic = "ParkingStatus";
const char* unlockKeyTopic = "UnlockKey";

unsigned long lockTick = 0;

void connectToWifi() {
    WiFi.begin(ssid, password);
    while (!WiFi.isConnected()) {
        delay(1000);
        Serial.println("Connecting to WiFi...");
    }
    Serial.println("WiFi connected");
}

// connect to mqtt server and subscribe to unlock key topic
void connectToMQTT() {
    String client_id = "esp8266-client-";
    client_id += String(WiFi.macAddress());

    if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {
        Serial.println("MQTT connected");

        // subscribe to unlock key topic
        client.subscribe(unlockKeyTopic);
    }
    else {
        Serial.print("MQTT failed, rc=");
        Serial.println(client.state());

        delay(1000);
        return;
    }
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");

  String mqtt_buff = "";
  for (int i = 0; i < length; i++) {
    mqtt_buff += (char)payload[i];
  }
  Serial.println(mqtt_buff);
  
  if (mqtt_buff.startsWith(WiFi.macAddress())) {
    String key = mqtt_buff.substring(18, 28);
    Serial.print("Get key: ");
    Serial.println(key);
    parkingLock.setLockKey(key);
    Serial.print("unlock key set");
  }
  
  mqtt_buff = "";
}

void recvBluetoothMsg() {
    if (EEBlue.available()) {
        String command = EEBlue.readString();
        Serial.print("Get msg from blue: ");
        Serial.println(command);

        if (command.startsWith("unlock:")) {
            // get key from bluetooth
            String key = command.substring(7, 17);
            Serial.print("Parse key: ");
            Serial.println(key);
            if (parkingLock.closeLockWithKey(key)) {
                Serial.println("Unlock success");
            } else {
                Serial.println("Unlock fail");
            }
            
        } else if (command.startsWith("lock")) {
            parkingLock.turnLock();
        }
    }
}

String packParkingInfo() {
    DynamicJsonDocument parkingStatus(1024);
    parkingStatus["macAddress"] = WiFi.macAddress();
    parkingStatus["lock"] = parkingLock.getLockStatus();
    parkingStatus["hasCar"] = detector.hasCar();
    parkingStatus["time"] = timeClient.getEpochTime();
    String msg;
    serializeJson(parkingStatus, msg);
    return msg;
}

void setup () {
    Serial.begin(115200);  
    EEBlue.begin(9600); 

    client.setServer(mqtt_server, mqtt_port);
    client.setCallback(callback);

    parkingLock.turnLock();
}

void loop () {
    if (!WiFi.isConnected()) {
        connectToWifi();
    }
    if (!client.connected()) {
        connectToMQTT();
    }

    timeClient.update();

    // when parking status or lock status changed, publish to mqtt server
    if (detector.parkingStatusChanged() || parkingLock.lockStatusChanged()) {
        String msg = packParkingInfo();
        client.publish(parkingStatusTopic, msg.c_str());
        Serial.print("Publish parking status: ");
        Serial.println(msg);
    }

    recvBluetoothMsg();
    client.loop();
    detector.loop();      
}