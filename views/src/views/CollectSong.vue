<template>
  <div style="position:relative;top:-30px;padding: 10px">
    <el-table :data="data.songData" stripe style="width: 100%;border-bottom: none" @row-dblclick="changeState">
      <el-table-column prop="name" label="歌曲名称" width="467px" />
      <el-table-column prop="singerName" label="歌手" width="370px" />
    </el-table>
  </div>
</template>

<script setup>
import {onMounted, reactive} from "vue";
import request from "@/utils/request.js";
import store from "@/store/store.js";

const data = reactive({
      songData:[],
    }
)

const user = reactive({
  consumerInfo: JSON.parse(localStorage.getItem("consumerInfo")) || {}, // 防止 null 时报错
});

const load = () => {
  request.get(`/collect/selectSongByUserId/${user.consumerInfo.id}`)
      .then((res) => {
        console.log(res.data)
        if (!res.data || !Array.isArray(res.data)) {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.songData = [];
        } else {
          data.songData = res.data;
          console.log("后端返回数据:", data.tableData);
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
      });
};
const changeState = async (row) => {
  if (!row || !row.url) {
    console.error('Invalid row data or missing URL:', row);
    return;
  }
  const songUrl = row.url;
  const id = row.song_id;
  const name = row.name;
  const pic =row.pic;
  console.log("播放的 URL:", songUrl);
  store.dispatch('updatePlayState', { isPlay: true, url: songUrl,name:name,pic:pic });
  store.dispatch('updatePlaying', row.song_id);
  try {
    await request.post(`/statistic/updateClickCount/${id}`);
    console.log("Statistic 更新成功");
  } catch (err) {
    console.error("Statistic 更新失败:", err);
  }
  try {
    await request.post(`/statistic/updateClickCount/${row.song_id}`);
    console.log("Statistic 更新成功");
  } catch (err) {
    console.error("Statistic 更新失败:", err);
  }
};
onMounted(load);

</script>


<style scoped>

</style>