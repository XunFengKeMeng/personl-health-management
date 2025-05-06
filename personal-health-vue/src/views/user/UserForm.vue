<template>
  <div class="form-submission-container">
    <div class="form-submission-layout">
      <!-- 左侧表单模板列表 -->
      <div class="template-list-panel">
        <div class="panel-header">
          <h2>表单模板</h2>
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索表单模板"
              prefix-icon="el-icon-search"
              clearable
              @clear="handleSearch"
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
          class="template-list"
          v-loading="loading"
        >
          <div
            v-if="templates.length === 0 && !loading"
            class="empty-template"
          >
            <el-empty description="暂无表单模板"></el-empty>
          </div>

          <div
            v-for="template in templates"
            :key="template.templateId"
            class="template-item"
            :class="{ 'selected': selectedTemplate && selectedTemplate.templateId === template.templateId }"
            @click="selectTemplate(template)"
          >
            <div class="template-name">
              <i class="el-icon-document"></i>
              {{ template.templateName }}
            </div>
          </div>
        </div>

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

      <!-- 右侧表单内容区 -->
      <div class="form-content-panel">
        <div
          v-if="selectedTemplate"
          class="form-detail"
          v-loading="detailLoading"
        >
          <div class="form-header">
            <h2 class="form-title">{{ selectedTemplate.templateName }}</h2>
          </div>

          <el-form
            ref="formRef"
            :model="formData"
            :rules="formRules"
            label-width="120px"
            class="form-content"
          >
            <el-form-item
              v-for="item in sortedItems"
              :key="item.itemId"
              :label="item.itemName"
              :prop="'item_' + item.itemId"
            >
              <!-- 文本框（style=0）  -->
              <el-input
                v-if="item.style === 0"
                v-model="formData['item_' + item.itemId]"
                :placeholder="item.placeholder || '请输入' + item.itemName"
              ></el-input>

              <!-- 文件上传（style=1） -->
              <el-upload
                v-else-if="item.style === 1"
                class="upload"
                action="http://localhost:21090/api/health-management/file/upload"
                :ref="item.itemId"
                :on-success="(res, file) => handleUploadSuccess(res, file, item.itemId)"
                :before-upload="beforeUpload"
                :show-file-list="false"
              >
                <el-button
                  size="small"
                  type="primary"
                >点击上传</el-button>

                <!-- 已上传文件显示 -->
                <a
                  v-if="uploadedFiles[item.itemId]"
                  class="uploaded-filename"
                  target="_blank"
                >
                  {{ uploadedFiles[item.itemId].name }}
                  <i
                    class="el-icon-close"
                    @click.stop="removeFile(item.itemId)"
                  ></i>
                </a>
                <div
                  slot="tip"
                  class="el-upload__tip"
                >
                  ({{item.placeholder}})
                </div>
              </el-upload>

            </el-form-item>

            <div class="form-actions">
              <el-button @click="resetForm">取消</el-button>
              <el-button
                type="primary"
                @click="submitForm"
              >提交</el-button>
            </div>
          </el-form>
        </div>

        <div
          v-else
          class="empty-content"
        >
          <el-empty description="选择其中一个表单模板进行填写"></el-empty>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getToken } from "@/utils/storage";

const API = {
  QUERY_TEMPLATES: "formTemplate/query",
  QUERY_TEMPLATE_DETAIL: "formTemplate/queryDetail",
  SAVE_SUBMISSION: "formSubmission/save"
};

