<template>
  <div style="width: 1200px; margin: auto;margin-top: 30px;display: flex; flex-wrap: wrap;justify-content: center;">
    <div style="display: flex;margin-left: 30px;margin-bottom: 50px;" v-for="item in data.songListData" :key="item.id" class="song-list-item">
      <img :src="item.pic" alt="Song Image" class="song-img" @click="jumpweb(item.id)" />
      <div >
        <div class="songlisttitle">{{ item.title }}</div>
        <div style="display: flex;width: 200px">
          <div  class="song-creator">{{ item.username || '系统生成' }}</div>
          <div class="songliststyle">{{ item.style}}</div>
        </div>
        <div style="width: 200px;height: 80px" >简介:{{ item.introduction}}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";

// Initialize data
const data = reactive({
  songListData: []
});

// Load song list data
const loadsonglist = () => {
  request.get('/songList/selectAll').then(res => {
    if (res && res.data) {
      data.songListData = res.data;
      const uniqueData = Array.from(new Map(data.songListData.map(item => [item.id, item])).values());
      data.songListData = uniqueData;
      console.log(data.songListData)
    } else {
      console.warn("查询结果为空或格式不正确:", res.data);
      data.songListData = [];
    }
  }).catch(error => {
    console.error("请求失败:", error);
    data.songListData = [];
  });
};

const jumpweb = (id) =>{
  localStorage.setItem("songlistId", id);
  router.push({ path: "/home/songlist" });
}

onMounted(() => {
  loadsonglist();
});
</script>

<style scoped>
.song-img {
  width: 150px;
  height: 150px;
  border-radius: 8px;
  margin-right: 15px;
}


.song-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}
.songlisttitle{
  font-size: 22px;
  font-weight: 400;
}
.song-creator{
  font-size: 15px;
  color: #656262;
  display: flex;
  width: 150px;
}
.songliststyle{
  float: right;
}
</style>
