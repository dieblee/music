<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <div class="header" >
          <el-menu
              router
              :default-active="router.currentRoute.value.path"
              class="el-menu-demo"
              mode="horizontal"
              :ellipsis="false"
              display:flex> <!-- 默认背景色浅灰色 -->
            <img style="width:50px;height:50px;cursor: pointer;" src="@/assets/music.png" alt="follow" @click="handleCommand('/home/homeview')" />
            <span style="color: white;font-size: 20px;font-weight: bold;cursor: pointer;margin-right: 32px" @click="handleCommand('/home/homeview')">随心音乐</span>
            <div class="search-box">
              <el-input class="search" v-model="search" placeholder="搜索">
                <template #prefix>
                  <el-icon @click="handleCommand('/home/search')">
                    <Search />
                  </el-icon>
                </template>
              </el-input>
            </div>
            <el-menu-item  style="color:#ffffff;font-size: 15px;font-weight: bold" index="/home/rank">排行榜</el-menu-item>
            <el-menu-item  style="color:#ffffff;font-size: 15px;font-weight: bold" index="/home/singer">音乐人</el-menu-item>
            <el-sub-menu  index="4" style="float: right; color:white;">
              <template #title>
                <span style="color: white;font-size: 15px;font-weight: bold">我的</span>
              </template>
              <el-menu-item index="/home/space" >查看个人信息</el-menu-item>
              <el-menu-item @click="logout">切换账号</el-menu-item>
            </el-sub-menu>
            <div style="position: relative;right: -270px;display: flex;flex-direction: row">
              <div >
                <img :src="database.consumerInfo.avator || defaultAvatar" style="width: 30px;height: 30px;border-radius: 50%">
              </div>
              <span style="color:white;font-size: 12px;height: 30px;line-height: 30px;margin-left: 7px; cursor: pointer" @click="handleLoginClick">{{database.consumerInfo.username || '请先登录'}}</span>
              <img :src="messageImg" style="width: 18px;height: 18px;position: relative;top:5px;left: 5px" @click="jumpweb">
            </div>
            <el-dialog v-model="isLoginDialogVisible"  title="登录" width="30%" style="height:300px;" center>
              <el-form ref="formRef" :rule="data.rules" :model="data.form" label-width="100px" >
                <el-form-item label="用户名:">
                  <el-input v-model="data.form.username" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item label="密码:" >
                  <el-input v-model="data.form.password" type="password" placeholder="请输入密码" />
                </el-form-item>
              </el-form>
              <template #footer>
                <div style="margin-bottom: 50px">
                <el-button @click="isLoginDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleLogin">登录</el-button>
                </div>
                <div style="text-align: right;color: #2c2c31;font-size: 14px">还没有账号?请<a style="color: #1756ce;text-decoration:none" @click="handleRegisterClick">注册</a></div>
              </template>
            </el-dialog>
          </el-menu>
          <el-dialog v-model="isRegisterDialogVisible"  title="注册" width="30%" style="height:300px;" center>
            <el-form ref="formRef" :rules="data.rules" :model="data.confirmform" label-width="100px" >
              <el-form-item label="用户名:">
                <el-input v-model="data.confirmform.username" placeholder="请输入用户名" />
              </el-form-item>
              <el-form-item label="密码:" >
                <el-input v-model="data.confirmform.password" type="password" placeholder="请输入密码" />
              </el-form-item>
              <el-form-item label="确认密码:" >
                <el-input v-model="data.confirmform.confirmpassword" type="password" placeholder="请再次输入密码" />
              </el-form-item>
            </el-form>
            <template #footer>
              <div style="margin-bottom: 50px">
                <el-button @click="isLoginDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleRegister">注册</el-button>
              </div>
            </template>
          </el-dialog>

        </div>

      </el-header>

      <el-main>
        <div>
          <RouterView />
        </div>

        <!-- Song Audio Component -->
        <div class="songAudio" v-if="isPlay"> <!-- 只有当 isPlay 为 true 且 url 存在时才显示 -->
          <playBar/>
        </div>
      </el-main>

      <!-- Footer -->
      <el-footer>
        <div class="footer">
          <span style="font-size: 12px;color: #B1B3B9;font-family: \5FAE\8F6F\96C5\9ED1, simsun, sans-serif;display: block">关于随心 | 商务合作 | 广告服务 | 投诉指引 | 隐私政策 |  儿童隐私政策 | 用户服务协议 | 招聘信息 | 客服信息 | 举报中心</span>
          <span style="font-size: 12px;color: #B1B3B9;font-family: \5FAE\8F6F\96C5\9ED1, simsun, sans-serif;">粤网文 （2022）1053-085号 | 网络视听许可证 1910546号 | 增值电信业务 粤B2-20060339 | 粤ICP备09017694号 </span>
          <span style="font-size: 12px;color: #B1B3B9;font-family: \5FAE\8F6F\96C5\9ED1, simsun, sans-serif;">粤公网号44023123123213号 | 互联网宗教信息服务许可证 粤 （2022） 00000123 | 营业执照 </span>
          <span style="font-size: 12px;color: #B1B3B9;font-family: \5FAE\8F6F\96C5\9ED1, simsun, sans-serif;">互联网不良信息举报中心 随心不良信息举报邮箱: bugennishuo@qq.com 客服电话: 0231231-123</span>
          <span style="font-size: 12px;color: #B1B3B9;font-family: \5FAE\8F6F\96C5\9ED1, simsun, sans-serif;">Copyright @ 2022-2027 SuiXin-lnc.All Rights Reserved</span>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup>
