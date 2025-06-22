
<template>
  <div class="dashboard-container">
    <!-- 顶部统计卡片 -->
    <div class="stats-row">
      <div class="stat-card" v-for="(item, index) in statItems" :key="index">
        <h3 class="stat-title">{{ item.title }}</h3>
        <div class="stat-value">{{ item.value }}</div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="chart-row">
      <div ref="linechartRef" class="chart line-chart"></div>
      <div ref="piechartRef" class="chart pie-chart"></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, watch, computed } from "vue";
import * as echarts from "echarts";
import request from "@/utils/request.js";
import { dayjs, ElMessage } from "element-plus";

// Refs
const linechartRef = ref(null);
const piechartRef = ref(null);
let lineChart = null;
let pieChart = null;

// Reactive data
const data = reactive({
  group: [],
  loginData: [],
  songsClick: 0,
  songsCount: 0,
  songListsCount: 0,
  consumersCount: 0,
  todayUploadCount: 0,
});

const database = reactive({
  currentDates: JSON.parse(localStorage.getItem("currentDates")) || [],
});

// 统计卡片数据
const statItems = computed(() => [
  { title: '今日点击人数', value: data.todayUploadCount },
  { title: '累计播放总数', value: data.songsClick },
  { title: '累计歌曲总数', value: data.songsCount },
  { title: '累计歌单总数', value: data.songListsCount },
  { title: '累计用户总数', value: data.consumersCount }
]);

// 图表配置
const lineChartOption = {
  tooltip: { trigger: 'axis' },
  xAxis: { type: "category", data: [] },
  yAxis: { type: "value" },
  series: [{
    type: "line",
    data: [],
    smooth: true,
    areaStyle: {
      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: 'rgba(58, 77, 233, 0.8)' },
        { offset: 1, color: 'rgba(58, 77, 233, 0.1)' }
      ])
    }
  }]
};

const pieChartOption = {
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', right: 10, top: 'center' },
  series: [{
    type: "pie",
    radius: ['40%', '70%'],
    avoidLabelOverlap: false,
    itemStyle: {
      borderRadius: 10,
      borderColor: '#fff',
      borderWidth: 2
    },
    label: { show: false, position: 'center' },
    emphasis: {
      label: {
        show: true,
        fontSize: '18',
        fontWeight: 'bold'
      }
    },
    data: []
  }]
};

// 数据加载
const loadData = async () => {
  try {
    await Promise.all([
      loadGroup(),
      loadLoginData(),
      loadSongsClick(),
      loadSongs(),
      loadSongList(),
      loadConsumer(),
      loadNewClick()
    ]);
    ElMessage.success('数据加载成功');
  } catch (error) {
    console.error("Failed to load data:", error);
    ElMessage.error('数据加载失败');
  }
};

const loadGroup = async () => {
  const res = await request.get("/singer/groupByType");
  if (res.code === "200") {
    data.group = res.data;
  }
};

const loadSongs = async () => {
  const res = await request.get("/song/selectAll");
  if (res.code === "200") {
    data.songsCount = res.data.length;
  }
};

const loadSongList = async () => {
  const res = await request.get("/songList/songListsCount");
  if (res.code === "200") {
    data.songListsCount = res.data.length;
  }
};

const loadConsumer = async () => {
  const res = await request.get("/consumer/selectAll");
  if (res.code === "200") {
    data.consumersCount = res.data.length;
  }
};

const loadSongsClick = async () => {
  const res = await request.get("/statistic/getsongsClick");
  if (res) {
    data.songsClick = res;
  }
};

const loadLoginData = async () => {
  const res = await request.get("/consumer/selectAll");
  if (res.code === "200") {
    data.loginData = res.data;
    const today = dayjs().format("YYYY-MM-DD");
    data.todayUploadCount = res.data.filter(
        item => item.updateTime && dayjs(item.updateTime).format("YYYY-MM-DD") === today
    ).length;
  }
};

const loadNewClick = async () => {
  const currentDate = dayjs().format("YYYY-MM-DD");
  const res = await request.get(`/statistic/getnewClicks/${currentDate}`);
  if (res) {
    let storedDates = JSON.parse(localStorage.getItem("currentDates")) || [];
    if (!Array.isArray(storedDates)) storedDates = [];
    const index = storedDates.findIndex(item => item.date === currentDate);
    if (index !== -1) {
      storedDates[index].value = res;
    } else {
      storedDates.push({ date: currentDate, value: res });
      if (storedDates.length > 7) {
        storedDates.shift();
      }
    }
    localStorage.setItem("currentDates", JSON.stringify(storedDates));
    database.currentDates = storedDates;
    updateLineChart();
  }
};

// 初始化图表
const initCharts = () => {
  if (linechartRef.value) {
    lineChart = echarts.init(linechartRef.value);
    updateLineChart();
  }
  if (piechartRef.value) {
    pieChart = echarts.init(piechartRef.value);
    updatePieChart();
  }
};

const updateLineChart = () => {
  if (!lineChart) return;
  const option = {
    ...lineChartOption,
    xAxis: {
      ...lineChartOption.xAxis,
      data: database.currentDates.map(item => item.date)
    },
    series: [
      {
        ...lineChartOption.series[0],
        data: database.currentDates.map(item => item.value)
      }
    ]
  };
  lineChart.setOption(option);
};

const updatePieChart = () => {
  if (!pieChart) return;
  const option = {
    ...pieChartOption,
    series: [
      {
        ...pieChartOption.series[0],
        data: data.group.map(item => ({
          value: item.count,
          name: item.type
        }))
      }
    ]
  };
  pieChart.setOption(option);
};

// Watch
watch(() => database.currentDates, () => {
  updateLineChart();
}, { deep: true });

watch(() => data.group, () => {
  updatePieChart();
}, { deep: true });

// 生命周期
onMounted(() => {
  loadData();
  initCharts();
});

// 自适应
window.addEventListener('resize', () => {
  lineChart?.resize();
  pieChart?.resize();
});
</script>

<style scoped>
.dashboard-container {
  background-color: #e9f8ff;
  padding: 20px;
}

.stats-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  flex: 1;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  text-align: center;
}

.stat-title {
  font-size: 18px;
  color: #555;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #3a4de9;
}

.chart-row {
  display: flex;
  gap: 20px;
}

.chart {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.line-chart {
  width: 65%;
  height: 400px;
}

.pie-chart {
  width: 35%;
  height: 400px;
}
</style>