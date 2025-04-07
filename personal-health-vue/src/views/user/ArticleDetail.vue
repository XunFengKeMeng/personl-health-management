<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="article-detail-container">
    <el-row :gutter="20">
      <!-- 左侧主要内容区域 -->
      <el-col :span="17">
        <div
          class="article-main-content"
          v-loading="loading"
        >
          <!-- 文章标题区域 -->
          <div class="article-header">
            <h1 class="article-title">{{ article.healthArticleTitle }}</h1>
            <div class="article-info">
              <span
                v-if="article.top"
                class="recommend-tag"
              ><i class="el-icon-star-on"></i> 推荐</span>
            </div>
          </div>

          <!-- 文章内容区域 -->
          <div class="article-content">
            <div>{{ article.healthArticleContent }}</div> <!-- 直接绑定内容 -->
          </div>

          <!-- 文章底部区域 -->
          <div class="article-footer">
            <span
              class="publish-time">发布于：{{ parseTime(article.healthArticleCreateTime) }}</span>
            <div class="article-actions">
              <el-tooltip
                :content="article.saved ? '取消收藏' : '收藏文章'"
                placement="top"
              >
                <div
                  class="favorite-btn"
                  @click="toggleFavorite"
                >
                  <i
                    :class="['heart-icon', article.saved ? 'el-icon-star-on' : 'el-icon-star-off']"></i>
                  <span>{{ article.saved ? '已收藏' : '收藏' }}</span>
                </div>
              </el-tooltip>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 右侧推荐阅读区域 -->
      <el-col :span="7">
        <div class="recommend-section">
          <div class="section-header">
            <h3>推荐阅读</h3>
          </div>
          <div
            class="recommend-list"
            v-loading="recommendLoading"
          >
            <div
              v-for="(item, index) in recommendArticles"
              :key="'recommend-'+index"
              class="recommend-item"
              @click="gotoArticle(item)"
            >
              <div class="recommend-item-title">{{ item.healthArticleTitle }}
              </div>
              <div class="recommend-item-meta">
                <span
                  v-if="item.top"
                  class="recommend-tag"
                ><i class="el-icon-star-on"></i> 推荐</span>
                <span
                  class="article-time">{{ parseTime(item.healthArticleCreateTime) }}</span>
              </div>
            </div>

            <el-empty
              v-if="recommendArticles.length === 0"
              description="暂无推荐资讯"
            ></el-empty>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { timeAgo } from "@/utils/data";
import { getToken } from "@/utils/storage"
const API = {
  QUERY_ARTICLE_DETAIL: "article/queryDetail",
  QUERY_ARTICLES: "article/queryArticles",
  SAVE_ARTICLE: "articleSaved/save",
  UNSAVE_ARTICLE: "articleSaved/deleteOne"
};

