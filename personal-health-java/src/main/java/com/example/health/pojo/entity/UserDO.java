package com.example.health.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/5 00:30
 * @description: 用户实体类，对应数据库表user
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDO {
    /**
     * 用户编号
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
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户角色（0：普通用户，1：管理员）
     * 默认为普通用户
     */
    private Boolean userRole;

    /**
     * 用户账号状态（0：待激活，1：已激活）
     * 注意：其对应的数据库字段为is_active，但是在POJO类中任何布尔类型的变量都不要加is前缀
     */
    private Boolean active;

    /**
     * 用户注册时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime userCreateTime;

}
