package com.verge.parking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.verge.parking.common.CommonResponse;
import com.verge.parking.common.UserContextHolder;
import com.verge.parking.controller.vo.ReserveInfo;
import com.verge.parking.entity.ParkingOrder;
import com.verge.parking.entity.ParkingPlace;
import com.verge.parking.entity.enums.OrderStatus;
import com.verge.parking.service.IParkingOrderService;
import com.verge.parking.service.IParkingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/reserve")
    public CommonResponse getReserveInfo() {
        Integer userId = UserContextHolder.getLoginInfo().getUserId();
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
        reserveInfo.setPlace(place);

        return CommonResponse.success(reserveInfo);
    }

    @PostMapping("/reserve/{placeNum}")
    public CommonResponse reserve(@PathVariable("placeNum") String placeNum){
        String area = placeNum.substring(0, 1);
        Integer number = Integer.valueOf(placeNum.substring(1));
        ParkingPlace place = parkingPlaceService.getOne(new QueryWrapper<>(new ParkingPlace())
                .eq("area", area)
                .eq("number", number)
        );
        Integer userId = UserContextHolder.getLoginInfo().getUserId();
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

    @PostMapping("/unlock/{orderId}")
    public CommonResponse unlockPlace(@PathVariable("orderId") Integer orderId) {
        if (parkingOrderService.unlockPlace(orderId)) {
            return CommonResponse.success();
        } else {
            return CommonResponse.fail();
        }
    }

    @PostMapping("/lock/{orderId}")
    public CommonResponse lockPlace(@PathVariable("orderId") Integer orderId) {
        if (parkingOrderService.lockPlace(orderId)) {
            return CommonResponse.success();
        } else {
            return CommonResponse.fail();
        }
    }

    @GetMapping("/popular/time")
    public CommonResponse getPopularTimePeriod() {
        int[] popularHours = parkingOrderService.getPopularHours();
        return CommonResponse.success(popularHours);
    }

    @GetMapping("/usage/{hour}")
    public CommonResponse getUsage(@PathVariable("hour") Integer hour) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusHours(hour);
        List<ParkingPlace> places = parkingPlaceService.list(new QueryWrapper<>(new ParkingPlace())
                .select("id", "area", "number")
        );
        Map<String, Double> res = new HashMap<>();
        for (ParkingPlace place : places) {
            List<ParkingOrder> orders = parkingOrderService.list(new QueryWrapper<>(new ParkingOrder())
                    .eq("parking_place_id", place.getId())
                    .between("start_time", oneDayAgo, now)
                    .and(i -> i.eq("order_status", OrderStatus.FINISHED.getValue()).or().eq("order_status", OrderStatus.WAITING_PAY.getValue()))
            );
            int cnt = 0;
            for (ParkingOrder order : orders) {
                Duration duration = Duration.between(order.getStartTime(), order.getStopTime());
                cnt += duration.toMinutes();
            }
            Double rate = cnt/(hour * 60.0);

            res.put(place.getArea() + place.getNumber(), rate);
        }

        return CommonResponse.success(res);
    }
    @GetMapping("/traffic/{hour}")
    public CommonResponse getTraffic(@PathVariable("hour") Integer hour) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourAgo = now.minusHours(hour);
        long size = parkingOrderService.count(new QueryWrapper<>(new ParkingOrder())
                .between("start_time", oneHourAgo, now)
        );
        return CommonResponse.success(size);
    }
}
