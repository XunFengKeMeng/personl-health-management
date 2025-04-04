package com.example.health.mapper;

import com.example.health.pojo.entity.SubmissionDO;
import com.example.health.pojo.vo.SubmissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/4 11:19
 * @description: 表单提交记录持久层接口
 */
@Mapper
public interface SubmissionMapper {
    /**
     * 新增表单提交记录（不包含关联项）
     *
     * @param submissionDO 新增的表单提交记录信息
     */
    void save(SubmissionDO submissionDO);

    /**
     * 删除表单提交记录（不包含关联项）
     *
     * @param submissionId 待删除的表单提交记录
     */
    void delete(Integer submissionId);

    /**
     * 修改表单提交记录（不包含关联项）
     * 主要修改审核状态
     *
     * @param submissionDO 修改后的表单提交记录信息
     */
    void update(SubmissionDO submissionDO);

    /**
     * 模糊查询表单提交信息
     *
     * @param templateName 表单模板名（部分）
     * @param userName 用户名（部分）
     * @param current 当前页
     * @param size 页大小
     * @return 满足查询条件的提交表单列表
     */
    List<SubmissionVO> query(
            @Param("templateName") String templateName,
            @Param("userName") String userName,
            @Param("current") Integer current,
            @Param("size") Integer size);

    /**
     * 满足模糊查询条件的表单提交信息数量
     *
     * @param templateName 表单模板名（部分）
     * @param userName 用户名（部分）
     * @return 满足查询条件的的数据数量
     */
    Integer count(
            @Param("templateName") String templateName,
            @Param("userName") String userName);

    /**
     * 根据提交ID查询表单记录结果
     *
     * @param submissionId 待查询的提交表单ID
     * @return 指定提交表单记录的数据（不包含表单项数据）
     */
    SubmissionVO queryById(Integer submissionId);
}
