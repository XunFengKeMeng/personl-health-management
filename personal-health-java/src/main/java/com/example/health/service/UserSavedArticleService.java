package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.UserSavedArticleDTO;
import com.example.health.pojo.vo.UserSavedArticleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/16 00:33
 * @description: 用户收藏健康资讯服务
 */
@Service
public interface UserSavedArticleService {
    /**
     * 新增收藏记录
     *
     * @param userSavedArticleDTO 收藏信息（用户ID，健康资讯ID）
     * @return 新增操作响应结果
     */
    ApiResponse<String> save(UserSavedArticleDTO userSavedArticleDTO);

    /**
     * 批量删除收藏记录
     *
     * @param ids 待删除的收藏记录ID集合
     * @return 删除操作响应结果
     */
    ApiResponse<String> deleteSavedArticles(@Param("ids") List<Integer> ids);

    /**
     * 分页查询收藏记录
     *
     * @param userSavedArticleDTO 查询参数（用户ID/健康资讯ID）
     * @return 查询操作响应结果 + 满足查询参数的记录列表
     */
    ApiResponse<List<UserSavedArticleVO>> querySavedArticles(UserSavedArticleDTO userSavedArticleDTO);
}
