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


//    @GetMapping("/testInsert")
//    public CommonResponse insert() {
//        Set<Integer> set = new HashSet<>();
//        set.add(0);
//        for (int i = 0; i < 6; i++) {
//            Random random = new Random();
//            ParkingPlace place = new ParkingPlace();
//            place.setId(getMac().toUpperCase());
//            place.setStatus(ParkingPlaceStatus.OCCUPIED);
//            place.setArea("B");
//
//            int randomNumber = 0;
//            while (set.contains(randomNumber)) {
//                randomNumber = random.nextInt(20) + 1;
//            }
//            set.add(randomNumber);
//
//            place.setNumber(randomNumber);
//            place.setFloor("1");
//            place.setLockStatus(false);
//            System.out.println(place);
//            parkingPlaceService.save(place);
//        }
//        for (int i = 0; i < 14; i++) {
//            Random random = new Random();
//            ParkingPlace place = new ParkingPlace();
//            place.setId(getMac().toUpperCase());
//            place.setStatus(ParkingPlaceStatus.AVAILABLE);
//            place.setArea("B");
//
//            int randomNumber = 0;
//            while (set.contains(randomNumber)) {
//                randomNumber = random.nextInt(20) + 1;
//            }
//            set.add(randomNumber);
//
//            place.setNumber(randomNumber);
//            place.setFloor("1");
//            place.setLockStatus(true);
//            System.out.println(place);
//            parkingPlaceService.save(place);
//        }
//        return null;
//    }
//
//    public String getMac() {
//        Random random = new Random();
//        byte[] mac = new byte[6];
//        random.nextBytes(mac);
//        mac[0] = (byte) (mac[0] & (byte) 254);
//        StringBuilder sb = new StringBuilder(18);
//        for (byte b : mac) {
//            if (sb.length() > 0)
//                sb.append(":");
//            sb.append(String.format("%02x", b));
//        }
//        return sb.toString();
//    }

}
