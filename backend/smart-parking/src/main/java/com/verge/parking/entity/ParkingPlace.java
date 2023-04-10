package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
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
@TableName("parking_place")
public class ParkingPlace implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer deviceId;

    private String area;

    private String floor;

    private Integer number;

    private ParkingPlaceStatus status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ParkingPlaceStatus getStatus() {
        return status;
    }

    public void setStatus(ParkingPlaceStatus status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "ParkingPlace{" +
            "id = " + id +
            ", deviceId = " + deviceId +
            ", area = " + area +
            ", floor = " + floor +
            ", number = " + number +
            ", status = " + status +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
        "}";
    }
}
