package com.example.health.mapper;

import com.example.health.pojo.entity.FormTemplateDO;
import com.example.health.pojo.vo.FormTemplateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/2 15:35
 * @description: 表单模板持久层接口
 */
@Mapper
public interface FormTemplateMapper {
    /**
     * 新增表单模板
     *
     * @param formTemplateDO 新增表单模板信息（不包含表单项）
     */
    void save(FormTemplateDO formTemplateDO);

    /**
     * 删除表单模板（不包含表单项）
     *
     * @param templateId 待删除的表单模板ID
     */
    void deleteByTemplateId(Integer templateId);

    /**
     * 更新表单模板信息（不包含表单项）
     *
     * @param formTemplateDO 修改后的表单模板信息
     */
    void update(FormTemplateDO formTemplateDO);

    /**
     * 模糊查询表单信息
     *
     * @param formTemplateDO 查询参数
     * @param current 当前页
     * @param size 页大小
     * @return 查询结果
     */
    List<FormTemplateVO> query(
            @Param("formTemplate") FormTemplateDO formTemplateDO,
            @Param("current") Integer current,
            @Param("size") Integer size);

    /**
     * 满足模糊查询的表单模板数
     *
     * @param formTemplateDO 查询参数
     * @return 满足模糊查询的表单模板数
     */
    Integer count(FormTemplateDO formTemplateDO);

    /**
     * 根据表单模板ID查询表单模板数据（不包括关联项）
     *
     * @param templateId 表单模板ID
     * @return 查询结果 + 查询响应结果
     */
    FormTemplateVO queryByTemplateId(Integer templateId);
}
