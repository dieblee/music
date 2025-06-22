<template>
  <div style="position:relative;top:-30px;padding: 10px">
    <el-table :data="data.songData" stripe style="width: 100%;border-bottom: none" @row-dblclick="changeState">
      <el-table-column prop="name" label="歌曲名称" width="467px" />
      <el-table-column prop="singerName" label="歌手" width="300px" />
      <el-table-column v-if="database.consumerInfo.username === database.owner" label="操作" width="70px">
        <template #default="scope">
          <el-icon
              @click.stop="handleDeleteSong(scope.row.song_id)"
              style="cursor: pointer; color: #999; font-size: 20px;"
          >
            <Close />
          </el-icon>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import {onMounted, reactive} from "vue";
import request from "@/utils/request.js";
import store from "@/store/store.js";
import dayjs from "dayjs";
import { Close } from '@element-plus/icons-vue';
import { ElMessageBox, ElMessage } from 'element-plus'


const data = reactive({
  songData:[],
    }
)

const database = reactive({
  songlistId: JSON.parse(localStorage.getItem("songlistId")) || {}, // 防止 null 时报错
  owner:localStorage.getItem("owner"),
  consumerInfo:JSON.parse(localStorage.getItem("consumerInfo"))
});

const load = () => {
  console.log("当前查询的名称:", database.songlistId);
  request.get("/listsong/selectAll", { params: { song_list_id: database.songlistId } })
      .then((res) => {
        console.log(res.data)
        if (!res.data || !Array.isArray(res.data)) {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.songData = [];
        } else {
          data.songData = res.data;
          console.log("后端返回数据:",  data.songData);
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
      });
};
const handleDeleteSong = async (songId) => {
  try {
    await ElMessageBox.confirm(
        '确定要删除这首歌吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
    );
    let songlistId = localStorage.getItem('songlistId')
    console.log(songId)
    const res = await request.delete(`/listsong/manageSongs/${songlistId}/${songId}`);
    console.log("删除请求响应:", res);  // 监控后端返回值

    if (res ) {  // 确保检查后端返回的状态
      ElMessage.success('删除成功');
      load();  // 重新加载数据
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error);
      ElMessage.error('删除失败');
    }
  }
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


<style scoped>

</style>