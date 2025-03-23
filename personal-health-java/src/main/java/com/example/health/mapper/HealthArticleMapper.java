package com.example.health.mapper;

import com.example.health.pojo.entity.HealthArticleDO;
import com.example.health.pojo.vo.ArticleTagStatisticsVO;
import com.example.health.pojo.vo.ArticleTrendVO;
import com.example.health.pojo.vo.HealthArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/5 09:49
 * @description: 健康资讯持久层接口
 */
@Mapper
public interface HealthArticleMapper {
    /**
     * 新增健康资讯
     *
     * @param healthArticleDO 新增的健康资讯信息
     */
    void saveArticle(HealthArticleDO healthArticleDO);

    /**
     * 批量删除健康资讯
     *
     * @param ids 待删除的健康资讯ID集合
     */
    void deleteArticles(@Param(value = "ids") List<Integer> ids);

    /**
     * 更新健康资讯信息
     *
     * @param healthArticleDO 修改后的健康资讯信息
     */
    void updateArticle(HealthArticleDO healthArticleDO);

    /**
     * 查询健康资讯（标题模糊查询、标签筛选、推荐标记筛选）
     *
     * @param healthArticleDO 查询参数
     * @param current 当前页
     * @param size 页大小
     * @return 满足查询参数的健康资讯信息列表
     */
    List<HealthArticleVO> queryArticles(
            @Param("article")HealthArticleDO healthArticleDO,
            @Param("current") Integer current,
            @Param("size") Integer size);

    /**
     * 返回满足模糊查询条件的数据个数
     *
     * @param HealthArticleDO 查询参数
     * @return 满足查询参数的健康资讯数
     */
    Integer queryArticlesCount(HealthArticleDO HealthArticleDO);

    /**
     * 使用资讯ID请求完整的资讯信息
     *
     * @param HealthArticleDO 包含资讯ID的资讯数据对象
     * @return 完整的资讯信息
     */
    HealthArticleDO getByArticleId(Integer articleId);

    /**
     * 查询每个标签下的文章数
     *
     * @return 标签及标签下的文章数
     */
    List<ArticleTagStatisticsVO> queryArticleTagStatistics();

    /**
     * 查询每天的文章数量
     *
     * @return 每天的文章数
     */
    List<ArticleTrendVO> queryArticleTrend();
}
