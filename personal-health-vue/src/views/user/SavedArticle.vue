<template>
  <div class="collection-container">
    <div class="collection-header">
      <h2 class="collection-title">我的收藏</h2>
      <div class="header-actions">
        <el-button 
          type="danger" 
          size="small" 
          :disabled="selectedArticles.length === 0" 
          @click="batchDeleteCollections"
        >
          <i class="el-icon-delete"></i> 批量删除
        </el-button>
        <el-button 
          type="default" 
          size="small" 
          @click="selectAllToggle"
        >
          {{ isAllSelected ? '取消全选' : '全选' }}
        </el-button>
      </div>
    </div>

    <div class="collection-content" v-loading="loading">
      <!-- 空状态显示 -->
      <el-empty 
        v-if="collections.length === 0 && !loading" 
        description="暂无收藏内容"
      >
        <el-button type="primary" @click="goToArticleHome">去浏览资讯</el-button>
      </el-empty>

      <!-- 收藏列表 -->
      <div class="collections-grid" v-if="collections.length > 0">
        <el-row :gutter="20">
          <el-col 
            :span="6" 
            v-for="article in collections" 
            :key="article.savedId" 
            class="collection-item-col"
          >
            <div class="collection-card">
              <div class="selection-checkbox">
                <el-checkbox 
                  v-model="article.isSelected" 
                  @change="updateSelection"
                ></el-checkbox>
              </div>
              
              <div class="card-content" @click="viewArticle(article)">
                <div class="card-image">
                  <img 
                    :src="article.healthArticleCover || defaultCover" 
                    :alt="article.healthArticleTitle"
                  >
                </div>
                
                <div class="card-info">
                  <h3 class="card-title">{{ article.healthArticleTitle }}</h3>
                  <div class="card-meta">
                    <span class="article-tags">{{ article.healthArticleTagName }}</span>
                    <span class="article-time">{{ parseTime(article.healthArticleCreateTime) }}</span>
                  </div>
                </div>
              </div>
              
              <div class="card-actions">
                <el-tooltip content="删除收藏" placement="top">
                  <i 
                    class="el-icon-delete action-icon" 
                    @click="removeCollection(article.savedId, article.articleId)"
                  ></i>
                </el-tooltip>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 分页控件 -->
      <div class="pagination-container" v-if="total > pageSize">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page.sync="currentPage"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { timeAgo } from "@/utils/data";
import { getToken } from "@/utils/storage";

const API = {
  QUERY_COLLECTIONS: "articleSaved/query",
  DELETE_COLLECTION: "articleSaved/deleteOne",
  BATCH_DELETE: "articleSaved/batchDelete",
  ARTICLE_DETAIL: "article/queryDetail"
};

