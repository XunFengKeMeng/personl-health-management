package com.example.health.mapper;

import com.example.health.pojo.dto.query.extend.UserQueryDTO;
import com.example.health.pojo.entity.UserDO;
import com.example.health.pojo.vo.HealthStatusStatisticsVO;
import com.example.health.pojo.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/5 09:47
 * @description: 用户持久层接口
 */
@Mapper
public interface UserMapper {

    /**
     * 用户信息新增
     * @param userDO 用户信息
     */
    void insertUser(UserDO userDO);

    /**
     * 批量删除用户信息
     *
     * @param ids 需删除的用户ID集合
     */
    void deleteUsers(@Param(value = "ids") List<Integer> ids);

    /**
     * 更新用户信息
     *
     * @param userDO 用户信息
     */
    void updateUser(UserDO userDO);

    /**
     * 根据用户id/用户账号/用户名查询用户
     * @param userDO 包含用户id的用户信息
     * @return 用户的全部信息
     */
    UserDO getByCondition(UserDO userDO);

    /**
     * 模糊查询用户（分页）
     *
     * @param userQueryDTO 用户查询条件参数
     * @return 符合条件的若干用户数据
     */
    List<UserVO> queryUsers(UserQueryDTO userQueryDTO);

    /**
     * 获取符合分页查询条件的用户记录总数
     *
     * @param userQueryDTO 用户查询条件参数
     * @return 符合图条件的用户记录数
     */
    int queryCount(UserQueryDTO userQueryDTO);

    /**
     * 获取健康状态码及每个健康状态下的用户数
     */
    List<HealthStatusStatisticsVO> queryHealthStatusStatistics();
}
