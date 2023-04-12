package com.verge.parking.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verge.parking.entity.OrderStatus;
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
                ParkingStatusMsg status = mapper.readValue(message.getPayload(), ParkingStatusMsg.class);

                String macAddress = status.getMacAddress();
                // update lock status
                parkingPlaceService.updateLockStatus(macAddress, status.isLock());

                // update parking status
                ParkingPlace place = parkingPlaceService.getById(macAddress);
                if (place.getStatus() == OrderStatus.RESERVED && status.isHasCar() && status.isLock()) {
                    place.setStatus(OrderStatus.PARKING);
                } else if (place.getStatus() == OrderStatus.PARKING && !status.isHasCar() && !status.isLock()) {
                    place.setStatus(OrderStatus.FREE);
                }

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
