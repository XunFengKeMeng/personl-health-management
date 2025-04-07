<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="menu-container">
    <!-- 左边侧边栏 -->
    <div
      class="menu-side"
      :class="{ 'menu-side-narrow': flag }"
    >
      <div style="display: flex;align-items: center;">
        <AppLogo
          style="padding: 0 40px; margin: 10px 0; font-size: 19px;"
          sysName="员工健康管理系统"
          :flag="flag"
        />
      </div>
      <div class="route-list">
        <AdminMenu
          :flag="flag"
          :routes="adminRoutes"
          @select="handleRouteSelect"
        />
      </div>
    </div>
    <!-- 右边主题内容区 -->
    <div class="main">
      <!-- 顶部 -->
      <div class="header-section">
        <LevelHeader
          @eventListener="eventListener"
          @selectOperation="selectOperation"
          :tag="tag"
          :userInfo="userInfo"
        />
      </div>
      <!-- 内容区 -->
      <div class="content-section">
        <router-view></router-view>
      </div>
    </div>
    <!-- 个人中心 -->
    <el-dialog
      :show-close="false"
      :visible.sync="dialogOperaion"
      width="26%"
    >
      <div
        slot="title"
        style="padding: 25px 0 0 20px;"
      >
        <span style="font-size: 18px;font-weight: 800;">个人中心</span>
      </div>
      <el-row style="padding: 10px 20px 20px 20px;">
        <el-row>
          <p style="font-size: 12px;padding: 3px 0;">
            <span class="modelName">*账号</span>
          </p>
          <input
            class="input-title"
            v-model="userInfo.account"
            placeholder="账号"
          >
        </el-row>
        <el-row>
          <p style="font-size: 12px;padding: 3px 0;">
            <span class="modelName">*用户名</span>
          </p>
          <input
            class="input-title"
            v-model="userInfo.name"
            placeholder="用户名"
          >
        </el-row>
        <el-row>
          <p style="font-size: 12px;padding: 3px 0;">
            <span class="modelName">*邮箱</span>
          </p>
          <input
            class="input-title"
            v-model="userInfo.email"
            placeholder="邮箱"
          >
        </el-row>
      </el-row>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          class="customer"
          size="small"
          style="background-color: rgb(241, 241, 241);border: none;"
          @click="dialogOperaion = false"
        >取 消</el-button>
        <el-button
          size="small"
          style="background-color: #15559a;border: none;"
          class="customer"
          type="info"
          @click="updateUserInfo"
        >修改</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import router from "@/router/index";
