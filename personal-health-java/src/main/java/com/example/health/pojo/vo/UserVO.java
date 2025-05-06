package com.example.health.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/9 17:54
 * @description: 用户视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;
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
    private Integer userRole;

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
