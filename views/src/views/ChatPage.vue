<template>
  <div class="chat-container">
    <div class="chat-header">
      <span>与 {{ targetNickname || '用户' }} 聊天中</span>
    </div>
    <div class="chat-messages">
      <div
          v-for="(msg, index) in messages"
          :key="index"
          class="chat-message"
          :class="{ 'mine': msg.sendId === consumerInfo.id }"
      >
        <div class="content">
          <div class="nickname">{{ msg.nickname }}</div>
          <div class="bubble">{{ msg.content }}</div>
          <div class="time">{{ formatTime(msg.time) }}</div>
        </div>
      </div>
    </div>
    <div class="chat-input">
      <input v-model="inputText" placeholder="输入消息..." @keyup.enter="sendMessage" />
      <button @click="sendMessage">发送</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const consumerInfo = JSON.parse(localStorage.getItem('consumerInfo'));
const targetId = parseInt(route.params.id);

const socket = ref(null);
const messages = ref([]);
const inputText = ref('');
const targetNickname = ref(route.params.id);
const isConnected = ref(false);

const formatTime = time => new Date(time).toLocaleTimeString();

const connectWebSocket = () => {
  socket.value = new WebSocket(`ws://localhost:8080/ws/${consumerInfo.id}`);

  socket.value.onopen = () => {
    console.log("WebSocket 连接成功");
    isConnected.value = true;
  };

  socket.value.onmessage = (event) => {
    const msg = JSON.parse(event.data);
    if (msg.sendId === targetId || msg.receiveId === targetId) {
      messages.value.push(msg);
      if (!targetNickname.value && msg.sendId === targetId) {
        targetNickname.value = msg.nickname;
      }
    }
  };

  socket.value.onclose = () => {
    console.log("WebSocket 连接关闭，尝试重连...");
    isConnected.value = false;
    setTimeout(() => {
      connectWebSocket();
    }, 2000);
  };

  socket.value.onerror = (error) => {
    console.error("WebSocket 出错:", error);
  };
};

onMounted(() => {
  connectWebSocket();
});

onUnmounted(() => {
  if (socket.value) {
    socket.value.close();
    isConnected.value = false;
  }
});

const sendMessage = () => {
  if (!inputText.value.trim()) return;

  if (!isConnected.value || socket.value.readyState !== WebSocket.OPEN) {
    alert("当前WebSocket未连接，无法发送消息");
    return;
  }

  const message = {
    sendId: consumerInfo.id,
    receiveId: targetId,
    content: inputText.value,
    avatarUrl: consumerInfo.avatar,
    nickname: consumerInfo.username,
    time: new Date().toISOString()
  };

  socket.value.send(JSON.stringify(message));
  messages.value.push(message);
  inputText.value = '';
};
</script>


<style scoped>
.chat-container {
  width: 600px;
  margin: 30px auto;
  border: 1px solid #ccc;
  display: flex;
  flex-direction: column;
  height: 80vh;
}

.chat-header {
  padding: 10px;
  background-color: #e0f7fa;
  text-align: center;
  font-weight: bold;
}

.chat-messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  background-color: #f9f9f9;
}

.chat-message {
  display: flex;
  margin-bottom: 10px;
}
.chat-message.mine {
  flex-direction: row-reverse;
  text-align: right;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.content {
  max-width: 70%;
  margin: 0 10px;
}
.nickname {
  font-size: 12px;
  font-weight: bold;
}
.bubble {
  background: #fff;
  border-radius: 10px;
  padding: 8px 12px;
  margin: 4px 0;
}
.time {
  font-size: 10px;
  color: gray;
}

.chat-input {
  padding: 10px;
  display: flex;
  gap: 10px;
  background: #fff;
  border-top: 1px solid #ddd;
}
.chat-input input {
  flex: 1;
  padding: 8px;
  border: 1px solid #aaa;
  border-radius: 5px;
}
.chat-input button {
  padding: 8px 12px;
}
</style>
