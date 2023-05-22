package com.verge.parking.controller.vo;

import com.verge.parking.entity.ParkingPlace;
import lombok.Data;

@Data
public class ReserveInfo {
    public Integer orderStatus;
    public String parkingPlaceNum;
    public Integer orderId;
    public ParkingPlace place;
}
