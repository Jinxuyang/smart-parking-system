package com.verge.parking.service.impl;

import com.verge.parking.entity.User;
import com.verge.parking.mapper.UserMapper;
import com.verge.parking.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private UserMapper userMapper;
    @Override
    public boolean register(User user) {
        if (userMapper.insert(user) == 1) {
            return true;
        }
        return false;
    }
}
