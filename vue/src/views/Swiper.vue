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
      <el-table :data="data.swiperList" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection"  width="55" />
        <el-table-column label="id" prop="id"/>
        <el-table-column label="实例">
          <template #default="scope">
            <img :src="scope.row.content" alt="实例图片" style="width: 100px; height: auto;" />
          </template>
        </el-table-column>
        <el-table-column label="操作" style="margin: auto;width: 300px">
          <template #default="scope" >
            <el-button link type="primary" @click="handleupdate(scope.row)" :icon="Edit">编辑</el-button>
            <el-button link type="danger" @click="del(scope.row.id)" :icon="Delete">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:10px ">
      </div>
      <el-dialog v-model="data.formVisible" title="添加图片" width="500px" destory-on-close>
        <el-form ref="formRef"  :model="data.form"  label-width="80px" style="padding-right:50px">
          <div style="width:100%;display: flex;flex-direction: column;justify-content: center;margin-bottom: 20px">
            <el-form-item label="图片" >
              <el-upload class="content-uploader" action="http://localhost:8080/files/upload" :show-file-list="false" :on-success="handleAvatarSuccess"
              >
                <img  v-if="data.form.content" :src="data.form.content" class="content" />
                <el-icon v-else class="content-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </div>

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
  swiperList:[],
  total:0,
  form:{},
  formVisible:false,
})
const formRef = ref()

const load = () => {
  request.get("/swiper/selectAll")
      .then((res) => {
        if (res?.data && Array.isArray(res.data)) {
          data.swiperList = res.data;
        } else {
          console.warn("查询结果为空或格式不正确:", res.data);
          data.swiperList = []; // 确保 swiperList 仍然是数组
        }
      })
      .catch((err) => {
        console.error("数据加载失败:", err);
        data.swiperList = []; // 请求失败时，也设置为空数组，防止页面崩溃
      });
}
load()

const handleadd = () => {
  data.formVisible = true
  data.form = {}
}

const save = () => {//做两种操作
  console.log('Form data before adding（save）:', data.form);
  formRef.value.validate((valid) => {
    if(valid){
      data.form.id? update():add()
    }
  })

}

const add = () => {
  // 打印表单数据，查看当前的 form 数据
  console.log('Form data before adding:', data.form);

  request.post('/swiper/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      load();  // 重新加载数据
    } else {
      ElMessage.error(res.msg);
    }
  });
}
const reset = () => {
  // 重置相关的字段
  data.name = null; // 重置搜索框的值
  data.form = {};   // 可选：重置表单数据
  data.ids = [];    // 可选：重置选中的ID
  // 你还可以在这里重置其他状态
};

const handleAvatarSuccess = (res) =>{
  console.log('上传成功返回的数据:', res);
  if (res?.data) {
    data.form.content = res.data; // 确保绑定的字段正确
  } else {
    ElMessage.error('图片上传失败');
  }
}

const update = () => {
  request.put('/swiper/update', data.form).then(res =>{
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
        request.delete('/swiper/deleteById/' + id).then(res => {
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
        request.delete('/swiper/deleteBatch/' ,{data:data.ids}).then(res => {
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
  console.log(rows)
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
.content-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  cursor: pointer;
  position: relative;
  width: 200px;
  height: 160px;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.content-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.content-uploader-icon {
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