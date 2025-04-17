<template>
  <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
    <!-- 顶部查询/操作区 -->
    <el-row style="padding: 10px;margin-left: 10px;">
      <el-row>
        <!-- 查询字段 -->
        <el-input
          size="small"
          style="width: 238px;margin-left: 5px;margin-right: 6px;"
          v-model="formItemQueryDto.itemName"
          placeholder="表单项名"
          clearable
          @clear="handleFilterClear"
        >
          <el-button
            slot="append"
            @click="handleFilter"
            icon="el-icon-search"
          ></el-button>
        </el-input>
        <!-- 操作按钮 -->
        <span style="float: right;">
          <el-button
            size="small"
            style="background-color: #ee3f4d; color: rgb(247,248,249);border: none;"
            class="customer"
            type="info"
            @click="batchDelete"
          ><i class="el-icon-minus"></i> 批量删除</el-button>

          <el-button
            size="small"
            style="background-color: #12a182; color: rgb(247,248,249);border: none;"
            class="customer"
            type="info"
            @click="add()"
          ><i class="el-icon-plus"></i> 新增表单项</el-button>
        </span>
      </el-row>
    </el-row>

    <!-- 表格展示 -->
    <el-row style="margin: 0 20px;border-top: 1px solid rgb(245,245,245);">
      <el-table
        @selection-change="handleSelectionChange"
        :data="tableData"
        style="width: 100%"
      >
        <el-table-column
          type="selection"
          width="80"
        ></el-table-column>
        <el-table-column
          prop="itemId"
          width="100"
          label="编号"
          :formatter="formatItemId"
        ></el-table-column>
        <el-table-column
          prop="itemName"
          label="表单项名"
          width="130"
        ></el-table-column>
        <el-table-column
          prop="healthMetricName"
          label="关联健康指标"
          width="130"
        >
          <template slot-scope="scope">
            {{ scope.row.healthMetricName || '无' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="placeholder"
          label="提示文本"
          width="180"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="required"
          width="100"
          label="是否必填"
        >
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.required ? 'success' : 'info'"
              size="small"
            >
              {{ scope.row.required ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column
          label="操作"
          width="170"
          fixed="right"
        >
          <template slot-scope="scope">
            <span
              class="text-button"
              @click="handleEdit(scope.row)"
            >编辑</span>
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

    <!-- 表单项管理弹窗 -->
    <el-dialog
      :show-close="false"
      :visible.sync="dialogFormItemOperation"
      width="30%"
    >
      <div slot="title">
        <p class="dialog-title">{{ isOperation ? '修改表单项' : '新增表单项' }}</p>
      </div>
      <div style="padding:0 20px;">
        <el-row>
          <span class="dialog-hover">表单项名</span>
          <input
            class="dialog-input"
            v-model="data.itemName"
            placeholder="请输入表单项名"
          />
        </el-row>
        <el-row style="margin-top: 15px;">
          <span class="dialog-hover">关联健康指标</span>
          <el-select
            class="dialog-select"
            v-model="data.healthMetricId"
            placeholder="请选择关联健康指标（可选）"
            clearable
          >
            <el-option
              v-for="item in healthMetricOptions"
              :key="item.healthMetricId"
              :label="item.healthMetricName"
              :value="item.healthMetricId"
            >
            </el-option>
          </el-select>
        </el-row>
        <el-row style="margin-top: 15px;">
          <span class="dialog-hover">提示文本</span>
          <input
            class="dialog-input"
            v-model="data.placeholder"
            placeholder="请输入提示文本"
          />
        </el-row>
        <el-row style="margin-top: 15px;">
          <span class="dialog-hover">是否必填</span>
          <div style="margin-top: 10px;">
            <el-switch
              v-model="data.required"
              active-color="#13ce66"
              inactive-color="#909399"
            ></el-switch>
            <span style="margin-left: 10px; font-size: 13px; color: #909399;">
              {{ data.required ? '必填' : '选填' }}
            </span>
          </div>
        </el-row>
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
          @click="isOperation ? updateOperation() : addOperation()"
        >{{ isOperation ? '修改' : '新增' }}</el-button>
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
  UPDATE_FORM_ITEM: '/formItem/update',
  DELETE_FORM_ITEMS: '/formItem/deleteItems',
  INSERT_FORM_ITEM: '/formItem/save',
  QUERY_FORM_ITEM_LIST: '/formItem/queryItems',
  QUERY_HEALTH_METRICS: '/metric/queryMetrics'
}

export default {
  data () {
    return {
      data: {
        required: false // 默认为非必填
      }, // 当前操作的表单项数据
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      dialogFormItemOperation: false, // 弹窗开关
      isOperation: false, // 是否是编辑操作
      tableData: [],
      selectedRows: [],
      formItemQueryDto: {}, // 查询参数
      healthMetricOptions: [] // 健康指标选项
    };
  },
  created () {
    this.fetchFormItemData();
    this.fetchHealthMetricOptions();
  },
  methods: {
    // 获取表单项数据
    async fetchFormItemData () {
      try {
        const params = {
          current: this.pageSize * (this.currentPage - 1),
          size: this.pageSize,
          ...this.formItemQueryDto
        };
        const response = await this.$axios.post(API.QUERY_FORM_ITEM_LIST, params, {withCredentials: true});
        const { data } = response;
        this.tableData = data.data;
        this.totalItems = data.total;
      } catch (error) {
        console.error('查询表单项异常:', error);
      }
    },

    // 获取健康指标选项
    async fetchHealthMetricOptions () {
      try {
        const params = {
          current: 0,
          size: 999 // 获取所有健康指标
        };
        const response = await this.$axios.post(API.QUERY_HEALTH_METRICS, params, {withCredentials: true});
        const { data } = response;
        this.healthMetricOptions = data.data || [];
      } catch (error) {
        console.error('获取健康指标选项异常:', error);
      }
    },

    // 批量删除
    async batchDelete () {
      if (!this.selectedRows.length) {
        this.$message.warning('请先选择表单项');
        return;
      }
      const confirmed = await this.$swalConfirm({
        title: '删除表单项',
        text: '删除后不可恢复，是否继续？',
        icon: 'warning',
      });
      if (confirmed) {
        try {
          let ids = this.selectedRows.map(item => item.itemId);
          const response = await this.$axios.post(API.DELETE_FORM_ITEMS, ids, {withCredentials: true});
          if (response.data.code === 200) {
            this.$notify({ duration: 2000, title: '删除成功', message: '表单项已删除', type: 'success' });
            this.fetchFormItemData();
          }
        } catch (e) {
          console.error('删除表单项异常：', e);
        }
      }
    },

    // 新增表单项
    async addOperation () {
      if (!this.validateFormData()) {
        return;
      }
      
      try {
        const response = await this.$axios.post(API.INSERT_FORM_ITEM, this.data, {withCredentials: true});
        if (response.data.code === 200) {
          this.fetchFormItemData();
          this.cancel();
          this.$notify({ duration: 2000, title: '新增成功', message: '表单项已添加', type: 'success' });
        }
      } catch (error) {
        console.error('新增表单项异常:', error);
        this.$message.error('提交失败，请稍后再试！');
      }
    },

    // 修改表单项
    async updateOperation () {
      if (!this.validateFormData()) {
        return;
      }
      
      try {
        const response = await this.$axios.post(API.UPDATE_FORM_ITEM, this.data, {withCredentials: true});
        if (response.data.code === 200) {
          this.fetchFormItemData();
          this.cancel();
          this.$notify({ duration: 2000, title: '修改成功', message: '表单项已更新', type: 'success' });
        }
      } catch (error) {
        console.error('修改表单项异常:', error);
        this.$message.error('提交失败，请稍后再试！');
      }
    },

    // 表单数据验证
    validateFormData () {
      if (!this.data.itemName || this.data.itemName.trim() === '') {
        this.$message.warning('请输入表单项名称');
        return false;
      }
      return true;
    },

    // 删除单个表单项
    async handleDelete (row) {
      const confirmed = await this.$swalConfirm({
        title: '删除表单项',
        text: '删除后不可恢复，是否继续？',
        icon: 'warning',
      });
      if (confirmed) {
        try {
          const response = await this.$axios.post(API.DELETE_FORM_ITEMS, [row.itemId], {withCredentials: true});
          if (response.data.code === 200) {
            this.$notify({ duration: 2000, title: '删除成功', message: '表单项已删除', type: 'success' });
            this.fetchFormItemData();
          }
        } catch (e) {
          console.error('删除表单项异常：', e);
        }
      }
    },

    handleFilter () { this.currentPage = 1; this.fetchFormItemData(); },
    handleFilterClear () { this.formItemQueryDto.itemName = ''; this.handleFilter(); },
    handleSelectionChange (selection) { this.selectedRows = selection; },
    handleSizeChange (val) { this.pageSize = val; this.currentPage = 1; this.fetchFormItemData(); },
    handleCurrentChange (val) { this.currentPage = val; this.fetchFormItemData(); },
    add () { this.data = { required: false }; this.isOperation = false; this.dialogFormItemOperation = true; },
    handleEdit (row) { this.data = { ...row }; this.isOperation = true; this.dialogFormItemOperation = true; },
    cancel () { this.dialogFormItemOperation = false; this.data = { required: false }; },
    // 将 itemId 格式化为 4 位，不足部分补零
    formatItemId (row, column, cellValue) {
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

// .dialog-input {
//   width: 100%;
//   height: 32px;
//   padding: 0 10px;
//   margin-top: 5px;
//   border: 1px solid #dcdfe6;
//   border-radius: 4px;
//   box-sizing: border-box;
// }

// .dialog-select {
//   width: 100%;
//   margin-top: 5px;
// }

// .dialog-hover {
//   font-size: 14px;
//   color: #606266;
// }
</style>

