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

    @GetMapping("/unlockKey/{orderId}")
    public CommonResponse getUnlockKey(@PathVariable Integer orderId) {
        return CommonResponse.success(parkingOrderService.getUnlockKey(orderId));
    }

    @GetMapping("/popular/time")
    public CommonResponse getPopularTimePeriod() {
        int[] popularHours = parkingOrderService.getPopularHours();
        return CommonResponse.success(popularHours);
    }

//    int MAX_USER_ID = 100;
//    int MAX_RANDOM_HOUR = 720; // 2 months = 60 days * 12 hours = 720 hours
//    int MAX_RANDOM_MINUTE = 60;
//    int MAX_RANDOM_SECOND = 60;
//    int MAX_STOP_TIME_DIFF = 3 * 60 * 60; // 3 hours in seconds
//    int MORNING_START_HOUR = 6; // 6 AM
//    int MORNING_END_HOUR = 11; // 11 AM
//    int AFTERNOON_START_HOUR = 13; // 1 PM
//    int AFTERNOON_END_HOUR = 18; // 6 PM
//    @GetMapping("/insertTest")
//    public CommonResponse insertTest() {
//        String[] PARKING_PLACES = {"16:7F:3B:BF:44:76", "18:AB:BE:D5:81:31", "20:01:20:5C:76:AA", "2A:5E:F5:2A:4A:DF", "2C:7B:AC:BE:B6:68", "30:83:98:A3:F2:3F", "32:64:00:95:0E:80", "3A:5F:60:22:D7:4D", "3A:C3:7B:D4:FE:8E", "40:B3:7C:57:0D:93", "42:24:A7:2A:9D:BE", "4C:F8:20:59:C7:33", "52:E2:60:D3:0E:99", "54:FA:B0:88:3C:01", "58:0C:C9:3D:86:46", "58:84:20:33:FC:EB", "58:D0:D6:F9:F3:43", "5A:F4:04:A1:35:4A", "5C:23:35:1D:87:66", "66:F5:CC:B3:0A:2D", "76:F1:83:A4:C8:6B", "7C:3F:19:79:A9:1F", "7C:83:9C:BC:A3:F1", "7E:59:2B:E4:68:AE", "80:97:93:C5:E9:E1", "88:0F:9D:3E:8B:3C", "A4:08:7B:C8:0B:00", "A4:E3:75:69:A6:CF", "B0:84:63:C7:72:37", "C8:04:38:9E:8B:99", "CA:A0:5F:67:2B:E2", "CC:0C:94:D4:76:A4", "CC:A8:B0:5B:D6:4E", "D4:D9:F5:A8:1F:A8", "D6:30:16:5A:D9:B0", "D6:DF:BF:BF:01:BD", "D8:3A:70:58:66:4C", "DE:2B:53:2F:15:50", "EC:4A:B3:8F:9B:E7", "FC:5F:A5:57:18:A6"};
//
//        for (int i = 0; i < 1000; i++) {
//            Random random = new Random();
//            int userId = random.nextInt(MAX_USER_ID + 1);
//            String parkingPlaceId = PARKING_PLACES[random.nextInt(PARKING_PLACES.length)];
//            String unlockKey = generateRandomString(10);
//            LocalDateTime startTime = generateRandomStartTime(random, LocalDateTime.now().minusMinutes(random.nextInt(MAX_RANDOM_HOUR * MAX_RANDOM_MINUTE)).minusSeconds(random.nextInt(MAX_RANDOM_SECOND)));
//            int parkingTime = random.nextInt(MAX_STOP_TIME_DIFF);
//            LocalDateTime stopTime = startTime.plusSeconds(random.nextInt(MAX_STOP_TIME_DIFF));
//
//            ParkingOrder order = new ParkingOrder();
//            order.setUserId(userId);
//            order.setParkingPlaceId(parkingPlaceId);
//            order.setOrderStatus(OrderStatus.FINISHED);
//            order.setStartTime(startTime);
//            order.setStopTime(stopTime);
//            order.setUnlockKey(unlockKey);
//            order.setFee(parkingTime / 60 / 60 * 3);
//            parkingOrderService.save(order);
//        }
//
//        return null;
//    }
//    private LocalDateTime generateRandomStartTime(Random random, LocalDateTime now) {
//
//        LocalDateTime morningStart = now.withHour(MORNING_START_HOUR).withMinute(0).withSecond(0).withNano(0);
//        LocalDateTime morningEnd = now.withHour(MORNING_END_HOUR).withMinute(0).withSecond(0).withNano(0);
//        LocalDateTime afternoonStart = now.withHour(AFTERNOON_START_HOUR).withMinute(0).withSecond(0).withNano(0);
//        LocalDateTime afternoonEnd = now.withHour(AFTERNOON_END_HOUR).withMinute(0).withSecond(0).withNano(0);
//        if (now.isBefore(morningStart)) {
//            // If current time is before morning start, use morning start as the earliest possible start time
//            return morningStart.minusMinutes(random.nextInt(MAX_RANDOM_HOUR * MAX_RANDOM_MINUTE)).minusSeconds(random.nextInt(MAX_RANDOM_SECOND));
//        } else if (now.isAfter(afternoonEnd)) {
//            // If current time is after afternoon end, use morning start of tomorrow as the earliest possible start time
//            return morningStart.plusDays(1).minusMinutes(random.nextInt(MAX_RANDOM_HOUR * MAX_RANDOM_MINUTE)).minusSeconds(random.nextInt(MAX_RANDOM_SECOND));
//        } else if (now.isAfter(morningEnd) && now.isBefore(afternoonStart)) {
//            // If current time is between morning end and afternoon start, use afternoon start as the earliest possible start time
//            return afternoonStart.minusMinutes(random.nextInt(MAX_RANDOM_HOUR * MAX_RANDOM_MINUTE)).minusSeconds(random.nextInt(MAX_RANDOM_SECOND));
//        } else {
//            // If current time is within morning or afternoon, use current time as the earliest possible start time
//            int startHour, endHour;
//            if (now.isAfter(morningStart) && now.isBefore(morningEnd)) {
//                // If current time is within morning, set startHour to current hour and endHour to morning end hour
//                startHour = now.getHour();
//                endHour = MORNING_END_HOUR;
//            } else {
//                // If current time is within afternoon, set startHour to afternoon start hour and endHour to current hour
//                startHour = AFTERNOON_START_HOUR;
//                endHour = now.getHour();
//            }
//            // Calculate the earliest possible start time based on startHour and endHour
//            LocalDateTime startTime = now.withHour(startHour).withMinute(0).withSecond(0).withNano(0);
//            if (endHour - startHour == 0) {
//                endHour ++;
//            }
//            startTime = startTime.minusMinutes(random.nextInt((Math.abs(endHour - startHour)) * MAX_RANDOM_MINUTE)).minusSeconds(random.nextInt(MAX_RANDOM_SECOND));
//            return startTime;
//        }
//    }
//    private static String generateRandomString(int length) {
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            sb.append((char) (random.nextInt(26) + 'a'));
//        }
//        return sb.toString();
//    }
}