export default {
  data () {
    return {
      templates: [],
      selectedTemplate: null,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      detailLoading: false,
      userId: null,
      searchKeyword: "",
      formData: {},
      formRules: {},
      uploadedFiles: {}, // 存储已上传文件 { [itemId]: { name: string, url: string } }
    };
  },
  computed: {
    // 按sortOrder排序的表单项
    sortedItems () {
      if (!this.selectedTemplate || !this.selectedTemplate.itemList) {
        return [];
      }
      return [...this.selectedTemplate.itemList].sort((a, b) => a.sortOrder - b.sortOrder);
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
        this.loadTemplates();
      } catch (error) {
        console.error('获取用户认证信息时发生错误:', error);
        this.$message.error('认证信息加载失败,请重试！');
      }
    },

    // 加载表单模板列表
    async loadTemplates () {
      this.loading = true;
      try {
        const params = {
          active: true,
          current: (this.currentPage - 1) * this.pageSize,
          size: this.pageSize
        };

        // 如果有搜索关键词，添加到参数中
        if (this.searchKeyword) {
          params.templateName = this.searchKeyword;
        }

        const response = await this.$axios.post(API.QUERY_TEMPLATES, params, { withCredentials: true });

        if (response.data && response.data.code === 200) {
          this.templates = response.data.data || [];
          this.total = response.data.data.total || 0;
        } else {
          this.$message.error('获取表单模板列表失败');
        }
      } catch (error) {
        console.error('获取表单模板列表失败:', error);
        this.$message.error('获取表单模板列表失败');
      } finally {
        this.loading = false;
      }
    },

    // 处理分页变化
    handlePageChange (page) {
      this.currentPage = page;
      this.loadTemplates();
    },

    // 处理搜索
    handleSearch () {
      this.currentPage = 1;
      this.loadTemplates();
    },

    // 选择表单模板
    async selectTemplate (template) {
      this.selectedTemplate = { ...template };
      this.detailLoading = true;

      try {
        const response = await this.$axios.post(API.QUERY_TEMPLATE_DETAIL, {
          templateId: template.templateId
        }, { withCredentials: true });

        if (response.data && response.data.code === 200) {
          this.selectedTemplate = response.data.data;
          this.initFormData();
        } else {
          this.$message.error('获取表单模板详情失败');
        }
      } catch (error) {
        console.error('获取表单模板详情失败:', error);
        this.$message.error('获取表单模板详情失败');
      } finally {
        this.detailLoading = false;
      }
    },

    // 初始化表单数据和验证规则
    initFormData () {
      const formData = {};
      const formRules = {};

      if (this.selectedTemplate && this.selectedTemplate.itemList) {
        this.selectedTemplate.itemList.forEach(item => {
          // 初始化表单数据
          formData['item_' + item.itemId] = '';

          // 设置验证规则
          if (item.required) {
            // formRules['item_' + item.itemId] = [
            //   { required: true, message: `请输入${item.itemName}`, trigger: 'blur' }
            // ];
            formRules['item_' + item.itemId] = [
              {
                validator: (rule, value, callback) => {
                  if (item.style === 1) {
                    // 文件上传项：检查 uploadedFiles 是否有值
                    this.uploadedFiles[item.itemId]
                      ? callback()
                      : callback(new Error(`请上传${item.itemName}`));
                  } else {
                    // 普通文本框
                    value ? callback() : callback(new Error(`请输入${item.itemName}`));
                  }
                },
                trigger: item.style === 0 ? 'blur' : 'change'
              }
            ];
          }
        });
      }

      // 重要：先初始化对象，然后再赋值给数据属性
      this.formData = formData;
      this.formRules = formRules;

      // 确保表单被正确地重置
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate();
        }
      });
    },

    // 重置表单
    resetForm () {
      this.selectedTemplate = null;
      this.formData = {};
      this.formRules = {};
      if (this.$refs.formRef) {
        this.$refs.formRef.resetFields();
      }
    },

    // 提交表单
    submitForm () {
      if (!this.$refs.formRef) {
        this.$message.warning('表单加载失败，请重试');
        return;
      }

      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          try {
            const fileUrl = sessionStorage.getItem("uploadUrl") || '';

            // 构建提交数据
            const formDataList = [];
            for (const key in this.formData) {
              if (key.startsWith('item_')) {
                const itemId = key.replace('item_', '');
                // 判断是否为文件上传项
                const isFileItem = this.sortedItems.some(item =>
                  item.itemId == itemId && item.style === 1
                );

                formDataList.push({
                  itemId: itemId,
                  value: isFileItem ? fileUrl : this.formData[key]
                });
              }
            }

            const response = await this.$axios.post(API.SAVE_SUBMISSION, {
              templateId: this.selectedTemplate.templateId,
              userId: this.userId,
              formDataList: formDataList
            }, { withCredentials: true });

            if (response.data && response.data.code === 200) {
              this.$message.success('表单提交成功');
              this.resetForm();
            } else {
              this.$message.error('表单提交失败');
            }
          } catch (error) {
            console.error('表单提交失败:', error);
            this.$message.error('表单提交失败');
          }
        } else {
          this.$message.warning('请填写必填项');
          return false;
        }
      });
    },

    // 上传成功回调，将后端返回的文件url存入sessionStorage中，方便提交表达时提交
    handleUploadSuccess (response, file, itemId) {
      sessionStorage.setItem("uploadUrl", response.msg);
      // 手动触发表单验证
      sessionStorage.setItem("uploadItemId", itemId);

      this.$set(this.uploadedFiles, itemId, {
        name: file.name,
        url: response.msg // 假设返回格式为 { msg: "文件URL" }
      });
    },

    // 上传前校验
    beforeUpload (file) {
      const isAllowedType = ['image/jpeg', 'image/png', 'application/pdf'].includes(file.type);
      const isLt10MB = file.size / 1024 / 1024 < 10;
      if (!isAllowedType) {
        this.$message.error('仅支持 JPG/PNG/PDF 格式!');
      }
      if (!isLt10MB) {
        this.$message.error('文件大小不能超过 10MB!');
      }
      return isAllowedType && isLt10MB;
    },

    // 移除已上传文件
    removeFile (itemId) {
      this.$delete(this.uploadedFiles, itemId);
      sessionStorage.removeItem(`uploadUrl_${itemId}`);
      this.$refs.formRef.validateField(`item_${itemId}`);
    }
  }
};
</script>

