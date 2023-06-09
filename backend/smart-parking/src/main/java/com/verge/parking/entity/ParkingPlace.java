package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.verge.parking.entity.enums.ParkingPlaceStatus;
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
 * @since 2023-04-14
 */
@Data
@TableName("parking_place")
public class ParkingPlace implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private Boolean lockStatus;

    private String area;

    private String floor;

    private Integer number;

    private ParkingPlaceStatus status;

    private String BleDeviceId;

    private String BleServiceId;

    private String BleCharacteristicId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
