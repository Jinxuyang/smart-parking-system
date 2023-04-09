package com.verge.parking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Verge
 * @since 2023-04-09
 */
@TableName("parking_device")
public class ParkingDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String macAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return "ParkingDevice{" +
            "id = " + id +
            ", macAddress = " + macAddress +
        "}";
    }
}
