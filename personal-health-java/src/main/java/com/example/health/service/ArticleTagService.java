package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.ArticleTagDTO;
import com.example.health.pojo.vo.ArticleTagVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/14 09:56
 * @description: 健康资讯标签服务
 */
@Service
public interface ArticleTagService {
    /**
     * 新增标签
     *
     * @param articleTagDTO 新增标签信息
     * @return 新增操作响应结果
     */
    ApiResponse<String> saveArticleTag(ArticleTagDTO articleTagDTO);

    /**
     * 批量删除标签
     *
     * @param ids 待删除的标签ID集合
     * @return 删除操作响应结果
     */
    ApiResponse<String> deleteArticleTags(List<Integer> ids);

    /**
     * 更新标签信息
     *
     * @param articleTagDTO 修改后的标签信息
     * @return 更新操作响应结果
     */
    ApiResponse<String> updateArticleTag(ArticleTagDTO articleTagDTO);

    /**
     * 分页+模糊查询标签
     *
     * @param articleTagDTO 标签模糊查询参数
     * @return 符合要求的标签信息列表
     */
    ApiResponse<List<ArticleTagVO>> queryArticleTags(ArticleTagDTO articleTagDTO);
}
