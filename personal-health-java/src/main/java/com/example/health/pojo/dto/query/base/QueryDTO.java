package com.example.health.pojo.dto.query.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/5 10:52
 * @description: 接收查询数据页
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDTO {
    /**
     * 当前页
     */
    private Integer current;

    /**
     * 页面数据大小
     */
    private Integer size;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
