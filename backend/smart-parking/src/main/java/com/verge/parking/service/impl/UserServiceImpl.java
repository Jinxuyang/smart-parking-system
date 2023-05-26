package com.verge.parking.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.verge.parking.common.JWTUtils;
import com.verge.parking.common.LoginInfo;
import com.verge.parking.common.UserContextHolder;
import com.verge.parking.entity.User;
import com.verge.parking.mapper.UserMapper;
import com.verge.parking.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean register(User user) {
        user.setPassword(SecureUtil.md5(user.getPassword()));
        return userMapper.insert(user) == 1;
    }

    @Override
    public boolean login(String username, String password) {
        User user = this.getOne(new QueryWrapper<>(new User())
                .eq("username", username)
        );
        if (user == null) return false;

        if (user.getPassword().equals(SecureUtil.md5(password))) {
            UserContextHolder.setUserInfo(new LoginInfo(user.getId()));
            return true;
        } else {
            return false;
        }
    }
}
