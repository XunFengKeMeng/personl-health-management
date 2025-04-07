package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.em.ReadStatusEnum;
import com.example.health.mapper.NoticeMapper;
import com.example.health.pojo.dto.NoticeDTO;
import com.example.health.pojo.dto.NoticeListDTO;
import com.example.health.pojo.entity.NoticeDO;
import com.example.health.pojo.vo.NoticeVO;
import com.example.health.service.NoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/17 14:33
 * @description: 通知服务实现类
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    /**
     * 注入持久层接口
     */
    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 新增若干通知
     *
     * @param notices 新增的通知信息列表
     * @return 新增操作响应结果
     */
    @Override
    public ApiResponse<String> insertNotices(NoticeListDTO noticeDTOList) {
        NoticeDO noticeDO = NoticeDO.builder()
                .read(ReadStatusEnum.UNREAD.getReadStatus())
                .noticeCreateTime(LocalDateTime.now())
                .build();
        BeanUtils.copyProperties(noticeDTOList, noticeDO);
        if(noticeDTOList.getAll() != null) {
            noticeMapper.insertAll(noticeDO);
        } else {
            noticeMapper.insertNotices(noticeDO, noticeDTOList.getReceiverIds());
        }

        return ApiResponse.success("新增成功");
    }

    /**
     * 批量删除通知
     *
     * @param ids 待删除的通知ID集合
     * @return 删除操作响应结果
     */
    @Override
    public ApiResponse<String> deleteNotices(List<Integer> ids) {
        noticeMapper.deleteNotices(ids);
        return ApiResponse.success("删除成功");
    }

    /**
     * 更新通知（用于修改是否已读）
     *
     * @param noticeDTO 待修改的通知信息（通知ID + 是否已读标记）
     * @return 更新操作响应结果
     */
    @Override
    public ApiResponse<String> updateNotice(NoticeDTO noticeDTO) {
        NoticeDO noticeDO = new NoticeDO();
        BeanUtils.copyProperties(noticeDTO, noticeDO);
        noticeMapper.updateNotice(noticeDO);
        return ApiResponse.success("修改成功");
    }

    /**
     * 分页查询通知
     *
     * @param noticeDTO 查询参数（通知类型、接收者/发送者ID、是否已读）
     * @return 查询结果 + 查询操作响应结果
     */
    @Override
    public ApiResponse<List<NoticeVO>> queryNotices(NoticeDTO noticeDTO) {
        NoticeDO noticeDO = new NoticeDO();
        BeanUtils.copyProperties(noticeDTO, noticeDO);
        List<NoticeVO> noticeVOList = noticeMapper.queryNotices(noticeDO,
                noticeDTO.getCurrent(),
                noticeDTO.getSize());
        Integer count = noticeMapper.queryCount(noticeDO);
        return ApiResponse.success(noticeVOList, count);
    }
}
