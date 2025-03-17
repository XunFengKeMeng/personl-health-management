package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.NoticeDTO;
import com.example.health.pojo.vo.NoticeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/17 11:29
 * @description: 通知服务
 */
@Service
public interface NoticeService {
    /**
     * 新增若干通知
     *
     * @param notices 新增的通知信息列表
     * @return 新增操作响应结果
     */
    ApiResponse<String> insertNotices(List<NoticeDTO> noticeDTOList);

    /**
     * 批量删除通知
     *
     * @param ids 待删除的通知ID集合
     * @return 删除操作响应结果
     */
    ApiResponse<String> deleteNotices(@Param("ids") List<Integer> ids);


    /**
     * 更新通知（用于修改是否已读）
     *
     * @param noticeDTO 待修改的通知信息（通知ID + 是否已读标记）
     * @return 更新操作响应结果
     */
    ApiResponse<String> updateNotice(NoticeDTO noticeDTO);

    /**
     * 分页+模糊查询通知
     *
     * @param noticeDTO 查询参数（通知类型、接收者/发送者ID、是否已读）
     * @return 查询结果 + 查询操作响应结果
     */
    ApiResponse<List<NoticeVO>> queryNotices(NoticeDTO noticeDTO);
}
