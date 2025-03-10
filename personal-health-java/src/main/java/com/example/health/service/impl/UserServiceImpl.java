package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.mapper.UserMapper;
import com.example.health.pojo.entity.UserDO;
import com.example.health.pojo.vo.UserVO;
import com.example.health.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author huanghaiming
 * @date 2025/3/10 11:05
 * @description: 用户服务
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 自动装配
     */
    @Resource
    private UserMapper userMapper;

    @Override
    public ApiResponse<UserVO> getById(Integer userId) {
        UserDO userDO = userMapper.getById(UserDO.builder().userId(userId).build());
        // Optional判空，并根据判空结果调用相应的响应方法作为返回值。
        // TODO...

        return Optional.ofNullable(userDO)
                .map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO);
                    return ApiResponse.success(userVO);
                }).orElseGet(()-> ApiResponse.error("用户不存在"));
    }
}