import {ref, computed, onMounted, reactive} from "vue";
import { useStore } from "vuex";
import router from "@/router";
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import request from "@/utils/request.js";
import PlayBar from "@/components/playBar.vue";// 引入 SongAudio 组件
import messageImg from "@/assets/message.png";
import defaultAvatar from "@/assets/default-avatar.png";
import dayjs from "dayjs";



const formRef = ref();
const store = useStore();
const search = ref("");
// 通过 Vuex 获取音频的播放状态和 URL
const isPlay = computed(() => store.getters.isPlay);
const url = computed(() => store.getters.url);
const isLoginDialogVisible = ref(false);
const isRegisterDialogVisible = ref(false);
const data = reactive({
  tableData:  {
    username: "", // 初始化为空字符串
  },
  form:{},
  confirmform: {
    username: "",
    password: "",
    confirmpassword: "",
  },
  rules: {
    username: [{ required: true, message: "请输入账号", trigger: 'blur' },
      { min: 6, message: "账号长度不能少于 6 位", trigger: 'blur' }],
    password: [{ required: true, message: "请输入密码", trigger: 'blur' },
      { min: 6, message: "账号长度不能少于 6 位", trigger: 'blur' }],
  }
});

const jumpweb= () =>{
  if(database.consumerInfo != null){
    router.push({ path: "/home/message" })
  }
  else{
    ElMessage.error('请先登录')
  }
}
const handleCommand = (command) => {
  router.push(command);
};
const database = reactive({
  userId: JSON.parse(localStorage.getItem("userId"))|| {},
  consumerInfo: JSON.parse(localStorage.getItem("consumerInfo"))|| {},// 防止 null 时报错
});
const handleRegisterClick = () => {
  if (data.tableData.username != null) {
    isRegisterDialogVisible.value = true;
    isLoginDialogVisible.value = false;
  }
};

const handleLoginClick = () => {
  if (data.tableData.username != null) {
    isLoginDialogVisible.value = true;
  }
};