export default {
  data() {
    return {
      collections: [],
      selectedArticles: [],
      currentPage: 1,
      pageSize: 8,
      total: 0,
      loading: true,
      userId: null,
      defaultCover: require('@/assets/img/article-default-cover.jpg'),
      isAllSelected: false
    };
  },
  created() {
    this.getUserID();
  },
  methods: {
    // 获取用户ID
    async getUserID() {
      try {
        const token = { token: getToken() };
        const res = await this.$axios.post('user/auth', token, {withCredentials: true});
        if (res.code === 400) {
          this.$message.error(res.data.msg);
          this.$router.push('/login');
          return;
        }
        this.userId = res.data.data.userId;
        this.loadCollections();
      } catch (error) {
        console.error('获取用户认证信息时发生错误:', error);
        this.$message.error('认证信息加载失败,请重试！');
      }
    },

    // 加载收藏列表
    async loadCollections() {
      this.loading = true;
      try {
        const response = await this.$axios.post(API.QUERY_COLLECTIONS, {
          userId: this.userId,
          current: (this.currentPage - 1) * this.pageSize,
          size: this.pageSize
        }, {withCredentials: true});
        
        const { data } = response;
        if (data.code === 200) {
          this.collections = data.data.map(item => ({
            ...item,
            isSelected: false,
            healthArticleCover: item.healthArticleCover || this.defaultCover
          }));
          this.total = data.total;
        } else {
          this.$message.error('获取收藏列表失败');
        }
      } catch (error) {
        console.error('获取收藏列表失败:', error);
        this.$message.error('获取收藏列表失败');
      } finally {
        this.loading = false;
      }
    },

    // 处理分页变化
    handlePageChange(page) {
      this.currentPage = page;
      this.loadCollections();
    },

    // 查看文章详情
    async viewArticle(article) {
      try {
        // 查询文章详情，获取完整信息
        const response = await this.$axios.post(API.ARTICLE_DETAIL, {
          healthArticleId: article.articleId,
          userId: this.userId
        }, {withCredentials: true});
        
        const { data } = response;
        if (data.code === 200) {
          // 保存文章信息到会话存储
          sessionStorage.setItem('articleInfo', JSON.stringify(data.data));
          // 跳转到文章详情页
          this.$router.push('/article-detail');
        } else {
          this.$message.error('获取文章详情失败');
        }
      } catch (error) {
        console.error('获取文章详情失败:', error);
        this.$message.error('获取文章详情失败');
      }
    },

    // 删除单个收藏
    async removeCollection(savedId, articleId) {
      try {
        const response = await this.$axios.post(API.DELETE_COLLECTION, {
          userId: this.userId,
          articleId: articleId
        }, {withCredentials: true});
        
        const { data } = response;
        if (data.code === 200) {
          this.$message.success('取消收藏成功');
          // 刷新当前页数据
          this.loadCollections();
        } else {
          this.$message.error('取消收藏失败');
        }
      } catch (error) {
        console.error('取消收藏失败:', error);
        this.$message.error('取消收藏失败');
      }
    },

    // 更新选择状态
    updateSelection() {
      this.selectedArticles = this.collections.filter(item => item.isSelected).map(item => item.savedId);
      // 检查是否全选
      this.isAllSelected = this.selectedArticles.length === this.collections.length && this.collections.length > 0;
    },

    // 全选/取消全选
    selectAllToggle() {
      const newState = !this.isAllSelected;
      this.collections.forEach(item => {
        item.isSelected = newState;
      });
      this.updateSelection();
    },

    // 批量删除收藏
    async batchDeleteCollections() {
      if (this.selectedArticles.length === 0) {
        this.$message.warning('请先选择要删除的收藏');
        return;
      }

      try {
        await this.$confirm('确定要删除选中的收藏吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await this.$axios.post(API.BATCH_DELETE, this.selectedArticles, {withCredentials: true});
        
        const { data } = response;
        if (data.code === 200) {
          this.$message.success(`成功删除${this.selectedArticles.length}个收藏`);
          this.selectedArticles = [];
          this.isAllSelected = false;
          this.loadCollections();
        } else {
          this.$message.error('批量删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error);
          this.$message.error('批量删除失败');
        }
      }
    },

    // 前往资讯首页
    goToArticleHome() {
      this.$router.push('/article-home');
    },

    // 格式化时间
    parseTime(time) {
      return timeAgo(time);
    }
  }
};
</script>

<style scoped lang="scss">
.collection-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .collection-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .collection-title {
      font-size: 24px;
      color: #333;
      margin: 0;
      position: relative;
      padding-left: 15px;

      &:before {
        content: "";
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 5px;
        height: 20px;
        background-color: #1d7ce1;
        border-radius: 2px;
      }
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .collection-content {
    min-height: 400px;

    .collections-grid {
      margin-bottom: 30px;
    }

    .collection-item-col {
      margin-bottom: 20px;
    }

    .collection-card {
      position: relative;
      background-color: #fff;
      border-radius: 10px;
      overflow: hidden;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s, box-shadow 0.3s;
      height: 100%;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);

        .card-actions {
          opacity: 1;
        }
      }

      .selection-checkbox {
        position: absolute;
        top: 5x;
        left: 8px;
        z-index: 2;
        border-radius: 50%;
        padding: 2px;
      }

      .card-content {
        cursor: pointer;
      }

      .card-image {
        height: 150px;
        overflow: hidden;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
          transition: transform 0.3s;
        }

        &:hover img {
          transform: scale(1.05);
        }
      }

      .card-info {
        padding: 12px;

        .card-title {
          font-size: 16px;
          color: #333;
          margin: 0 0 10px;
          line-height: 1.4;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          height: 44px;
        }

        .card-meta {
          display: flex;
          flex-wrap: wrap;
          gap: 8px;
          align-items: center;
          font-size: 12px;

          .article-tags {
            padding: 2px 6px;
            background-color: rgba(29, 124, 225, 0.8);
            color: #fff;
            border-radius: 4px;
          }

          .article-time {
            color: #999;
          }
        }
      }

      .card-actions {
        position: absolute;
        bottom: 10px;
        right: 10px;
        background-color: rgba(255, 255, 255, 0.9);
        border-radius: 20px;
        padding: 5px 10px;
        opacity: 0;
        transition: opacity 0.3s;

        .action-icon {
          color: #ff4d4f;
          font-size: 16px;
          cursor: pointer;
          margin: 0 5px;

          &:hover {
            color: #ff1f1f;
          }
        }
      }
    }
  }

  .pagination-container {
    text-align: center;
    margin-top: 20px;
  }
}
</style>

