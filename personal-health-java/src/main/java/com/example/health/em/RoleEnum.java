package com.example.health.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huanghaiming
 * @date 2025/3/11 14:34
 * @description: 用户身份枚举
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    USER(0, "用户"),
    ADMIN(1, "管理员");

    /**
     * 角色编码
     */
    private final Integer roleCode;
    /**
     * 角色名
     */
    private final String roleName;

    /**
     * 由角色编码获取角色名
     *
     * @param role 角色编码
     * @return 角色名
     */
    public static String getRoleNameByCode(Integer roleCode) {
        for (RoleEnum value : RoleEnum.values()) {
            if (value.getRoleCode().equals(roleCode)) {
                return value.getRoleName();
            }
        }
        return null;
    }
}
