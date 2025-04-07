package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.HealthArticleDTO;
import com.example.health.pojo.dto.query.extend.HealthArticleQueryDTO;
import com.example.health.pojo.vo.ArticleTagStatisticsVO;
import com.example.health.pojo.vo.ArticleTrendVO;
import com.example.health.pojo.vo.HealthArticleVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/14 22:03
 * @description: 健康资讯服务
 */
@Service
public interface HealthArticleService {
    /**
     * 新增健康资讯
     *
     * @param healthArticleDTO 新增的健康资讯信息
     * @return 新增操作响应结果
     */
    ApiResponse<String> saveArticle(HealthArticleDTO healthArticleDTO);

    /**
     * 批量删除健康资讯
     *
     * @param ids 待删除的健康资讯ID集合
     * @return 删除操作响应结果
     */
    ApiResponse<String> deleteArticles(List<Integer> ids);

    /**
     * 更新健康资讯信息
     *
     * @param healthArticleDTO 修改后的健康资讯信息
     * @return 更新操作响应结果
     */
    ApiResponse<String> updateArticle(HealthArticleDTO healthArticleDTO);

    /**
     * 查询健康资讯（标题模糊查询、标签筛选、推荐标记筛选）
     *
     * @param healthArticleQueryDTO 查询参数（包括当前页及页大小）
     * @return 查询操作响应结果
     */
    ApiResponse<List<HealthArticleVO>> queryArticles(HealthArticleQueryDTO healthArticleQueryDTO);

    /**
     * 查询每个标签下的文章数
     *
     * @return 标签及标签下的文章数
     */
    ApiResponse<List<ArticleTagStatisticsVO>>  queryArticleTagStatistics();

    /**
     * 查询每天的文章数
     *
     * @return 每天的文章数
     */
    ApiResponse<List<ArticleTrendVO>> queryArticleTrend();

    /**
     * 查询文章具体信息
     *
     * @param articleId 待查询资讯ID
     * @param userId 查看资讯的用户ID
     * @return 查询结果
     */
    ApiResponse<HealthArticleVO> queryArticleById(Integer articleId, Integer userId);
}