<template>
  <div style="width: 860px;margin: auto;">
    <el-scrollbar>
    <div style="width: 130px;margin: auto;position: relative;top:-20px;margin-bottom:5px; "><span style="color:#939393;">共有 {{ data.tableData?.length || 0 }}条评论</span></div>
    <div style="margin-bottom: 20px">
      <div style="display: flex;align-items: center;justify-content: center">
        <el-form  :model="data.form"   >
          <el-form-item>
            <el-input v-model="data.form.content" type="textarea"  placeholder="写评论" style="width: 700px;height: 50px" />
          </el-form-item>
          <el-form-item>
            <div style="position: relative;right:-610px">
              <el-button @click="addcomment"  >发表评论</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div style="width: 860px;height: 280px;margin-top: 20px;margin-bottom: 30px">
      <div style="padding-bottom:20px " v-for="(item,index) in data.tableData " :key="index">
        <div style="display: flex;flex-direction: row;margin-bottom: 8px">
          <img :src="item.avator" style="width: 20px;height: 20px;border-radius: 50%;margin-right: 10px">
          <span style="color: #737373">{{item.username}}</span>
        </div>
        <div style="width:500px;height: 100px;overflow:hidden;display: block;padding-left: 30px;font-size: 18px;font-family: '苹方 中等'">{{item.content}}</div>
        <div style="width:100px; position:relative;right: -760px" ><span style="font-size: 15px;color: #8c939d"> {{item.createTime}}</span></div>
      </div>
    </div>
    </el-scrollbar>
  </div>
</template>

<script setup>
import {onMounted, reactive} from "vue";
import request from "@/utils/request.js";
import dayjs from "dayjs";
import { ElMessage } from "element-plus";

const data = reactive({
  tableDate:{},
  form:{  content: "" },
})

const database = reactive({
  songlistId: JSON.parse(localStorage.getItem("songlistId")) || {},
  consumerInfo: JSON.parse(localStorage.getItem("consumerInfo"))|| {},// 防止 null 时报错
});

const load = () => {
  console.log("当前查询的名称:", database.songlistId);
  request.get(`/comment/selectBySongListId/${database.songlistId}`)
  // 使用 `params` 传递查询参数
      .then((res) => {
        console.log(res.data)
        if (!res.data || !Array.isArray(res.data)) {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.tableData = [];
        } else {
          data.tableData = res.data.map(item => ({
            ...item,
            createTime: dayjs(item.createTime).format("YYYY-MM-DD") // 格式化时间
          })); // 假设返回的是数组
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
      });
};
const addcomment = () => {
  if (!data.form.content.trim()) {
    ElMessage.warning("评论不能为空！");
    return;
  }

  request.post('/comment/add', {songListId: database.songlistId, userId: database.consumerInfo.id, content: data.form.content, createTime: dayjs().toISOString()}).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      data.form.content = "";
      load();
    } else {
      ElMessage.error(`操作失败: ${res.msg || '未知错误'}`);
    }
  });
};
onMounted(() =>{
  load();
})

</script>

<style>

</style>