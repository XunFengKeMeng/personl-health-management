<template>
  <div class="notification-container">
    <div class="notification-layout">
      <!-- 通知列表 -->
      <div class="notification-list-panel">
        <div class="panel-header">
          <h2>我的通知</h2>
          <div class="header-actions">
            <el-button
              type="danger"
              size="small"
              :disabled="selectedNotices.length === 0"
              @click="batchDeleteNotices"
            >
              <i class="el-icon-delete"></i> 批量删除
            </el-button>
            <el-button
              type="default"
              size="small"
              @click="selectAllToggle"
            >
              {{ isAllSelected ? '取消全选' : '全选' }}
            </el-button>
          </div>
        </div>

        <div
          class="notification-list"
          v-loading="loading"
        >
          <div
            v-if="notices.length === 0 && !loading"
            class="empty-notice"
          >
            <el-empty description="暂无通知"></el-empty>
          </div>

          <div
            v-for="notice in notices"
            :key="notice.noticeId"
            class="notification-item"
            :class="{ 'selected': selectedNotice && selectedNotice.noticeId === notice.noticeId }"
            @click="selectNotice(notice)"
          >
            <div class="checkbox-wrapper">
              <el-checkbox
                v-model="notice.isSelected"
                @click.stop
                @change="updateSelection"
              ></el-checkbox>
            </div>

            <div class="notification-item-content">
              <div class="notification-item-header">
                <span
                  class="notice-type"
                  :class="getTypeClass(notice.noticeType)"
                >
                  {{ getTypeText(notice.noticeType) }}
                </span>
                <span
                  v-if="!notice.read"
                  class="unread-badge"
                ></span>
                <span
                  class="notification-date">{{ parseTime(notice.noticeCreateTime) }}</span>
              </div>

              <div class="notification-item-body">
                <p class="notification-brief">
                  {{ getTruncatedContent(notice.noticeContent) }}</p>
              </div>
            </div>
          </div>
        </div>

        <div
          class="pagination-container"
          v-if="total > pageSize"
        >
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            :current-page.sync="currentPage"
            @current-change="handlePageChange"
          ></el-pagination>
        </div>
      </div>
      <!-- 通知内容 -->
      <div class="notification-content-panel">
        <div
          v-if="selectedNotice"
          class="notification-detail"
        >
          <div class="notification-detail-header">
            <div
              class="notice-type-badge"
              :class="getTypeClass(selectedNotice.noticeType)"
            >
              {{ getTypeText(selectedNotice.noticeType) }}
            </div>
            <div class="notification-time">
              {{ parseTime(selectedNotice.noticeCreateTime) }}</div>
          </div>

          <div class="notification-detail-content">
            {{ selectedNotice.noticeContent }}
          </div>
        </div>

        <div
          v-else
          class="empty-content"
        >
          <el-empty description="请选择一条通知查看详情"></el-empty>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { timeAgo } from "@/utils/data";
import { getToken } from "@/utils/storage";

const API = {
  QUERY_NOTICES: "notice/query",
  UPDATE_NOTICE: "notice/update",
  DELETE_NOTICES: "notice/deleteNotices"
};

