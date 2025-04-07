<template>
  <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
    <!-- 顶部查询/操作区 -->
    <el-row style="padding: 10px;margin-left: 10px;">
      <el-row>
        <!-- 顶部查询模块 -->
        <el-input
          size="small"
          style="width: 238px;margin-left: 5px;margin-right: 6px;"
          v-model="userQueryDto.userName"
          placeholder="用户名"
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
          ><i class="el-icon-plus"></i>新增用户</el-button>
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
          prop="userId"
          width="100"
          label="用户ID"
          :formatter="formatUserId"
        ></el-table-column>
        <el-table-column
          prop="userName"
          width="90"
          label="名称"
        ></el-table-column>
        <el-table-column
          prop="userAccount"
          width="128"
          label="账号"
        ></el-table-column>
        <el-table-column
          prop="userEmail"
          width="218"
          label="邮箱"
        ></el-table-column>
        <el-table-column
          prop="userRole"
          width="98"
          label="角色"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.userRole === 1 ? '管理员' : '用户' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="userActive"
          width="108"
          label="账号状态"
        >
          <template slot-scope="scope">
            <span :style="{ color: scope.row.active ? 'green' : 'red' }">
              {{ scope.row.active ? '已激活' : '待激活' }}
            </span>
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
              @click="handleStatus(scope.row)"
            >账号管理</span>
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
    <!-- 弹窗（新增用户/修改用户信息） -->
    <el-dialog
      :show-close="false"
      :visible.sync="dialogUserOperaion"
      width="25%"
    >
      <div slot="title">
        <p class="dialog-title">{{ !isOperation ? '新增用户' : '修改用户信息' }}</p>
      </div>
      <div style="padding:0 20px;">
        <el-row>
          <span class="dialog-hover">用户名</span>
          <input
            class="dialog-input"
            v-model="data.userName"
            placeholder="用户名"
          />
          <span class="dialog-hover">账号</span>
          <input
            class="dialog-input"
            v-model="data.userAccount"
            placeholder="账号"
          />
          <span class="dialog-hover">邮箱</span>
          <input
            class="dialog-input"
            v-model="data.userEmail"
            placeholder="邮箱"
          />
          <span class="dialog-hover">密码</span>
          <input
            class="dialog-input"
            v-model="userPwd"
            placeholder="密码"
          />
        </el-row>
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
    <!-- 账号管理窗口 -->
    <el-dialog
      :show-close="false"
      :visible.sync="dialogStatusOperation"
      width="25%"
    >
      <div slot="title">
        <p class="dialog-title">账号管理</p>
      </div>
      <div style="padding:0 20px;">
        <span class="dialog-hover">设为管理员</span>
        <el-switch
          v-model="roleStatus"
          active-color="rgb(230, 62, 49)"
          inactive-color="rgb(246,246,246)"
        >
        </el-switch>
      </div>
      <div style="padding:0 20px;">
        <span class="dialog-hover">账号激活</span>
        <el-switch
          v-model="activeStatus"
          active-color="rgb(230, 62, 49)"
          inactive-color="rgb(246,246,246)"
        >
        </el-switch>
      </div>
      
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          size="small"
          style="background-color: #0088d2;color: white;border: none;"
          class="customer"
          type="info"
          @click="comfirmStatus"
        >确认</el-button>
        <el-button
          class="customer"
          size="small"
          style="background-color: #0088d2; color: white; border: none;"
          @click="cannel"
        >取消</el-button>
      </span>
    </el-dialog>
  </el-row>
</template>

