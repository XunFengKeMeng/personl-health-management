<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="search-container">
    <div class="search-result">
      <!-- 加载中 -->
      <div
        v-if="loading"
        v-loading="loading"
        element-loading-text="加载中..."
        class="loading-wrapper"
        style="min-height: 300px"
      ></div>

      <!-- 无数据 -->
      <div
        v-else-if="articleList.length === 0"
        class="empty-result"
      >
        <i class="el-icon-document"></i>
        <!-- <p>{{ searchKeyword ? '未找到相关文章' : '请输入关键词开始搜索' }}</p> -->
        <p>请输入关键词开始搜索</p>
      </div>

      <!-- 数据展示 -->
      <div
        v-else
        class="article-grid"
      >
        <el-row :gutter="20">
          <el-col
            :span="6"
            v-for="(article, index) in articleList"
            :key="index"
            class="article-item-wrapper"
          >
            <div
              class="article-item"
              @click="articleItemClick(article)"
            >
              <div class="article-cover">
                <img
                  :src="article.healthArticleCover || defaultCover"
                  alt="文章封面"
                >
                <div
                  class="article-tag"
                  v-if="article.healthArticleTagName"
                >
                  {{ article.healthArticleTagName }}
                </div>
                <!-- 推荐 -->
                <div
                  class="article-top"
                  v-if="article.top"
                >
                  <span>推荐</span>
                </div>
              </div>
              <div class="article-info">
                <div class="article-title">{{ article.healthArticleTitle }}
                </div>
                <div class="article-time">
                  {{ formatTime(article.healthArticleCreateTime) }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 分页 -->
      <div
        class="pagination-wrapper"
        v-if="total > 0"
      >
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>

    </div>
  </div>
</template>

<script>
import { getToken } from "@/utils/storage"
const API = {
  QUERY_ARTICLE_DETAIL: "article/queryDetail",
  QUERY_ARTICLES: "article/queryArticles",
};
export default {
  data () {
    return {
      searchKeyword: '',
      articleList: [],
      loading: false,
      currentPage: 1,
      pageSize: 8, // 2行4列，每页8条数据
      total: 0,
      defaultCover: require('@/assets/img/article-default-cover.jpg'), // 确保有默认封面图片
      userId: null
    };
  },
  created () {
    // 获取会话存储中的关键词（如果有）
    const keyWord = sessionStorage.getItem('keyWord');
    // if (keyWord) {
    this.searchKeyword = keyWord;
    this.searchArticles();
    // }
    this.getUserID();
  },
  methods: {
    // 关键词修改后刷新
    refreshSearch (keyword) {
      this.searchKeyword = keyword;
      this.searchArticles();
    },

    // 搜索文章
    searchArticles () {
      this.currentPage = 1; // 重置到第一页
      this.fetchArticles();
    },

    // 获取文章数据
    async fetchArticles () {
      this.articleList = [];
      this.total = 0;

      this.loading = true;
      try {
        const params = {
          healthArticleTitle: this.searchKeyword,
          current: (this.currentPage - 1) * this.pageSize,
          size: this.pageSize
        };

        const response = await this.$axios.post(API.QUERY_ARTICLES, params, { withCredentials: true });
        const { data } = response;

        if (data.code === 200) {
          this.articleList = data.data || [];
          this.total = data.total || 0;
        } else {
          this.$message.error(data.msg || '搜索失败');
          this.articleList = [];
          this.total = 0;
        }
      } catch (error) {
        console.error('搜索文章异常:', error);
        this.$message.error('搜索文章时发生错误');
        this.articleList = [];
        this.total = 0;
      } finally {
        this.loading = false;
      }
    },

    // 处理页码变化
    handleCurrentChange (page) {
      this.currentPage = page;
      this.fetchArticles();
    },

    // 处理每页显示数量变化
    handleSizeChange (size) {
      this.pageSize = size;
      this.currentPage = 1;
      this.fetchArticles();
    },

    // 格式化时间
    formatTime (timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    },

    // 健康资讯列表的项点击事件
    async articleItemClick (article) {
      await this.articleItemClickBefore(article);
      sessionStorage.setItem('articleInfo', JSON.stringify(article));
      this.$router.push('/article-detail');
    },

    // 资讯信息与用户收藏
    async articleItemClickBefore (article) {
      var detailDto = {
        healthArticleId: article.healthArticleId,
        userId: this.userId
      }
      try {
        const response = await this.$axios.post(API.QUERY_ARTICLE_DETAIL, detailDto, { withCredentials: true });
        const { data } = response;
        if (data.code === 200) {
          article.saved = data.data.saved;
        }
      } catch (error) {
        console.error('请求失败:', error);
      }
    },
    // 获取当前用户ID
    async getUserID () {
      try {
        const token = { token: getToken() };
        const res = await this.$axios.post('user/auth', token, { withCredentials: true });
        // 错误处理
        if (res.code === 400) {
          this.$message.error(res.data.msg);
          this.$router.push('/login');
          return;
        }
        // 获取发送者ID
        this.userId = res.data.data.userId;
      } catch (error) {
        console.error('获取用户认证信息时发生错误:', error);
        this.$message.error('认证信息加载失败,请重试！');
      }
    },
  }
};
</script>

<style scoped lang="scss">
.search-container {
  padding: 20px;
  min-height: calc(100vh - 120px);
}

.search-header {
  margin-bottom: 30px;

  h2 {
    font-size: 22px;
    margin-bottom: 20px;
    color: #333;
    font-weight: 600;
  }

  .search-input-container {
    max-width: 500px;

    .search {
      border-radius: 15px;
      font-size: 14px;
      display: flex;
      justify-content: space-between;
      align-items: center;

      input {
        outline: none;
        border: none;
        border-left: 1px solid rgb(51, 51, 51);
        border-top: 1px solid rgb(51, 51, 51);
        border-bottom: 1px solid rgb(51, 51, 51);
        padding: 6px 10px;
        font-size: 12px;
        width: 100%;
      }

      input:focus {
        color: rgb(31, 31, 31);
      }

      span:hover {
        background-color: rgb(31, 31, 31);
      }

      span {
        background-color: rgb(51, 51, 51);
        display: inline-block;
        cursor: pointer;
        font-size: 12px;
        color: rgb(245, 245, 245);
        padding: 6px 10px;
        border-top-right-radius: 15px;
        border-bottom-right-radius: 15px;
        white-space: nowrap;
      }
    }
  }
}

.article-grid {
  margin-top: 20px;
}

.article-item-wrapper {
  margin-bottom: 30px;
}

.article-item {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s;
  cursor: pointer;
  height: 100%;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  }
}

