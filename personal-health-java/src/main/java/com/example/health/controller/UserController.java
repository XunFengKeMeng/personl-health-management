package com.example.health.controller;

import com.example.health.api.ApiResponse;
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

    @Resource
    private UserService userService;

    @GetMapping(value = "/getById/{userId}")
    @ResponseBody
    public ApiResponse<UserVO> getById(@PathVariable Integer userId) {
        return userService.getById(userId);
    }
}
