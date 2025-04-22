<template>
  <div class="nav">
    <div style="display: flex;align-items: center;">
      <AppLogo
        style="padding: 0 40px; margin: 10px 0; font-size: 19px;"
        sysName="员工健康管理系统"
      />
    </div>
    <!-- 路由children列表显示 -->
    <div
      :style="{color: selectedIndex === index ? '#1c1c1c' : 'rgb(102 102 102)'}"
      class="menu-item"
      v-for="(item, index) in menus"
      :key="index"
      @click="menuClick(`${item.path}`, index)"
    >
      <span>
        <i :class="item.icon"></i>
        <span>&nbsp; {{ item.name }}</span>
      </span>
    </div>

    <div class="search">
      <input
        placeholder="请输入资讯标题"
        @keyup.enter="search"
        v-model="filterText"
      />
      <span @click="search">搜索资讯</span>
    </div>
    <div>
      <el-dropdown class="user-dropdown">
        <span
          class="el-dropdown-link"
          style="display: flex; align-items: center;"
        >
          <el-avatar
            :size="35"
            :src="require('@/assets/img/user.png')"
            style="margin-top: 0; background-color: #e9e9e9;"
          ></el-avatar>
          <span
            class="userName"
            style="margin-left: 5px;font-size: 15px;"
          >{{ userInfo.name }}</span>
          <i
            class="el-icon-arrow-down el-icon--right"
            style="margin-left: 5px;"
          ></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item
            icon="el-icon-user"
            @click.native="userCenterPanel"
          >个人中心</el-dropdown-item>
          <el-dropdown-item
            icon="el-icon-warning-outline"
            @click.native="resetPwd"
          >修改密码</el-dropdown-item>
          <el-dropdown-item
            icon="el-icon-back"
            @click.native="logout"
          >退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>
<script>
import { clearToken } from "@/utils/storage.js";
import AppLogo from '@/components/Logo.vue';
export default {
  name: "UserMenu",
  components: {
    AppLogo
  },
  data () {
    return {
      selectedIndex: 0,
      loginPath: '/login',
      sysName: '员工健康管理系统',
      defaultPath: '/article',
      filterText: '',
      noReadMsg: 0,
    }
  },
  props: {
    // 路由菜单数据
    menus: {
      type: Array,
      required: true
    },
    // 用户信息数据
    userInfo: {
      type: Object,
      required: true
    }
  },
  mounted () {
    this.pathToDo(this.defaultPath);
    this.loadUserData();
  },
  methods: {
    // 搜索关键词，返回父组件处理
    search () {
      // 存储关键词到 sessionStorage
      sessionStorage.setItem('keyWord', this.filterText);
      // 如果已经在搜索页面
      if (this.$route.path === '/search-detail') {
        // 如果当前路由视图是搜索页面，并且具有 refreshSearch 方法
        if (this.$parent && this.$parent.$refs.currentView &&
          typeof this.$parent.$refs.currentView.refreshSearch === 'function') {
          // 直接调用搜索页面的刷新方法
          this.$parent.$refs.currentView.refreshSearch(this.filterText);
        }
        // 清空搜索栏
        this.filterText = '';
        return;
      }
      // 不在搜索页面时，通知父组件进行路由跳转
      this.$emit('eventListener', 'search-detail');
      // 清空搜索栏
      this.filterText = '';
    },

    // 个人中心，传回父组件处理
    userCenterPanel () {
      this.$emit('eventListener', 'center');
    },
    // 重置密码，传回父组件处理
    resetPwd () {
      this.$emit('eventListener', 'resetPwd');
    },
    // 退出登录，传回父组件处理
    logout () {
      this.$emit('eventListener', 'logout');
    },
    // 加载用户数据
    async loadUserData () {
      const userInfo = sessionStorage.getItem('userInfo');
    },
    // 不是存量路由，则跳转
    pathToDo (path) {
      if (this.$route.path !== path) {
        this.$router.push(path);
      }
    },
    //路由跳转
    menuClick (path, index) {
      this.selectedIndex = index;
      this.pathToDo(path);
    },
    // 退出登录
    async out () {
      const confirmed = await this.$swalConfirm({
        title: '是否退出登录',
        text: `退出后将重新登录，才能使用系统功能`,
        icon: 'warning',
      });
      if (confirmed) {
        this.$swal.fire({
          title: '退出登录',
          text: '您已成功退出登录。',
          icon: 'success', // 使用'success'图标表示操作成功
          showConfirmButton: false, // 隐藏确认按钮，使得弹窗只展示信息后自动关闭
          timer: 1300, // 自动关闭弹窗的延迟时间，这里是2秒
        });
        setTimeout(() => {
          clearToken();
          this.$router.push('/loginPath');
        }, 1300)
      } else {
        console.log('用户取消了退出操作');
      }
    },

  }
}
</script>
<style scoped lang="scss">
.nav {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  height: 80px;

  .menu-item {
    font-size: 14px;
    cursor: pointer;
  }

  .search {
    border-radius: 30px; // 更大的圆角更现代
    font-size: 14px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fff;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08); // 添加轻微阴影
    transition: all 0.3s ease; // 添加过渡动画
    overflow: hidden; // 确保子元素圆角生效
    border: 1px solid #e0e0e0; // 统一边框

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12); // 悬停时阴影加深
    }

    input {
      flex: 1;
      outline: none;
      border: none;
      padding: 10px 16px; // 更大的内边距
      font-size: 14px; // 稍大的字体
      color: #333;
      background: transparent;
      transition: all 0.3s ease;

      &::placeholder {
        color: #999;
        font-weight: 300;
      }

      &:focus {
        color: #222;
        &::placeholder {
          opacity: 0.5;
        }
      }
    }

    span {
      background-color: #289667; // 使用更柔和的绿色
      color: white;
      border: none;
      padding: 10px 20px; // 更大的点击区域
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
      display: flex;
      align-items: center;
      gap: 6px; // 图标和文字的间距

      &:hover {
        background-color: darken(#289667, 8%); // 悬停时颜色加深
      }

      .search-icon {
        font-size: 16px; // 稍大的图标
      }
    }
  }
}
</style>