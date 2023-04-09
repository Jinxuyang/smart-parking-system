package com.verge.parking.controller;

import com.verge.parking.common.CommonResponse;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.service.IParkingPlaceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping("/add")
    public CommonResponse add(@RequestBody ParkingPlace parkingPlace) {
        return CommonResponse.success(parkingPlaceService.save(parkingPlace));
    }
}
