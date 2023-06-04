package com.verge.parking.controller;

import com.verge.parking.common.CommonResponse;
import com.verge.parking.common.JWTUtils;
import com.verge.parking.common.UserContextHolder;
import com.verge.parking.controller.vo.AuthInfo;
import com.verge.parking.entity.User;
import com.verge.parking.service.IUserService;
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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public CommonResponse register(@RequestBody AuthInfo auth) {
        User user = new User(auth.getUsername() , auth.getPassword());
        if (userService.register(user)) {
            return CommonResponse.success("注册成功");
        } else {
            return CommonResponse.fail();
        }
    }

    @PostMapping("/login")
    public CommonResponse login(@RequestBody AuthInfo auth) {
        if (userService.login(auth.getUsername(), auth.getPassword())) {
            String token = JWTUtils.createToken(UserContextHolder.getLoginInfo().getUserId());
            return CommonResponse.success(token);
        } else {
            return CommonResponse.fail("登录失败");
        }
    }
}
