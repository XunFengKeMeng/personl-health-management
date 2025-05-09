<template>
  <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
    <!-- 顶部查询/操作区 -->
    <el-row style="padding: 10px;margin-left: 10px;">
      <el-row>
        <!-- 查询字段 -->
        <el-input
          size="small"
          style="width: 238px;margin-left: 5px;margin-right: 6px;"
          v-model="healthMetricQueryDto.healthMetricName"
          placeholder="健康指标名"
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
          ><i class="el-icon-plus"></i> 新增指标</el-button>
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
          prop="healthMetricId"
          width="150"
          label="指标编号"
          :formatter="formatHealthMetricId"
        ></el-table-column>
        <el-table-column
          prop="healthMetricName"
          width="180"
          label="指标名"
        ></el-table-column>
        <el-table-column
          prop="healthMetricUnit"
          label="单位"
          width="150"
        ></el-table-column>
        <el-table-column
          prop="healthMetricRange"
          label="正常值范围"
        ></el-table-column>
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

    <!-- 健康指标管理弹窗 -->
    <el-dialog
      :show-close="false"
      :visible.sync="dialogHealthMetricOperation"
      width="25%"
    >
      <div slot="title">
        <p class="dialog-title">{{ isOperation ? '修改健康指标' : '新增健康指标' }}</p>
      </div>
      <div style="padding:0 20px;">
        <el-row>
          <span class="dialog-hover">指标名</span>
          <input
            class="dialog-input"
            v-model="data.healthMetricName"
            placeholder="健康指标名"
          />
          <span class="dialog-hover">单位</span>
          <input
            class="dialog-input"
            v-model="data.healthMetricUnit"
            placeholder="请输入单位"
          />
          <span class="dialog-hover">正常值范围</span>
          <input
            class="dialog-input"
            v-model="data.healthMetricRange"
            placeholder="请输入正常值范围"
          />
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
  UPDATE_HEALTH_METRIC: '/metric/update',
  DELETE_HEALTH_METRICS: '/metric/deleteMetrics',
  INSERT_HEALTH_METRIC: '/metric/save',
  QUERY_HEALTH_METRIC_LIST: '/metric/queryMetrics'
}
export default {
  data () {
    return {
      data: {}, // 当前操作的健康指标数据
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      dialogHealthMetricOperation: false, // 弹窗开关
      isOperation: false, // 是否是编辑操作
      tableData: [],
      selectedRows: [],
      healthMetricQueryDto: {} // 查询参数
    };
  },
  created () {
    this.fetchHealthMetricData();
  },
  methods: {
    // 获取健康指标数据
    async fetchHealthMetricData () {
      try {
        const params = {
          current: this.pageSize * (this.currentPage - 1),
          size: this.pageSize,
          ...this.healthMetricQueryDto
        };
        const response = await this.$axios.post(API.QUERY_HEALTH_METRIC_LIST, params, { withCredentials: true });
        const { data } = response;
        this.tableData = data.data;
        this.totalItems = data.total;
      } catch (error) {
        console.error('查询健康指标异常:', error);
      }
    },

    // 批量删除
    async batchDelete () {
      if (!this.selectedRows.length) {
        this.$message.warning('请先选择健康指标');
        return;
      }
      const confirmed = await this.$swalConfirm({
        title: '删除健康指标',
        text: '删除后不可恢复，是否继续？',
        icon: 'warning',
      });
      if (confirmed) {
        try {
          let ids = this.selectedRows.map(metric => metric.healthMetricId);
          const response = await this.$axios.post(API.DELETE_HEALTH_METRICS, ids, { withCredentials: true });
          if (response.data.code === 200) {
            this.$notify({ duration: 2000, title: '删除成功', message: '健康指标已删除', type: 'success' });
            this.fetchHealthMetricData();
          }
        } catch (e) {
          console.error('删除健康指标异常：', e);
        }
      }
    },

    // 新增健康指标
    async addOperation () {
      try {
        const response = await this.$axios.post(API.INSERT_HEALTH_METRIC, this.data, { withCredentials: true });
        if (response.data.code === 200) {
          this.fetchHealthMetricData();
          this.cancel();
          this.$notify({ duration: 2000, title: '新增成功', message: '健康指标已添加', type: 'success' });
        }
      } catch (error) {
        console.error('新增健康指标异常:', error);
        this.$message.error('提交失败，请稍后再试！');
      }
    },

    // 修改健康指标
    async updateOperation () {
      try {
        const response = await this.$axios.post(API.UPDATE_HEALTH_METRIC, this.data, { withCredentials: true });
        if (response.data.code === 200) {
          this.fetchHealthMetricData();
          this.cancel();
          this.$notify({ duration: 2000, title: '修改成功', message: '健康指标已更新', type: 'success' });
        }
      } catch (error) {
        console.error('修改健康指标异常:', error);
        this.$message.error('提交失败，请稍后再试！');
      }
    },

    // 删除单个健康指标
    async handleDelete (row) {
      const confirmed = await this.$swalConfirm({
        title: '删除健康指标',
        text: '删除后不可恢复，是否继续？',
        icon: 'warning',
      });
      if (confirmed) {
        try {
          const response = await this.$axios.post(API.DELETE_HEALTH_METRICS, [row.healthMetricId], { withCredentials: true });
          if (response.data.code === 200) {
            this.$notify({ duration: 2000, title: '删除成功', message: '健康指标已删除', type: 'success' });
            this.fetchHealthMetricData();
          }
        } catch (e) {
          console.error('删除健康指标异常：', e);
        }
      }
    },

    handleFilter () { this.currentPage = 1; this.fetchHealthMetricData(); },
    handleFilterClear () { this.healthMetricQueryDto.healthMetricUnit = ''; this.handleFilter(); },
    handleSelectionChange (selection) { this.selectedRows = selection; },
    handleSizeChange (val) { this.pageSize = val; this.currentPage = 1; this.fetchHealthMetricData(); },
    handleCurrentChange (val) { this.currentPage = val; this.fetchHealthMetricData(); },
    add () { this.data = {}; this.isOperation = false; this.dialogHealthMetricOperation = true; },
    handleEdit (row) { this.data = { ...row }; this.isOperation = true; this.dialogHealthMetricOperation = true; },
    cancel () { this.dialogHealthMetricOperation = false; this.data = {}; },
    // 将 healthMetricId 格式化为 4 位，不足部分补零
    formatHealthMetricId (row, column, cellValue) {
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
</style>

