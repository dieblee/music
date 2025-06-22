<template >
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width:200px;margin-right:10px " v-model="data.username" placeholder="输入文字" :prefix-icon="Search"></el-input>
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
        <el-table-column label="id" prop="id" min-width="80" />
        <el-table-column label="接收人" prop="receiveName" min-width="100" />
        <el-table-column label="发送人"  min-width="100" >
        <template #default="scope">
          <span>{{ scope.row.sendName || '管理员' }}</span>
        </template>
        </el-table-column>
        <el-table-column label="信息内容" prop="content" min-width="120" ></el-table-column>
        <!-- 固定的操作列 -->
        <el-table-column label="操作" style="text-align: center;" fixed="right" min-width="200">
          <template #default="scope">
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
      <el-dialog v-model="data.formVisible" title="歌曲信息" width="600px" destroy-on-close>
        <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right:50px">
          <div style="margin-left: 12px;margin-bottom: 20px">
            <span style="margin-right:12px ">发送对象</span>
            <el-select
                v-model="data.form.ids"
                multiple
                collapse-tags
                collapse-tags-tooltip
                placeholder="选择发送对象"
                style="width: 240px"
                @click="loadconsumer"
            >
              <el-option
                  v-for="item in data.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </div>
          <el-form-item label="发送内容" prop="content">
            <el-input v-model="data.form.content" type="" style="width:250px" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer" style="margin-top:15px; display: flex; justify-content: center;">
            <el-button @click="data.formVisible = false">取 消</el-button>
            <el-button type="primary" @click="add">确 定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>
<script setup>
import  { reactive,ref } from "vue";
import  { Search,Edit,Delete } from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage,ElMessageBox } from "element-plus";
import warning from "@element-plus/icons/lib/Warning.js";

const data = reactive({
  username:null,
  tableData:[],
  pageNum:1,
  pageSize:10,
  total:0,
  ids:[],
  form:{ids: [] },
  formVisible:false,
  options:{},
})
const formRef = ref()

const load = () => {
  request.get('/message/selectPage', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username:data.username
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

const loadconsumer = () =>{
  request.get('/consumer/selectAll').then(res =>{
    if (res.code === '200') {
      data.options = res.data.map(item => ({
        label: item.username,
        value: item.id
      }))
    }else{
      ElMessage.error(res.msg)
    }
  })
}


const add = () => {
  request.post('/message/addBatch', {
    ids: data.form.ids,
    content: data.form.content,
    sendId:'999999'
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
      data.formVisible = false
    } else {
      ElMessage.error(`操作失败: ${res.msg || '未知错误'}`)
    }
  })
}


const handleupdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row))//深度复制
  data.formVisible = true
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗?', '删除确认', { type: 'warning' }).then(() => {
        request.delete(`/message/deleteById/${id}`).then(res => {
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
        request.delete('/message/deleteBatch' ,{data:data.ids}).then(res => {
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

</style>