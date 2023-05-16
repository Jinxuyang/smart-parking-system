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
                .orderByDesc("create_time")
                .last("limit 1")
        );
        if (order == null) {
            // 没有预约
            return CommonResponse.success();
        }
        ParkingPlace place = parkingPlaceService.getById(order.getParkingPlaceId());

        ReserveInfo reserveInfo = new ReserveInfo();
        reserveInfo.setOrderStatus(order.getOrderStatus().getValue());
        reserveInfo.setParkingPlaceNum(place.getArea() + String.format("%03d", place.getNumber()));
        reserveInfo.setOrderId(order.getId());

        return CommonResponse.success(reserveInfo);
    }

    @PostMapping("/reserve")
    public CommonResponse reserve(@RequestParam("placeNum") String placeNum, @RequestParam("userId") Integer userId){
        String area = placeNum.substring(0, 1);
        Integer number = Integer.valueOf(placeNum.substring(1));
        ParkingPlace place = parkingPlaceService.getOne(new QueryWrapper<>(new ParkingPlace())
                .eq("area", area)
                .eq("number", number)
        );
        if (parkingOrderService.reserve(place.getId(), userId)) {
            return CommonResponse.success();
        } else {
            return CommonResponse.fail();
        }
    }

    @PutMapping("/cancel/{orderId}")
    public CommonResponse cancel(@RequestParam("orderId") int orderId) {
        ParkingOrder order = parkingOrderService.getById(orderId);
        if (order == null) {
            return CommonResponse.fail("订单不存在");
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        parkingOrderService.updateById(order);
        return CommonResponse.success("取消成功");
    }

    @PutMapping("/pay/{orderId}")
    public CommonResponse pay(@PathVariable("orderId") Integer orderId) {
        ParkingOrder order = parkingOrderService.getById(orderId);
        if (order == null) {
            return CommonResponse.fail("订单不存在");
        }

        order.setOrderStatus(OrderStatus.FINISHED);
        parkingOrderService.updateById(order);
        return CommonResponse.success("支付成功");
    }

    @GetMapping("/unlockKey/{orderId}")
    public CommonResponse getUnlockKey(@PathVariable Integer orderId) {
        return CommonResponse.success(parkingOrderService.getUnlockKey(orderId));
    }

    @GetMapping("/popular/time")
    public CommonResponse getPopularTimePeriod() {
        int[] popularHours = parkingOrderService.getPopularHours();
        return CommonResponse.success(popularHours);
    }

    @PostMapping("/unlock/{key}")
    public CommonResponse unlockPlace(@PathVariable("key") String key) {
        if (parkingOrderService.unlockPlace(key)) {
            return CommonResponse.success();
        } else {
            return CommonResponse.fail();
        }
    }
}
