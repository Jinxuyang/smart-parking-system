package com.verge.parking.controller;

import com.verge.parking.common.CommonResponse;
import com.verge.parking.entity.ParkingOrder;
import com.verge.parking.service.IParkingOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Verge
 * @since 2023-04-09
 */
@Controller
@RequestMapping("/parkingOrder")
public class ParkingOrderController {
    @Resource
    private IParkingOrderService parkingOrderService;

    @PostMapping("/order")
    public CommonResponse order(Integer userId, Integer parkingPlaceId){
        ParkingOrder order = ParkingOrder.create(userId, parkingPlaceId);
        if (parkingOrderService.save(order)) {
            return CommonResponse.success();
        } else {
            return CommonResponse.fail();
        }
    }
}
