<template>
  <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
    <!-- 顶部查询/操作区 -->
    <el-row style="padding: 10px;margin-left: 10px;">
      <el-row>
        <!-- 顶部查询模块 -->
        <el-select
          size="small"
          style="width: 238px;margin-right: 6px;"
          v-model="articleQueryDto.healthArticleTagId"
          filterable
          clearable
          placeholder="选择或搜索标签"
          @clear="handleFilterClear"
        >
          <el-option
            v-for="item in tagOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>

        <el-input
          size="small"
          style="width: 238px;margin-right: 6px;"
          v-model="articleQueryDto.healthArticleTitle"
          placeholder="资讯标题"
          clearable
          @clear="handleFilterClear"
        >
          <el-button
            slot="append"
            @click="handleFilter"
            icon="el-icon-search"
          ></el-button>
        </el-input>

        <!-- 顶部操作模块 -->
        <span style="float: right;">
          <el-button
            size="small"
            style="background-color: #ee3f4d; color: rgb(247,248,249);border: none;"
            class="customer"
            type="info"
            @click="batchDelete"
          ><i class="el-icon-minus"></i>批量删除</el-button>

          <el-button
            size="small"
            style="background-color: #12a182; color: rgb(247,248,249);border: none;"
            class="customer"
            type="info"
            @click="add()"
          ><i class="el-icon-plus"></i>新增资讯</el-button>
        </span>
      </el-row>
    </el-row>

    <!-- 内容区 -->
    <el-row style="margin: 0 20px;border-top: 1px solid rgb(245,245,245);">
      <el-table
        @selection-change="handleSelectionChange"
        :data="tableData"
        style="width: 100%"
      >
        <el-table-column
          type="selection"
          width="68"
        >
        </el-table-column>
        <el-table-column
          prop="healthArticleId"
          width="98"
          label="资讯ID"
          :formatter="formatArticleId"
        ></el-table-column>
        <el-table-column
          prop="healthArticleTitle"
          width="228"
          label="标题"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="healthArticleTagName"
          width="148"
          label="标签"
        ></el-table-column>
        <el-table-column
          prop="top"
          width="100"
          label="是否推荐"
        >
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.top"
              active-color="#12a182"
              inactive-color="#dcdfe6"
              @change="handleTopChange(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          prop="healthArticleCreateTime"
          width="170"
          label="发布时间"
          :formatter="formatDateTime"
        ></el-table-column>
        <!-- 操作列 -->
        <el-table-column
          label="操作"
          width="130"
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

    <!-- 弹窗（新增/修改健康资讯） -->
    <el-dialog
      :show-close="false"
      :visible.sync="dialogArticleOperation"
      width="60%"
    >
      <div slot="title">
        <p class="dialog-title">{{ !isOperation ? '新增健康资讯' : '修改健康资讯' }}</p>
      </div>
      <div style="padding:0 20px;">
        <el-form
          :model="articleData"
          label-width="100px"
        >
          <el-form-item label="资讯标题">
            <el-input
              v-model="articleData.healthArticleTitle"
              placeholder="请输入资讯标题"
            ></el-input>
          </el-form-item>

          <el-form-item label="标签">
            <el-select
              v-model="articleData.healthArticleTagId"
              filterable
              allow-create
              default-first-option
              placeholder="请选择或输入标签"
              style="width: 100%"
            >
              <el-option
                v-for="item in tagOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="是否推荐">
            <el-switch
              v-model="articleData.top"
              active-color="#12a182"
              inactive-color="#dcdfe6"
            >
            </el-switch>
          </el-form-item>

          <el-form-item label="资讯内容">
            <el-input
              type="textarea"
              v-model="articleData.healthArticleContent"
              placeholder="请输入资讯内容"
              :rows="10"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          size="small"
          v-if="!isOperation"
          style="background-color: #12a182;color: rgb(247,248,249);border: none;"
          class="customer"
          type="info"
          @click="addOperation()"
        >新增</el-button>
        <el-button
          size="small"
          v-else
          style="background-color: #0088d2;color: white;border: none;"
          class="customer"
          type="info"
          @click="updateOperation()"
        >修改</el-button>
        <el-button
          class="customer"
          size="small"
          style="background-color: #0088d2;border: none; color: white;"
          @click="cannel"
        >取消</el-button>
      </span>
    </el-dialog>
  </el-row>
