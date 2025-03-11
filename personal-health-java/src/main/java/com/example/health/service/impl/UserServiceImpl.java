package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.em.ActiveStatusEnum;
import com.example.health.em.RoleEnum;
import com.example.health.mapper.UserMapper;
import com.example.health.pojo.dto.query.extend.UserQueryDTO;
import com.example.health.pojo.dto.update.UserLoginDTO;
import com.example.health.pojo.dto.update.UserRegisterDTO;
import com.example.health.pojo.dto.update.UserUpdateDTO;
import com.example.health.pojo.entity.UserDO;
import com.example.health.pojo.vo.UserVO;
import com.example.health.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author huanghaiming
 * @date 2025/3/10 11:05
 * @description: 用户服务
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Mapper自动装配
     */
    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     * @param userRegisterDTO 用户注册信息
     * @return 注册结果提示信息
     */
    @Override
    public ApiResponse<String> register(UserRegisterDTO userRegisterDTO) {
        // 根据用户账号查询数据库得到对应的用户数据
        UserDO userDO = userMapper.getByCondition(
                UserDO.builder().userAccount(userRegisterDTO.getUserAccount()).build()
        );
        // 判断用户账号是否已存在
        return Optional.ofNullable(userDO)
                .map(user -> {
                    // 显式指定泛型类型
                    return ApiResponse.<String>error("用户已存在");
                })
                .orElseGet(() -> {
                    // 新增用户数据（身份默认为用户，且默认账号未激活）
                    UserDO user = UserDO.builder()
                            .userAccount(userRegisterDTO.getUserAccount())
                            .userName(userRegisterDTO.getUserName())
                            .userPassword(userRegisterDTO.getUserPassword())
                            .userEmail(userRegisterDTO.getUserEmail())
                            .userRole(RoleEnum.USER.getRoleCode())
                            .active(ActiveStatusEnum.INACTIVE.getStatusCode())
                            .userCreateTime(LocalDateTime.now())
                            .build();
                    userMapper.insertUser(user);
                    return ApiResponse.success("注册成功");
        });
    }

    /**
     * 后台新增用户
     *
     * @param userRegisterDTO 管理员输入注册用户信息
     * @return 用户新增响应结果
     */
    @Override
    public ApiResponse<String> insertUser(UserRegisterDTO userRegisterDTO) {
        return register(userRegisterDTO);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 用户登录响应结果
     */
    @Override
    public ApiResponse<UserDO> login(UserLoginDTO userLoginDTO) {
        // 根据用户账号查询数据库得到对应的用户数据
        UserDO userDO = userMapper.getByCondition(
                UserDO.builder().userAccount(userLoginDTO.getUserAccount()).build()
        );
        // 检查用户是否存在
        return Optional.ofNullable(userDO)
                .map(user -> {
                    // 检查密码是否正常
                    if (!user.getUserPassword().equals(userLoginDTO.getUserPassword())) {
                        return ApiResponse.<UserDO>error("密码错误");
                    }
                    // 检查账号是否已激活
                    if (!user.getActive()) {
                        return ApiResponse.<UserDO>error("账号未激活，请联系管理员");
                    }
                    // 用户登录成功
                    return ApiResponse.success("登录成功",user);
                }).orElseGet(() -> ApiResponse.error("用户不存在"));
    }

    @Override
    public ApiResponse<List<UserDO>> queryUsers(UserQueryDTO userQueryDTO) {
        return null;
    }

    @Override
    public ApiResponse<String> updateSelf(UserUpdateDTO userUpdateDTO) {
        return null;
    }

    @Override
    public ApiResponse<String> updatePassword(Map<String, String> map) {
        return null;
    }

    @Override
    public ApiResponse<String> updateByAdmin(UserUpdateDTO userUpdateDTO) {
        return null;
    }

    @Override
    public ApiResponse<String> deleteUsers(List<Integer> ids) {
        return null;
    }

    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 若用户存在则返回用户信息，若不存在则返回error
     */
    @Override
    public ApiResponse<UserVO> getByUserId(Integer userId) {
        UserDO userDO = userMapper.getByCondition(UserDO.builder().userId(userId).build());
        // Optional判空，并根据判空结果调用相应的响应方法作为返回值。
        return Optional.ofNullable(userDO)
                .map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO);
                    return ApiResponse.success(userVO);
                }).orElseGet(()-> ApiResponse.error("用户不存在"));
    }
}
