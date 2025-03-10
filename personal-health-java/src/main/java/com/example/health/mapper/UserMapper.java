package com.example.health.mapper;

import com.example.health.pojo.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author huanghaiming
 * @date 2025/3/5 09:47
 * @description:
 */
@Mapper
public interface UserMapper {
    UserDO getById(UserDO userDO);
}
