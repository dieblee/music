<template>
  <div class="swiper">
    <el-carousel :interval="4000" type="card" height="250px">
      <el-carousel-item v-for="(item, index) in data.swiperList" :loop="true" :key="index">
        <img :src="item.content" />
      </el-carousel-item>
    </el-carousel>
  </div>
  <div class="content" >
    <div style="display: flex">
      <div style="width: 640px;height:492px;margin-right: 40px;display:flex;flex-direction:column;argin-bottom: 18px">
        <div style="width: 640px">
          <p style="font-size: 26px;font-weight: 500;color: #000000;margin-bottom: 25px;margin-top: 0px;display: block">精选歌曲</p>
        </div>
        <div  style="display: flex;flex-direction: row;">
          <div style="margin-right: 30px;">
            <img prop="pic" :src="data.tableData[0]?.pic" style="width:320px;height: 320px;border-radius: 8px" @click="changeState(data.tableData[0]?.url,data.tableData[0]?.name,data.tableData[0]?.pic,data.tableData[0]?.id)">
            <p>{{ data.tableData[0]?.introduction || '歌曲简介' }}</p>
          </div>
          <div style="display: flex;flex-direction: column">
            <div style="display: flex;flex-direction: row;" >
              <div>
                <div style="height: 130px;width: 130px;margin-right: 30px">
                  <img :src="data.tableData[1]?.pic" style="height: 130px;width: 130px;border-radius: 8px" @click="changeState(data.tableData[1]?.url,data.tableData[1]?.name,data.tableData[1]?.pic,data.tableData[1]?.id)">
                </div>
                <p style="width: 130px;height: 40px;margin-top:6px;margin-bottom: 0px">{{ data.tableData[1]?.introduction || '歌曲简介' }}</p>
              </div>
              <div>
                <div style="height: 130px;width: 130px;margin-right: 30px">
                  <img :src="data.tableData[2]?.pic" style="height: 130px;width: 130px;border-radius: 8px" @click="changeState(data.tableData[2]?.url,data.tableData[2]?.name,data.tableData[2]?.pic,data.tableData[2]?.id)">
                </div>
                <p style="width: 130px;height: 40px;margin-top:6px;margin-bottom: 0px">{{ data.tableData[2]?.introduction || '歌曲简介' }}</p>
              </div>
            </div>
            <div style="display: flex;flex-direction: row;margin-top: 15px">
              <div >
                <div style="height: 130px;width: 130px;margin-right: 30px">
                  <img :src="data.tableData[3]?.pic" style="height: 130px;width: 130px;border-radius: 8px" @click="changeState(data.tableData[3]?.url,data.tableData[3]?.name,data.tableData[3]?.pic,data.tableData[3]?.id)">
                </div>
                <p style="width:130px;height: 40px;margin-top: 6px;">{{ data.tableData[3]?.introduction || '歌曲简介' }}</p>
              </div>
              <div>
                <div style="height: 130px;width: 130px">
                  <img :src="data.tableData[4]?.pic" style="height: 130px;width: 130px;border-radius: 8px" @click="changeState(data.tableData[4]?.url,data.tableData[4]?.name,data.tableData[4]?.pic,data.tableData[4]?.id)">
                </div>
                <p style="width:130px;  height:40px;margin-top: 6px;">{{ data.tableData[4]?.introduction || '歌曲简介' }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div style="width: 320px;height: 492px;margin-bottom: 18px;display: inline-block;margin-bottom: 0px">
        <div class="singlist" >
          <div style="display: flex">
            <p style="font-size: 26px;font-weight: 500;color: #000000;margin-bottom: 20px;margin-top: 0px">热门榜单</p>
            <span style="margin-left: 160px" > <router-link to="/home/songlists">更多>></router-link></span>
          </div>
          <div style="display: flex;flex-direction: column" v-for="song in data.songList.slice(0, 3)" :key="song.id">
            <div style="display: flex; flex-direction: column" >
              <div  style="display: flex; margin-top: 16px" :data-id="song.id">
                <img :src="song.pic" style="width: 110px; height: 110px; border-radius: 8px" @click="jumpweb(song.id)" />
                <div style="margin-left: 24px; width: 186px; height: 110px; display: flex; text-align: center; align-items: center">
                  <ul style="padding-left: 0px; align-items: center; padding-right: 12px; margin: 0">
                    <li class="container"><span class="index">1.</span><span style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">{{ data.listSong[song.id]?.[0]?.name || '暂无歌曲' }}</span></li>
                    <li class="container"><span class="index">2.</span><span style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">{{ data.listSong[song.id]?.[1]?.name || '暂无歌曲' }}</span></li>
                    <li class="container"><span class="index">3.</span><span style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">{{ data.listSong[song.id]?.[2]?.name || '暂无歌曲' }}</span></li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div style="background-color: white;width: 1000px;height: 400px;display:flex;flex-direction: column  ;margin: auto">
    <el-container>
      <el-header style="display: flex;height: 34px;">
        <p style="font-size: 26px;position: relative;top:-30px;margin-right: 30px; font-weight: 500; color: #000000; margin-bottom: 0; display: inline-block;">
          新歌首发
        </p>
        <div style="display: flex; align-items: center;">
          <el-menu default-active="chinese" class="el-menu-demo" mode="horizontal" border="0px" @select="handleMenuSelect">
            <el-menu-item index="web"><span style="font-size: 18px">站内</span></el-menu-item>
            <el-menu-item index="chinese"><span style="font-size: 18px">华语</span></el-menu-item>
            <el-menu-item index="european"><span style="font-size: 18px">欧美</span></el-menu-item>
            <el-menu-item index="korean"><span style="font-size: 18px">韩流</span></el-menu-item>
          </el-menu>
          <div class="pagination" style="margin-left: 20px;">
            <el-pagination
                :current-page="currentPage"
                :page-size="pageSize"
                :total="totalItems"
                layout="prev, slot, next"
                @current-change="handleCurrentChange"
            >
              <span style="display: block; text-align: center; margin-top: 10px; position: relative; top: -6px;">{{ currentPage }} / {{ Math.ceil(totalItems / pageSize) }}</span>
            </el-pagination>
          </div>
        </div>
      </el-header>
      <el-main>
        <!-- 歌曲列表 -->
        <div>
          <ul style="display: flex; flex-wrap: wrap; padding: 0;">
            <li style="margin-bottom: 20px" v-for="song in adsongList" :key="song.id" class="song">
              <div>
                <img :src="song.pic" style="width: 90px; height: 90px;border-radius: 8px" />
              </div>
              <div style="display: flex; flex-direction: row;">
                <div style="display: flex; flex-direction: column; margin-top: 28px; margin-left: 16px">
                  <span style="font-size: 14px;width: 100px;white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">{{ song.name }}</span>
                  <span style="font-size: 12px; font-weight: 400; color: rgba(0, 0, 0, 0.7);">{{ song.singerName }}</span>
                </div>
                <div style="margin-left: 40px; margin-top: 35px; display: flex; flex-direction: row">
                  <img src="../assets/play.png" @click="changeState(song.url,song.name,song.pic,song.id)" style="height: 18px; width: 18px; margin-right: 10px">
<!--                  <img src="@/assets/download.png" style="height: 20px; width: 20px">-->
                </div>
              </div>
            </li>
          </ul>
        </div>
      </el-main>
    </el-container>
  </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from "vue"; // 确保路径正确
import 'element-plus/dist/index.css';
import router from "@/router/index.js";;
import request from "@/utils/request.js";
import store from "@/store/store.js";
import dayjs from "dayjs";

const activeMenu = ref("chinese");
const adsongList = ref([])
const data = reactive({
  tableData:[],
  swiperList:[],
  songList:[],
  listSong:{},
  adsongsData:{},
})


// 分页相关
const currentPage = ref(1);
const pageSize = 6;
const totalItems = ref(0);
const handleMenuSelect = async (index) => {
  activeMenu.value = index;
  localStorage.setItem('address', index);  // 保存当前选中的地区
  currentPage.value = 1;  // 重新选择时从第一页开始
  await loadadSongs(index); // 加载地区歌曲数据
  updateadSongs(index); // 更新歌曲列表
};

const updateadSongs = (category) => {
  const songs = data.adsongsData[category] || [];
  totalItems.value = songs.length;
  adsongList.value = songs.slice(0, pageSize); // 获取当前页的数据
};
const handleCurrentChange = (page) => {
  currentPage.value = page;
  const start = (page - 1) * pageSize;
  const end = start + pageSize;
  adsongList.value = data.adsongsData[activeMenu.value].slice(start, end); // 更新分页数据
};
const loadadSongs = async (category) => {
  const address = localStorage.getItem('address');
  console.log('当前选择的地区:', address); // 打印当前地区
  if (!address) {
    console.error("地区未选择或存储");
    return;
  }
  try {
    const res = await request.get(`song/selectByAddress/${address}`);
    console.log("返回的数据:", res);
    data.adsongsData[category] = res.data || [];
    totalItems.value = data.adsongsData[category].length;
    updateadSongs(category);
  } catch (err) {
    console.error("获取歌曲失败:", err);
  }
};
const loadSongs = () => {
  request.get("/song/selectAll")
      .then((res) => {
        if (!res.data || !Array.isArray(res.data)) {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.tableData = [];
        } else {
          data.tableData = res.data;
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
      });
};
const jumpweb = (id) =>{
  localStorage.setItem("songlistId", id);
  router.push({ path: "/home/songlist" });
}

const changeState = async (url, name, pic, id) => {
  store.dispatch('updatePlayState', { isPlay: true, url, name, pic });
  store.dispatch('updatePlaying', id);
  //store.commit('setListOfSongs', { url, id });
  const currentDate = dayjs().format("YYYY-MM-DD");
  try {
    await incrementRedisClick(id);

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

async function incrementRedisClick(songId) {
  try {
    await request.post('/redis/incrementClick', { songId });
    console.log("Redis 点击增加成功");
  } catch (err) {
    console.error("Redis 增加点击失败:", err);
    throw err;
  }
}

const loadSwipers = () => {
  request.get("/swiper/selectAll")
      .then((res) => {
        if (res?.data && Array.isArray(res.data)) {
          data.swiperList = res.data;
        } else {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.swiperList = []; // 确保 swiperList 仍然是数组
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
        data.swiperList = []; // 请求失败时，也设置为空数组，防止页面崩溃
      });
};
const loadSongList = async () => {
  try {
    const res = await request.get("/songList/selectAll");
    if (res?.data && Array.isArray(res.data)) {
      data.songList = res.data;
      const uniqueData = Array.from(new Map(data.songList.map(item => [item.id, item])).values());
      data.songList = uniqueData;
      await loadAllListSongs(); // 确保在获取 songList 后再加载歌曲
    } else {
      console.warn("查询结果为空或格式不正确:", res.data);
      data.songList = [];
    }
  } catch (err) {
    console.error("数据加载失败:", err);
    data.songList = [];
  }
};
const loadlistSongs = async (songListId) => {
  try {
    const res = await request.get("/listsong/selectAll", { params: { song_list_id: songListId } });
    if (res?.data && Array.isArray(res.data)) {
      data.listSong = { ...data.listSong, [songListId]: res.data }; // 确保响应式更新
    } else {
      data.listSong = { ...data.listSong, [songListId]: [] };
    }
  } catch (error) {
    data.listSong = { ...data.listSong, [songListId]: [] };
  }
};

const loadAllListSongs = async () => {
  if (data.songList.length === 0) {
    console.warn("没有可用的歌单，跳过加载歌曲");
    return;
  }
  await Promise.all(data.songList.map(songList => loadlistSongs(songList.id)));
};
onMounted(async () => {
  handleMenuSelect(activeMenu.value);
  await loadSongs();
  await loadSwipers();
  await loadSongList();
  await loadadSongs();// 确保 songList 先加载完成
  await loadAllListSongs(); // 然后再去加载 listSong
});

// 处理页码变化
</script>



<style scoped>
.swiper {
  width: 90%;
  margin: auto;
  margin-top: 40px;
  margin-bottom: 40px;
}

.swiper img {
  width: 100%; /* Ensure the image fills the container */
  height: auto;
  object-fit: cover; /* Ensures the aspect ratio is maintained */
}

.content {
  margin: auto;
  width: 1000px;
  height: 1000px;
  margin-bottom: 50px;
}
p{
  font-size: 14px;
  font-weight: 400;
}
* {
  font-family: \5FAE\8F6F\96C5\9ED1, simsun, sans-serif;
}
.container{
  display: flex;
  align-items: center;
  margin-left: 3px;
  font-size: 14px;
  height: 32px;
}
.index{
  font-weight: bold;
  margin-right: 3px;
}
.example-pagination-block + .example-pagination-block {
  margin-top: 10px;
}
.example-pagination-block .example-demonstration {
  margin-bottom: 16px;
}
.el-menu-demo{
  width: 600px;
  border-bottom: 0px;
}

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
.song {
  display: flex;
  flex-direction: row;
  width: 320px;
  height: 90px;
  list-style: none;
  padding--right: 10px;
}
</style>