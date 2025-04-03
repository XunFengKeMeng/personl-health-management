package com.example.health.mapper;

import com.example.health.pojo.entity.FormItemDO;
import com.example.health.pojo.vo.FormItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/2 13:25
 * @description: 表单项持久层接口
 */
@Mapper
public interface FormItemMapper {
    /**
     * 新增表单项
     *
     * @param formItemDO 新增表单项信息
     */
    void saveFormItem(FormItemDO formItemDO);

    /**
     * 批量删除表单项
     *
     * @param ids 待删除的表单项ID集合
     */
    void deleteFormItems(@Param(value = "ids") List<Integer> ids);

    /**
     * 修改表单项信息
     *
     * @param formItemDO 修改后的表单信息
     */
    void updateFormItem(FormItemDO formItemDO);

    /**
     * 模糊查询表单项
     *
     * @param formItemDO 查询参数
     * @param current 当前页
     * @param size 页大小
     * @return 查询结果（列表）
     */
    List<FormItemVO> queryFormItems(
            @Param("item") FormItemDO formItemDO,
            @Param("current") Integer current,
            @Param("size") Integer size);

    /**
     * 满足模糊查询的表单项总数
     *
     * @param formItemDO 查询参数
     * @return 满足模糊查询的表单项总数
     */
    Integer queryFormItemsCount(FormItemDO formItemDO);
}
