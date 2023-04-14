package com.verge.parking.mqtt;

import lombok.Data;

@Data
public class ParkingStatusMsg {
    private String macAddress;
    private boolean lock;
    private boolean hasCar;
    private long time;
}
