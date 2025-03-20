<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="register-body">
    <!-- 顶部区域 -->
    <div class="header">
      <AppLogo sysName="员工健康管理系统" />
    </div>
    <div class="register-container">
      <!-- 右侧图片 -->
      <div class="register-right-image">
        <img
          src="@/assets/img/register-right-image.png"
          alt="注册图片"
        />
      </div>
      <!-- 注册面板 -->
      <div class="register-panel">
        <div class="welcome-text">
          注&emsp;册
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
            v-model="name"
            class="name"
            placeholder="用户名"
          />
        </div>
        <div class="text">
          <input
            v-model="email"
            class="email"
            placeholder="邮箱"
          />
        </div>
        <div class="text">
          <input
            v-model="pwd"
            class="pwd"
            type="password"
            placeholder="输入密码"
          />
        </div>
        <div class="text">
          <input
            v-model="pwdConfirm"
            class="pwdConfirm"
            type="password"
            placeholder="再输入一次密码"
          />
        </div>
        <div>
          <span
            class="register-btn"
            @click="registerFunc"
          >注&emsp;册</span>
        </div>
        <div class="tip">
          <p>已有账号？
            <span
              class="act-tip"
              @click="toDoLogin"
            >点此登录</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
const TIP_DELAY_TIME = 5000;
const DELAY_TIME = 1300;
import AppLogo from '@/components/Logo.vue';
import request from "@/utils/request.js";
import md5 from 'js-md5';
export default {
  name: "AppLogin",
  components: { AppLogo },
  data () {
    return {
      act: '', // 账号
      pwd: '', // 密码
      pwdConfirm: '', // 确认密码
      name: '', // 用户名
      email: ''// 邮箱
    }
  },
  methods: {
    // 返回登录页面
    toDoLogin () {
      this.$router.push('/login');
    },
    async registerFunc () {
      // 判空校验
      if (!this.act || !this.pwd || !this.pwdConfirm || !this.name || !this.email) {
        this.$swal.fire({
          title: '提示',
          text: '账号、用户名、邮箱、密码、确认密码不能为空',
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
      // 密码校验
      if (this.pwd !== this.pwdConfirm) {
        this.$swal.fire({
          title: '提示',
          text: '前后密码输入不一致',
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
      const hashedPwd = md5(md5(this.pwd));
      const paramDTO = { userAccount: this.act, userPassword: hashedPwd, userName: this.name, userEmail: this.email };
      try {
        const { data } = await request.post(`user/register`, paramDTO);
        if (data.code !== 200) {
          this.$swal.fire({
            title: '注册失败',
            text: data.msg,
            icon: 'error',
            showConfirmButton: false,
            timer: DELAY_TIME,
          });
          return;
        }
        // 使用Swal通知注册成功，延迟后跳转
        this.$swal.fire({
          title: '注册成功',
          text: '即将返回登录页...',
          icon: 'success',
          showConfirmButton: false,
          timer: DELAY_TIME,
        });
        // 延迟跳转回登录界面
        setTimeout(() => {
          this.$router.push('/login');
        }, DELAY_TIME);
      } catch (error) {
        console.error('注册请求错误:', error);
      }
    }
  }
}

</script>

<style lang="scss" scoped>
* {
  user-select: none;
}
.register-body {
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
.register-container {
  width: 100%;
  min-height: calc(100vh - 70px); // 减去 header 的高度
  background-color: rgb(27, 167, 132);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: row;
  box-sizing: border-box; // 确保 padding 包含在宽度内

  .register-right-image {
    width: 500px;
    max-width: 50%;
    height: auto;
    overflow: hidden; // 隐藏超出部分
    margin-left: 13%;
    img {
      width: 100%;
      height: 100%;
      object-fit: cover; // 图片填充方式
    }
  }

  .register-panel {
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
      margin-bottom: 20px;
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
    .name,
    .email,
    .pwd,
    .pwdConfirm {
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
      margin-top: 10px;
    }

    .act:focus,
    .name:focus,
    .email:focus,
    .pwd:focus,
    .pwdConfirm:focus {
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

  .register-btn {
    display: inline-block;
    text-align: center;
    border-radius: 3px;
    margin-top: 15px;
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