package com.verge.parking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.verge.parking.common.CommonResponse;
import com.verge.parking.controller.vo.ReserveInfo;
import com.verge.parking.entity.ParkingOrder;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.entity.enums.OrderStatus;
import com.verge.parking.service.IParkingOrderService;
import com.verge.parking.service.IParkingPlaceService;
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
    @Autowired
    private IParkingPlaceService parkingPlaceService;

    @GetMapping("/reserve/{userId}")
    public CommonResponse getReserveInfo(@PathVariable Integer userId) {
        ParkingOrder order = parkingOrderService.getOne(new QueryWrapper<>(new ParkingOrder())
                .eq("user_id", userId)
                .ne("order_status", OrderStatus.FINISHED.getValue())
        );
        if (order == null) {
            // 没有预约
            return CommonResponse.success();
        }
        ParkingPlace place = parkingPlaceService.getById(order.getParkingPlaceId());

        ReserveInfo reserveInfo = new ReserveInfo();
        reserveInfo.setOrderStatus(order.getOrderStatus().getValue());
        reserveInfo.setParkingPlaceNum(place.getArea() + String.format("%02x", place.getNumber()));

        return CommonResponse.success(reserveInfo);
    }
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
