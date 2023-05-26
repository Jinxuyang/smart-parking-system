package com.verge.parking.service;

import com.verge.parking.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Verge
 * @since 2023-04-09
 */
public interface IUserService extends IService<User> {
    boolean register(User user);

    boolean login(String username, String password);
}