<script>
const API = {
  UPDATE_USER: '/user/updateByAdmin',
  DELETE_USERLIST: '/user/deleteUsers',
  INSERT_USER: '/user/insert',
  QUERY_USERLIST: '/user/queryUsers'
}
export default {
  data () {
    return {
      roleStatus: false, // 当前行用户角色（账号管理窗口）
      userPwd: '',
      activeStatus: false, // 当前行账号激活状态（账号管理窗口）
      data: {}, // 当前行数据
      filterText: '',
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      dialogStatusOperation: false,
      dialogUserOperaion: false, // 开关
      isOperation: false, // 开关-标识新增或修改
      tableData: [],
      searchTime: [],
      selectedRows: [],
      status: null,
      userQueryDto: {}, // 搜索条件
      messsageContent: ''
    };
  },
  // 初始化时获取用户数据
  created () {
    this.fetchFreshData();
  },
  methods: {
    // 获取用户数据
    async fetchFreshData () {
      try {
        this.tableData = [];
        // 请求参数
        const params = {
          current: this.pageSize * (this.currentPage - 1),
          size: this.pageSize,
          // ...是扩展运算符，其作用是将this.userQueryDto对象中的所有属性展开并合并到params对象中
          ...this.userQueryDto
        };
        const response = await this.$axios.post(API.QUERY_USERLIST, params);
        const { data } = response;
        this.tableData = data.data;
        this.totalItems = data.total;
      } catch (error) {
        console.error('查询用户信息异常:', error);
      }
    },
    // 将 userId 格式化为 8 位，不足部分补零
    formatUserId (row, column, cellValue) {
      return String(cellValue).padStart(8, '0');
    },
    // 账号管理修改用户信息（角色、账号激活状态）
    comfirmStatus () {
      const userUpdateDto = {
        userId: this.data.userId,
        userRole: this.roleStatus ? 1 : 0,
        active: this.activeStatus
      }
      this.$axios.post(API.UPDATE_USER, userUpdateDto).then(res => {
        if (res.data.code === 200) {
          this.$notify({
            duration: 2000,
            title: '操作反馈',
            message: '操作成功',
            type: 'success'
          });
          this.dialogStatusOperation = false;
          this.fetchFreshData();
        }
      }).catch(error => {
        console.log("修改状态异常：" + error);
      })
    },
    // 打开账号管理窗口
    handleStatus (data) {
      this.dialogStatusOperation = true;
      this.roleStatus = data.userRole === 1;
      this.activeStatus = data.active;
      this.data = data;
    },
    // 多选框选中
    handleSelectionChange (selection) {
      this.selectedRows = selection;
      console.log(this.selectedRows);
    },
    // 批量删除用户数据
    async batchDelete () {
      if (!this.selectedRows.length) {
        this.$message(`未选中任何数据`);
        return;
      }
      const confirmed = await this.$swalConfirm({
        title: '删除用户数据',
        text: `删除后不可恢复，是否继续？`,
        icon: 'warning',
      });
      if (confirmed) {
        try {
          console.log(this.selectedRows);
          let ids = this.selectedRows.map(entity => entity.userId);
          const response = await this.$axios.post(API.DELETE_USERLIST, ids);
          if (response.data.code === 200) {
            this.$notify({
              duration: 2000,
              title: '删除操作',
              message: '成功',
              type: 'success'
            });
            this.cannel();
            this.fetchFreshData();
            return;
          }
        } catch (e) {
          console.error(`用户信息删除异常：`, e);
        }
      }
    },

    // 修改信息
    async updateOperation () {
      if (this.userPwd !== '' && this.userPwd !== null) {
        console.log(this.userPwd)
        const pwd = this.$md5(this.$md5(this.userPwd));
        this.data.userPassword = pwd;
      } else {
        this.data.userPassword = null;
      }
      console.log(this.data);
      try {
        const response = await this.$axios.post(API.UPDATE_USER, this.data);
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
    // 新增用户
    async addOperation () {
      if (this.userPwd !== '' && this.userPwd !== null) {
        this.data.userPassword = this.$md5(this.$md5(this.userPwd));
      } else {
        this.data.userPassword = null;
      }
      try {
        const response = await this.$axios.post(API.INSERT_USER, this.data);
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
    // 窗口消失，并清除文本框数据
    cannel () {
      this.dialogStatusOperation = false;
      this.dialogUserOperaion = false;

      // 使用 setTimeout 延迟清除文本数据，否则会在弹窗消失前刷新文本框
      setTimeout(() => {
        this.clearText();
      }, 200);
    },
    // 清除弹窗文本数据
    clearText () {
      this.userPwd = '';
      this.data = {};
    },
    // 弹出添加用户窗口
    add () {
      this.clearText();
      this.isOperation = false;
      this.dialogUserOperaion = true;
    },
    // 查询用户名重置页数和数据
    handleFilter () {
      this.currentPage = 1;
      this.fetchFreshData();
    },
    // 清除查询框，并重新查询所有数据
    handleFilterClear () {
      this.filterText = '';
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
      // 弹窗标题设置为‘修改用户信息’
      this.isOperation = true;
      // 弹窗
      this.dialogUserOperaion = true;
      // 密码为MD5加密不可逆，因此不显示原密码，只能修改
      this.userPwd = null;
      // 显示其他密码
      this.data = { ...row }
    },
    // 删除单个用户
    async handleDelete (row) {
      const confirmed = await this.$swalConfirm({
        title: '删除用户数据',
        text: `删除后不可恢复，是否继续？`,
        icon: 'warning',
      });
      if (confirmed) {
        try {
          let ids = [row.userId];
          const response = await this.$axios.post(API.DELETE_USERLIST, ids);
          if (response.data.code === 200) {
            this.$notify({
              duration: 2000,
              title: '删除操作',
              message: '成功',
              type: 'success'
            });
            this.cannel();
            this.fetchFreshData();
            return;
          }
        } catch (e) {
          console.error(`用户信息删除异常：`, e);
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
  /* 根据需要调整间距 */
}

.text-button {
  margin-right: 10px;
  color: #409eff;
  cursor: pointer;
}
</style>