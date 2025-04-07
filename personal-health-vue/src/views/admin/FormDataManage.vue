<template>
  <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
    <!-- 顶部查询/操作区 -->
    <el-row style="padding: 10px;margin-left: 10px;">
      <el-row>
        <!-- 查询字段 -->
        <el-input
          size="small"
          style="width: 180px;margin-left: 5px;margin-right: 6px;"
          v-model="formSubmissionQueryDto.userName"
          placeholder="提交用户"
          clearable
          @clear="handleUserNameClear"
        >
        </el-input>
        <el-input
          size="small"
          style="width: 180px;margin-left: 5px;margin-right: 6px;"
          v-model="formSubmissionQueryDto.templateName"
          placeholder="表单名称"
          clearable
          @clear="handleTemplateNameClear"
        >
          <el-button
            slot="append"
            @click="handleFilter"
            icon="el-icon-search"
          ></el-button>
        </el-input>
      </el-row>
    </el-row>

    <!-- 表格展示 -->
    <el-row style="margin: 0 20px;border-top: 1px solid rgb(245,245,245);">
      <el-table
        :data="tableData"
        style="width: 100%"
      >
        <el-table-column
          prop="submissionId"
          width="120"
          label="提交记录ID"
          :formatter="formatSubmissionId"
        ></el-table-column>
        <el-table-column
          prop="templateName"
          label="表单模板名"
        ></el-table-column>
        <el-table-column
          prop="userName"
          label="提交用户"
        ></el-table-column>
        <el-table-column
          prop="status"
          width="120"
          label="审核状态"
        >
          <template slot-scope="scope">
            <el-tag
              :type="getStatusTagType(scope.row.status)"
              size="small"
            >
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="submitTime"
          width="180"
          label="提交时间"
        ></el-table-column>
        <!-- 操作列 -->
        <el-table-column
          label="操作"
          width="170"
        >
          <template slot-scope="scope">
            <span
              class="text-button"
              @click="handleEdit(scope.row)"
            >审核</span>
            <span
              class="text-button"
              @click="handleDelete(scope.row)"
            >删除</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        style="margin:10px 0;"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalItems"
      ></el-pagination>
    </el-row>

    <!-- 表单提交记录审核弹窗 -->
    <el-dialog
      :show-close="false"
      :visible.sync="dialogFormSubmissionOperation"
      width="40%"
    >
      <div slot="title">
        <p class="dialog-title">审核表单</p>
      </div>
      <div style="padding:0 20px;">
        <!-- 基本信息部分 -->
        <div class="form-info-section">
          <el-row class="info-item">
            <span class="info-label">提交记录ID:</span>
            <span class="info-value">{{ formatSubmissionId(null, null, data.submissionId) }}</span>
          </el-row>
          <el-row class="info-item">
            <span class="info-label">表单模板名:</span>
            <span class="info-value">{{ data.templateName }}</span>
          </el-row>
          <el-row class="info-item">
            <span class="info-label">提交用户:</span>
            <span class="info-value">{{ data.userName }}</span>
          </el-row>
          <el-row class="info-item">
            <span class="info-label">提交时间:</span>
            <span class="info-value">{{ data.submitTime }}</span>
          </el-row>
          <el-row class="info-item">
            <span class="info-label">审核状态:</span>
            <div style="display: inline-block; margin-left: 10px;">
              <el-switch
                v-model="approvalStatus"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="通过"
                inactive-text="驳回"
                @change="handleStatusChange"
              ></el-switch>
            </div>
          </el-row>
        </div>
        
        <div class="form-data-section">
          <h3>表单数据</h3>
          <div 
            v-for="(item, index) in sortedFormDataList" 
            :key="item.formDataId"
            class="form-item"
          >
            <div class="form-item-label">
              <span v-if="item.required" class="required-star">*</span>
              {{ item.itemName }}:
            </div>
            <div class="form-item-value">{{ item.value || '无' }}</div>
          </div>
        </div>
      </div>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          size="small"
          style="background-color: #12a182;color: rgb(247,248,249);border: none;"
          class="customer"
          type="info"
          @click="updateOperation"
        >确认</el-button>
        <el-button
          class="customer"
          size="small"
          style="background-color: #0088d2;border: none; color: white;"
          @click="cancel"
        >取消</el-button>
      </span>
    </el-dialog>
  </el-row>
</template>

<script>
const API = {
  UPDATE_FORM_SUBMISSION: '/formSubmission/update',
  DELETE_FORM_SUBMISSION: '/formSubmission/delete',
  QUERY_FORM_SUBMISSION_LIST: '/formSubmission/query',
  QUERY_FORM_SUBMISSION_DETAIL: '/formSubmission/queryDetail'
}

