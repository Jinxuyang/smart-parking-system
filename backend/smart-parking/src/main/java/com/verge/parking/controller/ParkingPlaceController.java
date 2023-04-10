package com.verge.parking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.verge.parking.common.CommonResponse;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.entity.ParkingPlaceStatus;
import com.verge.parking.service.IParkingPlaceService;
import jakarta.annotation.Resource;
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
@Controller
@RequestMapping("/parkingPlace")
public class ParkingPlaceController {
    @Resource
    private IParkingPlaceService parkingPlaceService;

    @PostMapping()
    public CommonResponse add(@RequestBody ParkingPlace parkingPlace) {
        return CommonResponse.success(parkingPlaceService.save(parkingPlace));
    }

    @GetMapping()
    public CommonResponse get() {
        return CommonResponse.success(parkingPlaceService.list());
    }

    @GetMapping("/{id}")
    public CommonResponse get(@PathVariable Integer id) {
        return CommonResponse.success(parkingPlaceService.getById(id));
    }

    @GetMapping("/status/{status}")
    public CommonResponse getByStatus(@PathVariable ParkingPlaceStatus status) {
        return CommonResponse.success(parkingPlaceService.list(
                new QueryWrapper<>(new ParkingPlace())
                        .eq("status", status)));
    }
}
