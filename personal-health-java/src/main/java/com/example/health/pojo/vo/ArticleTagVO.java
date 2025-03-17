package com.example.health.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/3/13 22:57
 * @description: 健康资讯标签视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTagVO {
    /**
     * 标签ID
     */
    private Integer tagId;

    /**
     * 标签名
     */
    private String tagName;
}