const handleLogin = async () => {
  console.log(data.form);
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.updateTime = dayjs().format('YYYY-MM-DD');
      request.post('/consumer/login', data.form).then(async res => {
        if (res.code === '200') {
          res.data.updateTime = data.form.updateTime;
          await request.put('/consumer/updatetime', {
            username: data.form.username, // 确保和 SQL 语句匹配
            updateTime: data.form.updateTime
          });
          localStorage.setItem('consumerInfo', JSON.stringify(res.data)); // 转换为JSON字符串
          ElMessage.success('登陆成功');
          data.tableData = res.data;
          setTimeout(() => {
            location.href = '/home/homeview';
          }, 500);
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

const handleRegister = () => {
  if (!formRef.value) {
    console.error("formRef 未定义");
    return;
  }

  formRef.value.validate((valid) => {
    if (valid) {
      if (data.confirmform.password !== data.confirmform.confirmpassword) {
        ElMessage.error("两次输入的密码不一致");
        return;
      }

      const registerData = {
        username: data.confirmform.username.trim(), // 避免空格导致问题
        password: data.confirmform.password.trim()
      };

      request.post('/consumer/add', registerData)
          .then(res => {
            if (res.code === '200') {
              ElMessage.success('注册成功');
              isRegisterDialogVisible.value = false;
              setTimeout(() => { location.href = '/home/homeview'; }, 500);
            } else {
              ElMessage.error(res.msg || '注册失败');
            }
          })
          .catch(error => {
            console.error("注册请求出错：", error);
            ElMessage.error("系统错误，请稍后重试");
          });
    }
  });
};

const logout =() =>{
    localStorage.removeItem('consumerInfo')
    location.href = '/home'
}

const load = () => {
  // 检查 userId 是否存在以及 username 是否有效
  if (!database.consumerInfo.username ) {
    console.warn("用户未登录，无法加载数据");
    return; // 如果没有登录，直接退出
  }
  // 请求用户数据
  request.get(`consumer/selectByUsername/${database.userId.id}`)
      .then((res) => {
        if (!res.data || !Array.isArray(res.data)) {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.tableData = []; // 设置为空数组
        } else {
          data.tableData = res.data;
          localStorage.setItem('consumerInfo', JSON.stringify(res.data));
          localStorage.setItem('userId', JSON.stringify(res.data.id));
          console.log("数据加载成功:", data.tableData);
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
      });
};

// 页面加载时检查 localStorage 是否有有效的 userId
onMounted(() => {
  const userId = { id: "00"}; // 设置 name 为 1111
  localStorage.setItem("userId", JSON.stringify(userId));
  load();
});

</script>

<style scoped>
.common-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  text-align: center;
}

.el-menu-demo {
  display: flex;
  justify-content: center;
  align-items: center;
}

.swiper {
  width: 90%;
  margin: auto;
  margin-top: 40px;
}

.swiper img {
  width: 100%; /* Ensure the image fills the container */
  height: auto;
  object-fit: cover; /* Ensures the aspect ratio is maintained */
}

.el-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.el-main {
  flex-grow: 1;
  overflow: auto;
  --el-main-padding: 0px;
}
.el-header {
  padding: 0 0px;
}
.el-footer {
  padding: 0 0px;
}
.footer span {
  display: block; /* 让每个 span 独占一行 */
  margin-bottom: 5px; /* 增加间距，优化视觉效果 */
}

.footer {
  background-color: #424242;
  color: white;
  padding-top: 30px;
  padding-bottom: 80px; /* 增加底部内边距，确保不会被播放器遮盖 */
  text-align: center;
  align-items: center;
}

.el-input {
  width: 240px;
  height: 35px;
  border-radius: 20px;
  margin-right: 20px;
}
.el-menu-item {
  user-select: none; /* 禁止选中文本 */
}

.el-menu-demo {
  user-select: none;
}
/* Menu background color and text color */
.el-menu-demo {
  background-color: #424242; /* 浅灰色背景 */
}
.el-menu--horizontal>.el-menu-item.is-active{
  color: white !important;
  border-bottom: 2px solid #fafcff;
}
</style>
<style>
/* Menu item hover and active state */
.el-menu-item:hover,
.el-sub-menu__title:hover,
.el-menu-item.is-active,
.el-sub-menu__title.is-active {
  background-color: #232323 !important; /* 深黑色 */
  color: white; /* 悬停时文字颜色 */
}

.footer{

}
.el-menu--horizontal .el-menu .el-menu-item.is-active, .el-menu--horizontal .el-menu .el-sub-menu.is-active>.el-sub-menu__title {
  color: #fff;
}

:root {
  --el-menu-active-color: #fff;
  --el-menu-text-color: #ffffff;
  --el-menu-hover-text-color: #ffffff;
  --el-menu-bg-color: #282c34;
  --el-menu-hover-bg-color: #282c34;
  --el-menu-item-height: 56px;
  --el-menu-sub-item-height: calc(var(--el-menu-item-height) - 6px);
  --el-menu-horizontal-height: 60px;
  --el-menu-horizontal-sub-item-height: 36px;
  --el-menu-item-font-size: var(--el-font-size-base);
  --el-menu-item-hover-fill: #000000;
  --el-menu-border-color: #000;
  --el-menu-base-level-padding: 20px;
  --el-menu-level-padding: 20px;
  --el-menu-icon-width: 24px;
}
</style>