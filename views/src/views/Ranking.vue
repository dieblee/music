<template>
  <div style="width: 1200px; height: 700px; margin: auto; margin-top: 50px; margin-bottom: 80px; display: flex; justify-content: space-between;">
    <!-- 歌曲排行榜 -->
    <transition name="fade" appear>
      <div class="ranking" style="background-color: #9efdc3;">
        <span class="title">歌曲排行榜</span>
        <div class="list">
          <div v-for="(item, index) in data.songData.slice(0, 6)" :key="index" class="list-item">
            <span class="rank">{{ index + 1 }}</span>
            <img :src="item.songPic" class="list-img"  />
            <div style="display: flex;flex-direction: column;width: 100px;margin-right: 40px">
              <span style="font-family: 'Segoe UI';font-size: 20px" >{{item.songTitle}}</span>
              <span style="font-family: 'Segoe UI';font-size: 12px;font-weight: 20">{{item.singerName}}</span>
            </div>
            <span class="hot">{{ item.click }}</span>
          </div>
        </div>
      </div>
    </transition>

    <!-- 歌单排行榜 -->
    <transition name="fade" appear>
      <div class="ranking">
        <span class="title" style="background-color: #9efdef;">歌单排行榜</span>
        <div class="list">
          <div v-for="(item, index) in data.songListData.slice(0, 6)" :key="index" class="list-item">
            <span class="rank">{{ index + 1 }}</span>
            <img :src="item.songListPic" class="list-img"  />
            <div style="display: flex;flex-direction: column;width: 100px;margin-right: 40px">
              <span style="font-family: 'Segoe UI';font-size: 20px" >{{item.songListTitle}}</span>
              <span style="font-family: 'Segoe UI';font-size: 12px;font-weight: 50">{{item.createName}}</span>
            </div>
            <span class="hot">{{ item.songListStyle}}</span>
          </div>
        </div>
      </div>
    </transition>

    <!-- 歌手排行榜 -->
    <transition name="fade" appear>
      <div class="ranking" style="background-color: #fd9ea6;">
        <span class="title">歌手排行榜</span>
        <div class="list">
          <div v-for="(item, index) in data.singerData.slice(0, 6)" :key="index" class="list-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.singerName }}</span>
            <span class="hot">{{ item.hot }}</span>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import request from "@/utils/request.js";
import { onMounted, reactive } from "vue";
import { ElMessage } from "element-plus";

const data = reactive({
  songData: [],
  songListData: [],
  singerData: [],
});

const loadSong = () => {
  request.get("/statistic/rankSong").then((res) => {
    if (res.code === "200") {
      data.songData = res.data;
      console.log(data.songData)
    } else {
      ElMessage.error(`加载歌曲排行榜失败: ${res.msg || "未知错误"}`);
    }
  }).catch((err) => {
    ElMessage.error(`加载歌曲排行榜失败: ${err.message}`);
  });
};

const loadSongList = () => {
  request.get("/statistic/rankSongList").then((res) => {
    if (res.code === "200") {
      data.songListData = res.data;
      console.log(data.songListData)
    } else {
      ElMessage.error(`加载歌单排行榜失败: ${res.msg || "未知错误"}`);
    }
  }).catch((err) => {
    ElMessage.error(`加载歌单排行榜失败: ${err.message}`);
  });
};

const loadSinger = () => {
  request.get("/statistic/rankSinger").then((res) => {
    if (res.code === "200") {
      data.singerData = res.data;
      console.log(data.singerData);
    } else {
      ElMessage.error(`加载歌手排行榜失败: ${res.msg || "未知错误"}`);
    }
  }).catch((err) => {
    ElMessage.error(`加载歌手排行榜失败: ${err.message}`);
  });
};

onMounted(() => {
  loadSong();
  loadSongList();
  loadSinger();
});
</script>

<style scoped>
.ranking {
  height: 700px;
  width: 350px;
  background-color: #9efdef;
  border-radius: 8px;
  padding-top: 30px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.title {
  font-size: 20px;
  font-weight: 900;
  font-family: system-ui;
  text-align: center;
  display: block;
  padding: 10px 0;
}

.list {
  margin-top: 20px;
  padding: 0 20px;
}

.list-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.rank {
  font-weight: bold;
  color: rgba(19, 224, 192, 0.91);
  width: 30px;

}

.name {
  flex: 1;
  text-align: left;
  padding-left: 10px;
  font-size: 16px;
}

.hot {
  color: #666;
  width: 50px;
  text-align: right;
}
.list-img{
  width: 72px;
  height:72px;
  margin-right: 18px;
  border-radius: 3px;
  position: relative;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 1s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>