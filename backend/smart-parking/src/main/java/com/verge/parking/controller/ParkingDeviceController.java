package com.verge.parking.controller;

import com.verge.parking.common.CommonResponse;
import com.verge.parking.entity.ParkingDevice;
import com.verge.parking.service.IParkingDeviceService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Verge
 * @since 2023-04-09
 */
@Controller
@RequestMapping("/device")
public class ParkingDeviceController {
    @Resource
    private IParkingDeviceService parkingDeviceService;

    @GetMapping("/list")
    CommonResponse list() {
        return CommonResponse.success(parkingDeviceService.list());
    }

    @PostMapping("/add")
    CommonResponse add(String macAddress) {
        return CommonResponse.success(parkingDeviceService.save(new ParkingDevice(macAddress)));
    }


}
