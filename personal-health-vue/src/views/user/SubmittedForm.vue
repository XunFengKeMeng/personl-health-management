<template>
  <div class="form-submission-container">
    <div class="page-header">
      <h2 class="page-title">我的表单档案</h2>
      <div class="search-section">
        <el-input
          v-model="searchQuery"
          placeholder="搜索表单模板名称"
          clearable
          prefix-icon="el-icon-search"
          @clear="loadSubmissions"
          @keyup.enter.native="handleSearch"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="handleSearch"
          ></el-button>
        </el-input>
      </div>
    </div>

    <div
      class="form-submission-list"
      v-loading="loading"
    >
      <!-- 空状态 -->
      <el-empty
        v-if="submissions.length === 0 && !loading"
        description="暂无表单提交记录"
      >
        <el-button
          type="primary"
          @click="goToFormSubmit"
        >去填写表单</el-button>
      </el-empty>

      <!-- 表单提交列表 -->
      <el-table
        v-if="submissions.length > 0"
        :data="submissions"
        style="width: 100%"
        border
        :header-cell-style="{'background-color': '#f5f7fa', 'color': '#606266'}"
      >
        <el-table-column
          label="表单名称"
          prop="templateName"
          min-width="180"
        >
          <template slot-scope="scope">
            <span class="form-name">{{ scope.row.templateName }}</span>
          </template>
        </el-table-column>

        <el-table-column
          label="提交时间"
          width="180"
        >
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span>{{ formatTime(scope.row.submitTime) }}</span>
          </template>
        </el-table-column>

        <el-table-column
          label="审核状态"
          width="120"
        >
          <template slot-scope="scope">
            <el-tag
              :type="getStatusType(scope.row.status)"
              size="medium"
            >
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          label="操作"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status !== 2"
              type="primary"
              size="mini"
              @click="viewSubmission(scope.row.submissionId)"
              icon="el-icon-view"
            >
              查看
            </el-button>
            <el-button
              v-else
              type="warning"
              size="mini"
              @click="editSubmission(scope.row.submissionId)"
              icon="el-icon-edit"
            >
              修改
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页控件 -->
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

    <!-- 查看表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="viewDialogVisible"
      width="60%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <div class="form-detail-container">
        <h3 class="form-detail-title">{{ currentSubmission.templateName }}</h3>

        <div class="form-items">
          <div
            v-for="item in sortedFormDataList"
            :key="item.formDataId"
            class="form-item"
          >
            <div class="form-item-label">
              {{ item.itemName }}
              <span
                class="required-mark"
                v-if="item.required"
              >*</span>
            </div>

            <div class="form-item-value">
              <!-- 文件类型显示下载链接 -->
              <a
                v-if="isFileType(item)"
                :href="getFileUrl(item.value)"
                target="_blank"
                class="file-download-link"
              >
                <i class="el-icon-document"></i>
                {{ getFileName(item.value) || '无文件' }}
              </a>

              <!-- 非文件类型显示文本框 -->
              <el-input
                v-else
                v-model="item.value"
                :placeholder="item.placeholder || '请输入' + item.itemName"
                disabled
                class="disabled-input"
              ></el-input>
            </div>

          </div>
        </div>
      </div>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 修改表单对话框 -->
    <el-dialog
      title="修改表单"
      :visible.sync="editDialogVisible"
      width="60%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <div class="form-detail-container">
        <h3 class="form-detail-title">{{ currentSubmission.templateName }}</h3>

        <!-- <div class="form-items">
          <div
            v-for="item in sortedFormDataList"
            :key="item.formDataId"
            class="form-item"
          >
            <div class="form-item-label">
              {{ item.itemName }}
              <span
                class="required-mark"
                v-if="item.required"
              >*</span>
            </div>
            <div class="form-item-value">
              <el-input
                v-model="item.value"
                :placeholder="item.placeholder || '请输入' + item.itemName"
              ></el-input>
            </div>
          </div>
        </div> -->

        <div class="form-items">
          <div
            v-for="item in sortedFormDataList"
            :key="item.formDataId"
            class="form-item"
          >
            <div class="form-item-label">
              {{ item.itemName }}
              <span
                class="required-mark"
                v-if="item.required"
              >*</span>
            </div>

            <div class="form-item-value">
              <!-- 文件类型特殊处理 -->
              <div
                v-if="isFileType(item)"
                class="file-upload-container"
              >
                <!-- 显示已上传文件 -->
                <div
                  v-if="item.value"
                  class="file-preview"
                >
                  <a
                    :href="getFileUrl(item.value)"
                    target="_blank"
                    class="file-link"
                  >
                    <i :class="getFileIcon(item.value)"></i>
                    {{ getFileName(item.value) }}
                  </a>
                  <el-button
                    size="mini"
                    type="danger"
                    icon="el-icon-delete"
                    @click="removeFile(item)"
                    circle
                  ></el-button>
                </div>

                <!-- 文件上传组件 -->
                <el-upload
                  class="uploader"
                  action="http://localhost:21090/api/health-management/file/upload"
                  :on-success="(res, file) => handleUploadSuccess(res, file, item)"
                  :before-upload="beforeUpload"
                  :show-file-list="false"
                >
                  <el-button
                    size="small"
                    type="primary"
                  >重新上传</el-button>
                  <div
                    slot="tip"
                    class="el-upload__tip"
                  >
                    支持扩展名：.jpg/.png/.pdf，大小不超过10MB
                  </div>
                </el-upload>
              </div>

              <!-- 普通文本输入 -->
              <el-input
                v-else
                v-model="item.value"
                :placeholder="item.placeholder || '请输入' + item.itemName"
              ></el-input>
            </div>
          </div>
        </div>
      </div>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="submitUpdatedForm"
        >提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { timeAgo } from "@/utils/data";
