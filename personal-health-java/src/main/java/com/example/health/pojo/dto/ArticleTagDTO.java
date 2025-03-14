package com.example.health.pojo.dto;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/3/14 10:04
 * @description: 健康资讯标签数据传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTagDTO extends QueryDTO {
    /**
     * 标签ID
     */
    private Integer tagId;

    /**
     * 标签名
     */
    private String tagName;
}
