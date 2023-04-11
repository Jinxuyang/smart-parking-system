package com.verge.parking.mqtt;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.service.IParkingPlaceService;
import jakarta.annotation.Resource;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Component
public class MQTTCallback implements MqttCallback {
    @Resource
    private IParkingPlaceService parkingPlaceService;
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connection lost：" + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Received message: \n  topic：" + topic + "\n  Qos：" + message.getQos() + "\n  payload：" + new String(message.getPayload()));
        if (topic.equals("ParkingStatus")) {
            // parse json
            ObjectMapper mapper = new ObjectMapper();
            try {
                ParkingStatusMsg parkingStatusMsg = mapper.readValue(message.getPayload(), ParkingStatusMsg.class);
                String macAddress = parkingStatusMsg.getMacAddress();
                ParkingPlace place = parkingPlaceService.getOne(new QueryWrapper<>(
                        new ParkingPlace()).eq("device_mac_address", macAddress)
                );

                //place.setLockStatus(parkingStatusMsg.getIsLock());

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("deliveryComplete");
    }
}
