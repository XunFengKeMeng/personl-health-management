package com.example.health.mapper;

import com.example.health.pojo.entity.HealthRecordDO;
import com.example.health.pojo.vo.HealthRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/5 09:48
 * @description: 用户健康记录持久层接口
 */
@Mapper
public interface HealthRecordMapper {
    /**
     * 新增若干健康记录信息
     *
     * @param records 新增的健康记录信息
     */
    void saveRecords(List<HealthRecordDO> records);

    /**
     * 批量删除健康记录信息
     *
     * @param ids 待删除的健康记录ID集合
     */
    void deleteRecords(@Param(value = "ids") List<Long> ids);

    /**
     * 更新健康记录信息
     *
     * @param record 修改后的健康记录信息
     */
    void update(HealthRecordDO record);

    /**
     * 分页查询健康记录信息
     *
     * @param record 查询参数（用户ID，指标ID）
     * @param current 当前页
     * @param size 页大小
     * @return 满足条件的健康记录列表
     */
    List<HealthRecordVO> queryRecords(
            @Param("record") HealthRecordDO record,
            @Param("current") Integer current,
            @Param("size") Integer size);

    /**
     * 满足分页查询条件的数据数
     *
     * @param record 查询参数
     * @return 满足分页查询条件的数据数
     */
    Integer queryRecordsCount(HealthRecordDO record);
}
