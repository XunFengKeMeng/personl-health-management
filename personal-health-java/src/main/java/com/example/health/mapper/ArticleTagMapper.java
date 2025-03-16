package com.example.health.mapper;

import com.example.health.pojo.entity.ArticleTagDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/5 09:51
 * @description: 健康资讯标签持久层接口
 */
@Mapper
public interface ArticleTagMapper {
    /**
     * 新增标签
     *
     * @param articleTagDO 新增标签信息
     */
    void saveArticleTag(ArticleTagDO articleTagDO);

    /**
     * 批量删除标签
     *
     * @param ids 需要删除的标签ID集合
     */
    void deleteArticleTags(@Param(value = "ids") List<Integer> ids);

    /**
     * 更新标签信息
     *
     * @param articleTagDO 修改后的标签信息
     */
    void updateArticleTag(ArticleTagDO articleTagDO);

    /**
     * 模糊查询标签
     * -
     * @param articleTagDO 查询参数
     * @param current 当前页
     * @param size 页大小
     * @return 满足查询条件的标签列表
     */
    List<ArticleTagDO> queryArticleTags(
            @Param("tag") ArticleTagDO articleTagDO,
            @Param("current") Integer current,
            @Param("size") Integer size);

    /**
     * 获取满足模糊查询条件的标签数
     *
     * @param articleTagDO 查询参数
     * @return 满足查询条件的标签数量
     */
    Integer queryCount(ArticleTagDO articleTagDO);

    /**
     * 根据标签ID/标签名查询标签
     *
     * @param articleTagDO 标签部分信息
     * @return 查询到的标签信息
     */
    ArticleTagDO getByCondition(ArticleTagDO articleTagDO);
}
