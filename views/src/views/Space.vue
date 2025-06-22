<template>
  <div style="width:60%;height:700px;margin: auto;margin-top: 60px;margin-bottom: 40px">
    <div style="display:flex;flex-direction:row;margin-bottom: 50px">
      <div>
        <img :src="avatarUrl" style=";border-radius:50%;width: 200px;height: 200px;margin-left: 30px">
      </div>
      <div style="display: flex;flex-direction: row">
        <div>
          <div style="display: flex;flex-direction: row">
            <span style="font-size: 21px;font-weight: 600;">{{user.consumerInfo.username}}</span>
              <div v-if="isSingerExist">
                <span style="font-size: 12px; font-weight: 14;margin-left: 3px;color:#707070;">歌手认证</span>
              </div>
          </div>
          <div style="display: flex;flex-direction: column;width: 600px">
            <span style="margin-top: 15px;font-size: 15px;color: rgba(66,66,66,0.71);margin-left: 37px;position: relative;left: -15px">生日:{{formattedBirth}}</span>
            <span style="margin-top: 15px;font-size: 15px;color: rgba(66,66,66,0.71);margin-left: 37px;position: relative;left: -10px">电话:{{user.consumerInfo.phoneNum}}</span>
            <span style="margin-top: 15px;font-size: 15px;color: rgba(66,66,66,0.71);margin-left: 37px;position: relative;left: -15px">邮箱:{{user.consumerInfo.email}}</span>
            <span style="margin-top: 15px;font-size: 15px;color: rgba(66,66,66,0.71);margin-left: 37px;position: relative;left: -28px">地址:{{user.consumerInfo.location}}</span>
            <span style="margin-top: 15px;font-size: 15px;color: rgba(66,66,66,0.71);margin-left: 37px;position: relative;left: -48px">性别:{{user.consumerInfo.sex}}</span>
          </div>
        </div>
        <div class="button">
          <el-button @click="handleChangeClick" style="margin-bottom: 20px">修改个人信息</el-button>
          <el-button v-if="!isSingerExist" style="margin-left: 10px;margin-bottom: 20px" @click="handleLoginClick">
            注册为歌手
          </el-button>
          <el-button v-if="isSingerExist" @click="handleUploadClick">上传歌曲</el-button>
        </div>
      </div>
    </div>
    <div class="demo-collapse">
      <el-collapse v-model="activeName" accordion>
        <el-collapse-item title="我的收藏" name="1">
          <div style="display: flex; flex-wrap: wrap;">
            <div style="display: flex;flex-direction: column;text-align: center;margin-right: 30px" >
              <img :src="defaultPic" style="width: 120px;height: 120px;border-radius: 8px" @click="jumpcollect()" >
              <span style="font-size: 16px">收藏单曲</span>
            </div>
            <div style="display: flex;flex-direction: column;text-align: center;margin-right: 30px" v-for="(item, index) in data.collectData" :key="index ">
              <img :src="item.pic" style="width: 120px;height: 120px;border-radius: 8px" @click="jumpweb(item.id)">
              <span style="font-size: 16px">{{ item.title }}</span>
            </div>
          </div>
        </el-collapse-item>
        <el-collapse-item title="我的歌单" name="2">
          <div style="display: flex; flex-wrap: wrap;">
            <div style="display: flex;flex-direction: column;text-align: center;margin-right: 30px" v-for="(item, index) in data.songListData" :key="index ">
              <img :src="item.pic" style="width: 120px;height: 120px;border-radius: 8px" @click="jumpweb(item.id)">
              <span style="font-size: 16px">{{ item.title }}</span>
            </div>
            <div style="display: flex;flex-direction: column;text-align: center" >
              <div style="width: 120px;height: 120px;border-radius: 8px;border: 2px dashed #ccc;cursor: pointer;align-items: center;justify-content: center" @click="handleSongListClick"  ><span style="font-size: 65px;color: #8c939d">+</span></div>
              <span style="font-size: 12px">请添加新歌单</span>
            </div>
          </div>
        </el-collapse-item>
        <el-collapse-item v-if="isSingerExist" title="创作列表" name="3">
          <div>
            <div style="display: flex; justify-content: center">
              <span style="font-size: 20px; font-weight: 14">创作歌曲列表</span>
            </div>
            <div style="float: right; position: relative; right: 80px">
              <el-pagination
                  background
                  layout="prev, slot, next"
                  :total="data.total"
                  :current-page="data.pageNum"
                  :page-size="data.pageSize"
                  @current-change="handlePageChange">
          <span style="display: block; text-align: center; margin-top: 10px; position: relative; top: -6px;">
            {{ data.pageNum }} / {{ Math.ceil(data.total / data.pageSize) }}
          </span>
              </el-pagination>
            </div>

            <div  style="width: 1000px;margin-bottom: 50px; padding-left: 40px">
              <el-table style="width: 900px; margin: auto" :data="data.tableData" stripe @row-dblclick="changeState">
                <el-table-column label="序号" width="70px" align="center">
                  <template #default="scope">
                    {{ (data.pageNum - 1) * data.pageSize + scope.$index + 1 }}
                  </template>
                </el-table-column>
                <el-table-column label="图片" width="80px">
                  <template #default="scope">
                    <el-image style="width: 50px; height: 50px; border-radius: 8px" :src="scope.row.songPic"></el-image>
                  </template>
                </el-table-column>
                <el-table-column label="名称" width="700px" prop="songName"></el-table-column>
              </el-table>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>

  </div>

  <el-dialog v-model="isChangeDialogVisible"  title="修改个人信息" width="30%"  center>
    <el-form ref="formRef" :model="data.form" label-width="100px" >
      <el-form-item label="账号" prop="username">
        <el-input v-model="data.form.username" style="width:250px" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="data.form.password" style="width:250px" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="头像">
        <el-upload
            action="http://localhost:8080/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <el-button type="primary">上传头像</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="data.form.sex">
          <el-radio value="男" label="男"></el-radio>
          <el-radio value="女" label="女"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="电话号码" prop="phoneNum">
        <el-input v-model="data.form.phoneNum" style="width:300px" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电子邮箱" prop="email">
        <el-input v-model="data.form.email" style="width:300px" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="生日" prop="birth">
        <el-date-picker v-model="data.form.birth"  type="date" placeholder="Pick a day" size="default"/>
      </el-form-item>
      <el-form-item label="个人简介" prop="introduction">
        <el-input v-model="data.form.introduction"  style="width: 500px" :rows="4" type="textarea" placeholder="Please input"/>
      </el-form-item>
      <el-form-item label="地址" prop="location">
        <el-input v-model="data.form.location" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div>
        <el-button @click="isChangeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChange">修改</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="isUploadDialogVisible" title="歌曲信息" width="600px" destroy-on-close>
    <template #header>
      <div style="text-align: center; font-size: 18px; font-weight: bold;">
        歌曲信息
      </div>
    </template>
    <el-form ref="formRef" :model="data.songData" label-width="80px" style="padding-right:50px">
      <el-form-item label="歌名" prop="name">
        <el-input v-model="data.songData.name" style="width:250px" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="歌曲简介" prop="introduction">
        <el-input v-model="data.songData.introduction" style="width:250px" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="封面">
        <el-upload
            action="http://localhost:8080/files/upload"
            :show-file-list="false"
            :on-success="handlePicSuccess"
        >
          <el-button type="primary">上传封面</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="歌源">
        <el-upload
            action="http://localhost:8080/files/upload"
            :show-file-list="false"
            :on-success="handleUrlSuccess"
            accept=".mp3"
        >
          <el-button type="primary">上传MP3文件</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <el-form-item>
      <el-checkbox style="margin-left: 48px" v-model="data.songData.radio">
        <div style="white-space: normal; line-height: 20px; width: 500px;">
          已经阅读过《随心音乐用户服务协议》、《版权政策》等，接受免除或者限制责任、诉讼管辖约定等粗体标示条款
        </div>
      </el-checkbox>
    </el-form-item>
    <template #footer>
      <div class="dialog-footer" style="margin-top:15px; display: flex; justify-content: center;">
        <el-button @click="data.isUploadDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleUpload">确 定</el-button>
      </div>
    </template>

  </el-dialog>

  <el-dialog v-model="isLoginDialogVisible" title="注册为歌手" width="600px" destroy-on-close>
    <el-form ref="formRef" :model="data.singerInfo" label-width="80px" style="padding: 20px;">
      <el-form-item label="账号" prop="name" >
        <el-input v-model="data.singerInfo.name" style="width:250px" autocomplete="off"  disabled></el-input>
      </el-form-item>
      <el-form-item label="歌手简介" prop="introduction">
        <el-input v-model="data.singerInfo.introduction" style="width: 500px" :rows="4" type="textarea" placeholder="请输入简介"/>
      </el-form-item>
      <el-form-item label="国籍" prop="location">
        <el-input v-model="data.singerInfo.location" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-checkbox style="margin-left: -65px" v-model="data.singerInfo.radio">
          <div style="white-space: normal; line-height: 20px; width: 500px;">
            已经阅读过《随心音乐用户服务协议》、《随心音乐网站歌手协议》等，接受免除或者限制责任、诉讼管辖约定等粗体标示条款
          </div>
        </el-checkbox>
      </el-form-item>
    </el-form>
    <div style="text-align: center; padding: 15px 0;">
      <el-button @click="isUploadDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="handleLogin">确 定</el-button>
    </div>
  </el-dialog>

  <el-dialog v-model="isSongListDialogVisible" title="歌单信息" width="600px" destroy-on-close>
    <el-form ref="formRef" :model="data.songListInfo" label-width="80px" style="padding: 20px;">
      <el-form-item label="歌单名称" prop="username" >
        <el-input v-model="data.songListInfo.title" style="width:250px" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="歌单封面">
        <el-upload
            action="http://localhost:8080/files/upload"
            :show-file-list="false"
            :on-success="handlePicSuccess">
          <el-button type="primary">上传封面</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="歌单简介" prop="introduction">
        <el-input v-model="data.songListInfo.introduction" style="width: 500px" :rows="4" type="textarea" placeholder="请输入简介"/>
      </el-form-item>
      <el-form-item label="歌单风格" prop="style">
        <el-input v-model="data.songListInfo.style" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-checkbox style="margin-left: -65px" v-model="data.songListInfo.radio">
          <div style="white-space: normal; line-height: 20px; width: 500px;">
            已经阅读过《随心音乐用户服务协议》、《版权政策》等，接受免除或者限制责任、诉讼管辖约定等粗体标示条款
          </div>
        </el-checkbox>
      </el-form-item>
    </el-form>
    <div style="text-align: center; padding: 15px 0;">
      <el-button @click="isUploadDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="handlesongList">确 定</el-button>
    </div>
  </el-dialog>

