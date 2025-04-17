import Vue from 'vue'
import App from './App.vue'
import router from "./router";
import 'element-ui/lib/theme-chalk/index.css';
import VueSweetalert2 from 'vue-sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';
import './assets/css/editor.scss'
import './assets/css/button.scss'
import './assets/css/elementui-cover.scss'
import './assets/css/basic.scss'
import './assets/css/dialog.scss'
import './assets/css/input.scss'
import request from '@/utils/request'
import md5 from 'js-md5';

import axios from "axios";
// 全局配置
axios.defaults.withCredentials = true; // 允许跨域携带 Cookie
axios.defaults.baseURL = "http://localhost:21090"; // 后端 API 地址
Vue.prototype.$axios = axios; // 挂载到 Vue 实例（可选）

/**
 * 关闭关闭Vue 的生产环境提示。
 */
Vue.config.productionTip = false
/**
 * 弹窗组件引用
 */
Vue.use(VueSweetalert2);
Vue.prototype.$md5 = md5;
Vue.prototype.$axios = request;
import swalPlugin from '@/utils/swalPlugin';
Vue.use(swalPlugin);

/**
 * 创建一个Vue实例并挂载到DOM中
 * 将router实例挂载到Vue根实例
 */
new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
