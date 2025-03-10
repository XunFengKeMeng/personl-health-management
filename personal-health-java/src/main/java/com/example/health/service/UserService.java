package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.vo.UserVO;
import org.springframework.stereotype.Service;

/**
 * @author huanghaiming
 * @date 2025/3/9 17:03
 * @description: 用户服务
 */
@Service
public interface UserService {
    ApiResponse<UserVO> getById(Integer id);
}
