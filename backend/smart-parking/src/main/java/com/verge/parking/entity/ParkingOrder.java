package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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

    private Integer id;

    private Integer userId;

    private String parkingPlaceId;

    private String unlockKey;

    private LocalDateTime startTime;

    private LocalDateTime stopTime;

    private Integer fee;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private OrderStatus status;
}
