<template>
  <div class="line-main">
    <div>
      <!-- 折线图标题 -->
      <span class="tag">{{ tag }}</span>
    </div>
    <div
      ref="chart"
      :style="{ width: '100%', height: height }"
    ></div>
  </div>
</template>
<script>
// 折线图组件
import * as echarts from 'echarts';
export default {
  name: 'DialogLine',
  props: {
    tag: {
      type: String,
      default: '折线图'
    },
    // y轴数据
    values: {
      type: Array,
      required: true
    },
    // x轴数据
    date: {
      type: Array,
      required: true
    },
    // 折线图高度
    height: {
      type: String,
      default: '220px'
    },
  },
  watch: {
    // 监听下拉选择框的值变化。当值发生变化时，触发on-selected事件，将新值传递给父组件。
    selectedValue: {
      // eslint-disable-next-line no-unused-vars
      handler (newVal, oldVal) {
        this.$emit('on-selected', newVal);
      },
      deep: true,
      immediate: false // 如果需要在初始化时也触发监听，可以设为 true
    },
    // 监听value的变化，当数据发生变化时，重新初始化图表
    values: {
      // eslint-disable-next-line no-unused-vars
      handler (newVal, oldVal) {
        this.initChart();
      },
      deep: true,
      immediate: false // 如果需要在初始化时也触发监听，可以设为 true
    }
  },
  data () {
    return {
      chart: null,
      options: [{ num: 7, name: '7天内' }, { num: 30, name: '30天内' }, { num: 60, name: '60天内' }],
      selectedValue: '',
    }
  },
  methods: {
    // 图表初始化
    initChart () {
      this.chart = echarts.init(this.$refs.chart);
      let option = {
        // 图表内边距设置
        grid: {
          left: 40,
          right: 20,
          top: 40,
          bottom: 40,
          containLabel: true
        },
        title: {
          text: '',
          color: '#ffffff',
        },
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          borderColor: '#eee',
          borderWidth: 1,
          textStyle: {
            color: '#333'
          },
          formatter: '{b}: {c}',
        },
        legend: {
          data: ['']
        },
        // x轴配置
        xAxis: {
          data: this.date,
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: {
            color: '#666',
            fontSize: 12
          },
        },
        // y轴配置
        yAxis: {
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: {
            color: 'rgb(51，51，51)',
            fontSize: '14',
            formatter: function (value) {
              // 只显示整数
              if (value % 1 === 0) {
                return value;
              }
            },
          },
        },
        // 折线图的数据、样式、标签等设置
        series: [{
          name: '',
          type: 'line',
          smooth: true,
          data: this.values,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(80, 141, 255, 0.5)' },
              { offset: 1, color: 'rgba(80, 141, 255, 0.1)' }
            ])
          },
          lineStyle: {
            width: 3,
            color: '#508dff'
          },
          symbolSize: 6,
          itemStyle: {
            color: '#5B8FF9',
            borderColor: '#5B8FF9',
            borderWidth: 2
          },
          label: {
            show: false
          },
        }]
      };
      this.chart.setOption(option);

      // 添加窗口大小变化时的自适应调整
      window.addEventListener('resize', this.resizeChart);
    },
  },
  // 在组件销毁前，销毁ECharts实例，释放资源
  beforeDestroy () {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
  },
};  
</script>
<style scoped lang="scss">
.line-main {
  margin-bottom: 5px;
  border-radius: 3px;

  .tag {
    font-size: 14px;
    padding: 15px 16px;
    display: inline-block;
    color: #6f6d6d;
    font-weight: bold;
  }

  .time-show {
    padding: 15px 6px;
    float: right;
  }
}
</style>