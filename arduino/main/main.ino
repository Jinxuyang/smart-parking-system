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
WiFiClientSecure espClient;
PubSubClient client(espClient);

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, 8 * 3600);

const char* fingerprint = "7E:52:D3:84:48:3C:5A:9F:A4:39:9A:8B:27:01:B1:F8:C6:AD:D4:47";

const char* ssid = "SEXBABY";
const char* password = "204204204";

const char* mqtt_server = "n4d7f818.ala.cn-hangzhou.emqxsl.cn";
const int mqtt_port = 8883;
const char *mqtt_username = "verge";
const char *mqtt_password = "123456";

const char* parkingStatusTopic = "ParkingStatus";
const char* controlTopic = "Control";

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
        client.subscribe(controlTopic);
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
  if (mqtt_buff.startsWith(WiFi.macAddress()) && topic == "Control") {
    String cmd = mqtt_buff.substring(18);
    if (cmd == "lock") {
      parkingLock.turnLock();
    } else if (cmd == "unlock") {
      parkingLock.closeLock();
    }
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

    espClient.setFingerprint(fingerprint);
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