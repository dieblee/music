<template>
  <div style="background-color: white; height: 576px; padding-left: 10px; padding-top: 20px">
    <div>
      <el-scrollbar height="400px">
        <div class="message-container">
          <div
              v-for="message in sortedMessages"
              :key="message.id"
              :class="message.sendId === database.consumerInfo.id ? 'message-right' : 'message-left'"
          >
            <el-card style="display: inline-block; max-width: 60%; padding: 10px;">
              <div><strong>{{ message.username }}</strong></div>
              <div>{{ message.content }}</div>
              <div style="font-size: 12px; color: #666; margin-top: 5px;">{{ formatTime(message.createTime) }}</div>
            </el-card>
          </div>
        </div>
      </el-scrollbar>
    </div>

    <div>
      <el-form :inline="true" :model="data.form" class="demo-form-inline">
        <el-form-item>
          <el-input
              type="textarea"
              placeholder="发消息"
              v-model="data.form.content"
              style="width: 500px"
              :rows="4"
          />
        </el-form-item>
        <el-form-item>
          <el-button style="float: right; position: relative; right: 80px; height: 94px" @click="addComment()">
            发送
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from "vue";
import { ElMessage } from "element-plus";
import request from "@/utils/request.js";

// 读取本地存储的用户信息
const database = reactive({
  consumerInfo: JSON.parse(localStorage.getItem("consumerInfo")),
  receiveId:JSON.parse(localStorage.getItem("receiveId"))// 当前用户信息
});

// 存储消息
const messages = ref([]);

// 消息数据
const data = reactive({
  sendMessages: [],
  receiveMessages: [],
  form: {
    receiveId:database.receiveId , // 发送给谁
    sendId: database.consumerInfo.id, // 发送者 ID
    statue: "open",
    content: "",
  },
});

const sortedMessages = computed(() => {
  return [...messages.value].sort((a, b) => a.id - b.id);
});

// 格式化时间显示
const formatTime = (timeString) => {
  if (!timeString) return '';
  const date = new Date(timeString);
  return date.toLocaleString();
};

// 获取当前用户收到的消息
const loadReceive = async () => {
  try {
    const res = await request.get(`/message/selectByReceiveId/${database.consumerInfo.id}`);
    data.receiveMessages = Array.isArray(res.data) ? res.data : [];
  } catch (err) {
    console.error("加载接收的消息失败:", err);
  }
};

// 获取当前用户发送的消息
const loadSend = async () => {
  try {
    const res = await request.get(`/message/selectBySendId/${database.consumerInfo.id}`);
    data.sendMessages = Array.isArray(res.data) ? res.data : [];
  } catch (err) {
    console.error("加载发送的消息失败:", err);
  }
};

const updateMessages = () => {
  const currentUserId = database.consumerInfo.id;  // 自己
  const chattingUserId = database.receiveId;       // 正在聊天的对方

  // 收到的消息：对方发给我
  const received = data.receiveMessages
      .filter(msg => msg.sendId === chattingUserId && msg.receiveId === currentUserId)
      .map(msg => ({
        ...msg,
        direction: 'received',
      }));

  // 发送的消息：我发给对方
  const sent = data.sendMessages
      .filter(msg => msg.sendId === currentUserId && msg.receiveId === chattingUserId)
      .map(msg => ({
        ...msg,
        direction: 'sent',
      }));

  // 合并并按时间排序
  messages.value = [...received, ...sent].sort((a, b) => new Date(a.createTime) - new Date(b.createTime));
};

// 刷新消息
const refreshMessages = async () => {
  await Promise.all([loadSend(), loadReceive()]);
  updateMessages();
};

// 发送消息
const addComment = () => {
  if (!data.form.content.trim()) {
    ElMessage.warning("消息内容不能为空");
    return;
  }

  request.post('/message/add', data.form).then(res => {
    if (res.code === '200') {
      data.form.content = "";
      refreshMessages();
    } else {
      ElMessage.error(`发送失败: ${res.msg || '未知错误'}`);
    }
  }).catch(err => {
    ElMessage.error(`发送失败: ${err.message}`);
  });
};

// 组件挂载时加载消息
onMounted(refreshMessages);
</script>

<style scoped>
.demo-form-inline {
  position: absolute;
  bottom: 10px;
  top: 700px;
  right: 200px;
  margin: auto;
}

.message-container {
  display: flex;
  flex-direction: column;
  padding: 10px;
  gap: 10px;
}

.message-left {
  text-align: left;
}

.message-right {
  text-align: right;
}

.message-left .el-card {
  background-color: #f5f5f5;
  border-radius: 10px 10px 10px 0;
}

.message-right .el-card {
  background-color: #409eff;
  color: white;
  border-radius: 10px 10px 0 10px;
}
</style>