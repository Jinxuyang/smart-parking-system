package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Verge
 * @since 2023-04-12
 */
@Data
@TableName("parking_order")
public class ParkingOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String parkingPlaceId;

    private String unlockKey;

    private LocalDateTime startTime;

    private LocalDateTime stopTime;

    private Integer fee;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private OrderStatus status;

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
