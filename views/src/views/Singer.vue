<template>
  <div class="demo-collapse" >
    <el-collapse accordion>
      <el-collapse-item name="2">
        <template #title>
          <div class="singer-container">
            <div class="row" v-for="rowIndex in 2" :key="`row-${rowIndex}`">
              <div
                  class="singer-item"
                  v-for="colIndex in 6"
                  :key="`col-${colIndex}`"
                  style="display: flex;flex-direction: column"
              >
                <img :src="getSingerPic(rowIndex, colIndex)" class="singer-avatar">
                <span class="singer-name">{{ getSingerName(rowIndex, colIndex) }}</span>
              </div>
            </div>
          </div>
        </template>
        <div class="extracontainer">
          <div class="row">
            <div class="singer-item" v-for="colIndex in 6" :key="`extra-col-${colIndex}` "style="display: flex;flex-direction: column">
              <img :src="getSingerPic(rowIndex, colIndex)" class="singer-avatar">
              <span class="extra-singer-name">{{ getSingerName(3, colIndex) }}</span>
            </div>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
  <div style="width: 1000px; height: 260px; margin: auto; margin-bottom: 40px; display: flex; flex-direction: column; margin-top: 20px">
  </div>
</template>

<script setup>
import { InfoFilled } from "@element-plus/icons-vue";
import {computed, onMounted, reactive} from "vue";
import request from "@/utils/request.js";
import store from "@/store/store.js";
import { ElMessage } from "element-plus";
import defaultPic from "@/assets/logo.svg";

const data = reactive({
  tableData:[],
  singerData:[],
  total: 0,
  pageNum: 1,
  pageSize: 5,
})


const getSingerPic = (row, col) => {
  const index = (row - 1) * 6 + (col - 1); // 计算在 singerData 里的索引
  return data.singerData[index]?.pic || defaultPic; // 有数据用 singerData，否则用默认图
};

const getSingerName = (row, col) => {
  const index = (row - 1) * 6 + (col - 1); // 计算在 singerData 里的索引
  return data.singerData[index]?.name || '默认'; // 有数据用 singerData，否则用默认图
};

const loadSingers = () => {
  request.get(`singer/selectAll`).then(
      (res) =>{
        if (!res.data || !Array.isArray(res.data)) {
          data.singerData = [];
        } else {
          data.singerData = res.data;
          console.log(data.singerData)
        }
      }).catch((err) => {
    console.error("数据加载失败:", err);
  });
};
onMounted(() => {
  loadSingers();
})
</script>

<style scoped>
.singer-container {
  max-width: 1000px;
  width: 100%;
  margin: auto;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}


.extracontainer {
  max-width: 1000px;
  width: 100%;
  margin: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  top: 344px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  justify-content: center;
}

.singer-avatar {
  background-color: rgba(134, 238, 205, 0.88);
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin: 10px 20px;
}

:deep(.el-collapse-item__wrap) {
  transition: height 0.3s ease-in-out;
}

:deep(.el-collapse-item__header) {
  position: relative;
  border-bottom: 0;
}
:deep(.el-collapse-item__content) {
  height: 540px;
}
.demo-collapse {
  width: 100%;
  display: flex;
  justify-content: center;
  min-height: 430px;
  height: auto;
}
.extra-singer-name{
  position: relative;
  left: 70px;
  top:12px;
}
.el-collapse-item__wrap{
  border-bottom: 0;
}

path {
  fill: currentcolor;
  d: path("@/assets/drowdown.png");
}
:root{
  --el-border-color-lighter: none;
}

:deep(.el-collapse-item__arrow.is-active){
  position: absolute;
  top:610px;
  right:466px;
  transform: rotate(270deg);
}
:deep(.el-collapse-item__arrow){
  position: absolute;
  top:420px;
  right:466px;
  transform: rotate(90deg);
}
.el-collapse {
  --el-collapse-border-color: var(--el-border-color-lighter);
  --el-collapse-header-height: 48px;
  --el-collapse-header-bg-color: var(--el-fill-color-blank);
  --el-collapse-header-text-color: var(--el-text-color-primary);
  --el-collapse-header-font-size: 13px;
  --el-collapse-content-bg-color: var(--el-fill-color-blank);
  --el-collapse-content-font-size: 13px;
  --el-collapse-content-text-color: var(--el-text-color-primary);
  border-bottom:0px;
  border-top: 1px solid var(--el-collapse-border-color);
}
</style>
