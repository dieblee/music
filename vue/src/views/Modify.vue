<template>
  <div class="card" style="padding:40px 20px;width:50%">
    <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="100px" style="padding-right: 40px">
    <el-form-item label="原密码" prop="password">
      <el-input v-model="data.form.password" autocomplete="off" placeholder="请输入原密码" />
    </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input  v-model="data.form.newPassword" autocomplete="off" placeholder="请输入新密码" />
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input  v-model="data.form.confirmPassword" autocomplete="off" placeholder="请再次确认新密码" />
      </el-form-item>
      <div style="text-align: center">
        <el-button @click="modify" type="primary" style="padding:20px 30px">立即修改</el-button>
      </div>
    </el-form>
  </div>
</template>
<script setup>
import {reactive,ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage}  from "element-plus";

const validatePass = (rule, value, callback) => {
  if (!value ) {
    callback(new Error('请输入密码'))
  } else if (value !== data.form.newPassword){
    callback(new Error('两次输入密码不一致'))
  }
  else{
    callback()
  }
}
const formRef = ref()

const data = reactive({
  user:JSON.parse(localStorage.getItem('user-info')),
  form: {},
  rules:{
    password: [{required: true, message: "请输入密码", trigger: 'blur'},
      {min: 6, message: "账号长度不能少于 6 位", trigger: 'blur'}],
    newPassword: [{required: true, message: "请输入密码", trigger: 'blur'},
      {min: 6, message: "账号长度不能少于 6 位", trigger: 'blur'}],
    confirmPassword: [{required: true, message: "请输入密码", trigger: 'blur'},
      {min: 6, message: "账号长度不能少于 6 位", trigger: 'blur'},
      {validator:validatePass,trigger: 'blur'}]
  }
})

const modify =()=>{
  data.form.id=data.user.id
  formRef.value.validate((valid) => {
    if(valid){
      request.put('/user/modify',data.form).then (res => {
        if(res.code === '200'){
          ElMessage.success('修改成功')
          localStorage.removeItem('user-info')
          setTimeout( () =>{
            location.href='/login'
          },500)
        }else{
          ElMessage.success('修改失败')

        }
      })
    }
  })

}


</script>
<style>
</style>