import { getToken } from "@/utils/storage";

const API = {
  QUERY_SUBMISSIONS: "formSubmission/query",
  QUERY_DETAIL: "formSubmission/queryDetail",
  UPDATE_SUBMISSION: "formSubmission/update"
};

export default {
  data () {
    return {
      userId: null,
      searchQuery: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: true,
      submissions: [],
      currentSubmission: {
        submissionId: null,
        templateId: null,
        templateName: "",
        status: 0,
        submitTime: "",
        formDataList: []
      },
      viewDialogVisible: false,
      editDialogVisible: false,
      dialogTitle: "查看表单详情"
    };
  },
  computed: {
    sortedFormDataList () {
      if (!this.currentSubmission.formDataList) return [];
      return [...this.currentSubmission.formDataList].sort((a, b) => a.sortOrder - b.sortOrder);
    }
  },
  created () {
    this.getUserID();
  },
  methods: {
    // 获取用户ID
    async getUserID () {
      try {
        const token = { token: getToken() };
        const res = await this.$axios.post('user/auth', token, { withCredentials: true });
        if (res.code === 400) {
          this.$message.error(res.data.msg);
          this.$router.push('/login');
          return;
        }
        this.userId = res.data.data.userId;
        this.loadSubmissions();
      } catch (error) {
        console.error('获取用户认证信息时发生错误:', error);
        this.$message.error('认证信息加载失败,请重试！');
      }
    },

    // 加载表单提交列表
    async loadSubmissions () {
      this.loading = true;
      try {
        const response = await this.$axios.post(API.QUERY_SUBMISSIONS, {
          userId: this.userId,
          templateName: this.searchQuery,
          current: (this.currentPage - 1) * this.pageSize,
          size: this.pageSize
        }, { withCredentials: true });

        const { data } = response;
        if (data.code === 200) {
          this.submissions = data.data;
          this.total = data.data.total;
        } else {
          this.$message.error('获取表单提交列表失败');
        }
      } catch (error) {
        console.error('获取表单提交列表失败:', error);
        this.$message.error('获取表单提交列表失败');
      } finally {
        this.loading = false;
      }
    },

    // 处理搜索
    handleSearch () {
      this.currentPage = 1;
      this.loadSubmissions();
    },

    // 处理分页变化
    handlePageChange (page) {
      this.currentPage = page;
      this.loadSubmissions();
    },

    // 查看表单详情
    async viewSubmission (submissionId) {
      try {
        const response = await this.$axios.post(API.QUERY_DETAIL, {
          submissionId: submissionId
        }, { withCredentials: true });

        const { data } = response;
        if (data.code === 200) {
          this.currentSubmission = data.data;
          this.dialogTitle = "查看表单详情";
          this.viewDialogVisible = true;
        } else {
          this.$message.error('获取表单详情失败');
        }
      } catch (error) {
        console.error('获取表单详情失败:', error);
        this.$message.error('获取表单详情失败');
      }
    },

    // 修改表单
    async editSubmission (submissionId) {
      try {
        const response = await this.$axios.post(API.QUERY_DETAIL, {
          submissionId: submissionId,
        }, { withCredentials: true });

        const { data } = response;
        if (data.code === 200) {
          this.currentSubmission = data.data;
          this.editDialogVisible = true;
        } else {
          this.$message.error('获取表单详情失败');
        }
      } catch (error) {
        console.error('获取表单详情失败:', error);
        this.$message.error('获取表单详情失败');
      }
    },

    // 提交修改后的表单
    async submitUpdatedForm () {
      try {
        // 验证必填项
        const missingRequired = this.sortedFormDataList.find(
          item => item.required && (!item.value || !item.value.trim())
        );

        if (missingRequired) {
          this.$message.error(`请填写必填项: ${missingRequired.itemName}`);
          return;
        }

        // 构建提交数据
        const formDataList = this.sortedFormDataList.map(item => ({
          itemId: item.itemId,
          value: item.value
        }));

        const response = await this.$axios.post(API.UPDATE_SUBMISSION, {
          submissionId: this.currentSubmission.submissionId,
          status: 0,
          formDataList: formDataList
        }, { withCredentials: true });

        const { data } = response;
        if (data.code === 200) {
          this.$message.success('修改表单成功');
          this.editDialogVisible = false;
          this.loadSubmissions();
        } else {
          this.$message.error(data.msg || '修改表单失败');
        }
      } catch (error) {
        console.error('修改表单失败:', error);
        this.$message.error('修改表单失败');
      }
    },

    // 跳转到表单填写页面
    goToFormSubmit () {
      this.$router.push('/form-submit');
    },

    // 获取状态类型（用于标签颜色）
    getStatusType (status) {
      const statusMap = {
        0: 'info',    // 待审核
        1: 'success', // 审核通过
        2: 'danger'   // 审核驳回
      };
      return statusMap[status] || 'info';
    },

    // 获取状态文本
    getStatusText (status) {
      const statusMap = {
        0: '待审核',
        1: '审核通过',
        2: '审核驳回'
      };
      return statusMap[status] || '未知状态';
    },

    // 格式化时间
    formatTime (time) {
      return timeAgo(time);
    },

    // 判断是否为文件类型
    isFileType (item) {
      // return item.style === 1;
      return item.style === 1 || /\.(pdf|jpg|png|docx?|xlsx?)$/i.test(item.value || '');
    },

    // 获取完整文件URL
    getFileUrl (value) {
      if (!value) return '#';

      // 如果已经是完整URL直接返回
      if (value.startsWith('http')) {
        return value.replace(
          'http://localhost:21090/',
          'http://localhost:21090/api/health-management/'
        );
      }

      // 拼接基础路径
      return `http://localhost:21090/api/health-management/${encodeURIComponent(value)}`;
    },

    // 提取文件名
    getFileName (value) {
      return value?.split('/').pop() || '文件';
    },

    // 获取文件类型图标
    getFileIcon (value) {
      const ext = this.getFileName(value).split('.').pop().toLowerCase();
      const iconMap = {
        pdf: 'el-icon-document-pdf',
        jpg: 'el-icon-picture-outline',
        png: 'el-icon-picture-outline',
        doc: 'el-icon-document',
        xls: 'el-icon-document'
      };
      return iconMap[ext] || 'el-icon-document';
    },

    // 删除文件
    removeFile (item) {
      this.$confirm('确定删除该文件吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        item.value = ''; // 清空文件引用
        this.$message.success('删除成功');
      });
    },

    // 文件上传成功处理
    handleUploadSuccess (response, file, item) {
      if (response.code === 200) {
        item.value = response.msg; // 更新文件URL
        this.$message.success('上传成功');
      } else {
        this.$message.error(response.msg || '上传失败');
      }
    },


    // 上传前校验
    beforeUpload (file) {
      const isAllowed = /\.(jpg|png|pdf)$/i.test(file.name);
      const isLt10MB = file.size / 1024 / 1024 < 10;

      if (!isAllowed) {
        this.$message.error('仅支持JPG/PNG/PDF格式!');
      }
      if (!isLt10MB) {
        this.$message.error('文件大小不能超过10MB!');
      }

      return isAllowed && isLt10MB;
    },
  }
};
</script>

