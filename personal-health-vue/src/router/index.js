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
    },
    children: [
      {
        path: "/adminLayout",
        name: '仪表盘',
        icon: 'el-icon-pie-chart',
        component: () => import(`@/views/admin/AdminMain.vue`),
        meta: { requireAuth: true }
      },
      {
        path: "/userManage",
        name: '用户管理',
        icon: 'el-icon-user',
        component: () => import(`@/views/admin/UserManage.vue`),
        meta: { requireAuth: true }
      },
      {
        path: "/TagManage",
        name: '标签管理',
        icon: 'el-icon-edit',
        component: () => import(`@/views/admin/TagManage.vue`),
        meta: { requireAuth: true }
      },
      {
        path: "/ArticleManage",
        name: '资讯管理',
        icon: 'el-icon-edit-outline',
        component: () => import(`@/views/admin/ArticleManage.vue`),
        meta: { requireAuth: true }
      },
      {
        path: "/NoticeManage",
        name: '通知管理',
        icon: 'el-icon-chat-dot-square',
        component: () => import(`@/views/admin/NoticeManage.vue`),
        meta: { requireAuth: true }
      },
      {
        path: "/MetricManage",
        name: '健康指标管理',
        icon: 'el-icon-sort',
        component: () => import(`@/views/admin/MetricManage.vue`),
        meta: { requireAuth: true }
      },
      {
        path: "/FormItemManage",
        name: '表单项管理',
        icon: 'el-icon-news',
        component: () => import(`@/views/admin/FormItemManage.vue`),
        meta: { requireAuth: true }
      },
      {
        path: "/FormManage",
        name: '表单模板管理',
        icon: 'el-icon-tickets',
        component: () => import(`@/views/admin/FormManage.vue`),
        meta: { requireAuth: true }
      },
      {
        path: "/FormDataManage",
        name: '提交表单审核',
        icon: 'el-icon-document-copy',
        component: () => import(`@/views/admin/FormDataManage.vue`),
        meta: { requireAuth: true }
      }
    ]
  },
  {
    path: "/user",
    component: () => import(`@/views/user/Main.vue`),
    meta: {
      requireAuth: true // 需要登录验证
    },
    children: [
      {
        name: '首页',
        path: "/article",
        component: () => import(`@/views/user/Home.vue`),
        meta: {
          requireAuth: true,
        },
      },
      {
        name: '健康资讯详情',
        path: "/article-detail",
        component: () => import(`@/views/user/ArticleDetail.vue`),
        meta: {
          requireAuth: true,
          isHidden: true, // 不显示
        },
      },
      {
        name: '我的收藏',
        path: "/savedArticle",
        component: () => import(`@/views/user/SavedArticle.vue`),
        meta: {
          requireAuth: true,
        },
      },
      {
        name: '表单',
        path: "/form",
        component: () => import(`@/views/user/UserForm.vue`),
        meta: {
          requireAuth: true,
        },
      },
      {
        name: '通知',
        path: "/notice",
        component: () => import(`@/views/user/UserNotice.vue`),
        meta: {
          requireAuth: true,
        },
      },
    ]
  },

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