export default {
  data () {
    return {
      article: {
        healthArticleId: "",
        healthArticleTitle: "",
        healthArticleContent: "",
        healthArticleCreateTime: "",
        top: false,
        saved: false
      },
      recommendArticles: [],
      loading: true,
      recommendLoading: true,
      userId: null
    };
  },
  created () {
    this.getUserID();
    // 从sessionStorage获取文章ID或基本信息
    const articleInfo = sessionStorage.getItem('articleInfo');
    if (articleInfo) {
      const parsedInfo = JSON.parse(articleInfo);
      this.article.healthArticleId = parsedInfo.healthArticleId;

      // 如果有完整文章信息则直接使用，否则通过API获取
      if (parsedInfo.healthArticleContent) {
        this.article = parsedInfo;
        this.loading = false;
        this.loadRecommendArticles();
      } else {
        this.loadArticleDetail();
      }
    } else {
      // 从URL参数获取文章ID
      const articleId = this.$route.query.id;
      if (articleId) {
        this.article.healthArticleId = articleId;
        this.loadArticleDetail();
      } else {
        this.$message.error('文章ID不存在');
        this.loading = false;
      }
    }
  },
  methods: {
    // 获取当前用户ID
    async getUserID () {
      try {
        const token = { token: getToken() };
        const res = await this.$axios.post('user/auth', token);
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

    // 加载文章详情
    async loadArticleDetail () {
      this.loading = true;
      this.$axios.post(API.QUERY_ARTICLE_DETAIL, {
        healthArticleId: this.article.healthArticleId,
        userId: this.userId
      }).then(response => {
        const { data } = response;
        if (data.code === 200) {
          this.article = data.data;
        } else {
          this.$message.error('获取文章详情失败');
        }
        this.loading = false;
        // 加载推荐文章
        this.loadRecommendArticles();
      }).catch(() => {
        this.$message.error('获取文章详情失败');
        this.loading = false;
      });
    },

    // 加载推荐文章
    async loadRecommendArticles () {
      this.recommendLoading = true;
      this.$axios.post(API.QUERY_ARTICLES, {}).then(response => {
        const { data } = response;
        if (data.code === 200) {
          const randomArticles = data.data
            .sort(() => Math.random() - 0.5) // 打乱数组
            .slice(0, 5); // 取前 5 项
          this.recommendArticles = randomArticles;
        }
        this.recommendLoading = false;
      }).catch(() => {
        this.recommendLoading = false;
      });
    },

    // 切换收藏状态
    async toggleFavorite () {
      const apiEndpoint = this.article.saved ? API.UNSAVE_ARTICLE : API.SAVE_ARTICLE;

      this.$axios.post(apiEndpoint, {
        articleId: this.article.healthArticleId,
        userId: this.userId
      }).then(response => {
        const { data } = response;
        if (data.code === 200) {
          this.article.saved = !this.article.saved;
          this.$message.success(this.article.saved ? '收藏成功' : '已取消收藏');
        } else {
          this.$message.error(this.article.saved ? '取消收藏失败' : '收藏失败');
        }
      }).catch(() => {
        this.$message.error('操作失败');
      });
    },

    // 资讯信息与用户收藏
    async articleItemClickBefore (article) {
      var detailDto = {
        healthArticleId: article.healthArticleId,
        userId: this.userId
      }
      try {
        const response = await this.$axios.post(API.QUERY_ARTICLE_DETAIL, detailDto);
        const { data } = response;
        if (data.code === 200) {
          article.saved = data.data.saved;
        }
      } catch (error) {
        console.error('请求失败:', error);
      }
    },

    // 跳转到另一篇文章
    async gotoArticle (article) {
      await this.articleItemClickBefore(article);
      sessionStorage.setItem('articleInfo', JSON.stringify(article));
      // 当前页刷新而不是新建页面
      this.article = article;
      this.loadArticleDetail();
      // 滚动到顶部
      window.scrollTo(0, 0);
    },

    // 转换时间
    parseTime (time) {
      return time ? timeAgo(time) : '';
    }
  }
};
</script>

<style scoped lang="scss">
.article-detail-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;

  .article-main-content {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    padding: 30px;
    margin-bottom: 20px;

    .article-header {
      margin-bottom: 30px;
      border-bottom: 1px solid #f0f0f0;
      padding-bottom: 20px;

      .article-title {
        font-size: 24px;
        font-weight: 600;
        color: #333;
        margin: 0 0 15px;
        line-height: 1.4;
      }

      .article-info {
        display: flex;
        align-items: center;
      }
    }

    .article-content {
      font-size: 16px;
      line-height: 1.8;
      color: #333;
      margin-bottom: 30px;

      // 添加内容样式
      :deep(p) {
        margin-bottom: 16px;
      }

      :deep(h2) {
        font-size: 20px;
        margin: 25px 0 15px;
        font-weight: 600;
      }

      :deep(img) {
        max-width: 100%;
        height: auto;
        margin: 15px 0;
        border-radius: 4px;
      }

      :deep(ul),
      :deep(ol) {
        padding-left: 20px;
        margin-bottom: 16px;
      }

      :deep(li) {
        margin-bottom: 8px;
      }

      :deep(blockquote) {
        border-left: 4px solid #1d7ce1;
        padding: 10px 15px;
        background-color: #f8f9fa;
        margin: 15px 0;
        color: #555;
      }

      :deep(code) {
        background-color: #f5f5f5;
        padding: 2px 4px;
        border-radius: 3px;
        font-family: monospace;
      }
    }

    .article-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 20px;
      border-top: 1px solid #f0f0f0;

      .publish-time {
        color: #999;
        font-size: 14px;
      }

      .article-actions {
        display: flex;
        align-items: center;

        .favorite-btn {
          display: flex;
          align-items: center;
          cursor: pointer;
          padding: 8px 15px;
          border-radius: 20px;
          background-color: #f8f8f8;
          transition: all 0.3s ease;

          &:hover {
            background-color: #ffebee;
          }

          .heart-icon {
            font-size: 20px;
            margin-right: 5px;
            color: #ff4d4f;
            transition: all 0.3s ease;

            &.el-icon-star-on {
              animation: pulse 0.5s;
            }
          }

          span {
            color: #666;
            font-size: 14px;
          }
        }
      }
    }
  }

  .recommend-section {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    overflow: hidden;

    .section-header {
      padding: 15px;
      border-bottom: 1px solid #f0f0f0;

      h3 {
        margin: 0;
        font-size: 18px;
        color: #333;
        position: relative;
        padding-left: 12px;

        &:before {
          content: "";
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 4px;
          height: 18px;
          background-color: #1d7ce1;
          border-radius: 2px;
        }
      }
    }

    .recommend-list {
      padding: 10px 15px;

      .recommend-item {
        padding: 12px 0;
        border-bottom: 1px solid #f5f5f5;
        cursor: pointer;
        transition: background-color 0.3s;

        &:hover {
          background-color: #f9f9f9;

          .recommend-item-title {
            color: #1d7ce1;
          }
        }

        &:last-child {
          border-bottom: none;
        }

        .recommend-item-title {
          font-size: 15px;
          margin-bottom: 8px;
          color: #333;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          line-height: 1.4;
        }

        .recommend-item-meta {
          display: flex;
          align-items: center;
          font-size: 12px;
          color: #999;

          .recommend-tag {
            display: flex;
            align-items: center;
            color: #ff6b6b;
            margin-right: 10px;

            i {
              margin-right: 2px;
            }
          }
        }
      }
    }
  }
}

.recommend-tag {
  display: flex;
  align-items: center;
  color: #ff6b6b;
  font-size: 14px;
  margin-right: 10px;

  i {
    margin-right: 3px;
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
.article-content {
  line-height: 1.8;
  white-space: pre-wrap; /* 保留换行和空格 */
}
.article-content p {
  text-indent: 2em; /* 首行缩进2字符 */
  margin-bottom: 1em; /* 段间距 */
}
</style>

