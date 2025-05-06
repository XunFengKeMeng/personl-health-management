package com.example.health.pojo.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/3/10 18:37
 * @description: 用户注册DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 性别（0女1男）
     */
    private Integer sex;

    /**
     * 部门
     */
    private String departmentName;
}
