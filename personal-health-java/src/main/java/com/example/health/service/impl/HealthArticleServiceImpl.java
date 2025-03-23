package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.mapper.HealthArticleMapper;
import com.example.health.pojo.dto.HealthArticleDTO;
import com.example.health.pojo.dto.query.extend.HealthArticleQueryDTO;
import com.example.health.pojo.entity.HealthArticleDO;
import com.example.health.pojo.vo.ArticleTagStatisticsVO;
import com.example.health.pojo.vo.ArticleTrendVO;
import com.example.health.pojo.vo.HealthArticleVO;
import com.example.health.service.HealthArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/14 22:14
 * @description: 健康资讯服务实现类
 */
@Service
public class HealthArticleServiceImpl implements HealthArticleService {

    /**
     * 持久层接口注入
     */
    @Resource
    private HealthArticleMapper healthArticleMapper;

    /**
     * 新增健康资讯
     *
     * @param healthArticleDTO 新增的健康资讯信息
     * @return 新增操作响应结果
     */
    @Override
    public ApiResponse<String> saveArticle(HealthArticleDTO healthArticleDTO) {
        HealthArticleDO healthArticleDO = HealthArticleDO.builder()
                .healthArticleTitle(healthArticleDTO.getHealthArticleTitle())
                .healthArticleContent(healthArticleDTO.getHealthArticleContent())
                .healthArticleTagId(healthArticleDTO.getHealthArticleTagId())
                .healthArticleCover(healthArticleDTO.getHealthArticleCover())
                .top(healthArticleDTO.getTop())
                .healthArticleCreateTime(LocalDateTime.now()).build();
        healthArticleMapper.saveArticle(healthArticleDO);
        return ApiResponse.success("新增资讯成功");
    }

    /**
     * 批量删除健康资讯
     *
     * @param ids 待删除的健康资讯ID集合
     * @return 删除操作响应结果
     */
    @Override
    public ApiResponse<String> deleteArticles(List<Integer> ids) {
        healthArticleMapper.deleteArticles(ids);
        return ApiResponse.success("删除成功");
    }

    /**
     * 更新健康资讯信息
     *
     * @param healthArticleDTO 修改后的健康资讯信息（必须包含资讯ID）
     * @return 更新操作响应结果
     */
    @Override
    public ApiResponse<String> updateArticle(HealthArticleDTO healthArticleDTO) {
        HealthArticleDO healthArticleDO = healthArticleMapper.getByArticleId(healthArticleDTO.getHealthArticleId());
        BeanUtils.copyProperties(healthArticleDTO, healthArticleDO);
        healthArticleMapper.updateArticle(healthArticleDO);
        return ApiResponse.success("修改成功");
    }

    /**
     * 查询健康资讯（标题模糊查询、标签筛选、推荐标记筛选）
     *
     * @param healthArticleQueryDTO 查询参数（包括当前页及页大小）
     * @return 查询操作响应结果
     */
    @Override
    public ApiResponse<List<HealthArticleVO>> queryArticles(HealthArticleQueryDTO healthArticleQueryDTO) {
        HealthArticleDO healthArticleDO = HealthArticleDO.builder()
                .healthArticleTitle(healthArticleQueryDTO.getHealthArticleTitle())
                .healthArticleTagId(healthArticleQueryDTO.getHealthArticleTagId())
                .top(healthArticleQueryDTO.getTop())
                .build();
        List<HealthArticleVO> healthArticleVOList = healthArticleMapper.queryArticles(
                healthArticleDO,
                healthArticleQueryDTO.getCurrent(),
                healthArticleQueryDTO.getSize());
        Integer count = healthArticleMapper.queryArticlesCount(healthArticleDO);
        return ApiResponse.success(healthArticleVOList, count);
    }

    /**
     * 查询每个标签下的文章数
     *
     * @return 标签及标签下的文章数
     */
    public ApiResponse<List<ArticleTagStatisticsVO>> queryArticleTagStatistics() {
        return ApiResponse.success(healthArticleMapper.queryArticleTagStatistics());
    }

    /**
     * 查询每天的文章数量
     *
     * @return 每天的文章数
     */
    public ApiResponse<List<ArticleTrendVO>> queryArticleTrend() {
        return ApiResponse.success(healthArticleMapper.queryArticleTrend());
    }
}
