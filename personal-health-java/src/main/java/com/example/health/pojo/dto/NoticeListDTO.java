package com.example.health.pojo.dto;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/25 14:52
 * @description: 新增通知使用到的数据传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeListDTO extends QueryDTO {
    /**
     * 接收者ID集合
     */
    private List<Integer> receiverIds;

    /**
     * 发送者ID
     */
    private Integer senderId;

    /**
     * 消息类型
     */
    private Integer noticeType;

    /**
     * 消息内容
     */
    private String noticeContent;

    /**
     * 是否为全体发布
     */
    private Boolean all;
}
