<template>
  <div class="login-body">
    <!-- 顶部区域 -->
    <div class="header">
      <AppLogo sysName="员工健康管理系统" />
    </div>
    <div class="login-container">
      <!-- 右侧图片 -->
      <div class="login-right-image">
        <img
          src="@/assets/img/login-right-image.png"
          alt="登录图片"
        />
      </div>
      <!-- 登录面板 -->
      <div class="login-panel">
        <div class="welcome-text">
          登&emsp;录
        </div>
        <div class="text">
          <input
            v-model="act"
            class="act"
            placeholder="账号"
          />
        </div>
        <div class="text">
          <input
            v-model="pwd"
            class="pwd"
            type="password"
            placeholder="密码"
          />
        </div>
        <div>
          <span
            class="login-btn"
            @click="login"
          >登&emsp;录</span>
        </div>
        <div class="tip">
          <p>没有账号？<span
              class="act-tip"
              @click="toDoRegister"
            >点此注册</span></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
const ADMIN_ROLE = 1;
const USER_ROLE = 0;
const TIP_DELAY_TIME = 5000;
const DELAY_TIME = 1300;
import AppLogo from '@/components/Logo.vue';
import request from "@/utils/request.js";
import md5 from 'js-md5';
import { setToken } from "@/utils/storage.js";
export default {
  name: "AppLogin",
  components: { AppLogo },
  data () {
    return {
      act: '',
      pwd: '',
    }
  },
  methods: {
    // 跳转注册页面
    toDoRegister () {
      this.$router.push('/register');
    },
    // 登录请求
    async login () {
      // 校验账号密码是否为空
      if (!this.act || !this.pwd) {
        this.$swal.fire({
          title: '提示',
          text: '账号或密码不能为空',
          icon: 'error',
          showConfirmButton: true, // 显示确认按钮
          confirmButtonText: '我知道了', // 按钮文本
          confirmButtonColor: '#3085d6', // 按钮颜色
          customClass: {
            popup: 'custom-swal-popup', // 弹窗样式
            title: 'custom-swal-title', // 标题样式
            content: 'custom-swal-content', // 内容样式
            confirmButton: 'custom-swal-confirm-button', // 按钮样式
          },
          backdrop: 'rgba(0,0,0,0.5)', // 背景遮罩颜色
          timer: TIP_DELAY_TIME
        });
        return;
      }
      const hashedPwd = md5(md5(this.pwd)); // MD5加密密码
      const paramDTO = { userAccount: this.act, userPassword: hashedPwd };
      // 向后端发送请求
      try {
        const { data } = await request.post(`user/login`, paramDTO, {withCredentials: true});
        if (data.code !== 200) {
          this.$swal.fire({
            title: '登录失败',
            text: data.msg,
            icon: 'error',
            showConfirmButton: false,
            timer: DELAY_TIME,
          });
          return;
        }
        // 接收后端返回的token，设置token
        setToken(data.data.token);
        // 根据角色延迟跳转页面
        setTimeout(() => {
          const role = data.data.role;
          this.navigateToRole(role);
        }, DELAY_TIME);
      } catch (error) {
        console.error('登录请求错误:', error);
        this.$message.error('登录请求出错，请重试！');
      }
    },
    // 根据用户类型跳转到对应页面
    navigateToRole (role) {
      switch (role) {
        case ADMIN_ROLE:
          this.$router.push('/admin');
          break;
        case USER_ROLE:
          this.$router.push('/user');
          break;
        default:
          console.warn('未知的角色类型:', role);
          break;
      }
    }
  }
}
</script>

<style lang="scss" scoped>
* {
  user-select: none;
}
.login-body {
  height: 100vh;
  margin: 0;
  padding: 0;
  overflow: hidden; // 隐藏滚动条
}
.header {
  width: 100%;
  height: 70px;
  background-color: rgb(255, 255, 255);
  display: flex;
  justify-content: flex-start; // 靠左对齐
  align-items: center; // 垂直居中
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); // 添加阴影效果
  padding-left: 10%;
}

.login-container {
  width: 100%;
  min-height: calc(100vh - 70px); // 减去 header 的高度
  background-color: rgb(27, 167, 132);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: row;
  box-sizing: border-box; // 确保 padding 包含在宽度内

  .login-right-image {
    width: 400px;
    max-width: 50%;
    height: auto;
    overflow: hidden; // 隐藏超出部分
    margin-left: 18%;
    img {
      width: 100%;
      height: 100%;
      object-fit: cover; // 图片填充方式
    }
  }

  .login-panel {
    width: 263px;
    height: auto;
    padding: 40px 30px 16px 30px;
    border-radius: 10px;
    margin-right: 10%;
    background-color: rgb(255, 255, 255);
    box-shadow: 0 4px 6px rgba(36, 36, 36, 0.1),
      0 1px 3px rgba(40, 40, 40, 0.06);

    .welcome-text {
      text-align: center;
      font-size: 20px;
      font-weight: bold;
      color: #333;
      margin-bottom: 30px;
      position: relative;

      &::after {
        content: "";
        position: absolute;
        bottom: -10px;
        left: 50%;
        transform: translateX(-50%);
        width: 80%;
        height: 4px;
        background: linear-gradient(90deg, #edfae7, #1ba784); // 渐变下划线
        border-radius: 2px;
      }
    }

    .act,
    .pwd {
      margin: 8px 0;
      height: 45px;
      line-height: 53px;
      width: 100%;
      background-color: rgb(248, 248, 248);
      box-sizing: border-box;
      border: 1px solid rgb(248, 248, 248);
      border-radius: 5px;
      font-weight: 500;
      font-size: 15px;
      padding: 0 15px;
      margin-top: 13px;
    }

    .act:focus,
    .pwd:focus {
      outline: none;
      background-color: rgb(242, 243, 244);
      transition: 0.5s;
    }

    .role {
      display: inline-block;
      color: rgb(30, 102, 147);
      font-size: 14px;
      padding-right: 10px;
    }
  }

  .login-btn {
    display: inline-block;
    text-align: center;
    border-radius: 3px;
    margin-top: 20px;
    height: 38px;
    line-height: 38px;
    width: 100%;
    background-color: rgb(27, 167, 132);
    font-size: 14px !important;
    border: none;
    color: rgb(250, 250, 250);
    padding: 0 !important;
    cursor: pointer;
    user-select: none;
  }

  .tip {
    margin: 12px 0;

    p {
      padding: 3px 0;
      margin: 0;
      font-size: 14px;
      color: #a0a0a0;

      i {
        margin-right: 3px;
      }
      .act-tip {
        color: #2d76d5;
        cursor: pointer;
        border-radius: 2px;
        margin: 0 6px;
      }
    }
  }

  /* 弹窗样式 */
  .custom-swal-popup {
    border-radius: 10px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  /* 标题样式 */
  .custom-swal-title {
    font-size: 20px;
    font-weight: bold;
    color: #333;
  }

  /* 内容样式 */
  .custom-swal-content {
    font-size: 16px;
    color: #666;
  }

  /* 按钮样式 */
  .custom-swal-confirm-button {
    font-size: 14px;
    font-weight: bold;
    padding: 8px 20px;
    border-radius: 5px;
    background-color: #3085d6;
    transition: background-color 0.3s ease;
  }
  .custom-swal-confirm-button:hover {
    background-color: #1c6bb8;
  }
}
</style>