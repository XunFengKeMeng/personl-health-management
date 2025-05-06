package com.example.health.pojo.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/3/10 18:41
 * @description: 用户信息更新DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    /**
     * 用户ID（不可修改，仅限定位用户）
     */
    private Integer userId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户密码（用户需要单独的修改密码操作）
     */
    private String userPassword;

    /**
     * 用户角色（管理员端才可修改）
     */
    private Integer userRole;

    /**
     * 用户账号状态（管理员端才可修改）
     */
    private Boolean active;

    /**
     * 性别（0女1男）
     */
    private Integer sex;

    /**
     * 健康状态（0未知，1未见异常，2疑似职业病，3职业禁忌病，4其他疾患，5职业病）
     */
    private Integer healthStatus;

    /**
     * 部门
     */
    private String departmentName;
}
