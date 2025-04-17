<template>
  <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
    <!-- 顶部查询/操作区 -->
    <el-row style="padding: 10px;margin-left: 10px;">
      <el-row>
        <!-- 顶部查询模块 -->
        <el-select
          size="small"
          style="width: 180px;margin-right: 6px;"
          v-model="noticeQueryDto.noticeType"
          filterable
          clearable
          placeholder="消息类型"
          @clear="handleFilterClear"
        >
          <el-option
            v-for="item in noticeTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        
        <el-input
          size="small"
          style="width: 180px;margin-right: 6px;"
          v-model="noticeQueryDto.receiverId"
          placeholder="接收者ID"
          clearable
          @clear="handleFilterClear"
        ></el-input>
        
        <el-input
          size="small"
          style="width: 180px;margin-right: 6px;"
          v-model="noticeQueryDto.senderId"
          placeholder="发送者ID"
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
          ><i class="el-icon-plus"></i>新增通知</el-button>
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
          width="50"
        >
        </el-table-column>
        <el-table-column
          prop="noticeId"
          width="100"
          label="通知ID"
          :formatter="formatNoticeId"
        ></el-table-column>
        <el-table-column
          prop="noticeType"
          width="120"
          label="消息类型"
        >
          <template slot-scope="scope">
            <el-tag :type="getNoticeTypeTag(scope.row.noticeType)">
              {{ getNoticeTypeName(scope.row.noticeType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="noticeContent"
          label="消息内容"
          width="120"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="senderName"
          width="120"
          label="发送者"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ scope.row.senderName || '系统' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="receiverName"
          width="120"
          label="接收者"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="read"
          width="100"
          label="是否已读"
        >
          <template slot-scope="scope">
            <el-tag :type="scope.row.read ? 'success' : 'info'">
              {{ scope.row.read ? '已读' : '未读' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="contentId"
          width="120"
          label="关联信息ID"
          show-overflow-tooltip
        ></el-table-column>
        <!-- 操作列 -->
        <el-table-column
          label="操作"
          width="80"
          fixed="right"
        >
          <template slot-scope="scope">
            <!-- <span
              class="text-button"
              @click="handleEdit(scope.row)"
            >编辑</span> -->
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
    
    <!-- 弹窗（新增/修改通知） -->
    <el-dialog
      :show-close="false"
      :visible.sync="dialogNoticeOperation"
      width="50%"
    >
      <div slot="title">
        <p class="dialog-title">{{ !isOperation ? '新增通知' : '修改通知' }}</p>
      </div>
      <div style="padding:0 20px;">
        <el-form :model="noticeData" label-width="100px">
          <el-form-item label="消息类型">
            <el-select
              v-model="noticeData.noticeType"
              filterable
              placeholder="请选择消息类型"
              style="width: 100%"
            >
              <el-option
                v-for="item in noticeTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="消息内容">
            <el-input
              type="textarea"
              v-model="noticeData.noticeContent"
              placeholder="请输入消息内容"
              :rows="4"
            ></el-input>
          </el-form-item>
          
          <el-form-item label="关联信息ID">
            <el-input
              v-model="noticeData.contentId"
              placeholder="请输入关联信息ID（可选）"
            ></el-input>
          </el-form-item>
          
          <el-form-item label="接收者">
            <div style="display: flex; align-items: center; margin-bottom: 10px;">
              <el-checkbox v-model="selectAllUsers" @change="handleSelectAllUsers">全体用户</el-checkbox>
            </div>
            <el-select
              v-model="selectedReceivers"
              multiple
              filterable
              remote
              reserve-keyword
              placeholder="请输入用户名搜索"
              :remote-method="remoteSearchUsers"
              :loading="loading"
              style="width: 100%"
              :disabled="selectAllUsers"
            >
              <el-option
                v-for="item in userOptions"
                :key="item.userId"
                :label="item.userName"
                :value="item.userId"
              >
                <span>{{ item.userName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">ID: {{ item.userId }}</span>
              </el-option>
            </el-select>
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
import { getToken } from "@/utils/storage"
const API = {
  QUERY_NOTICE_LIST: '/notice/query',
  INSERT_NOTICES: '/notice/insertNotices',
  UPDATE_NOTICE: '/notice/update',
  DELETE_NOTICES: '/notice/deleteNotices',
  QUERY_USERS: '/user/queryUsers'
}

export default {
  data() {
    return {
      noticeData: {}, // 当前操作的通知数据
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      dialogNoticeOperation: false, // 开关
      isOperation: false, // 开关-标识新增或修改
      tableData: [],
      selectedRows: [],
      noticeQueryDto: {}, // 搜索条件
      noticeTypeOptions: [
        { value: 1, label: '健康指标异常提示' },
        { value: 2, label: '活动提示' },
        { value: 3, label: '版本更新提示' }
      ],
      userOptions: [], // 用户选项
      selectedReceivers: [], // 选中的接收者
      selectAllUsers: false, // 是否选择全体用户
      loading: false, // 用户搜索加载状态
      senderId: null
    };
  },
  // 初始化时获取通知数据
  created() {
    this.fetchFreshData();
    this.getSenderID();
  },
  methods: {
    // 由Token获取当前用户（发送者ID）
    async getSenderID() {
      try {
        const token = { token: getToken() };
        const res = await this.$axios.post('user/auth', token, {withCredentials: true});
        // 错误处理
        if (res.code === 400) {
          this.$message.error(res.data.msg);
          this.$router.push('/login');
          return;
        }
        // 获取发送者ID
        this.senderId = res.data.data.userId;
      } catch (error) {
        console.error('获取用户认证信息时发生错误:', error);
        this.$message.error('认证信息加载失败,请重试！');
      }
    },
    // 获取通知数据
    async fetchFreshData() {
      try {
        this.tableData = [];
        // 请求参数
        const params = {
          current: this.pageSize * (this.currentPage - 1),
          size: this.pageSize,
          ...this.noticeQueryDto
        };
        const response = await this.$axios.post(API.QUERY_NOTICE_LIST, params, {withCredentials: true});
        const { data } = response;
        this.tableData = data.data;
        this.totalItems = data.total;
      } catch (error) {
        console.error('查询通知信息异常:', error);
      }
    },
    // 将 noticeId 格式化为 8 位，不足部分补零
    formatNoticeId(row, column, cellValue) {
      return String(cellValue).padStart(8, '0');
    },
    // 获取消息类型名称
    getNoticeTypeName(type) {
      const option = this.noticeTypeOptions.find(item => item.value === type);
      return option ? option.label : '未知类型';
    },
    // 获取消息类型对应的标签类型
    getNoticeTypeTag(type) {
      switch (type) {
        case 1: return 'danger';  // 健康指标异常提示
        case 2: return 'success'; // 活动提示
        case 3: return 'info';    // 版本更新提示
        default: return '';
      }
    },
    // 远程搜索用户
    async remoteSearchUsers(query) {
      if (query !== '') {
        this.loading = true;
        try {
          const params = {
            userName: query,
            size: 20
          };
          const response = await this.$axios.post(API.QUERY_USERS, params, {withCredentials: true});
          this.loading = false;
          if (response.data.code === 200) {
            this.userOptions = response.data.data;
          }
        } catch (error) {
          this.loading = false;
          console.error('搜索用户异常:', error);
        }
      } else {
        this.userOptions = [];
      }
    },
    
    // 处理全选用户
    handleSelectAllUsers(val) {
      if (val) {
        this.selectedReceivers = [];
      }
    },
    
    // 多选框选中
    handleSelectionChange(selection) {
      this.selectedRows = selection;
    },
    
    // 批量删除通知
    async batchDelete() {
      if (!this.selectedRows.length) {
        this.$message(`未选中任何数据`);
        return;
      }
      const confirmed = await this.$swalConfirm({
        title: '删除通知',
        text: `删除后不可恢复，是否继续？`,
        icon: 'warning',
      });
      if (confirmed) {
        try {
          let ids = this.selectedRows.map(entity => entity.noticeId);
          const response = await this.$axios.post(API.DELETE_NOTICES, ids, {withCredentials: true});
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
          console.error(`通知删除异常：`, e);
        }
      }
    },

    // 修改通知
    async updateOperation() {
      try {
        // 处理接收者
        if (this.selectAllUsers) {
          this.noticeData.receiverId = 'ALL';
        } else {
          this.noticeData.receiverIds = this.selectedReceivers;
        }
        
        const response = await this.$axios.post(API.UPDATE_NOTICE, this.noticeData, {withCredentials: true});
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
    
    // 新增通知
    async addOperation() {
      try {
        // 接收者
        if (this.selectAllUsers) {
          this.noticeData.all = true;
        } else {
          this.noticeData.receiverIds = this.selectedReceivers;
        }
        // 发送者
        this.noticeData.senderId = this.senderId;
        const response = await this.$axios.post(API.INSERT_NOTICES, this.noticeData, {withCredentials: true});
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
    cannel() {
      this.dialogNoticeOperation = false;

      // 使用 setTimeout 延迟清除数据，否则会在弹窗消失前刷新表单
      setTimeout(() => {
        this.clearForm();
      }, 200); 
    },
    // 清除弹窗表单数据
    clearForm() {
      this.noticeData = {
        noticeType: '',
        noticeContent: '',
        contentId: null
      };
      this.selectedReceivers = [];
      this.selectAllUsers = false;
      this.userOptions = [];
    },
    // 弹出添加通知窗口
    add() {
      this.clearForm();
      this.isOperation = false;
      this.dialogNoticeOperation = true;
    },
    // 查询重置页数和数据
    handleFilter() {
      this.currentPage = 1;
      this.fetchFreshData();
    },
    // 清除查询框，并重新查询所有数据
    handleFilterClear() {
      this.noticeQueryDto = {};
      this.handleFilter();
    },
    // 修改每页的size，并将页数与数据重置
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
      this.fetchFreshData();
    },
    // 切换到指定页码
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchFreshData();
    },
    // 编辑点击事件
    handleEdit(row) {
      // 弹窗标题设置为'修改通知'
      this.isOperation = true;
      // 弹窗
      this.dialogNoticeOperation = true;
      
      // 显示当前行数据
      this.noticeData = { ...row };
      
      // 处理接收者显示
      if (row.receiverId === 'ALL') {
        this.selectAllUsers = true;
        this.selectedReceivers = [];
      } else {
        this.selectAllUsers = false;
        this.selectedReceivers = [row.receiverId];
        // 添加当前接收者到选项中
        if (row.receiverId && row.receiverName) {
          this.userOptions = [{
            userId: row.receiverId,
            userName: row.receiverName
          }];
        }
      }
    },
    
    // 删除单个通知
    async handleDelete(row) {
      const confirmed = await this.$swalConfirm({
        title: '删除通知',
        text: `删除后不可恢复，是否继续？`,
        icon: 'warning',
      });
      if (confirmed) {
        try {
          let ids = [row.noticeId];
          const response = await this.$axios.post(API.DELETE_NOTICES, ids, {withCredentials: true});
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
          console.error(`通知删除异常：`, e);
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
</style>
