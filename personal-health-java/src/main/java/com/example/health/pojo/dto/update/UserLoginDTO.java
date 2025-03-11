package com.example.health.pojo.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/3/10 18:40
 * @description: 用户登录DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;
}
