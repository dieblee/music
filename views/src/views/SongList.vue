<template>
  <div style="width:60%;height:700px;margin: auto;margin-top: 60px;margin-bottom: 40px">
    <div style="display:flex;flex-direction:row;">
      <div>
        <img :src="data.tableData[0]?.pic" style=";border-radius: 8px;width: 150px;height: 150px;margin-left: 30px">
      </div>
      <div style="display: flex;flex-direction: column">
        <span style="font-size: 21px;font-weight: 600;margin-left: 40px">{{data.tableData[0]?.title}}</span>
        <div style="display: flex;flex-direction: row;width: 600px">
          <div style="margin-left: 40px;margin-top: 16px">
            <img style="width:20px;height:20px;border-radius: 50%;" :src="data.tableData[0]?.avator || defaultAvatar"  @click.stop="changeStatue(data.tableData[0]?.createId)" >
          </div>
          <span style="margin-top: 15px;font-size: 15px;color: rgba(66,66,66,0.71);margin-left: 8px">{{ data.tableData[0]?.username || '管理员' }}</span>
        </div>
        <div class="button">
          <el-button @click="playAll">播放全部</el-button>
        </div>
        <div
            v-if="data.tableData[0]?.username !== user.consumerInfo.username"
            style="background-color: black; position: absolute; top: 246px; right: 320px"
            @click="toggleCollect"
        >
          <img :src="collectModeIcon" style="width: 20px; height: 20px" />
        </div>
      </div>
    </div>
    <el-container>
      <el-header>
        <el-menu router :default-active="router.currentRoute.value.path" class="el-menu-demo" mode="horizontal">
          <el-menu-item @click="goToSongList">歌曲</el-menu-item>
          <el-menu-item index="/home/songlist/songlistcontent">评论</el-menu-item>
        </el-menu>
      </el-header>
      <el-main style="width: 900px">
        <RouterView/>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import {computed, onMounted, reactive, ref} from "vue";
import request from "@/utils/request.js";
import store from "@/store/store.js";
import dislikeIcon from "@/assets/dislike.png";
import likeIcon from "@/assets/like.png";
import dayjs from "dayjs";
import { ElMessage } from "element-plus";
import defaultAvatar from "@/assets/logo.svg";

const collectMode = ref('dislike');

const data = reactive({
  tableData: [],
});

const user = reactive({
  songlistId: JSON.parse(localStorage.getItem("songlistId")) || {},
  consumerInfo:JSON.parse(localStorage.getItem("consumerInfo")) || {},// 防止 null 时报错
});


const checkCollectStatus = async () => {
  try {
    const consumerInfo = JSON.parse(localStorage.getItem('consumerInfo'));
    if (!consumerInfo) return;
    const res = await request.get(`/collect/selectCollectSongList/${user.songlistId}/${consumerInfo.id}`);
    console.log(res)
    collectMode.value = res.data ? 'like' : 'dislike';
  } catch (error) {
    console.error("获取收藏状态失败:", error);
  }
};

const collectModeIcon = computed(() => {
  switch (collectMode.value) {
    case 'dislike': return dislikeIcon;
    case 'like': return likeIcon;
    default: return dislikeIcon;
  }
});

const goToSongList = () => {
  const owner = data.tableData[0]?.username;
  if (owner) {
    localStorage.setItem("owner", owner); // 先存入 localStorage
    router.push({
      path: "/home/songlist/songlistselect",
    });
  } else {
    localStorage.setItem("owner", "管理员");
    router.push({
      path: "/home/songlist/songlistselect",
    });
  }
};

const toggleCollect = async () => {
  try {
    const consumerInfo = JSON.parse(localStorage.getItem('consumerInfo'));
    if (!consumerInfo) {
      alert('请先登录后再操作!');
      return;
    }
    if (collectMode.value === 'dislike') {
      await request.post('/collect/add', {
        userId: consumerInfo.id,
        songListId: user.songlistId,
        createTime: new Date().toISOString(),
      });
      collectMode.value = 'like';
    } else {
      await request.delete(`/collect/removeBySongListId/${user.songlistId}/${consumerInfo.id}`);
      collectMode.value = 'dislike';
    }
  } catch (error) {
    console.error("收藏操作失败:", error);
  }
};

const load = () => {
  console.log("当前查询的名称:", user.songlistId);
  // Use `params` to pass the query parameter
  request.get("/songList/selectAll", { params: { id: user.songlistId } })
      .then((res) => {
        console.log(res.data);
        if (!res.data || !Array.isArray(res.data) || res.data.length === 0) {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.tableData = {};  // Set empty object if data is not valid or empty
        } else {
          data.tableData = res.data;
          console.log(data.tableData);
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
      });
};

const changeStatue = (id) => {
  const consumerInfo = JSON.parse(localStorage.getItem('consumerInfo'));
  router.push(`/chat/${id}`);
  request.post("/statue/add", {
    sendId: consumerInfo.id,
    receiveId: id,
    statue: 'open'
  }).then(res => {
    ElMessage.success("打招呼成功！");
  }).catch(() => {
    ElMessage.error("打招呼失败！");
  })
};

const playAll = async () => {
  const songListId = user.songlistId;
  console.log("获取到的 song_list_id:", songListId);
  const currentDate = dayjs().format("YYYY-MM-DD");
  request.get("/listsong/selectAll", { params: { song_list_id: songListId } })
      .then((response) => {
        if (!response.data || !Array.isArray(response.data) || response.data.length === 0) {
          console.warn("没有找到匹配的歌曲列表:", response.data);
          return;
        }
        console.log(response.data)
        const songs = response.data.map(so => ({ pic:so.pic,url: so.url, name: so.name, id: so.song_id }));
        store.commit('setListOfSongs', songs);
        if (songs.length > 0) {
          store.commit('setPlaying', songs[0].song_id);
          console.log(songs[0].id);
          localStorage.setItem('playing', songs[0].song_id);
          store.dispatch('updatePlayState', { isPlay: true, url: songs[0].url, name:songs[0].name, pic:songs[0].pic });
        }
      })
      .catch((err) => {
        console.error("查询歌曲列表失败:", err);
      });
  try {
    const response = await request.get(`/statistic/getslClickDate/${songListId}`);
    if (response.clickDate) {
      const oldClick = response.clickDate;
      if (oldClick) {
        if (currentDate !== oldClick) {
          await request.post(`/statistic/updateslOldClickCount/${songListId}`);
        } else {
          await request.post(`/statistic/updateslClickCount/${songListId}`)
        }
      } else {
        console.error("获取点击日期失败");
      }
    } else {
      await request.post(`/statistic/add`, {
        songListId: songListId,
        date: currentDate,
        click: 1,
      });
      console.log("添加点击统计成功");
    }
  } catch (err) {
    console.error("Statistic 更新失败:", err);
  }
};

onMounted(() => {
  checkCollectStatus();
  load();
});

</script>
<style scoped>

::v-deep(.el-menu-item) {
  background-color: transparent !important;
}

::v-deep(.el-menu-item:hover) {
  background-color: transparent !important;
}

::v-deep(.el-menu-item.is-active) {
  background-color: transparent !important;
}
::v-deep(.el-menu--horizontal .el-menu-item.is-active) {
  border-bottom: none !important;
}
.el-menu-demo{
  width: 600px;
  border-bottom: 0px;
}
.button{
  position: relative;
  top:46px;
  margin-left: 30px;
}
.el-menu el-menu--horizontal el-menu-demo{
  height: 30px;
}
</style>
<style>
</style>
