package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
@TableName("parking_place")
public class ParkingPlace implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String deviceMacAddress;

    private Boolean lockStatus;

    private String area;

    private String floor;

    private Integer number;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
