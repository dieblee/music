<template>
  <div class="common-layout">
    <el-container>
      <!-- 侧边栏菜单 -->
      <el-aside width="200px">
        <el-menu class="el-menu-vertical-demo">
          <el-menu-item
              v-for="(item, index) in data.tableData"
              :key="index"
              @click="receive(item.receiveId, item.receiveName)"
          >
            <span style="width: 150px;font-weight: 40">{{ item.receiveName }}</span>
            <el-icon @click="change(item.receiveId,item.receiveName)"><Close /></el-icon><!-- 显示好友名称 -->
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header>
          <div style="display: flex; justify-content: center">
            <span style="font-family: 'Sitka Heading';font-size: 16px;font-weight:bold">{{ currentFriendName || "请选择好友" }}</span>
          </div>
        </el-header>

        <el-main>
          <RouterView />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import { Close } from "@element-plus/icons-vue";

const data = reactive({
  tableData: [],
});

const currentFriendName = ref(localStorage.getItem("receiveName") || "");

const database = reactive({
  consumerInfo: JSON.parse(localStorage.getItem("consumerInfo")),
});
const receive = (id, name) => {
  localStorage.setItem("receiveId", JSON.stringify(id));
  localStorage.setItem("receiveName", name);
  currentFriendName.value = name;
  router.replace({ path: "/home/message/content", query: { receiveId: id, receiveName: name } });
};

const change = (id) =>{
  request.put(`/statue/updateStatue/${id}`)
      .then(() => {
        // 修改成功后，从列表中移除该项
        data.tableData = data.tableData.filter(item => item.receiveId !== id);
        localStorage.removeItem('receiveName')
        router.replace({ path: '/home/message' })
        load()
      })
      .catch((err) => console.error("状态更新失败:", err));
};
const load = () => {
  if (database.consumerInfo.id) {
    request.get(`/statue/selectByStatue/${database.consumerInfo.id}`)
        .then((res) => {
          if (Array.isArray(res.data)) {
            const uniqueMessages = Array.from(new Map(res.data.map(item => [item.receiveId, item])).values());
            data.tableData = uniqueMessages;
            console.log(data.tableData)
          } else {
            console.warn("查询结果为空或格式不正确:", res.data);
            data.tableData = [];
          }
        })
        .catch((err) => console.error("数据加载失败:", err));
  } else {
    console.warn("用户 ID 为空，未发送请求");
  }
};

onMounted(() => {
  load();
});
</script>

<style scoped>
.common-layout {
  width: 1000px;
  margin: auto;
  margin-top: 80px;
  margin-bottom: 40px;
  background-color: rgba(232, 233, 238, 0.8);
  border-radius: 15px;
}

.el-header {
  padding-top: 30px;
  width: 500px;
  margin: auto;
  border-bottom: solid 2px;
}

.el-menu-item {
  font-size: 15px;
  background-color: rgba(229, 230, 243, 0.5);
  border-bottom: solid 1px #989898;
}

.el-main {
  padding: 10px;
}

:deep(.el-menu .is-active) {
  background-color: #676767 !important;
  color: white !important;
}
</style>
