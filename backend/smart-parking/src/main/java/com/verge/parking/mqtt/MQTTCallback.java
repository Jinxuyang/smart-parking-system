package com.verge.parking.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verge.parking.entity.enums.OrderStatus;
import com.verge.parking.entity.ParkingOrder;
import com.verge.parking.service.IParkingOrderService;
import com.verge.parking.service.IParkingPlaceService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQTTCallback implements MqttCallback {
    @Autowired
    private IParkingPlaceService parkingPlaceService;
    @Autowired
    private IParkingOrderService parkingOrderService;

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connection lost：" + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Received message: \n  topic：" + topic + "\n  Qos：" + message.getQos() + "\n  payload：" + new String(message.getPayload()));
        if (topic.equals("ParkingStatus")) {
            // parse json
            ObjectMapper mapper = new ObjectMapper();
            ParkingStatusMsg status;
            try {
                status = mapper.readValue(message.getPayload(), ParkingStatusMsg.class);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            String macAddress = status.getMacAddress();

            // update lock status
            parkingPlaceService.updateLockStatus(macAddress, status.isLock());

            ParkingOrder order = parkingOrderService.getLatestOrder(macAddress);
            if (order == null) {
                return;
            }
            // update order status
            if (order.getOrderStatus() == OrderStatus.WAITING && status.isHasCar() && !status.isLock()) {
                parkingOrderService.carIn(order.getId(), status.getTime());
            } else if (order.getOrderStatus() == OrderStatus.PARKING && !status.isHasCar() && status.isLock()) {
                parkingOrderService.carOut(order.getId(), status.getTime());
            }


        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("deliveryComplete");
    }
}
