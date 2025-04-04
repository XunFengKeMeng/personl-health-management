package com.example.health.mapper;

import com.example.health.pojo.entity.FormDataDO;
import com.example.health.pojo.vo.FormDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/3 19:21
 * @description: 表单数据持久层接口
 */
@Mapper
public interface FormDataMapper {
    /**
     * 新增提交表单项数据（多条）
     *
     * @param formDataDOList 新增的提交表单项数据
     */
    void batchSave(List<FormDataDO> formDataDOList);

    /**
     * 删除提交表单的所有表单项数据
     *
     * @param submissionId 待删除的提交表单ID
     */
    void deleteBySubmissionId(Integer submissionId);

    /**
     * 更新提交表单
     *
     * @param formDataDOList 修改后表单下的表单项数据
     */
    void update(List<FormDataDO> formDataDOList);

    /**
     * 查询提交表单的详细数据
     *
     * @param submissionId 待查询的提交表单ID
     * @param templateId 表单模板ID
     * @return 提交表单下所有的表单项数据
     */
    List<FormDataVO> queryBySubmissionId(
            @Param("sid") Integer submissionId,
            @Param("tid") Integer templateId);
}
