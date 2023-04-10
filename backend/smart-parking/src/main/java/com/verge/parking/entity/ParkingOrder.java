package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Verge
 * @since 2023-04-09
 */
@TableName("parking_order")
public class ParkingOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer userId;

    private Integer parkingPlaceId;

    private LocalDateTime startTime;

    private LocalDateTime stopTime;

    private Integer fee;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private OrderStatus status;

    public static ParkingOrder create(Integer userId, Integer parkingPlaceId) {
        ParkingOrder order = new ParkingOrder();
        order.setUserId(userId);
        order.setParkingPlaceId(parkingPlaceId);
        order.setStartTime(LocalDateTime.now());
        order.setFee(0);
        order.setStatus(OrderStatus.WAITING);
        return order;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParkingPlaceId() {
        return parkingPlaceId;
    }

    public void setParkingPlaceId(Integer parkingPlaceId) {
        this.parkingPlaceId = parkingPlaceId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalDateTime stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ParkingOrder{" +
            "id = " + id +
            ", userId = " + userId +
            ", parkingPlaceId = " + parkingPlaceId +
            ", startTime = " + startTime +
            ", stopTime = " + stopTime +
            ", fee = " + fee +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
            ", status = " + status +
        "}";
    }
}
