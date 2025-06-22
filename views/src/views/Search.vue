<template>
  <div class="search">
    <el-input style="width: 500px;" :prefix-icon="Search" v-model="data.find" @keyup.enter="handleSearch"></el-input>
  </div>
  <div class="common-layout">
    <el-container >
      <el-header style="width: 70%;margin: auto">
        <el-menu router :default-active="router.currentRoute.value.path" class="el-menu-demo" mode="horizontal" border="0px">
          <el-menu-item style="width: 200px;cursor: pointer;" index="/home/search/searchsongs">单曲</el-menu-item>
          <el-menu-item style="width: 200px;cursor: pointer;" index="/home/search/searchsinger"> 歌手</el-menu-item>
          <el-menu-item style="width: 200px;cursor: pointer;" index="/home/search/searchsonglist"> 歌单</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        <div >
          <RouterView/>
        </div>
      </el-main>
    </el-container>
  </div>

</template>
<script setup>
import {Search} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {reactive} from "vue";

const data= reactive({
  find:''
})

const handleSearch = () => {
  // 获取 localStorage 里的 user-info，如果为空则初始化为 {}
  let userInfo = JSON.parse(localStorage.getItem('user-info')) || {};

  // 确保 userInfo 是对象
  if (typeof userInfo !== "object" || userInfo === null) {
    userInfo = {}; // 避免 userInfo 变成字符串等非法格式
  }

  // 添加 find 属性
  userInfo.find = data.find;

  // 存回 localStorage
  localStorage.setItem('user-info', JSON.stringify(userInfo));
};
</script>

<style scoped>
:deep(.el-menu-item.is-active) {
  border-bottom: 0px;
  color:black!important;
}

</style>
<style >
.search{
  height: 100px;
  background: linear-gradient(to bottom, #d5d5d5,rgba(220, 220, 220, 20%));
  display: flex;
  align-items: center;
  justify-content: center;
}
.el-menu-demo {
  background-color: #ffffff; /* 浅灰色背景 */
  display: flex;
  justify-content: center;
  align-items: center;
}
/* Menu item hover and active state */
.el-menu-item:hover, .el-sub-menu__title:hover,
.el-menu-item.is-active, .el-sub-menu__title.is-active {
  background-color: rgba(220, 220, 220, 20%) !important; /* 深黑色 */
  color: white; /* 悬停时文字颜色 */
}
:root {
  --el-menu-active-color: var(--el-color-primary);
  --el-menu-text-color: var(--el-text-color-primary);
  --el-menu-hover-text-color: #282c34;
  --el-menu-bg-color: var(--el-fill-color-blank);
  --el-menu-hover-bg-color: var(--el-color-primary-light-9);
  --el-menu-item-height: 56px;
  --el-menu-sub-item-height: calc(var(--el-menu-item-height) - 6px);
  --el-menu-horizontal-height: 60px;
  --el-menu-horizontal-sub-item-height: 36px;
  --el-menu-item-font-size: var(--el-font-size-base);
  --el-menu-item-hover-fill: var(--el-color-primary-light-9);
  --el-menu-border-color: var(--el-border-color);
  --el-menu-base-level-padding: 20px;
  --el-menu-level-padding: 20px;
  --el-menu-icon-width: 24px;
}
.el-menu-item {
  user-select: none; /* 禁止选中文本 */
}
</style>