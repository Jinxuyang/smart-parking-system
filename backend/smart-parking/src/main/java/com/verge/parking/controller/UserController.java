package com.verge.parking.controller;

import com.verge.parking.common.CommonResponse;
import com.verge.parking.entity.User;
import com.verge.parking.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Verge
 * @since 2023-04-09
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public CommonResponse register(String username, String password) {
        User user = new User(username ,password);
        if (userService.register(user)) {
            return CommonResponse.success();
        } else {
            return CommonResponse.fail();
        }
    }
}
