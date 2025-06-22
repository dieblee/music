<template>
  <div style="width: 700px;margin: auto;display: flex;position: relative;left: 30px">
  <el-table :data="data.tableData" stripe style="width: 100%">
    <el-table-column prop="id" label="ID" width="100" />
    <el-table-column prop="name" label="歌手姓名" width="120" />
    <el-table-column prop="pic" label="歌手" width="140">
    <template #default="scope">
      <el-image
          style="width: 50px; height: 50px;"
          :src="scope.row.pic"
          fit="cover">
      </el-image>
    </template>
      </el-table-column>
    <el-table-column prop="location" label="国籍" width="120"/>
    <el-table-column prop="introduction" label="个人介绍" width="220"/>
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
  console.log("当前查询的名称:", database.user.find);

  if (!database.user.find) {
    console.error("user-info 中未找到 find 字段");
    return;
  }

  // 使用 `params` 传递查询参数
  request.get("/singer/selectAll", { params: { name: database.user.find } })
      .then((res) => {
        if (!res.data || !Array.isArray(res.data)) {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.tableData = [];
        } else {
          data.tableData = res.data; // 假设返回的是数组
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
      });
};

onMounted(load);
</script>

<style>
</style>
