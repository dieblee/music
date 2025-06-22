<template>
  <div class="login-container">
    <div class="login-box">
      <div style="padding: 50px 30px;background-color:rgba(143,233,244,50%);border-radius: 8px;box-shadow: 0 0 10px rgba(0,0,0,0.2)">
        <el-form ref="formRef" :rules="data.rules" :model="data.form" style="width: 330px">
          <div style="margin-bottom: 40px;color:#d7f6ea;font-size: 20px;font-weight: bold;text-align: center;">欢迎登录后台管理系统</div>
          <el-form-item prop="name">
            <el-input size="large" v-model="data.form.name" :prefix-icon="User" placeholder="请输入账号" class="input-with-icon"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password size="large" v-model="data.form.password" :prefix-icon="Lock" placeholder="请输入密码" class="input-with-icon"></el-input>
          </el-form-item>
          <div style="margin-bottom:20px;margin-top: 35px">
            <el-button size="large" style="width: 100%" type="primary" @click="login">登录</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const data = reactive({
  form: { role: 'EMP' },
  rules: {
    name: [{ required: true, message: "请输入账号", trigger: 'blur' },
      { min: 6, message: "账号长度不能少于 6 位", trigger: 'blur' }],
    password: [{ required: true, message: "请输入密码", trigger: 'blur' },
      { min: 6, message: "账号长度不能少于 6 位", trigger: 'blur' }],
  }
});

const formRef = ref();

const login = () => {
  console.log(data.form);
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          localStorage.setItem('user-info', JSON.stringify(res.data)); // 转换为JSON字符串
          ElMessage.success('登陆成功');
          setTimeout(() => { location.href = '/home'; }, 500);
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/silas-baisch-K785Da4A_JA-unsplash.jpg");
  background-size: 100% 180%;
  background-position: 0 -500px;
}

.login-box {
  width: 50%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  right: 0;
}

::v-deep .input-with-icon .el-input__prefix {
  font-size: 24px; /* 图标大小 */
  color: #4caf50; /* 图标颜色 */
  margin-right: 10px; /* 图标和输入框之间的间距 */
  display: flex;
  align-items: center;
  justify-content: center; /* 居中图标 */
}

.el-select {
  padding: 0 !important;
  margin: 0 !important;
}
:deep(.el-select-dropdown){
  position: relative;
}
</style>
