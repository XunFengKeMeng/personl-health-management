package com.example.health.pojo.dto.query.extend;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/3/5 14:44
 * @description: 健康资讯数据传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthArticleQueryDTO extends QueryDTO {
    /**
     * 健康资讯ID
     */
    private Integer healthArticleId;

    /**
     * 健康资讯标题
     */
    private String healthArticleTitle;

    /**
     * 健康资讯标签ID
     */
    private Integer healthArticleTagId;

    /**
     * 是否推荐
     */
    private Boolean top;
}
