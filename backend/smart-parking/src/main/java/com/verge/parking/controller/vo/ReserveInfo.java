package com.verge.parking.controller.vo;

import lombok.Data;

@Data
public class ReserveInfo {
    public Integer orderStatus;
    public String parkingPlaceNum;
    public Integer orderId;
}
