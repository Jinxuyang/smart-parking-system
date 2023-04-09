package com.verge.parking.service.impl;

import com.verge.parking.entity.ParkingOrder;
import com.verge.parking.mapper.ParkingOrderMapper;
import com.verge.parking.service.IParkingOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Verge
 * @since 2023-04-09
 */
@Service
public class ParkingOrderServiceImpl extends ServiceImpl<ParkingOrderMapper, ParkingOrder> implements IParkingOrderService {

}
