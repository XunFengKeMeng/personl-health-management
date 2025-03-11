package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.update.UserLoginDTO;
import com.example.health.pojo.dto.update.UserRegisterDTO;
import com.example.health.pojo.entity.UserDO;
import com.example.health.pojo.vo.UserVO;
import com.example.health.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author huanghaiming
 * @date 2025/3/10 12:07
 * @description:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 自动装配服务
     */
    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册信息（用户账号、用户名、用户密码、用户邮箱）
     * @return 注册响应结果
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public ApiResponse<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        System.out.println(userRegisterDTO.getUserAccount());
        return userService.register(userRegisterDTO);
    }

    /**
     * 管理员新增用户
     */
    public ApiResponse<String> insertUser(UserRegisterDTO userRegisterDTO) {
        // TODO...
        return null;
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息（用户账号、密码）
     * @return 登录响应结果
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public ApiResponse<UserDO> login(@RequestBody UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }


}
