<template>
  <div class="playBar">
    <div style="display: flex;flex-direction: row;">
      <img :src="pic" style="width: 30px;height: 30px;border-radius: 8px" />
      <span style="color: white;font-size: 16px;font-weight: 20;line-height: 30px;margin-left: 14px;margin-right: 28px">{{name}}</span>
    </div>
    <div class="component">
      <!-- 上一首 -->
      <div class="icon" @click="playPrevious">
        <img src="@/assets/pre.png" />
      </div>

      <!-- 播放/暂停 -->
      <div class="icon play-btn" @click="togglePlay">
        <img :src="isPlaying ? pauseIcon : playIcon" />
      </div>

      <!-- 下一首 -->
      <div class="icon" @click="playNext">
        <img src="@/assets/next.png" />
      </div>

      <!-- 进度条 -->
      <div class="progress-container">
        <span class="current-time">{{ formatTime(currentTime) }}</span>
        <input
            type="range"
            ref="progressBar"
            min="0"
            :max="duration"
            v-model="currentTime"
            @input="handleProgressChange"
            @change="syncProgress"
            class="progress-bar"
        />
        <span class="total-time">{{ formatTime(duration) }}</span>
      </div>

      <!-- 播放模式 -->
      <div class="icon" @click="togglePlayMode">
        <img :src="playModeIcon" alt="logo" style="width: 20px; height: 20px" />
      </div>

      <!-- 收藏 -->
      <div class="item-collect" @click="toggleCollect">
        <img :src="collectModeIcon" style="width: 20px; height: 20px" />
      </div>

      <div class="item-collect" >
        <el-dropdown trigger="click">
          <img :src="addIcon" style="width: 20px; height: 20px"  />
          <template #dropdown>
            <el-dropdown-menu >
              <el-dropdown-item
                  v-for="item in data.songListData"
                  :key="item.id"
                  :icon="Plus"
                  @click="toggleAdd(item.id)"
              >
                {{ item.title }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 音频播放控件 -->
    <audio
        ref="audioPlayer"
        :src="url"
        @canplay="startPlay"
        @ended="handleTrackEnd"
        @timeupdate="updateProgress"
    ></audio>
  </div>
</template>

<script setup>
import {computed, ref, onMounted, watch, reactive} from 'vue';
import { useStore } from 'vuex';
import playIcon from '@/assets/play3.png';
import pauseIcon from '@/assets/pause1.png';
import addIcon from '@/assets/add.png'
import orderIcon from '@/assets/single1.png';
import randomIcon from '@/assets/random.png';
import singleIcon from '@/assets/single.png';
import likeIcon from '@/assets/like.png';
import dislikeIcon from '@/assets/dislike.png';
import request from "@/utils/request.js";
import defaultCover from '@/assets/defaultCover.png';
import {Plus } from '@element-plus/icons-vue'

const store = useStore();
const audioPlayer = ref(null); // 修改为audioPlayer避免命名冲突
const progressBar = ref(null);

const isPlay = computed(() => store.getters.isPlay);
const url = computed(() => store.getters.url);
const name = computed(() => store.getters.name);
const pic = computed(() => store.getters.pic || defaultCover);

const isPlaying = ref(false);
const currentTime = ref(0);
const duration = ref(0);
const playMode = ref('order');
const collectMode = ref('dislike');
const playing = computed(() => store.getters.playing);
const data = reactive({
  songListData:[],
})

const consumerInfo = JSON.parse(localStorage.getItem('consumerInfo'));

// 检查收藏状态
const checkCollectStatus = async () => {
  try {
    if (!consumerInfo) return;
    const res = await request.get(`/collect/selectCollectSong/${playing.value}/${consumerInfo.id}`);
    collectMode.value = res.data ? 'like' : 'dislike';
  } catch (error) {
    console.error("获取收藏状态失败:", error);
  }
};

watch(playing, (newVal) => {
  if (newVal) {
    checkCollectStatus();
  }
});

const loadSongLists = () => {
  //const singerId = user.consumerInfo.id;
  if (!consumerInfo) {
    console.warn('用户信息未加载，无法获取歌单');
    return;
  }
  request.get(`songList/selectByCreateId/${consumerInfo.id}`)
    .then((res) => {
    if (Array.isArray(res.data)) {
      data.songListData = res.data;
      console.log(data.songListData)
    } else {
      console.warn("查询结果为空或格式不正确:", res.data);
      data.songListData = [];
    }
  })
  .catch((err) => console.error("数据加载失败:", err));
}

const toggleAdd = (id) => {
  let playing = localStorage.getItem('playing')
  request.post('listsong/add', {
    song_id: playing,
    song_list_id: id
  }).then(res => {
    console.log(res)
  })
}


const toggleCollect = async () => {
  try {
    if (!consumerInfo) {
      alert('请先登录后再操作!');
      return;
    }
    if (collectMode.value === 'dislike') {
      await request.post('/collect/add', {
        userId: consumerInfo.id,
        songId: playing.value,
        createTime: new Date().toISOString(),
      });
      collectMode.value = 'like';
    } else {
      await request.delete(`/collect/removeBySongId/${playing.value}/${consumerInfo.id}`);
      collectMode.value = 'dislike';
    }
  } catch (error) {
    console.error("收藏操作失败:", error);
  }
};

// 播放模式图标
const playModeIcon = computed(() => {
  switch (playMode.value) {
    case 'one': return orderIcon;
    case 'random': return randomIcon;
    case 'single': return singleIcon;
    default: return singleIcon;
  }
});

const collectModeIcon = computed(() => {
  return collectMode.value === 'like' ? likeIcon : dislikeIcon;
});

// 播放/暂停
const togglePlay = async () => {
  if (!audioPlayer.value) return;
  try {
    if (isPlaying.value) {
      await audioPlayer.value.pause();
    } else {
      await audioPlayer.value.play();
    }
    isPlaying.value = !isPlaying.value;
  } catch (error) {
    console.error("播放控制失败:", error);
  }
};

// 播放下一首
const playNext = () => {
  const songList = store.state.listOfSongs || [];
  const currentIndex = store.state.listOfSongs.findIndex(song => song.id === store.state.playing);
  if (songList.length === 0 || currentIndex === -1) return;
  if (playMode.value === 'random') {
    let randomIndex;
    do {
      randomIndex = Math.floor(Math.random() * songList.length);
    } while (randomIndex === currentIndex && songList.length > 1);
    store.dispatch('playSongAtIndex', randomIndex);
  } else if (playMode.value === 'one') {
    audioPlayer.value.currentTime = 0;
    audioPlayer.value.play();
  } else {
    store.dispatch('playNextSong');
  }
};


// 播放上一首
const playPrevious = () => {
  const songList = store.state.listOfSongs || [];
  const currentIndex = store.state.listOfSongs.findIndex(song => song.id === store.state.playing);
  if (songList.length === 0 || currentIndex === -1) return;
  if (playMode.value === 'random') {
    let randomIndex;
    do {
      randomIndex = Math.floor(Math.random() * songList.length);
    } while (randomIndex === currentIndex && songList.length > 1);
    store.dispatch('playSongAtIndex', randomIndex);
  } else if (playMode.value === 'one') {
    audioPlayer.value.currentTime = 0;
    audioPlayer.value.play();
  } else {
    store.dispatch('playPreviousSong');
  }
};

// 切换播放模式
const togglePlayMode = () => {
  const modes = ['one', 'random', 'single'];
  const currentIndex = modes.indexOf(playMode.value);
  playMode.value = modes[(currentIndex + 1) % modes.length];
};

// 处理进度条变化
const handleProgressChange = (e) => {
  if (audioPlayer.value) {
    currentTime.value = e.target.valueAsNumber;
  }
};
const syncProgress = () => {
  if (audioPlayer.value) {
    audioPlayer.value.currentTime = currentTime.value;
  }
};
// 处理播放结束
const handleTrackEnd = () => {
  if (playMode.value === 'one') {
    playNext();
  } else if (playMode.value === 'random') {
    const randomIndex = Math.floor(Math.random() * store.getters.songList.length);
    store.dispatch('playSongAtIndex', randomIndex);
  } else if (playMode.value === 'single') {
    audioPlayer.value.currentTime = 0;
    audioPlayer.value.play();
  }
};

// 更新进度
const updateProgress = () => {
  if (audioPlayer.value) {
    currentTime.value = audioPlayer.value.currentTime;
    duration.value = audioPlayer.value.duration ;
  }
};

// 开始播放
const startPlay = () => {
  if (audioPlayer.value) {
    duration.value = audioPlayer.value.duration;
    audioPlayer.value.play().then(() => {
      isPlaying.value = true;
    }).catch(error => {
      console.error("播放失败:", error);
    });
  }
};

// 格式化时间
const formatTime = (time) => {
  if (isNaN(time)) return '0:00';
  const minutes = Math.floor(time / 60);
  const seconds = Math.floor(time % 60);
  return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
};

onMounted(() => {
  loadSongLists();
  checkCollectStatus();
});
</script>

<style scoped>
/* 播放栏组件 */
.component {
  display: flex;
  align-items: center;
  justify-content: space-around;
  width: 90%;
  max-width: 600px;
}

/* 按钮样式 */
.icon img {
  width: 20px;
  height: 20px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.icon img:active {
  transform: scale(0.9);
}

/* 播放/暂停按钮加大 */
.play-btn img {
  width: 30px;
  height: 30px;
}

/* 进度条容器 */
.progress-container {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 60%;
}

/* 进度条 */
.progress-bar {
  flex: 1;
  height: 4px;
  appearance: none;
  background: #ddd;
  border-radius: 5px;
  cursor: pointer;
}

.progress-bar::-webkit-slider-thumb {
  appearance: none;
  width: 10px;
  height: 10px;
  background: white;
  border-radius: 50%;
  cursor: pointer;
}

.current-time, .total-time {
  font-size: 12px;
  color: white;
}

.playBar {
  position: fixed;
  bottom: 0;
  left: 8px;
  right: 8px;
  padding: 10px;
  padding-right: 20px;
  width: 97%;
  background-color: #424242;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

img {
  display: block !important;
  visibility: visible !important;
}
</style>