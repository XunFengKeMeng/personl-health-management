<!-- eslint-disable vue/multi-word-component-names -->
<template>
    <div class="article-homepage">
      <!-- 顶部区域：轮播图和推荐区 -->
      <el-row :gutter="20" class="top-section">
        <!-- 轮播图区域 -->
        <el-col :span="16">
          <el-carousel v-loading="carouselLoading" height="300px" class="main-carousel">
            <el-carousel-item v-for="(article, index) in carouselArticles" :key="'carousel-'+index" @click.native="articleItemClick(article)">
              <div class="carousel-content">
                <img v-if="article.healthArticleCover" :src="article.healthArticleCover" :alt="article.healthArticleTitle">
                <div class="carousel-info">
                  <h3>{{ article.healthArticleTitle }}</h3>
                  <div class="article-meta">
                    <span class="article-tags">{{ article.healthArticleTagName }}</span>
                    <span class="article-time">{{ parseTime(article.healthArticleCreateTime) }}</span>
                  </div>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </el-col>
        
        <!-- 推荐区域 -->
        <el-col :span="8">
          <div class="top-recommend-section">
            <div class="section-header">
              <h3>推荐资讯</h3>
              <el-button type="text" @click="refreshTopArticles" class="refresh-btn">
                <i class="el-icon-refresh"></i> 换一换
              </el-button>
            </div>
            
            <div v-loading="topArticlesLoading" class="top-articles-list">
              <div 
                v-for="(article, index) in articleTopList" 
                :key="'top-'+index" 
                class="top-article-item"
                @click="articleItemClick(article)"
              >
                <div class="item-image">
                  <img v-if="article.healthArticleCover" :src="article.healthArticleCover" :alt="article.healthArticleTitle">
                </div>
                <div class="item-info">
                  <h4>{{ article.healthArticleTitle }}</h4>
                  <div class="article-meta">
                    <span class="article-tags">{{ article.healthArticleTagName }}</span>
                    <span v-if="article.top" class="recommend-tag"><i class="el-icon-star-on"></i> 推荐</span>
                    <span class="article-time">{{ parseTime(article.healthArticleCreateTime) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      
      <!-- 你可能感兴趣 -->
      <div class="article-list-section">
        <el-row :gutter="20">
            <!--  -->
            <template v-if="articleList.length === 0">
                <el-col :span="24">
                    <el-empty description="暂无资讯"></el-empty>
                </el-col>
            </template>
          
            <el-col
                :span="24 / 4" 
                v-for="(article, index) in articleList" 
                :key="'list-'+index" 
                class="article-item"
                v-show="index < 8" 
            >
                <div class="article-card" @click="articleItemClick(article)">
                    <div class="article-img-wrapper" v-loading="!article.imageLoaded">
                        <img 
                        :src="article.healthArticleCover" 
                        :alt="article.healthArticleTitle" 
                        @load="article.imageLoaded = true"
                        >
                    </div>
                    <div class="article-content">
                        <h3 class="article-title">{{ article.healthArticleTitle }}</h3>
                        <div class="article-meta">
                            <span class="article-tags">{{ article.healthArticleTagName }}</span>
                            <span v-if="article.top" class="recommend-tag"><i class="el-icon-star-on"></i> 推荐</span>
                            <span class="article-time">{{ parseTime(article.healthArticleCreateTime) }}</span>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
      </div>
    </div>
  </template>
  
  <script>
  import { timeAgo } from "@/utils/data";
  import { getToken } from "@/utils/storage"
  
  const API = {
    QUERY_ARTICLES: "article/queryArticles",
    QUERY_ARTICLE_DETAIL:"article/queryDetail"
  };
  
  export default {
    data() {
      return {
        articleList: [], // 健康资讯列表
        articleTopList: [], // 推荐的健康资讯数据列表
        carouselArticles: [], // 轮播图文章
        carouselLoading: true, // 轮播图加载状态
        topArticlesLoading: true, // 推荐文章加载状态
        defaultCover: require('@/assets/img/article-default-cover.jpg'),
        userId: null
      };
    },
    created() {
      this.loadCarouselArticles();
      this.loadTopArticle();
      this.loadArticle();
      this.getUserID();
    },
    methods: {
      // 加载轮播图文章（最先创建的3篇）
      async loadCarouselArticles() {
        this.carouselLoading = true;
        this.$axios.post(API.QUERY_ARTICLES, {}).then(response => {
          const { data } = response;
          if (data.code === 200) {
            this.carouselArticles = data.data.slice(0, 3).map(article => this.processArticleImage(article));
          }
          this.carouselLoading = false;
        }).catch(() => {
          this.carouselLoading = false;
        });
      },
      
      // 加载感兴趣的资讯（8篇，两行四列）
      async loadArticle() {
        this.$axios.post(API.QUERY_ARTICLES, {}).then(response => {
          const { data } = response;
          if (data.code === 200) {
            this.articleList = data.data
              .sort(() => Math.random() - 0.5)
              .slice(0, 8)
              .map(article => {
                return {
                  ...this.processArticleImage(article),
                  imageLoaded: false
                };
              });
          }
        });
      },
      
      // 加载推荐资讯
      async loadTopArticle() {
        this.topArticlesLoading = true;
        this.$axios.post(API.QUERY_ARTICLES, {top: true}).then(response => {
          const { data } = response;
          if (data.code === 200) {
            this.articleTopList = data.data
              .sort(() => Math.random() - 0.5)
              .slice(0, 3)
              .map(article => this.processArticleImage(article));
          }
          this.topArticlesLoading = false;
        }).catch(() => {
          this.topArticlesLoading = false;
        });
      },
      
      // 处理文章图片
      processArticleImage(article) {
        return {
          ...article,
          healthArticleCover: article.healthArticleCover 
            ? article.healthArticleCover 
            : this.defaultCover
        };
      },
      
      // 刷新推荐文章
      refreshTopArticles() {
        this.loadTopArticle();
      },
      
    // 获取当前用户ID
    async getUserID() {
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

      // 资讯信息与用户收藏
      async articleItemClickBefore(article) {
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
      
      // 健康资讯列表的项点击事件
      async articleItemClick(article) {
        await this.articleItemClickBefore(article);
        sessionStorage.setItem('articleInfo', JSON.stringify(article));
        this.$router.push('/article-detail');
      },
      
      // 转换时间
      parseTime(time) {
        return timeAgo(time);
      }
    }
  };
</script>
  
<style scoped lang="scss">
    .article-homepage {
    padding: 20px 0;

    .top-section {
        margin-bottom: 30px;
    }

    .main-carousel {
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        
        .carousel-content {
        position: relative;
        height: 100%;
        cursor: pointer;
        
        img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .carousel-info {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
            color: #fff;
            padding: 20px;
            
            h3 {
            margin: 0 0 10px;
            font-size: 20px;
            }
        }
        
        /* 自定义轮播图指示器 */
        .carousel-indicators {
            position: absolute;
            bottom: 10px;
            right: 20px;
            display: flex;
            gap: 6px;
            
            .indicator-dot {
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background-color: rgba(255, 255, 255, 0.6);
            transition: all 0.3s ease;
            
            &.active {
                background-color: #fff;
                transform: scale(1.2);
            }
            }
        }
        }
    }

    .top-recommend-section {
        height: 300px;
        border-radius: 12px;
        overflow: hidden;
        background-color: #fff;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        
        .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 15px;
        border-bottom: 1px solid #f0f0f0;
        
        h3 {
            margin: 0;
            font-size: 18px;
            color: #333;
        }
        
        .refresh-btn {
            padding: 0;
            
            i {
            margin-right: 4px;
            }
        }
        }
        
        .top-articles-list {
        height: calc(100% - 50px);
        overflow: hidden; /* 改为hidden，不再允许滚动 */
        
        .top-article-item {
            display: flex;
            padding: 10px 15px;
            border-bottom: 1px solid #f5f5f5;
            cursor: pointer;
            transition: background-color 0.3s;
            
            &:hover {
            background-color: #f9f9f9;
            }
            
            .item-image {
            width: 80px;
            height: 60px;
            margin-right: 10px;
            border-radius: 6px;
            overflow: hidden;
            flex-shrink: 0;
            
            img {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }
            }
            
            .item-info {
            flex: 1;
            overflow: hidden;
            
            h4 {
                margin: 0 0 8px;
                font-size: 14px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }
            }
        }
        }
    }

    .article-list-section {
        margin-bottom: 20px;
        
        .section-header {
        margin-bottom: 20px;
        
        h3 {
            font-size: 20px;
            color: #333;
            position: relative;
            padding-left: 12px;
            margin: 0;
            
            &:before {
            content: '';
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            width: 4px;
            height: 18px;
            background-color: #1D7CE1;
            border-radius: 2px;
            }
        }
        }
    }

    .article-card {
        width:240px;
        background-color: #fff;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        transition: transform 0.3s, box-shadow 0.3s;
        margin-bottom: 25px;
        cursor: pointer;
        
        &:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
        }
        
        .article-img-wrapper {
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
        
        .article-content {
        padding: 12px;
        
        .article-title {
            font-size: 16px;
            margin: 0 0 10px;
            line-height: 1.4;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            white-space: normal;
            height: 44px;
        }
        }
    }

    .article-meta {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        font-size: 12px;
        align-items: center;
        
        .article-tags {
        padding: 2px 6px;
        background-color: rgba(29, 124, 225, 0.8);
        color: #fff;
        border-radius: 4px;
        }
        
        .recommend-tag {
        color: #ff6b6b;
        display: flex;
        align-items: center;
        
        i {
            margin-right: 2px;
        }
        }
        
        .article-time {
        color: #999;
        }
    }

}
</style>
  
  