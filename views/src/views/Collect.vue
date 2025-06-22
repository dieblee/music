<template>
  <div style="width:60%;height:700px;margin: auto;margin-top: 60px;margin-bottom: 40px">
    <div style="display:flex;flex-direction:row;">
      <div>
        <img :src="defaultPic" style=";border-radius: 8px;width: 150px;height: 150px;margin-left: 30px">
      </div>
      <div style="display: flex;flex-direction: column">
        <span style="font-size: 21px;font-weight: 600;margin-left: 40px">收藏单曲</span>
        <div style="display: flex;flex-direction: row;width: 600px">
          <div style="margin-left: 40px;margin-top: 16px">
            <img style="width:20px;height:20px;border-radius: 50%;" :src="user.consumerInfo.avator">
          </div>
          <span style="margin-top: 15px;font-size: 15px;color: rgba(66,66,66,0.71);margin-left: 8px">{{user.consumerInfo.username}}</span>
        </div>
        <div class="button">
          <el-button @click="playAll">播放全部</el-button>
        </div>
      </div>
    </div>
    <el-container>
      <el-header>
        <el-menu router :default-active="router.currentRoute.value.path" class="el-menu-demo" mode="horizontal">
          <el-menu-item index="/home/collect/collectsong">歌曲</el-menu-item>
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
import defaultPic from "@/assets/music.png";

const collectMode = ref('dislike');

const data = reactive({
  tableData: [],
});

const user = reactive({
  consumerInfo: JSON.parse(localStorage.getItem("consumerInfo")) || {}, // 防止 null 时报错
});


const checkCollectStatus = async () => {
  try {
    const consumerInfo = JSON.parse(localStorage.getItem('consumerInfo'));
    if (!consumerInfo) return;
    const res = await request.get(`/collect/selectCollectSongList/${user.songlistId}/${consumerInfo.id}`);
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

const playAll = async () => {
  const songListId = user.songlistId;
  console.log("获取到的 song_list_id:", songListId);
  request.get("/listsong/selectAll", { params: { song_list_id: songListId } })
      .then((response) => {
        if (!response.data || !Array.isArray(response.data) || response.data.length === 0) {
          console.warn("没有找到匹配的歌曲列表:", response.data);
          return;
        }
        console.log(response.data)
        const songs = response.data.map(so => ({ pic:so.pic,url: so.url, name: so.name, id: so.song_id }));
        console.log("获取到的歌曲列表:", songs);
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
</style>
<style>
.el-menu el-menu--horizontal el-menu-demo{
  height: 30px;
}
</style>
