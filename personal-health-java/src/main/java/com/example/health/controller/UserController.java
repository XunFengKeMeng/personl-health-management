package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.query.extend.UserQueryDTO;
import com.example.health.pojo.dto.update.UserLoginDTO;
import com.example.health.pojo.dto.update.UserRegisterDTO;
import com.example.health.pojo.dto.update.UserUpdateDTO;
import com.example.health.pojo.entity.UserDO;
import com.example.health.pojo.vo.HealthStatusStatisticsVO;
import com.example.health.pojo.vo.UserVO;
import com.example.health.service.UserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author huanghaiming
 * @date 2025/3/10 12:07
 * @description: 用户模块Controller（增删改查、登录注册）
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
        return userService.register(userRegisterDTO);
    }

    /**
     * 管理员新增用户
     */
    @PostMapping(value = "/insert")
    @ResponseBody
    @RequiresRoles("admin")
    public ApiResponse<String> insertUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.insertUser(userRegisterDTO);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 登录响应结果
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public ApiResponse<Object> login(@RequestBody UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }

    /**
     * token校验
     *
     * @return 校验结果，若通过令牌校验则返回用户数据
     */
    @PostMapping(value = "/auth")
    @ResponseBody
    public ApiResponse<UserVO> auth(@RequestBody  Map<String, String> request) {
        String token = request.get("token");
        return userService.auth(token);
    }


    /**
     * 管理员批量删除用户
     *
     * @param ids 用户ID集合
     * @return 删除操作响应结果
     */
    @PostMapping(value = "/deleteUsers")
    @ResponseBody
    @RequiresRoles("admin")
    public ApiResponse<String> deleteUsers(@RequestBody List<Integer> ids) {
        return userService.deleteUsers(ids);
    }

    /**
     * 修改用户信息（用户）
     *
     * @param userUpdateDTO 用户ID + 修改后的用户信息（用户账号、用户名、用户邮箱）
     * @return 修改操作响应结果
     */
    @PostMapping(value = "/updateSelf")
    @ResponseBody
    @RequiresRoles("user")
    public ApiResponse<String> updateSelf(@RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.updateSelf(userUpdateDTO);
    }

    /**
     * 修改密码（用户）
     *
     * @param map 用户账号与新密码
     * @return 修改操作响应结果
     */
    @PostMapping(value = "/updatePassword")
    @ResponseBody
//    @RequiresRoles("user")
    public ApiResponse<String> updatePassword(@RequestBody Map<String, String> map) {
        return userService.updatePassword(map);
    }

    /**
     * 修改用户信息（管理员）
     *
     * @param userUpdateDTO 用户ID + 修改后的用户信息（用户账号、用户名、用户邮箱 + 密码 + 用户角色、用户账号激活状态）
     * @return 修改操作响应结果
     */
    @PostMapping(value = "/updateByAdmin")
    @ResponseBody
    @RequiresRoles("admin")
    public ApiResponse<String> updateByAdmin(@RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.updateByAdmin(userUpdateDTO);
    }

    /**
     * 分页+模糊查询用户（管理员）
     *
     * @param userQueryDTO 查询参数
     * @return 查询操作响应结果 + 满足查询参数的用户信息列表
     */
    @PostMapping(value = "/queryUsers")
    @ResponseBody
    @RequiresRoles("admin")
    public ApiResponse<List<UserVO>> queryUsers(@RequestBody UserQueryDTO userQueryDTO) {
        return userService.queryUsers(userQueryDTO);
    }

    /**
     * 通过用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 包含用户信息的响应结果
     */
    @GetMapping(value = "/getByUserId/{userId}")
    @ResponseBody
    public ApiResponse<UserVO> getByUserId(@PathVariable Integer userId) {
        return userService.getByUserId(userId);
    }

    /**
     * 获取健康状态码及每个健康状态下的用户数
     */
    @GetMapping(value = "/getHealthStatusStatistics")
    @ResponseBody
    @RequiresRoles("admin")
    public ApiResponse<List<HealthStatusStatisticsVO>> getHealthStatusStatistics() {
        return userService.getHealthStatusStatistics();
    }
}
