<template>
  <div style="width: 700px;margin: auto;display: flex;position: relative;left: 30px">
    <el-table :data="data.tableData" stripe style="width: 100%" @row-dblclick="changeState">
      <el-table-column prop="pic" label="图片" width="120px">
        <template #default="scope">
          <el-image :src="scope.row.pic" style="width: 50px; height: 50px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="歌曲名" width="120" />
      <el-table-column prop="singerName" label="歌手" width="120" />
      <el-table-column prop="address" label="国家" width="120"/>
      <el-table-column prop="introduction" label="歌曲介绍" width="200"/>
    </el-table>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import store from "@/store/store.js";
import dayjs from "dayjs"; // 确保 request 变量已正确导入

const data = reactive({
  tableData: [],
});

const database = reactive({
  user: JSON.parse(localStorage.getItem("user-info")) || {}, // 防止 null 时报错
});

const load = () => {
  console.log("当前查询的名称:", database.user.find);

  if (!database.user.find) {
    console.error("user-info 中未找到 find 字段");
    return;
  }

  // 使用 `params` 传递查询参数
  request.get("/song/selectAll", { params: { name: database.user.find } })
      .then((res) => {
        if (!res.data || !Array.isArray(res.data)) {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.tableData = [];
        } else {
          data.tableData = res.data; // 假设返回的是数组
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
      });
};
const changeState = async (row) => {
  if (!row || !row.url) {
    console.error('未找到歌源:', row);
    return;
  }
  const songUrl = row.url;
  const id = row.song_id;
  const name = row.name;
  const pic =row.pic;
  console.log("播放的 URL:", songUrl);
  store.dispatch('updatePlayState', { isPlay: true, url: songUrl,name:name,pic:pic });
  store.dispatch('updatePlaying', row.song_id);
  const currentDate = dayjs().format("YYYY-MM-DD");
  try {
    const response = await request.get(`/statistic/getClickDate/${id}`);
    console.log("API Response:", response);
    if (response.clickDate) {
      const oldClick = response.clickDate;
      if (oldClick) {
        if (currentDate !== oldClick) {
          await request.post(`/statistic/updateOldClickCount/${id}`);
          console.log("Statistic 更新成功");
        } else {
          await request.post(`/statistic/updateClickCount/${id}`);
          console.log("日期相同，无需更新");
        }
      } else {
        console.error("获取点击日期失败");
      }
    } else {
      await request.post(`/statistic/add`, {
        songId: id,
        date: currentDate,
        click: 1,
      });
      console.log("添加点击统计成功");
    }
  } catch (err) {
    console.error("Statistic 更新失败:", err);
  }
};
onMounted(load);
</script>

<style>
</style>
