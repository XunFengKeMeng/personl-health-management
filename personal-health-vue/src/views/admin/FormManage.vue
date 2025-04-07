<template>
  <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
    <!-- 顶部查询/操作区 -->
    <el-row style="padding: 10px;margin-left: 10px;">
      <el-row>
        <!-- 查询字段 -->
        <el-input
          size="small"
          style="width: 238px;margin-left: 5px;margin-right: 6px;"
          v-model="templateQueryDto.templateName"
          placeholder="模板名称"
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
            style="background-color: #12a182; color: rgb(247,248,249);border: none;"
            class="customer"
            type="info"
            @click="add()"
          ><i class="el-icon-plus"></i> 新增模板</el-button>
        </span>
      </el-row>
    </el-row>

    <!-- 表格展示 -->
    <el-row style="margin: 0 20px;border-top: 1px solid rgb(245,245,245);">
      <el-table
        :data="tableData"
        style="width: 100%"
      >
        <el-table-column
          prop="templateId"
          width="120"
          label="模板ID"
          :formatter="formatTemplateId"
        ></el-table-column>
        <el-table-column
          prop="templateName"
          label="模板名称"
        ></el-table-column>
        <el-table-column
          prop="creatorName"
          label="创建者"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="active"
          label="状态"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.active ? 'success' : 'info'"
              size="small"
            >
              {{ scope.row.active ? '已发布' : '未发布' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="180"
        >
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column
          label="操作"
          width="170"
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

    <!-- 表单模板管理弹窗 -->
    <el-dialog
      :show-close="false"
      :visible.sync="dialogTemplateOperation"
      width="50%"
    >
      <div slot="title">
        <p class="dialog-title">{{ isOperation ? '编辑表单模板' : '新增表单模板' }}</p>
      </div>
      <div style="padding:0 20px;">
        <el-row>
          <span class="dialog-hover">模板名称</span>
          <input
            class="dialog-input"
            v-model="data.templateName"
            placeholder="请输入模板名称"
          />
        </el-row>
        <el-row style="margin-top: 15px;">
          <span class="dialog-hover">是否发布</span>
          <div style="margin-top: 10px;">
            <el-switch
              v-model="data.active"
              active-color="#13ce66"
              inactive-color="#909399"
            ></el-switch>
            <span style="margin-left: 10px; font-size: 13px; color: #909399;">
              {{ data.active ? '已发布' : '未发布' }}
            </span>
          </div>
        </el-row>
        
        <!-- 表单项管理区域 -->
        <el-row style="margin-top: 20px;">
          <div class="form-items-header">
            <span class="dialog-hover" style="font-weight: bold;">表单项管理</span>
            <el-button 
              size="mini"
              type="primary"
              icon="el-icon-plus"
              @click="showFormItemSelector"
              style="margin-left: 10px;"
            >添加表单项</el-button>
          </div>
          
          <!-- 已选择的表单项列表 -->
          <div v-if="data.itemList && data.itemList.length > 0" class="form-items-container">
            <el-card 
              v-for="(item, index) in data.itemList" 
              :key="index"
              class="form-item-card"
              shadow="hover"
            >
              <div class="form-item-header">
                <span>
                  <span v-if="item.required" class="required-mark">*</span>
                  <span>{{ item.itemName }}</span>
                </span>
                <el-button 
                  type="text" 
                  icon="el-icon-delete" 
                  @click="removeFormItem(index)"
                  class="delete-btn"
                ></el-button>
              </div>
              <div class="form-item-content">
                <el-input
                  :placeholder="item.placeholder"
                  disabled
                ></el-input>
              </div>
              <div class="form-item-order">
                <el-button 
                  type="text" 
                  icon="el-icon-arrow-up" 
                  @click="moveItemUp(index)"
                  :disabled="index === 0"
                ></el-button>
                <span>序号: {{ item.sortOrder }}</span>
                <el-button 
                  type="text" 
                  icon="el-icon-arrow-down" 
                  @click="moveItemDown(index)"
                  :disabled="index === data.itemList.length - 1"
                ></el-button>
              </div>
            </el-card>
          </div>
          <div v-else class="empty-form-items">
            <i class="el-icon-document"></i>
            <p>暂无表单项，请点击"添加表单项"按钮添加</p>
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
        >{{ isOperation ? '保存' : '新增' }}</el-button>
        <el-button
          class="customer"
          size="small"
          style="background-color: #0088d2;border: none; color: white;"
          @click="cancel"
        >取消</el-button>
      </span>
    </el-dialog>

    <!-- 表单项选择器弹窗 -->
    <el-dialog
      title="选择表单项"
      :visible.sync="formItemSelectorVisible"
      width="40%"
      append-to-body
    >
      <div style="margin-bottom: 15px;">
        <el-input
          v-model="formItemSearchKey"
          placeholder="搜索表单项"
          clearable
          prefix-icon="el-icon-search"
          @input="filterFormItems"
        ></el-input>
      </div>
      <el-table
        :data="filteredFormItems"
        style="width: 100%"
        height="350px"
        @selection-change="handleItemSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
        ></el-table-column>
        <el-table-column
          prop="itemId"
          label="ID"
          width="80"
          :formatter="formatItemId"
        ></el-table-column>
        <el-table-column
          prop="itemName"
          label="表单项名称"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.required" class="required-mark">*</span>
            {{ scope.row.itemName }}
          </template>
        </el-table-column>
        <el-table-column
          prop="placeholder"
          label="提示文本"
        ></el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formItemSelectorVisible = false">取消</el-button>
        <el-button type="primary" @click="addSelectedFormItems">确认添加</el-button>
      </div>
    </el-dialog>
  </el-row>
</template>

<script>
import { getToken } from "@/utils/storage"
const API = {
  UPDATE_TEMPLATE: '/formTemplate/update',
  DELETE_TEMPLATE: '/formTemplate/delete',
  INSERT_TEMPLATE: '/formTemplate/save',
  QUERY_TEMPLATE_LIST: '/formTemplate/query',
  QUERY_TEMPLATE_DETAIL: '/formTemplate/queryDetail',
  QUERY_FORM_ITEMS: '/formItem/queryItems'
}

export default {
  data () {
    return {
      data: {
        active: false,
        itemList: []
      }, // 当前操作的表单模板数据
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      dialogTemplateOperation: false, // 表单模板弹窗开关
      formItemSelectorVisible: false, // 表单项选择器弹窗开关
      isOperation: false, // 是否是编辑操作
      tableData: [],
      templateQueryDto: {}, // 查询参数
      formItems: [], // 所有可选的表单项
      filteredFormItems: [], // 过滤后的表单项
      formItemSearchKey: '', // 表单项搜索关键字
      selectedFormItems: [], // 选中的表单项
      currentUserId:null
    };
  },
  created () {
    this.fetchTemplateData();
    this.fetchAllFormItems();
    this.getCurrentUserId();
  },
  methods: {
    // 获取当前用户ID
    async getCurrentUserId() {
      try {
        const token = { token: getToken() };
        const res = await this.$axios.post('user/auth', token);
        // 错误处理
        if (res.code === 400) {
          this.$message.error(res.data.msg);
          this.$router.push('/login');
          return;
        }
        // 获取发送者ID
        this.currentUserId = res.data.data.userId;
      } catch (error) {
        console.error('获取用户认证信息时发生错误:', error);
        this.$message.error('认证信息加载失败,请重试！');
      }
    },

    // 获取表单模板数据
    async fetchTemplateData () {
      try {
        const params = {
          current: this.pageSize * (this.currentPage - 1),
          size: this.pageSize,
          ...this.templateQueryDto
        };
        const response = await this.$axios.post(API.QUERY_TEMPLATE_LIST, params);
        const { data } = response;
        this.tableData = data.data;
        this.totalItems = data.total;
      } catch (error) {
        console.error('查询表单模板异常:', error);
      }
    },

    // 获取表单模板详情（包含关联表单项）
    async fetchTemplateDetail (templateId) {
      try {
        const response = await this.$axios.post(API.QUERY_TEMPLATE_DETAIL, { templateId });
        const { data } = response;
        if (data.code === 200 && data.data) {
          this.data = data.data;
          // 确保itemList存在
          if (!this.data.itemList) {
            this.data.itemList = [];
          }
        }
      } catch (error) {
        console.error('获取表单模板详情异常:', error);
      }
    },

    // 获取所有表单项
    async fetchAllFormItems () {
      try {
        const params = {
          current: 0,
          size: 999 // 获取所有表单项
        };
        const response = await this.$axios.post(API.QUERY_FORM_ITEMS, params);
        const { data } = response;
        this.formItems = data.data || [];
        this.filteredFormItems = [...this.formItems];
      } catch (error) {
        console.error('获取表单项异常:', error);
      }
    },

    // 新增表单模板
    async addOperation () {
      if (!this.validateFormData()) {
        return;
      }
      
      try {
        // 设置创建者ID
        this.data.creatorId = this.currentUserId;
        // 确保每个表单项都有正确的sortOrder
        this.updateSortOrders();
        const response = await this.$axios.post(API.INSERT_TEMPLATE, this.data);
        if (response.data.code === 200) {
          this.fetchTemplateData();
          this.cancel();
          this.$notify({ duration: 2000, title: '新增成功', message: '表单模板已添加', type: 'success' });
        }
      } catch (error) {
        console.error('新增表单模板异常:', error);
        this.$message.error('提交失败，请稍后再试！');
      }
    },

    // 修改表单模板
    async updateOperation () {
      if (!this.validateFormData()) {
        return;
      }
      
      try {
        // 确保每个表单项都有正确的sortOrder
        this.updateSortOrders();
        
        const response = await this.$axios.post(API.UPDATE_TEMPLATE, this.data);
        if (response.data.code === 200) {
          this.fetchTemplateData();
          this.cancel();
          this.$notify({ duration: 2000, title: '修改成功', message: '表单模板已更新', type: 'success' });
        }
      } catch (error) {
        console.error('修改表单模板异常:', error);
        this.$message.error('提交失败，请稍后再试！');
      }
    },

    // 删除表单模板
    async handleDelete (row) {
      const confirmed = await this.$swalConfirm({
        title: '删除表单模板',
        text: '删除后不可恢复，是否继续？',
        icon: 'warning',
      });
      if (confirmed) {
        try {
          const response = await this.$axios.post(API.DELETE_TEMPLATE, { templateId: row.templateId });
          if (response.data.code === 200) {
            this.$notify({ duration: 2000, title: '删除成功', message: '表单模板已删除', type: 'success' });
            this.fetchTemplateData();
          }
        } catch (e) {
          console.error('删除表单模板异常：', e);
        }
      }
    },

    // 显示表单项选择器
    showFormItemSelector () {
      this.formItemSearchKey = '';
      this.selectedFormItems = [];
      this.filterFormItems();
      this.formItemSelectorVisible = true;
    },

    // 添加选中的表单项
    addSelectedFormItems () {
      if (this.selectedFormItems.length === 0) {
        this.$message.warning('请至少选择一个表单项');
        return;
      }

      // 获取已有的itemId列表，避免重复添加
      const existingItemIds = this.data.itemList.map(item => item.itemId);
      
      // 过滤出未添加的表单项
      const newItems = this.selectedFormItems.filter(item => !existingItemIds.includes(item.itemId));
      
      if (newItems.length === 0) {
        this.$message.warning('选中的表单项已全部添加');
        this.formItemSelectorVisible = false;
        return;
      }

      // 为新表单项设置初始的sortOrder
      const nextSortOrder = this.data.itemList.length > 0 
        ? Math.max(...this.data.itemList.map(item => item.sortOrder)) + 1 
        : 1;
      
      newItems.forEach((item, index) => {
        this.data.itemList.push({
          ...item,
          sortOrder: nextSortOrder + index
        });
      });

      // 更新排序号
      this.updateSortOrders();
      
      this.formItemSelectorVisible = false;
      this.$message.success(`已添加${newItems.length}个表单项`);
    },

    // 移除表单项
    removeFormItem (index) {
      this.data.itemList.splice(index, 1);
      // 更新排序号
      this.updateSortOrders();
    },

    // 上移表单项
    moveItemUp (index) {
      if (index === 0) return;
      
      const temp = this.data.itemList[index];
      this.data.itemList[index] = this.data.itemList[index - 1];
      this.data.itemList[index - 1] = temp;
      
      // 更新排序号
      this.updateSortOrders();
    },

    // 下移表单项
    moveItemDown (index) {
      if (index === this.data.itemList.length - 1) return;
      
      const temp = this.data.itemList[index];
      this.data.itemList[index] = this.data.itemList[index + 1];
      this.data.itemList[index + 1] = temp;
      
      // 更新排序号
      this.updateSortOrders();
    },

    // 更新排序号，确保从1开始连续递增
    updateSortOrders () {
      if (this.data.itemList && this.data.itemList.length > 0) {
        this.data.itemList.forEach((item, index) => {
          item.sortOrder = index + 1;
        });
      }
    },

    // 过滤表单项
    filterFormItems () {
      if (!this.formItemSearchKey) {
        this.filteredFormItems = [...this.formItems];
      } else {
        const keyword = this.formItemSearchKey.toLowerCase();
        this.filteredFormItems = this.formItems.filter(item => 
          item.itemName.toLowerCase().includes(keyword) || 
          item.placeholder.toLowerCase().includes(keyword)
        );
      }
    },

    // 表单数据验证
    validateFormData () {
      if (!this.data.templateName || this.data.templateName.trim() === '') {
        this.$message.warning('请输入表单模板名称');
        return false;
      }
      return true;
    },

    // 处理表单项选择变化
    handleItemSelectionChange (selection) {
      this.selectedFormItems = selection;
    },

    handleFilter () { this.currentPage = 1; this.fetchTemplateData(); },
    handleFilterClear () { this.templateQueryDto.templateName = ''; this.handleFilter(); },
    handleSizeChange (val) { this.pageSize = val; this.currentPage = 1; this.fetchTemplateData(); },
    handleCurrentChange (val) { this.currentPage = val; this.fetchTemplateData(); },
    
    add () { 
      this.data = { 
        templateName: '',
        active: false,
        itemList: [] 
      }; 
      this.isOperation = false; 
      this.dialogTemplateOperation = true; 
    },
    
    async handleEdit (row) { 
      this.isOperation = true;
      // 先获取模板详情，包含关联表单项信息
      await this.fetchTemplateDetail(row.templateId);
      this.dialogTemplateOperation = true; 
    },
    
    cancel () { 
      this.dialogTemplateOperation = false; 
      this.data = { 
        active: false,
        itemList: [] 
      }; 
    },
    
    // 将 templateId 格式化为 4 位，不足部分补零
    formatTemplateId (row, column, cellValue) {
      return String(cellValue).padStart(4, '0');
    },
    
    // 将 itemId 格式化为 4 位，不足部分补零
    formatItemId (row, column, cellValue) {
      return String(cellValue).padStart(4, '0');
    },
    
    // 格式化日期
    formatDate (timestamp) {
      if (!timestamp) return '';
      
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      const hour = date.getHours().toString().padStart(2, '0');
      const minute = date.getMinutes().toString().padStart(2, '0');
      
      return `${year}-${month}-${day} ${hour}:${minute}`;
    }
  }
};
</script>

<style scoped lang="scss">
.text-button {
  margin-right: 10px;
  color: #409eff;
  cursor: pointer;
}

.dialog-input {
  width: 100%;
  height: 32px;
  padding: 0 10px;
  margin-top: 5px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-sizing: border-box;
}

.dialog-hover {
  font-size: 14px;
  color: #606266;
}

.form-items-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.form-items-container {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
  background-color: #f8f8f8;
  max-height: 400px;
  overflow-y: auto;
}

.form-item-card {
  margin-bottom: 10px;
  border-left: 3px solid #409eff;
}

.form-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: bold;
}

.form-item-content {
  margin-bottom: 10px;
}

.form-item-order {
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #909399;
  font-size: 12px;
}

.required-mark {
  color: #f56c6c;
  margin-right: 4px;
}

.delete-btn {
  color: #f56c6c;
}

.empty-form-items {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  background-color: #f8f8f8;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  color: #909399;
}

.empty-form-items i {
  font-size: 48px;
  margin-bottom: 10px;
}
</style>
