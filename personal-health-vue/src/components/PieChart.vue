<template>
  <div
    class="line-main"
    :style="{ backgroundColor: bag }"
  >
    <div
      ref="chart"
      :style="{ width: width, height: height }"
    ></div>
  </div>
</template>
<script>
import * as echarts from 'echarts'
export default {
  name: 'PieChart',
  props: {
    // 饼图中的分类名称
    types: {
      type: Array,
      default: function () { return [] }
    },
    // 饼图的数据值
    values: {
      type: Array,
      default: function () { return [] }
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    },
    // 背景颜色
    bag: {
      type: String,
      default: '#fff'
    },
  },
  data () {
    return {
      chart: null,
      colorList: [
        '#508dff', // Primary blue
        '#36c6d3', // Teal
        '#f7b924', // Yellow
        '#ef5350', // Red
        '#6772e5', // Purple
        '#2ecc71', // Green
        '#fd9644', // Orange
        '#9c27b0'  // Deep purple
      ]
    }
  },
  // 监听饼图中的分类，当分类更新时，重新初始化图表
  watch: {
    // eslint-disable-next-line no-unused-vars
    types (v1, v2) {
      this.initChart();
    }
  },
  // 在组件挂载时，初始化图表
  mounted () {
    this.initChart();
  },
  methods: {
    // 初始化图表
    initChart () {
      this.chart = echarts.init(this.$refs.chart)
      let option = {
        title: {
          text: '',
          subtext: '',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          borderColor: '#eee',
          borderWidth: 1,
          textStyle: {
            color: '#333'
          },
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'horizontal',
          bottom: 0,
          left: 'center',
          show: true,
          itemWidth: 12,
          itemHeight: 12,
          textStyle: {
            color: '#666',
            fontSize: 12
          }
        },
        // ECharts配置
        series: [
          {
            name: '',
            type: 'pie',  // 图表类型为饼图
            radius: ['40%', '70%'], // 环形图效果
            center: ['50%', '40%'], //
            avoidLabelOverlap: true, // 避免饼图中有重叠的配置项
            emphasis: {
              label: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.2)'
              }
            },
            labelLine: {
              length: 10,
              length2: 12,
              smooth: true
            },
            // 设置显示的数据标签，格式为百分比
            label: {
              show: true,
              position: 'outer',
              formatter: (params) => {
                // 计算百分比并保留一位小数
                const percent = params.percent.toFixed(1);
                return `${percent}%`;
              },
              fontWeight: 500
            },
            // 将types和values组合成ECharts所需的数据格式
            data: this.values.map((value, index) => ({
              name: this.types[index],
              value: value,
            })),
            // 设置图表各部分的颜色
            itemStyle: {
              color: (params) => this.colorList[params.dataIndex % this.colorList.length],
              borderRadius: 4,
              borderWidth: 2,
              borderColor: '#fff'
            },
          }
        ]
      }
      this.chart.setOption(option)
    }
  },
  // 在组件销毁前，销毁ECharts实例，释放资源
  beforeDestroy () {
    if (this.chart) {
      this.chart.dispose()
    }
  }
}  
</script>

<style scoped lang="scss">
.line-main {
  padding-top: 10px;
  margin-bottom: 5px;
  border-radius: 3px;
  background-color: #000000;

  .tag {
    font-size: 14px;
    text-align: center;
    padding: 15px 6px;
    display: block;
  }
}
</style>