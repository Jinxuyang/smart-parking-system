package com.verge.parking.mqtt;

import jakarta.annotation.Resource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MQTTServer {
    String[] topic = {"ParkingStatus", "UnlockKey"};

    // qos 是服务质量的缩写，它是一个数字，代表了消息传递的可靠性。
    // 0 表示最多一次，也就是消息可能会丢失，但是不会重复传递。
    // 1 表示至少一次，也就是消息可能会重复传递，但是不会丢失。
    // 2 表示只有一次，也就是消息不会丢失，也不会重复传递。
    int[] qos = {2, 2};
    String broker = "tcp://121.43.147.234:1883";

    @Resource
    MQTTCallback mqttCallback = new MQTTCallback();
    @Bean
    public MqttClient getMQTTClient() {

        String clientId = MqttClient.generateClientId();
        //  持久化
        MemoryPersistence persistence = new MemoryPersistence();
        // MQTT 连接选项
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // 设置认证信息
        connOpts.setUserName("verge");
        connOpts.setPassword("123456".toCharArray());

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            // 设置回调
            client.setCallback(mqttCallback);
            // 建立连接
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected to broker: " + broker);
            // 订阅 topic
            client.subscribe(topic, qos);
            System.out.println("Subscribed to topic: " + Arrays.toString(topic));

            return client;
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("exception " + me);
            me.printStackTrace();
            return null;
        }

    }

}
