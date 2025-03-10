package com.example.health.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/3/5 00:36
 * @description: 资讯标签实体类，对应数据库表health_article_tag
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthArticleTagDO {
    /**
     * 标签ID
     */
    private Integer tagId;

    /**
     * 标签名
     */
    private String tagName;
}
