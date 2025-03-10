package com.example.health.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/5 00:36
 * @description: 记录用户收藏的健康资讯，对应数据库表user_saved_article
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSavedArticleDO {
    /**
     * 资讯收藏记录编号
     */
    private Integer savedId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 健康资讯ID
     */
    private Integer articleId;

    /**
     * 用户收藏健康资讯的时间
     */
    private LocalDateTime savedCreateTime;
}