</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import defaultPic from "@/assets/music.png";
import router from "@/router/index.js";
import defaultAvatar from "@/assets/logo.svg";

const isChangeDialogVisible = ref(false);
const isUploadDialogVisible = ref(false);
const isLoginDialogVisible = ref(false);
const isSingerExist = ref(false);
const isSongListDialogVisible =ref(false);

const user = reactive({
  consumerInfo: JSON.parse(localStorage.getItem("consumerInfo")) || {}, // 防止 null 时报错
});

const data = reactive({
  form: {...user.consumerInfo},
  songData: {},
  singerInfo:{name:user.consumerInfo.username},
  songListData:[],
  tableData:[],
  collectData:[],
  songListInfo:{createId:user.consumerInfo.id},
  total: 0,
  pageNum: 1,
  pageSize: 5,
});

const avatarUrl = computed(() => {
  return user?.consumerInfo?.avator && user.consumerInfo.avator.trim()
      ? user.consumerInfo.avator
      : defaultAvatar;
});


// 解析用户信息


// 计算生日格式化
const formattedBirth = computed(() => {
  if (user.consumerInfo.birth) {
    const birthDate = new Date(user.consumerInfo.birth);
    const year = birthDate.getFullYear();
    const month = String(birthDate.getMonth() + 1).padStart(2, '0');
    const day = String(birthDate.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
  return '';
});

// 检查用户是否是歌手
const checkSingerExist = async () => {
  try {
    const res = await request.get(`/singer/selectByName/${user.consumerInfo.username}`);
    if (res.code === '200' && res.data) {
      isSingerExist.value = true; // 确保 data 存在时才赋值为 true
    } else {
      isSingerExist.value = false;
    }
  } catch (error) {
    console.error("查询歌手信息失败", error);
    isSingerExist.value = false;
  }
};

const jumpcollect = () =>{
  router.push({ path: "/home/collect" });
}
const jumpweb = (id) =>{
  localStorage.setItem("songlistId", id);
  router.push({ path: "/home/songlist" });
}
const handleChangeClick = () => {
  isChangeDialogVisible.value = true;
};

const handleSongListClick = () => {
  isSongListDialogVisible.value = true;
};

const handleUploadClick = () => {
  isUploadDialogVisible.value = true;
};

const handleLoginClick = () => {
  isLoginDialogVisible.value = true;
}

const handlePageChange = (newPage) => {
  data.pageNum = newPage;
  load(); // 重新加载数据
};
// 头像上传成功
const handleAvatarSuccess = (res) => {
  data.form.avator = res.data;
  user.consumerInfo.avator = res.data;
};

const handleSingerPicSuccess = (res) => {
  data.singerInfo.pic = res.data;
};


const handlePicSuccess = (res) => {
  data.songListInfo.pic = res.data;
};

const handleUrlSuccess = (res) =>{
  data.songData.url = res.data
}
const loadSongList = () => {
  const res = request.get(`songList/selectByCreateId/${user.consumerInfo.id}`).then((res) => {
    console.log(res.data)
    if (!res.data || !Array.isArray(res.data)) {
      console.warn("查询结果为空或格式不正确:", res.data);
      data.songListData = [];
    } else {
      data.songListData = res.data;
      console.log("后端返回数据:", data.songListData);
    }
  }).catch((err) => {
    console.error("数据加载失败:", err);
  });
}

const loadCollect = () => {
  const res = request.get(`collect/selectSongListByUserId/${user.consumerInfo.id}`).then((res) => {
    console.log(res.data)
    if (!res.data || !Array.isArray(res.data)) {
      console.warn("查询结果为空或格式不正确:", res.data);
      data.collectData = [];
    } else {
      data.collectData = res.data;
      console.log("后端返回数据:", data.collectData);
    }
  }).catch((err) => {
    console.error("数据加载失败:", err);
  });
}


const handleChange = async () => {
  try {
    let oldUsername = user.consumerInfo.username;
    let newUsername = data.form.username;
    if (oldUsername !== newUsername) {
      const singerRes = await request.put("/singer/updatename", {
        oldname: oldUsername,
        newname: newUsername,
      });

      if (singerRes.code !== '200') {
        ElMessage.error(`歌手信息同步失败: ${singerRes.msg || "未知错误"}`);
        return; // 阻止后续执行，避免数据不一致
      }
    }

    const res = await request.put("/consumer/update", data.form);
    if (res.code === '200') {
      ElMessage.success("修改成功");
      Object.assign(user.consumerInfo, data.form);
      localStorage.setItem("consumerInfo", JSON.stringify(user.consumerInfo));
      isChangeDialogVisible.value = false;
    } else {
      ElMessage.error(res.msg || "修改失败");
    }
  } catch (error) {
    ElMessage.error("请求失败，请检查网络");
  }
};
const handleLogin = async () => {
  if (!data.singerInfo.radio ) {
    ElMessage.warning("请阅读确认相关政策");
    return;
  }
  const params = { ...data.singerInfo, type: '站内',pic:user.consumerInfo.avator };
  const res = await request.post("/singer/add", params);
  if (res.code === "200") {
    ElMessage.success("注册成功");
    isLoginDialogVisible.value = false;
    isSingerExist();
  } else {
    ElMessage.error(`注册失败: ${res.msg || "未知错误"}`);
  }
};

const handleUpload = () => {
  if (!data.songData.radio ) {
    ElMessage.warning("请阅读确认相关政策");
    return;
  }

  const params = { ...data.songData, type: '原创',singerId: user.consumerInfo.id,createTime:new Date().toISOString(),address:'web'};
  request.post('/song/add', params).then(res =>{
    if (res.code === '200') {
      console.log('singer_id:', user.consumerInfo.id);
      ElMessage.success('操作成功')
      load()
    }else{
      ElMessage.error(`操作失败: ${res.msg || '未知错误'}`);
    }
  })
}

const handlesongList = () => {
  if (!data.songListInfo.radio ) {
    ElMessage.warning("请阅读确认相关政策");
    return;
  }
  request.post('/songList/add', data.songListInfo).then(res =>{
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    }else{
      ElMessage.error(`操作失败: ${res.msg || '未知错误'}`);
    }
  })
}


const load = () => {
  if (!user.consumerInfo || !user.consumerInfo.username) {
    ElMessage.error('请先登录');
    return;
  }
  request.get(`singer/selectBySongsname/${user.consumerInfo.username}`, {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then((res) => {
    if (!res.data || !Array.isArray(res.data.list)) {
      data.tableData = []; // 为空数组，避免错误
      data.total = 0;
    } else {
      data.tableData = res.data.list;  // 正确获取列表数据
      data.total = res.data.total;     // 获取总数据量
      console.log("创作数据:", data.tableData);
    }
  }).catch((err) => {
    console.error("数据加载失败:", err);
  });
};

onMounted(() => {
  console.log(avatarUrl.value);
  load();
  loadSongList();
  checkSingerExist();
  loadCollect();
});
</script>

<style scoped>

::v-deep(.el-menu-item) {
  background-color: transparent !important;
}

::v-deep(.el-menu-item:hover) {
  background-color: transparent !important;
}

::v-deep(.el-menu-item.is-active) {
  background-color: transparent !important;
}
::v-deep(.el-menu--horizontal .el-menu-item.is-active) {
  border-bottom: none !important;
}
.el-menu-demo{
  width: 600px;
  border-bottom: 0px;
}
.button{
  position: relative;
  top:46px;
  right: 100px;
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
  border-bottom: 1px solid var(--el-collapse-border-color);
  border-top: 0px;
}
:deep(.el-collapse-item__header) {
  font-size: 22px !important;
}
</style>
<style>
.el-menu el-menu--horizontal el-menu-demo{
  height: 30px;
}
.el-pagination.is-background .btn-next.is-disabled, .el-pagination.is-background .btn-next:disabled, .el-pagination.is-background .btn-prev.is-disabled, .el-pagination.is-background .btn-prev:disabled, .el-pagination.is-background .el-pager li.is-disabled, .el-pagination.is-background .el-pager li:disabled {
  background-color: transparent;
  color: var(--el-text-color-placeholder);
}
.el-pagination.is-background .btn-next, .el-pagination.is-background .btn-prev, .el-pagination.is-background .el-pager li {
  background-color: transparent;
  margin: 0 4px;
}
.el-menu--horizontal>.el-sub-menu.is-active .el-sub-menu__title {
  border-bottom: 2px solid #fff;
  color: #fff;
}
.el-menu--horizontal .el-menu .el-menu-item.is-active, .el-menu--horizontal .el-menu .el-sub-menu.is-active>.el-sub-menu__title {
  color: #fff;
}
</style>