<style scoped lang="scss">
.form-submission-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .form-submission-layout {
    display: flex;
    min-height: 600px;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    overflow: hidden;

    // 左侧 - 表单模板列表
    .template-list-panel {
      flex: 1;
      display: flex;
      flex-direction: column;
      border-right: 1px solid #f0f0f0;

      .panel-header {
        padding: 20px;
        border-bottom: 1px solid #f0f0f0;

        h2 {
          margin: 0 0 15px;
          font-size: 20px;
          color: #333;
        }

        .search-box {
          width: 100%;
        }
      }

      .template-list {
        flex: 1;
        overflow-y: auto;
        padding: 10px;

        .template-item {
          padding: 12px 15px;
          border-radius: 6px;
          margin-bottom: 8px;
          cursor: pointer;
          transition: all 0.3s;
          border: 1px solid #f0f0f0;

          &:hover {
            background-color: #f5f7fa;
            border-color: #e4e7ed;
          }

          &.selected {
            background-color: #ecf5ff;
            border-color: #c6e2ff;
            color: #409eff;
          }

          .template-name {
            display: flex;
            align-items: center;
            font-size: 14px;

            i {
              margin-right: 8px;
              font-size: 16px;
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

    // 右侧 - 表单内容区
    .form-content-panel {
      flex: 2;
      padding: 20px;
      display: flex;
      flex-direction: column;

      .form-detail {
        height: 100%;
        display: flex;
        flex-direction: column;

        .form-header {
          margin-bottom: 20px;
          padding-bottom: 15px;
          border-bottom: 1px solid #f0f0f0;

          .form-title {
            margin: 0;
            font-size: 24px;
            color: #333;
            font-weight: bold;
          }
        }

        .form-content {
          flex: 1;
          overflow-y: auto;
          padding-right: 10px;

          .el-form-item {
            margin-bottom: 22px;
          }
        }

        .form-actions {
          margin-top: 30px;
          text-align: center;
          padding-top: 20px;
          border-top: 1px solid #f0f0f0;

          .el-button {
            padding: 12px 30px;
            margin: 0 10px;
          }
        }
      }

      .empty-content {
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
  }

  .upload {
    display: flex;
    align-items: center;
    gap: 10px; /* 控制按钮和提示之间的间距 */
  }

  .el-upload__tip {
    font-size: 10px;
    color: #909399;
    margin-left: auto; /* 关键属性，使文本向右靠齐 */
  }
}
</style>