</template>

<script>
const API = {
  QUERY_ARTICLE_LIST: '/article/queryArticles',
  INSERT_ARTICLE: '/article/save',
  UPDATE_ARTICLE: '/article/update',
  DELETE_ARTICLES: '/article/deleteArticles',
  QUERY_TAGS: '/tag/queryTags'
}

export default {
  data () {
    return {
      articleData: {}, // 当前操作的文章数据
      filterText: '',
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      dialogArticleOperation: false, // 开关
      isOperation: false, // 开关-标识新增或修改
      tableData: [],
      selectedRows: [],
      articleQueryDto: {}, // 搜索条件
      tagOptions: [], // 标签选项
    };
  },
  // 初始化时获取健康资讯数据和标签选项
  created () {
    this.fetchFreshData();
    this.fetchTagOptions();
  },
  methods: {
    // 获取标签选项
    async fetchTagOptions () {
      const params = {
        tagId: null
      };
      try {
        const response = await this.$axios.post(API.QUERY_TAGS, params, {withCredentials: true});
        const { data } = response;
        if (data.code === 200) {
          this.tagOptions = data.data.map(tag => ({
            value: tag.tagId,
            label: tag.tagName
          }));
        }
      } catch (error) {
        console.error('获取标签选项异常:', error);
      }
    },
    // 获取健康资讯数据
    async fetchFreshData () {
      try {
        this.tableData = [];
        // 请求参数
        const params = {
          current: this.pageSize * (this.currentPage - 1),
          size: this.pageSize,
          ...this.articleQueryDto,
        };
        const response = await this.$axios.post(API.QUERY_ARTICLE_LIST, params, {withCredentials: true});
        const { data } = response;
        this.tableData = data.data;
        this.totalItems = data.total;
      } catch (error) {
        console.error('查询健康资讯异常:', error);
      }
    },
    // 将 healthArticleId 格式化为 8 位，不足部分补零
    formatArticleId (row, column, cellValue) {
      return String(cellValue).padStart(8, '0');
    },
    // 格式化日期时间
    formatDateTime (row, column, cellValue) {
      if (!cellValue) return '';
      const date = new Date(cellValue);
      return date.toLocaleString();
    },
    // 推荐状态切换
    async handleTopChange (row) {
      try {
        const params = {
          healthArticleId: row.healthArticleId,
          top: row.top
        };
        console.log(params)
        const response = await this.$axios.post(API.UPDATE_ARTICLE, params, {withCredentials: true});
        if (response.data.code === 200) {
          this.$notify({
            duration: 2000,
            title: '推荐状态更新',
            message: '成功',
            type: 'success'
          });
        }
      } catch (error) {
        console.error('更新推荐状态异常:', error);
        // 恢复原状态
        row.top = !row.top;
      }
    },
    // 多选框选中
    handleSelectionChange (selection) {
      this.selectedRows = selection;
    },
    // 批量删除健康资讯
    async batchDelete () {
      if (!this.selectedRows.length) {
        this.$message(`未选中任何数据`);
        return;
      }
      const confirmed = await this.$swalConfirm({
        title: '删除健康资讯',
        text: `删除后不可恢复，是否继续？`,
        icon: 'warning',
      });
      if (confirmed) {
        try {
          let ids = this.selectedRows.map(entity => entity.healthArticleId);
          const response = await this.$axios.post(API.DELETE_ARTICLES, ids, {withCredentials: true});
          if (response.data.code === 200) {
            this.$notify({
              duration: 2000,
              title: '删除操作',
              message: '成功',
              type: 'success'
            });
            this.fetchFreshData();
            return;
          }
        } catch (e) {
          console.error(`健康资讯删除异常：`, e);
        }
      }
    },
    // 修改健康资讯
    async updateOperation () {
      try {
        const response = await this.$axios.post(API.UPDATE_ARTICLE, this.articleData, {withCredentials: true});
        if (response.data.code === 200) {
          this.fetchFreshData();
          this.cannel();
          this.$notify({
            duration: 2000,
            title: '修改操作',
            message: '成功',
            type: 'success'
          });
        }
      } catch (error) {
        console.error('提交表单时出错:', error);
        this.$message.error('提交失败，请稍后再试！');
      }
    },
    // 新增健康资讯
    async addOperation () {
      try {
        const response = await this.$axios.post(API.INSERT_ARTICLE, this.articleData, {withCredentials: true});
        if (response.data.code === 200) {
          this.fetchFreshData();
          this.cannel();
          this.$notify({
            duration: 2000,
            title: '新增操作',
            message: '成功',
            type: 'success'
          });
        }
      } catch (error) {
        console.error('提交表单时出错:', error);
        this.$message.error('提交失败，请稍后再试！');
      }
    },

    // 窗口消失，并清除表单数据
    cannel () {
      this.dialogArticleOperation = false;

      // 使用 setTimeout 延迟清除数据，否则会在弹窗消失前刷新表单
      setTimeout(() => {
        this.clearForm();
      }, 200);
    },
    // 清除弹窗表单数据
    clearForm () {
      this.articleData = {
        healthArticleTitle: '',
        healthArticleContent: '',
        healthArticleTagId: '',
        top: false
      };
    },
    // 弹出添加健康资讯窗口
    add () {
      this.clearForm();
      this.isOperation = false;
      this.dialogArticleOperation = true;
    },
    // 查询重置页数和数据
    handleFilter () {
      this.currentPage = 1;
      this.fetchFreshData();
    },
    // 清除查询框，并重新查询所有数据
    handleFilterClear () {
      this.articleQueryDto = {};
      this.handleFilter();
    },
    // 修改每页的size，并将页数与数据重置
    handleSizeChange (val) {
      this.pageSize = val;
      this.currentPage = 1;
      this.fetchFreshData();
    },
    // 切换到指定页码
    handleCurrentChange (val) {
      this.currentPage = val;
      this.fetchFreshData();
    },
    // 编辑点击事件
    handleEdit (row) {
      // 弹窗标题设置为'修改健康资讯'
      this.isOperation = true;
      // 弹窗
      this.dialogArticleOperation = true;
      // 显示当前行数据
      this.articleData = { ...row };
    },

    // 删除单个健康资讯
    async handleDelete (row) {
      const confirmed = await this.$swalConfirm({
        title: '删除健康资讯',
        text: `删除后不可恢复，是否继续？`,
        icon: 'warning',
      });
      if (confirmed) {
        try {
          let ids = [row.healthArticleId];
          const response = await this.$axios.post(API.DELETE_ARTICLES, ids, {withCredentials: true});
          if (response.data.code === 200) {
            this.$notify({
              duration: 2000,
              title: '删除操作',
              message: '成功',
              type: 'success'
            });
            this.fetchFreshData();
            return;
          }
        } catch (e) {
          console.error(`健康资讯删除异常：`, e);
        }
      }
    },
  },
};
</script>

<style scoped lang="scss">
.tag-tip {
  display: inline-block;
  padding: 5px 10px;
  border-radius: 5px;
  background-color: rgb(245, 245, 245);
  color: rgb(104, 118, 130);
}

.input-def {
  height: 40px;
  line-height: 40px;
  outline: none;
  border: none;
  font-size: 20px;
  color: rgb(102, 102, 102);
  font-weight: 900;
  width: 100%;
}

.dialog-footer {
  /* 使按钮水平居中 */
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 如果需要调整按钮之间的间距 */
.customer {
  margin: 0 8px;
}

.text-button {
  margin-right: 10px;
  color: #409eff;
  cursor: pointer;
}

.content-preview {
  max-height: 50px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