export default {
  data () {
    return {
      data: {
        formDataList: []
      }, // 当前操作的表单提交记录数据
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      dialogFormSubmissionOperation: false, // 弹窗开关
      tableData: [],
      formSubmissionQueryDto: {}, // 查询参数
      approvalStatus: true // 默认审核通过
    };
  },
  computed: {
    sortedFormDataList() {
      if (!this.data.formDataList) return [];
      return [...this.data.formDataList].sort((a, b) => a.sortOrder - b.sortOrder);
    }
  },
  created () {
    this.fetchFormSubmissionData();
  },
  methods: {
    // 获取表单提交记录数据
    async fetchFormSubmissionData () {
      try {
        const params = {
          current: this.pageSize * (this.currentPage - 1),
          size: this.pageSize,
          ...this.formSubmissionQueryDto
        };
        const response = await this.$axios.post(API.QUERY_FORM_SUBMISSION_LIST, params);
        const { data } = response;
        this.tableData = data.data;
        this.totalItems = data.total;
      } catch (error) {
        console.error('查询表单提交记录异常:', error);
      }
    },

    // 获取表单提交记录详情
    async fetchFormSubmissionDetail (submissionId) {
      try {
        const response = await this.$axios.post(API.QUERY_FORM_SUBMISSION_DETAIL, { submissionId });
        if (response.data.code === 200) {
          this.data = response.data.data;
          // 根据状态设置开关值
          this.approvalStatus = this.data.status === 1;
        }
      } catch (error) {
        console.error('获取表单提交记录详情异常:', error);
      }
    },

    // 更新表单提交记录
    async updateOperation () {
      try {
        const submitData = {
          submissionId: this.data.submissionId,
          status: this.approvalStatus ? 1 : 2, // 1通过 2驳回
          formDataList: this.data.formDataList
        };
        
        const response = await this.$axios.post(API.UPDATE_FORM_SUBMISSION, submitData);
        if (response.data.code === 200) {
          this.fetchFormSubmissionData();
          this.cancel();
          this.$notify({ 
            duration: 2000, 
            title: '审核成功', 
            message: `表单已${this.approvalStatus ? '通过' : '驳回'}`, 
            type: 'success' 
          });
        }
      } catch (error) {
        console.error('更新表单提交记录异常:', error);
        this.$message.error('提交失败，请稍后再试！');
      }
    },

    // 处理状态变更
    handleStatusChange(value) {
      // 已在计算属性中更新
    },

    // 删除表单提交记录
    async handleDelete (row) {
      const confirmed = await this.$swalConfirm({
        title: '删除表单提交记录',
        text: '删除后不可恢复，是否继续？',
        icon: 'warning',
      });
      if (confirmed) {
        try {
          const response = await this.$axios.post(API.DELETE_FORM_SUBMISSION, { submissionId: row.submissionId });
          if (response.data.code === 200) {
            this.$notify({ duration: 2000, title: '删除成功', message: '表单提交记录已删除', type: 'success' });
            this.fetchFormSubmissionData();
          }
        } catch (e) {
          console.error('删除表单提交记录异常：', e);
        }
      }
    },

    // 获取状态文本
    getStatusText(status) {
      switch (status) {
        case 0: return '待审核';
        case 1: return '已通过';
        case 2: return '已驳回';
        default: return '未知';
      }
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      switch (status) {
        case 0: return 'info';
        case 1: return 'success';
        case 2: return 'danger';
        default: return 'info';
      }
    },

    handleFilter () { this.currentPage = 1; this.fetchFormSubmissionData(); },
    handleUserNameClear () { this.formSubmissionQueryDto.userName = ''; this.handleFilter(); },
    handleTemplateNameClear () { this.formSubmissionQueryDto.templateName = ''; this.handleFilter(); },
    handleSizeChange (val) { this.pageSize = val; this.currentPage = 1; this.fetchFormSubmissionData(); },
    handleCurrentChange (val) { this.currentPage = val; this.fetchFormSubmissionData(); },
    async handleEdit (row) { 
      await this.fetchFormSubmissionDetail(row.submissionId);
      this.dialogFormSubmissionOperation = true; 
    },
    cancel () { this.dialogFormSubmissionOperation = false; this.data = { formDataList: [] }; },
    // 将 submissionId 格式化为 4 位，不足部分补零
    formatSubmissionId (row, column, cellValue) {
      return String(cellValue).padStart(4, '0');
    },
  }
};
</script>

<style scoped lang="scss">
.text-button {
  margin-right: 10px;
  color: #409eff;
  cursor: pointer;
}

.form-info-section {
  padding: 10px 0;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.info-item {
  margin-bottom: 10px;
}

.info-label {
  font-weight: 500;
  color: #606266;
  width: 90px;
  display: inline-block;
}

.info-value {
  color: #333;
}

.form-data-section {
  h3 {
    font-size: 16px;
    margin-bottom: 15px;
    color: #303133;
  }
}

.form-item {
  display: flex;
  margin-bottom: 15px;
  align-items: flex-start;
}

.form-item-label {
  width: 120px;
  text-align: right;
  padding-right: 12px;
  font-weight: 500;
  color: #606266;
}

.form-item-value {
  flex: 1;
  line-height: 1.5;
  padding: 0 5px;
  color: #333;
}

.required-star {
  color: #f56c6c;
  margin-right: 4px;
}

.dialog-title {
  font-size: 18px;
  font-weight: 500;
}
</style>
