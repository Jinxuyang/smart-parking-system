package com.verge.parking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.verge.parking.common.CommonResponse;
import com.verge.parking.controller.vo.PlaceInfo;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.service.IParkingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Verge
 * @since 2023-04-09
 */
@CrossOrigin(origins = "*")
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

    @GetMapping("/info")
    public CommonResponse getPlaceInfo() {
        long total = parkingPlaceService.count();
        long available = parkingPlaceService.count(new QueryWrapper<>(new ParkingPlace())
                .eq("status", 0)
        );
        PlaceInfo info  = new PlaceInfo(total, available);
        return CommonResponse.success(info);
    }

    @GetMapping("/status/{status}")
    public CommonResponse getByStatus(@PathVariable Integer status) {
        List<ParkingPlace> list = parkingPlaceService.list(
                new QueryWrapper<>(new ParkingPlace())
                        .eq("status", status));
        List<String> numbers = new ArrayList<>();
        for (ParkingPlace place : list) {
            numbers.add(place.getArea() + String.format("%03d", place.getNumber()));
        }
        return CommonResponse.success(numbers);
    }

    @GetMapping("/recommend")
    public CommonResponse getPopularPlace() {
        List<String> recommend = parkingPlaceService.getRecommend(0.5, 0.5);
        return CommonResponse.success(recommend);
    }
}
