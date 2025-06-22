<template>
  <div style="width: 700px;margin: auto;display: flex;position: relative;left: 30px">
    <el-table :data="data.tableData" stripe style="width: 100%">
      <el-table-column prop="title" label="歌单名称" width="120px" />
      <el-table-column prop="introduction" label="歌曲介绍" width="200"/>
      <!-- 新增 pic 和 create_id 列 -->
      <el-table-column prop="pic" label="图片" width="120px">
        <template #default="scope">
          <el-image :src="scope.row.pic" style="width: 50px; height: 50px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="username" label="创建人" width="100" />
      <el-table-column prop="style" label="歌单风格" width="100" />
    </el-table>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js"; // 确保 request 变量已正确导入

const data = reactive({
  tableData: [],
});

const database = reactive({
  user: JSON.parse(localStorage.getItem("user-info")) || {}, // 防止 null 时报错
});

const load = () => {
  console.log("当前查询的名称:", database.user.find); // 查看查询名称
  if (!database.user.find) {
    console.error("user-info 中未找到 find 字段");
    return;
  }
  request.get(`/songList/selectByName/${database.user.find}`)
      .then((res) => {
        if (res.data) {
          console.log("返回的数据:", res.data); // 查看返回的数据
          // 如果返回的数据是单个对象而不是数组，处理为数组
          if (!Array.isArray(res.data)) {
            console.log("数据不是数组，包装为数组:", [res.data]);
            data.tableData = [res.data]; // 如果是单个对象，包装成数组
          } else {
            data.tableData = res.data; // 直接赋值
          }
        } else {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.tableData = [];
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
        data.tableData = []; // 请求失败时清空数据
      });
};

onMounted(load);
</script>

<style>
</style>
