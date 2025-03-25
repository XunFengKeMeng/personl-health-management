<template>
  <div
    style="box-sizing: border-box;overflow-y: hidden;overflow-x: hidden;padding: 10px;"
  >
    <!-- 卡片显示区 -->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card
          class="card"
          shadow="hover"
        >
          <div class="card-header">
            <h3>用户数</h3>
          </div>
          <div class="card-content">
            <h2>{{ userCount }}</h2>
            <p>当前用户数量</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card
          class="card"
          shadow="hover"
        >
          <div class="card-header">
            <h3>文章数</h3>
          </div>
          <div class="card-content">
            <h2>{{ articleCount }}</h2>
            <p>当前文章数量</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card
          class="card"
          shadow="hover"
        >
          <div class="card-header">
            <h3>健康指标数</h3>
          </div>
          <div class="card-content">
            <h2>{{ healthMetricCount }}</h2>
            <p>健康指标数量</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 饼图与折线图 -->
    <el-row>
      <!-- 饼图 -->
      <el-col :span="8">
        <div style="padding: 15px 5px;box-sizing: border-box;">
          <div class="card-header">
            <h3>文章标签分布</h3>
          </div>
          <PieChart
            fontColor="rgb(51,51,51)"
            bag="rgb(248,248,248)"
            :values="tagValues"
            :types="tagTypes"
          />
        </div>
      </el-col>
      <!-- 折线图 -->
      <el-col :span="16">
        <div style="padding: 15px;box-sizing: border-box;">
          <LineChart
            height="290px"
            tag="文章数量趋势"
            @on-selected="articleDatesSelected"
            :values="articleValues"
            :date="articleDates"
          />
        </div>
      </el-col>
    </el-row>
  </div>
</template>


<script>
import LineChart from "@/components/LineChart"
import PieChart from "@/components/PieChart"
const API = {
  ARTICLE_TAGS: '/article/queryStatistics',
  ARTICLE_TREND: '/article/queryTrend',
  NUMBER_USER: 'user/queryUsers',
  NUMBER_ARTICLE: 'article/queryArticles',
  NUMBER_METRIC: 'metric/queryMetrics'
};
export default {
  components: { LineChart, PieChart },
  data () {
    return {
      tagValues: [], // 饼图数据：文章数量
      tagTypes: [], // 饼图数据：文章标签
      articleValues: [], // 折线图数据：文章数量
      articleDates: [], // 折线图数据：日期
      userCount: 0, // 用户数
      articleCount: 0, // 文章数
      healthMetricCount: 0 // 健康指标数
    }
  },
  created () {
    this.loadPieCharts();   // 加载饼图数据
    this.articleDatesSelected(); // 加载折线图数据
    this.loadStatistics(); // 加载统计数据
  },
  methods: {
    // 加载加载饼图
    loadPieCharts () {
      this.$axios.get(API.ARTICLE_TAGS)
        .then(response => {
          const { data } = response;
          if (data.code === 200) {
            this.tagValues = data.data.map(entity => entity.articleCount);
            this.tagTypes = data.data.map(entity => entity.tagName);
          }
        })
        .catch(error => {
          console.error('加载饼图数据失败:', error);
          this.$message.error('加载饼图数据失败，请重试！');
        });
    },
    // 加载折线图数据（文章数量趋势）
    articleDatesSelected () {
      this.$axios.get(API.ARTICLE_TREND)
        .then(response => {
          const { data } = response;
          if (data.code === 200) {
            this.articleValues = data.data.map(entity => entity.articleCount);
            this.articleDates = data.data.map(entity => entity.articleDate.slice(0, 10));
          }
        })
        .catch(error => {
          console.error('加载文章数量趋势数据失败:', error);
          this.$message.error('加载文章数量趋势数据失败，请重试！');
        });
    },
    // 加载统计数据（用户数、文章数、健康指标数）
    loadStatistics () {
      // 用户数
      this.$axios.post(API.NUMBER_USER, { userName: '' })
        .then(response => {
          const { data } = response;
          if (data.code === 200) {
            this.userCount = data.total;
          }
        })
        .catch(error => {
          console.error('加载用户数量数据失败:', error);
          this.$message.error('加载用户数量数据失败，请重试！');
        });
      // 文章数 
      this.$axios.post(API.NUMBER_ARTICLE, { healthArticleTitle: '' })
        .then(response => {
          const { data } = response;
          if (data.code === 200) {
            this.articleCount = data.total;
          }
        })
        .catch(error => {
          console.error('加载用户数量数据失败:', error);
          this.$message.error('加载用户数量数据失败，请重试！');
        });
      // 健康指标数
      this.$axios.post(API.NUMBER_METRIC, { healthMetricName: '' })
        .then(response => {
          const { data } = response;
          if (data.code === 200) {
            this.healthMetricCount = data.total;
          }
        })
        .catch(error => {
          console.error('加载用户数量数据失败:', error);
          this.$message.error('加载用户数量数据失败，请重试！');
        });
    }
  }


}
</script>

<style scoped lang="scss">
.card-header {
  font-size: 12px;
  color: #6f6d6d;
}
.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 15px 0;
  text-align: center;
}
.card-content h2 {
  font-size: 30px;
  margin: 0;
  color: #3498db;
}
.card-content p {
  color: #7f8c8d;
}
.card {
  border-radius: 10px;
  background-color: #ffffff;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>