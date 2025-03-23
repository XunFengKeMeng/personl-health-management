package com.example.health.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/21 23:49
 * @description: 查询每天的文章数量，用于生成折线图
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTrendVO {
    /**
     * 日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime articleDate;

    /**
     * 该日期下发布的文章数
     */
    private Integer articleCount;
}
