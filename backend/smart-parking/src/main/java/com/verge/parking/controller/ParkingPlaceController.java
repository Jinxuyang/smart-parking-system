package com.verge.parking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.verge.parking.common.CommonResponse;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.entity.ParkingPlaceStatus;
import com.verge.parking.service.IParkingPlaceService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Verge
 * @since 2023-04-09
 */
@RestController
@RequestMapping("/parkingPlace")
public class ParkingPlaceController {
    @Autowired
    private IParkingPlaceService parkingPlaceService;

    @GetMapping("/all")
    public CommonResponse get() {
        return CommonResponse.success(parkingPlaceService.list());
    }

    @GetMapping("/{id}")
    public CommonResponse get(@PathVariable String id) {
        return CommonResponse.success(parkingPlaceService.getById(id));
    }

    @GetMapping("/status/{status}")
    public CommonResponse getByStatus(@PathVariable Integer status) {
        return CommonResponse.success(parkingPlaceService.list(
                new QueryWrapper<>(new ParkingPlace())
                        .eq("status", status)));
    }
}
