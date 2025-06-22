<template >
<div>
  <div class="card" style="margin-bottom: 5px">
    <el-input style="width:200px;margin-right:10px " v-model="data.name" placeholder="输入文字" :prefix-icon="Search"></el-input>
    <el-button type="primary" @click="load">查询</el-button>
    <el-button type="primary" @click="reset">重置</el-button>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-button type="primary" @click="handleadd">新 增</el-button>
    <el-button type="primary" @click="delBatch">批量删除</el-button>
    <NewComponent/>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection"  width="55" />
      <el-table-column label="id" prop="id"/>
      <el-table-column label="账号" prop="name"/>
      <el-table-column label="密码" prop="password"/>
      <el-table-column label="权限" prop="role"/>
      <el-table-column label="操作" style="margin: auto;width: 300px">
        <template #default="scope" >
          <el-button link type="primary" @click="handleupdate(scope.row)" :icon="Edit">编辑</el-button>
          <el-button link type="danger" @click="del(scope.row.id)" :icon="Delete">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top:10px ">
      <el-pagination
          @size-change="load"
          @current-change="load"
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5,10,15,20]"
          background
          layout="total,sizes,prev,pager,next,jumper"
          :total="data.total"/>
    </div>
    <el-dialog v-model="data.formVisible" title="员工信息" width="500px" destory-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form"  label-width="80px" style="padding-right:50px">
        <div style="width:100%;display: flex;justify-content: center;margin-bottom: 20px">
          <el-upload class="avatar-uploader" action="http://localhost:8080/files/upload" :show-file-list="false" :on-success="handleAvatarSuccess"
          >
            <img v-if="data.form.avator" :src="data.form.avator" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </div>
        <el-form-item label="账号" prop="name">
          <el-input v-model="data.form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" >
          <el-input v-model="data.form.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-switch  v-model="data.form.role"  size="large" active-text="管理员" inactive-text="员工" active-value="ADMIN" inactive-value="EMP"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-top:15px; display: flex; justify-content: center;">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</div>
</template>
<script setup>
import {computed, reactive, ref} from "vue";
import  { Search,Edit,Delete } from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage,ElMessageBox } from "element-plus";
import warning from "@element-plus/icons/lib/Warning.js";

const data = reactive({
  name:null,
  tableData:[],
  pageNum:1,
  pageSize:10,
  total:0,
  form:{},
  formVisible:false,
  rules: {
    name: [{required: true, message: "请输入账号", trigger: 'blur'},
      {min: 6, message: "账号长度不能少于 6 位", trigger: 'blur'}],
    password: [{required: true, message: "请输入密码", trigger: 'blur'},
      {min: 6, message: "密码长度不能少于 6 位", trigger: 'blur'}],
  }
})
const formRef = ref()

const load = () => {
  request.get('/user/selectPage', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name:data.name
    }
  }).then( res => {
    data.tableData = res.data.list;
    data.total = res.data.total;
  })
}
load()

const reset = () => {
  data.name = null
  load()
}

const handleadd = () => {
  data.formVisible = true
  data.form = {}
}

const save = () => {//做两种操作
  formRef.value.validate((valid) => {
    if(valid){
      data.form.id? update():add()
    }
  })

}

const add = () => {
  // 打印表单数据，查看当前的 form 数据
  console.log('Form data before adding:', data.form);

  request.post('/user/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      load();  // 重新加载数据
    } else {
      ElMessage.error(res.msg);
    }
  });
}

const handleAvatarSuccess = (res) =>{
  data.form.avator = res.data
}

const update = () => {
  request.put('/user/update', data.form).then(res =>{
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const handleupdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row))//深度复制
  data.formVisible = true
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗?', '删除确认', { type: 'warning' }).then(() => {
        request.delete('/user/deleteById/' + id).then(res => {
          if (res.code === '200') {
            ElMessage.success('操作成功')
            load()
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
  ).catch()
}

const delBatch = () => {
  if(data.ids.length === 0){
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗?', '删除确认', { type: 'warning' }).then(() => {
        request.delete('/user/deleteBatch/' ,{data:data.ids}).then(res => {
          if (res.code === '200') {
            ElMessage.success('操作成功')
            load()
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
  ).catch()
}

const handleSelectionChange = (rows) =>{
  data.ids = rows.map(row => row.id)
  console.log(data.ids)
}
const roleStatus = computed({
  set(value) {
    data.form.role = value ? 'ADMIN' : 'EMP'; // 根据开关状态设置角色值
  }
});

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