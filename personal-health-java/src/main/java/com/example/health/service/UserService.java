package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.query.extend.UserQueryDTO;
import com.example.health.pojo.dto.update.UserLoginDTO;
import com.example.health.pojo.dto.update.UserRegisterDTO;
import com.example.health.pojo.dto.update.UserUpdateDTO;
import com.example.health.pojo.entity.UserDO;
import com.example.health.pojo.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author huanghaiming
 * @date 2025/3/9 17:03
 * @description: 用户服务
 */
@Service
public interface UserService {
    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册信息
     * @return 响应注册是否成功
     */
    ApiResponse<String> register(UserRegisterDTO userRegisterDTO);

    /**
     * 管理员新增用户
     *
     * @param userRegisterDTO 管理员输入注册用户信息
     * @return 响应用户是否新增成功
     */
    ApiResponse<String> insertUser(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 响应登录是否成功
     */
    ApiResponse<UserDO> login(UserLoginDTO userLoginDTO);

    /**
     * 管理员批量删除用户
     *
     * @param ids 待删除的用户ID集合
     * @return 删除操作响应结果
     */
    ApiResponse<String> deleteUsers(List<Integer> ids);

    /**
     * 用户更新用户信息
     *
     * @param userUpdateDTO 修改后的用户信息
     * @return 更新操作响应结果
     */
    ApiResponse<String> updateSelf(UserUpdateDTO userUpdateDTO);

    /**
     * 用户修改密码
     *
     * @param map 用户账号及修改后的密码信息
     * @return 响应密码修改结果
     */
    ApiResponse<String> updatePassword(Map<String, String> map);

    /**
     * 管理员更新用户信息
     *
     * @param userUpdateDTO 修改后的用户数据
     * @return 更新响应结果
     */
    ApiResponse<String> updateByAdmin(UserUpdateDTO userUpdateDTO);

    /**
     * 管理员分页+模糊查询用户
     *
     * @param userQueryDTO 用户模糊查询参数
     * @return 符合要求的用户信息列表
     */
    ApiResponse<List<UserVO>> queryUsers(UserQueryDTO userQueryDTO);

    /**
     * 管理员根据用户ID查询信息
     *
     * @param userId 用户ID
     * @return 根据用户ID查询到的用户整体信息
     */
    ApiResponse<UserVO> getByUserId(Integer userId);
}
