package com.example.health.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/3/21 23:45
 * @description: 查询每个标签下的文章数量，用于前端生成饼图
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTagStatisticsVO {
    /**
     * 标签ID
     */
    private Integer tagId;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 标签所属的文章数
     */
    private Integer articleCount;
}
