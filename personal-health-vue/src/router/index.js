/**
 * 引入vue和vue-router插件
 */
import Vue from "vue";
import VueRouter from "vue-router";
/**
 * `element-ui/lib/theme-chalk/index.css`包含了所有组件样式
 */
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
/**
 * 引入登录认证
 */
import { getToken } from "@/utils/storage.js";
/**
 * 引入ECharts，并将ECharts挂载到Vue实例中
 */
import echarts from 'echarts';
Vue.prototype.$echarts = echarts;

/**
 * Vue.use()全局注册插件
 */
Vue.use(ElementUI);
Vue.use(VueRouter);

/**
 * 路由规则配置数组
 */
const routes = [
  /**
   * 通配符路由，将所有匹配到的路径重定向到redirect标识的路径
   */
  {
    path: "*",
    redirect: "/login"
  },
  /**
   * path：路由路径
   * component：对应的组件
   */
  {
    path: "/login",
    component: () => import(`@/views/login/Login.vue`)
  },
  {
    path: "/register",
    component: () => import(`@/views/register/Register.vue`)
  },
  {
    path: "/admin",
    component: () => import(`@/views/admin/Home.vue`),
    meta: {
      requireAuth: true // 需要登录验证
    }
  },
  {
    path: "/user",
    component: () => import(`@/views/user/Main.vue`),
    meta: {
      requireAuth: true // 需要登录验证
    }
  }
]

/**
 * 创建一个Vue Router实例，并配置路由规则与路由模式。
 * history模式：URL中没有#，但需要服务器支持，否则在刷新页面时会出现404错误。
 */
const router = new VueRouter({
  routes,
  mode: 'history'
});

/**
 * 设置Vue Router 的全局前置守卫，用于在路由跳转前进行权限验证
 */
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    const token = getToken();
    if (token !== null) {
      next();
    } else {
      next("/login");
    }
  }
  else {
    next();
  }
});

/**
 * 导出router实例
 * 导出后，可以在main.js中导入并使用
 */
export default router;