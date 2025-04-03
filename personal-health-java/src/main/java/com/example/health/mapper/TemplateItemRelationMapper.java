package com.example.health.mapper;

import com.example.health.pojo.entity.TemplateItemRelationDO;
import com.example.health.pojo.vo.TemplateItemRelationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/2 15:04
 * @description: 表单表单项关系持久层接口
 */
@Mapper
public interface TemplateItemRelationMapper {
    /**
     * 新增表单模板与表单项的关联信息（单条）
     * 该方法回使后端与数据库交互次数过多，后续可以考虑优化。
     *
     * @param templateItemRelationDO 新增的关联信息
     */
    void save(TemplateItemRelationDO templateItemRelationDO);

    /**
     * 新增表单模板与表单项的关联信息（多条）
     *
     * @param templateItemRelationDOList 新增的关联信息列表
     */
    void batchSave(List<TemplateItemRelationDO> templateItemRelationDOList);

    /**
     * 删除表单模板内所有关联表单项信息（每次修改表单模板需要删除所有关联表单项信息后进行重新添加）
     *
     * @param templateId 表单模板ID
     */
    void delete(Integer templateId);

    /**
     * 查询目标表单模板下所有表单项
     *
     * @param templateId 表单模板ID
     * @return 表单项列表
     */
    List<TemplateItemRelationVO> query(Integer templateId);
}