<style scoped lang="scss">
.form-submission-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .page-title {
      font-size: 22px;
      color: #333;
      margin: 0;
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

    .search-section {
      width: 300px;
    }
  }

  .form-submission-list {
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
    min-height: 400px;

    .form-name {
      font-weight: 500;
      color: #333;
    }

    .el-table {
      margin-bottom: 20px;
    }
  }

  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  .form-detail-container {
    .form-detail-title {
      font-size: 20px;
      color: #333;
      margin-bottom: 20px;
      text-align: center;
      border-bottom: 1px solid #ebeef5;
      padding-bottom: 15px;
    }

    .form-items {
      max-height: 60vh;
      overflow-y: auto;
      padding: 0 20px;

      .form-item {
        display: flex;
        margin-bottom: 20px;

        .form-item-label {
          width: 120px;
          text-align: right;
          padding-right: 12px;
          line-height: 40px;
          color: #606266;
          font-weight: 500;

          .required-mark {
            color: #f56c6c;
            margin-left: 4px;
          }
        }

        .form-item-value {
          flex: 1;

          .disabled-input {
            .el-input__inner {
              background-color: #f5f7fa;
              border-color: #e4e7ed;
              color: #909399;
              cursor: not-allowed;
            }
          }
        }
      }
    }
  }
  /* 文件下载链接样式 */
  .file-download-link {
    display: inline-flex;
    align-items: center;
    color: #409eff;
    text-decoration: none;
    padding: 8px 12px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    transition: all 0.3s;
    background-color: #f5f7fa;
  }

  .file-download-link:hover {
    color: #66b1ff;
    border-color: #c6e2ff;
    background-color: #ecf5ff;
  }

  .file-download-link i {
    margin-right: 6px;
    font-size: 16px;
  }

  /* 禁用输入框样式 */
  .disabled-input .el-input__inner {
    background-color: #f5f7fa;
    color: #606266;
    cursor: default;
  }

  /* 文件操作区域样式 */
  .file-upload-container {
    width: 100%;
  }

  .file-preview {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    padding: 8px;
    background: #f5f7fa;
    border-radius: 4px;
  }

  .file-link {
    flex: 1;
    display: flex;
    align-items: center;
    color: #606266;
    text-decoration: none;
  }

  .file-link i {
    margin-right: 8px;
    font-size: 20px;
    color: #409eff;
  }

  .file-link:hover {
    color: #409eff;
  }

  .uploader {
    margin-top: 5px;
  }
}
</style>

