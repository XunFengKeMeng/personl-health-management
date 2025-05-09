package com.example.health.pojo.dto.query.extend;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

import java.io.Serializable;

/**
 * @author huanghaiming
 * @date 2025/3/5 10:57
 * @description: 用户查询DTO，Service层向外传输的对象。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryDTO extends QueryDTO implements Serializable {
    /**
     * 用户ID
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
     * 用户角色（0：普通用户，1：管理员）
     */
    private Boolean userRole;

    /**
     * 用户账号状态（0：可用，1：未激活）
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