export default {
  data () {
    return {
      notices: [],
      selectedNotice: null,
      selectedNotices: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: true,
      userId: null,
      isAllSelected: false
    };
  },
  created () {
    this.getUserID();
  },
  methods: {
    // 获取用户ID
    async getUserID () {
      try {
        const token = { token: getToken() };
        const res = await this.$axios.post('user/auth', token, {withCredentials: true});
        if (res.code === 400) {
          this.$message.error(res.data.msg);
          this.$router.push('/login');
          return;
        }
        this.userId = res.data.data.userId;
        this.loadNotices();
      } catch (error) {
        console.error('获取用户认证信息时发生错误:', error);
        this.$message.error('认证信息加载失败,请重试！');
      }
    },

    // 加载通知列表
    async loadNotices () {
      this.loading = true;
      try {
        const response = await this.$axios.post(API.QUERY_NOTICES, {
          receiverId: this.userId,
          current: (this.currentPage - 1) * this.pageSize,
          size: this.pageSize
        }, {withCredentials: true});

        const { data } = response;
        if (data.code === 200) {
          this.notices = data.data.map(item => ({
            ...item,
            isSelected: false
          }));
          this.total = data.data.total;

          // 默认选中最新通知
          if (this.notices.length > 0 && !this.selectedNotice) {
            this.selectNotice(this.notices[0]);
          }
        } else {
          this.$message.error('获取通知列表失败');
        }
      } catch (error) {
        console.error('获取通知列表失败:', error);
        this.$message.error('获取通知列表失败');
      } finally {
        this.loading = false;
      }
    },

    // 选择通知
    async selectNotice (notice) {
      this.selectedNotice = notice;

      // 如果通知未读，则标记为已读
      if (!notice.read) {
        try {
          await this.$axios.post(API.UPDATE_NOTICE, {
            noticeId: notice.noticeId,
            read: true
          }, {withCredentials: true});

          // 更新本地状态
          notice.read = true;
        } catch (error) {
          console.error('标记通知已读失败:', error);
        }
      }
    },

    // 处理分页变化
    handlePageChange (page) {
      this.currentPage = page;
      this.selectedNotice = null;
      this.loadNotices();
    },

    // 更新选择状态
    updateSelection () {
      this.selectedNotices = this.notices.filter(item => item.isSelected).map(item => item.noticeId);
      // 检查是否全选
      this.isAllSelected = this.selectedNotices.length === this.notices.length && this.notices.length > 0;
    },

    // 全选/取消全选
    selectAllToggle () {
      const newState = !this.isAllSelected;
      this.notices.forEach(item => {
        item.isSelected = newState;
      });
      this.updateSelection();
    },

    // 批量删除通知
    async batchDeleteNotices () {
      if (this.selectedNotices.length === 0) {
        this.$message.warning('请先选择要删除的通知');
        return;
      }

      try {
        await this.$confirm('确定要删除选中的通知吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await this.$axios.post(API.DELETE_NOTICES, this.selectedNotices, {withCredentials: true});

        const { data } = response;
        if (data.code === 200) {
          this.$message.success(`成功删除${this.selectedNotices.length}条通知`);

          // 如果删除的通知中包含当前选中的通知，则清空选中状态
          if (this.selectedNotice && this.selectedNotices.includes(this.selectedNotice.noticeId)) {
            this.selectedNotice = null;
          }

          this.selectedNotices = [];
          this.isAllSelected = false;
          this.loadNotices();
        } else {
          this.$message.error('批量删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error);
          this.$message.error('批量删除失败');
        }
      }
    },

    // 获取通知类型文本
    getTypeText (type) {
      switch (parseInt(type)) {
        case 1: return '健康指标异常';
        case 2: return '活动提示';
        case 3: return '版本更新';
        default: return '其他通知';
      }
    },

    // 获取通知类型样式类
    getTypeClass (type) {
      switch (parseInt(type)) {
        case 1: return 'type-warning';
        case 2: return 'type-info';
        case 3: return 'type-update';
        default: return 'type-default';
      }
    },

    // 截断通知内容
    getTruncatedContent (content) {
      if (content && content.length > 30) {
        return content.substring(0, 30) + '...';
      }
      return content;
    },

    // 格式化时间
    parseTime (time) {
      return timeAgo(time);
    }
  }
};
</script>

<style scoped lang="scss">
.notification-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .notification-layout {
    display: flex;
    min-height: 450px;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    overflow: hidden;

    // 左侧通知列表
    .notification-content-panel {
      flex: 3;
      border-left: 1px solid #f0f0f0;
      padding: 20px;
      display: flex;
      flex-direction: column;

      .notification-detail {
        height: 100%;
        display: flex;
        flex-direction: column;

        .notification-detail-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding-bottom: 15px;
          border-bottom: 1px solid #f0f0f0;
          margin-bottom: 20px;

          .notice-type-badge {
            font-size: 16px;
            font-weight: 500;
            padding: 5px 10px;
            border-radius: 4px;
          }

          .notification-time {
            color: #909399;
            font-size: 14px;
          }
        }

        .notification-detail-content {
          flex: 1;
          font-size: 15px;
          line-height: 1.8;
          color: #333;
          white-space: pre-line;
        }
      }

      .empty-content {
        display: flex;
        height: 100%;
        justify-content: center;
        align-items: center;
      }
    }

    // 右侧通知内容区
    .notification-list-panel {
      flex: 2;
      display: flex;
      flex-direction: column;
      border-right: 1px solid #f0f0f0;

      .panel-header {
        padding: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #f0f0f0;

        h2 {
          margin: 0;
          font-size: 20px;
          color: #333;
          position: relative;
          padding-left: 12px;

          &:before {
            content: "";
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            width: 4px;
            height: 20px;
            background-color: #1d7ce1;
            border-radius: 2px;
          }
        }

        .header-actions {
          display: flex;
          gap: 10px;
        }
      }

      .notification-list {
        flex: 1;
        overflow-y: auto;
        max-height: calc(115px * 3);

        /* 自定义滚动条样式 */
        &::-webkit-scrollbar {
          width: 4px;
        }

        &::-webkit-scrollbar-track {
          background: #f1f1f1;
          border-radius: 3px;
        }

        &::-webkit-scrollbar-thumb {
          background: #c1c1c1;
          border-radius: 3px;
        }

        &::-webkit-scrollbar-thumb:hover {
          background: #a8a8a8;
        }

        .notification-item {
          padding: 15px;
          border-bottom: 1px solid #f0f0f0;
          cursor: pointer;
          transition: background-color 0.3s;
          display: flex;
          height: 85px; /* 固定高度确保每个项目高度一致 */
          box-sizing: border-box;

          &:hover {
            background-color: #f9f9f9;
          }

          &.selected {
            background-color: #ecf5ff;
          }

          // 未读通知的样式增强
          &:not(.selected) {
            &:has(.unread-badge) {
              background-color: rgba(236, 245, 255, 0.4);
            }
          }

          .checkbox-wrapper {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-right: 15px;
          }

          .notification-item-content {
            flex: 1;

            .notification-item-header {
              display: flex;
              align-items: center;
              margin-bottom: 8px;

              .notice-type {
                font-size: 12px;
                padding: 2px 6px;
                border-radius: 4px;
                margin-right: 8px;
              }

              .unread-badge {
                width: 8px;
                height: 8px;
                background-color: #ff4d4f;
                border-radius: 50%;
                display: inline-block;
                margin-right: 8px;
              }

              .notification-date {
                font-size: 12px;
                color: #909399;
                margin-left: auto;
              }
            }

            .notification-item-body {
              .notification-brief {
                margin: 0;
                font-size: 14px;
                color: #606266;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
            }
          }
        }
      }

      .pagination-container {
        padding: 15px;
        text-align: center;
        border-top: 1px solid #f0f0f0;
      }
    }
  }
}

.type-warning {
  background-color: rgba(230, 162, 60, 0.1);
  color: #e6a23c;
  border: 1px solid rgba(230, 162, 60, 0.2);
}

.type-info {
  background-color: rgba(64, 158, 255, 0.1);
  color: #409eff;
  border: 1px solid rgba(64, 158, 255, 0.2);
}

.type-update {
  background-color: rgba(103, 194, 58, 0.1);
  color: #67c23a;
  border: 1px solid rgba(103, 194, 58, 0.2);
}

.type-default {
  background-color: rgba(144, 147, 153, 0.1);
  color: #909399;
  border: 1px solid rgba(144, 147, 153, 0.2);
}
</style>

