package com.verge.parking.controller;

import com.verge.parking.common.CommonResponse;
import com.verge.parking.service.IParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/parkingOrder")
public class ParkingOrderController {
    @Autowired
    private IParkingOrderService parkingOrderService;

    @PostMapping("/order")
    public CommonResponse reserve(String macAddress, Integer userId){
        if (parkingOrderService.reserve(macAddress, userId)) {
            return CommonResponse.success();
        } else {
            return CommonResponse.fail();
        }
    }

    @GetMapping("/unlockKey/{orderId}")
    public CommonResponse getUnlockKey(@PathVariable Integer orderId) {
        return CommonResponse.success(parkingOrderService.getUnlockKey(orderId));
    }
}
