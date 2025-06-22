<template>
  <div class="card" style="width: 50%; padding: 40px 30px">
    <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 50px">
      <div style="width: 100%; display: flex; justify-content: center; margin-bottom: 20px">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="data.form.avator" :src="data.form.avator" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>
      <el-form-item label="账号" prop="name">
        <el-input ref="nameInputRef" v-model="data.form.name" style="width: 400px" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="password" >
        <el-input ref="passwordInputRef" v-model="data.form.password" style="width: 400px" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="role"  >
        <el-switch v-model="data.form.role" size="large" active-text="管理员" inactive-text="员工" active-value="ADMIN" inactive-value="EMP" disabled/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer" style="margin-top: 15px; display: flex; justify-content: center;">
      <el-button @click="data.formVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";

const formRef = ref()

// 读取 localStorage 的用户信息
const consumer = reactive({
  user: JSON.parse(localStorage.getItem("user-info")) || {} // 防止 localStorage 为空导致报错
});

// 表单数据
const data = reactive({
  form: {
    name: "",
    password: "",
    avator: "",
    role: "EMP",
  },
  formVisible: false,
  rules: {
    name: [
      { required: true, message: "请输入账号", trigger: "blur" },
      { min: 6, message: "账号长度不能少于 6 位", trigger: "blur" }
    ],
    password: [
      { required: true, message: "请输入密码", trigger: "blur" },
      { min: 6, message: "密码长度不能少于 6 位", trigger: "blur" }
    ],
  }
});

// 账号 & 密码输入框的 ref
const nameInputRef = ref(null);
const passwordInputRef = ref(null);

// 组件加载时填充用户信息
onMounted(() => {
  if (consumer.user) {
    data.form = { ...consumer.user }; // 赋值用户信息
  }

  // 确保数据加载完成后再聚焦账号输入框
  setTimeout(() => {
    if (nameInputRef.value) {
      nameInputRef.value.focus();
    }
  }, 100);
});

// 保存用户信息
const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add();
    }
  });
};

// 新增用户
const add = () => {
  console.log("Form data before adding:", data.form);
  request.post("/user/add", data.form).then(res => {
    if (res.code === "200") {
      ElMessage.success("操作成功");
      localStorage.setItem("user-info", JSON.stringify(data.form)); // 更新 localStorage
    } else {
      ElMessage.error(res.msg);
    }
  });
};

// 更新用户
const update = () => {
  request.put("/user/update", data.form).then(res => {
    if (res.code === "200") {
      ElMessage.success("操作成功");
      localStorage.setItem("user-info", JSON.stringify(data.form)); // 更新 localStorage
    } else {
      ElMessage.error(res.msg);
    }
  });
};

// 头像上传成功
const handleAvatarSuccess = (res) => {
  data.form.avator = res.data;
};

// 编辑用户
const handleupdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row)); // 深拷贝数据
  data.formVisible = true;
};
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
.el-switch {
  --el-switch-on-color: var(--el-color-primary);
  --el-switch-off-color: var(--el-border-color);
  align-items: center;
  display: inline-flex;
  font-size: 14px;
  height: 32px;
  line-height: 20px;
  position: relative;
  top: 2px;
  vertical-align: middle;
}
</style>
