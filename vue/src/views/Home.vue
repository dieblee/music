<template>
  <div>
    <div style="height: 60px;background: linear-gradient(to right,#1db3f5,#084eff);display: flex;padding-left:20px;align-items: center" >
      <img src="../assets/音乐.png" alt="" style="width:40px;height: 40px;margin-right: 8px">
      <div style="width: 200px;display: flex;align-items: center;font-size: 20px;color:white">后台管理系统</div>
      <div style="flex:1"></div>
      <div style="width:fit-content;display: flex;align-items: center;padding-right: 10px">
        <img :src="data.user.avator" alt="" style="width:40px;height: 40px">
        <el-dropdown  @command="handleCommand" trigger="click">
        <span class="el-dropdown-link" style="color: white;margin:0 15px 0 8px;font-weight: bold">{{data.user.name}}</span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item  command="/home/modify" >
                修改密码
              </el-dropdown-item>
              <el-dropdown-item  command="/home/person" >
                修改个人信息
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div style="display: flex">
      <div style="width: 200px;border-right: 1px solid #ddd;height:calc(100vh - 80px)">
        <el-menu router :default-active="router.currentRoute.value.path" :default-openeds="['1']">
          <el-menu-item>
            <template #title>
              <el-icon><User /></el-icon>
              <el-menu-item style="margin-left: 0px;padding: 0px" index="/home/consumer">用户信息</el-menu-item>
            </template>
          </el-menu-item>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><PieChart /></el-icon>
              <span>音乐信息管理</span>
            </template>
            <el-menu-item index="/home/song" >歌曲信息</el-menu-item>
            <el-menu-item index="/home/songlist">歌单信息</el-menu-item>
            <el-menu-item index="/home/singer">歌手信息</el-menu-item>
            <el-menu-item index="/home/message">消息信息</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/home/swiper">
            <el-icon><Notification /></el-icon>
            轮播图管理
          </el-menu-item>
          <el-menu-item index="/home/data">
            <el-icon><DataAnalysis /></el-icon>
            数据统计
          </el-menu-item>
          <el-menu-item index="/login"  @click="logout" >
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </el-menu-item>
        </el-menu>
      </div>
      <div style="flex: 1;width: 0;background-color: #f2f5fa;padding-top: 10px;padding-left: 10px">
        <RouterView/>
      </div>
    </div>

  </div>
</template>



<script setup>

const handleCommand = (command) => {
  router.push(command);
};

import { User,House,DataAnalysis,SwitchButton,UserFilled,PieChart,Notification } from '@element-plus/icons-vue';
import router from "@/router/index.js";
import {computed, reactive} from "vue";  // 导入 User 图标
const data=reactive({
  user:JSON.parse(localStorage.getItem('user-info'))
})

const logout = () =>{
  localStorage.removeItem('user-info')
  location.href = '/login'

}

const isEmp = computed(() => {
  return data.user.role === 'EMP'; // 如果 role 是 'EMP'，返回 true
});
</script>
<style>
:deep(.el-menu .is-active){
  background-color: #adc5fd !important;
}


</style>