.article-cover {
  position: relative;
  height: 180px;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: all 0.3s;
  }

  .article-tag {
    position: absolute;
    top: 10px;
    left: 10px;
    background-color: #15559a;
    color: #fff;
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 12px;
  }

  .article-save {
    position: absolute;
    top: 10px;
    right: 10px;
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(255, 255, 255, 0.8);
    border-radius: 50%;
    cursor: pointer;

    i {
      font-size: 18px;
      color: #ccc;

      &.is-saved {
        color: #ff9900;
      }
    }
  }

  .article-top {
    position: absolute;
    bottom: 10px;
    right: 10px;

    span {
      background-color: #ff4757;
      color: #fff;
      padding: 2px 8px;
      border-radius: 4px;
      font-size: 12px;
    }
  }
}

.article-info {
  padding: 15px;
}

.article-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
  height: calc(1.5em * 2); /* 2行高度 = 行高 × 行数 */
  min-height: calc(1.5em * 2); /* 确保最小高度 */
}

.article-time {
  font-size: 12px;
  color: #999;
}

.empty-result {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 300px;

  i {
    font-size: 50px;
    color: #dcdcdc;
    margin-bottom: 20px;
  }

  p {
    color: #999;
    font-size: 16px;
  }
}

.loading-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.pagination-wrapper {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>