import { clearToken, getToken } from "@/utils/storage"
import AdminMenu from '@/components/VerticalMenu.vue';
import AppLogo from '@/components/Logo.vue';
import LevelHeader from '@/components/LevelHeader.vue';
export default {
  name: "AppAdmin",
  components: {
    AppLogo,
    LevelHeader,
    AdminMenu
  },
  data () {
    return {
      adminRoutes: [], // 菜单路由配置
      activeIndex: '', // 当前选中的菜单项
      userInfo: { // 用户信息
        id: null,
        url: '@/assets/img/user.png',
        name: '',
        role: null,
        email: '',
        account: ''
      },
      flag: false, // 控制侧边栏是否收起
      tag: '可视化', // 当前页面标题
      dialogOperaion: false // 控制个人中心弹窗显示
    };
  },
  // 在组件创建时进行操作
  created () {
    let menus = router.options.routes.filter(route => route.path == '/admin')[0];
    // 初始化菜单路由
    this.adminRoutes = menus.children;
    // 检查用户认证信息
    this.tokenCheckLoad();
    // 加载侧边栏状态（刷新以后，侧边栏不会默认展开）
    this.menuOperationHistory();
  },
  methods: {
    // 退出登录
    async logout () {
      const confirmed = await this.$swalConfirm({
        title: '退出登录？',
        text: `退出后需重新登录？`,
        icon: 'warning',
      });
      if (confirmed) {
        this.$swal.fire({
          title: '退出登录成功',
          text: '1s 后返回登录页面',
          icon: 'success',
          showConfirmButton: false,
          timer: 1000,
        });
        setTimeout(() => {
          // 清除本地存储的token
          clearToken();
          // 返回登陆页面
          this.$router.push("/login");
        }, 1000)
      }
    },
    // 退出选项监听
    eventListener (event) {
      // 个人资料取消事件
      if (event === 'center') {
        this.dialogOperaion = true;
      }
      // 退出登录事件
      if (event === 'logout') {
        this.logout();
      }
    },
    // 加载侧边栏上次的状态
    menuOperationHistory () {
      this.flag = sessionStorage.getItem('flag') === 'true';
    },
    // 收起侧边栏
    selectOperation (flag) {
      this.flag = flag;
    },
    // Token检验
    async tokenCheckLoad () {
      try {
        const token = { token: getToken() };
        const res = await request.post('user/auth', token);
        // 错误处理
        if (res.code === 400) {
          this.$message.error(res.data.msg);
          this.$router.push('/login');
          return;
        }
        // 用户信息赋值
        const { userId: id, userName: name, userRole: role, userEmail: email, userAccount: account } = res.data.data;
        this.userInfo = { id, name, role, email, account };
        // 根据角色解析路由
        const rolePath = role === 1 ? '/admin' : '/user';
        const targetMenu = router.options.routes.find(route => route.path === rolePath);
        if (targetMenu) {
          this.routers = targetMenu.children;
        } else {
          console.warn(`未找到与角色对应的路由：${rolePath}`);
        }
      } catch (error) {
        console.error('获取用户认证信息时发生错误:', error);
        this.$message.error('认证信息加载失败,请重试！');
      }
    },
    // 修改用户信息
    async updateUserInfo () {
      try {
        const userUpdateDTO = {
          userId: this.userInfo.id,
          userAccount: this.userInfo.account,
          userName: this.userInfo.name,
          userEmail: this.userInfo.email
        }
        const resposne = await this.$axios.post(`/user/updateSelf`, userUpdateDTO);
        const { data } = resposne;
        if (data.code === 200) {
          this.dialogOperaion = false;
          this.tokenCheckLoad();
          this.$swal.fire({
            title: '修改个人信息',
            text: data.msg,
            icon: 'success',
            showConfirmButton: false,
            timer: 1000,
          });
        }
      } catch (e) {
        this.dialogOperaion = false;
        this.$swal.fire({
          title: '修改个人信息异常',
          text: e,
          icon: 'error',
          showConfirmButton: false,
          timer: 2000,
        });
        console.error(`修改个人信息异常:${e}`);
      }
    },
    // 处理菜单栏选项的路由跳转
    handleRouteSelect (index) {
      // 根据选中的菜单项更新页面标题
      let ary = this.adminRoutes.filter(entity => entity.path == index);
      this.tag = ary[0].name;
      // 避免重复跳转
      if (this.$router.currentRoute.fullPath == index) {
        return;
      }
      // 路由跳转
      this.$router.push(index);
    }
  }
}
</script>

<style scoped lang="scss">
.menu-container {
  display: flex;
  height: 100vh;
  width: 100%;

  .menu-side {
    width: 253px;
    min-width: 115px;
    height: 100vh;
    padding-top: 10px;
    box-sizing: border-box;
    transition: width 0.3s ease;
    background-color: rgb(248, 248, 248);
    border-right: 1px solid rgb(240, 240, 240);

    .route-list {
      margin-top: 12px;
    }
  }

  .menu-side-narrow {
    width: 115px;
  }

  .main {
    flex-grow: 1;
    overflow-x: hidden;

    .header-section {
      max-width: 100%;
      padding: 0 15px;
    }

    .content-section {
      flex-grow: 1;
      padding: 5px;
      overflow-y: auto;
    }
  }
}
</style>