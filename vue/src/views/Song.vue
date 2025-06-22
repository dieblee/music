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
        <el-table-column label="歌名" prop="name" min-width="100" />
        <el-table-column label="歌曲简介" prop="introduction" min-width="100" />
        <el-table-column label="封面" min-width="120">
          <template #default="scope">
            <el-image
                style="width: 50px; height: 50px;"
                :src="scope.row.pic"
                fit="cover">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="歌手" prop="singerName" min-width="120" ></el-table-column>
        <el-table-column label="歌源" prop="url" min-width="120" ></el-table-column>
        <el-table-column label="创作时间" prop="createTime" format="YYYY-MM-DD" min-width="150" >
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="类型" prop="type" min-width="120" ></el-table-column>
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
          <el-form-item label="歌名" prop="name">
            <el-input v-model="data.form.name" style="width:250px" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="歌手" prop="singerId">
            <el-input v-model="data.form.singerId" style="width:80px;margin-right: 10px" autocomplete="off"></el-input>
            <el-input :value="singerName" disabled style="width:170px" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="歌曲简介" prop="introduction">
            <el-input v-model="data.form.introduction" style="width:250px" autocomplete="off"></el-input>
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
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker v-model="data.form.createTime" type="date" placeholder="Pick a day" size="default"/>
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
          <el-form-item label="地区" prop="address">
            <el-input v-model="data.form.address" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer" style="margin-top:15px; display: flex; justify-content: center;">
            <el-button @click="data.formVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>
<script setup>
import {reactive, ref, watchEffect} from "vue";
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
  form:{ singerId: null},
  formVisible:false,
})

const formRef = ref()

const singerName = ref('')

const load = () => {
  request.get('/song/selectPage', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username:data.username
    }
  }).then( res => {
    console.log(res.data.list);
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
  console.log('Form data before adding（save）:', data.form);
  formRef.value.validate((valid) => {
    if(valid){
      data.form.id? update():add()
    }
  })

}

watchEffect(() => {
  if (data.form.singerId) {
    getSingerName(data.form.singerId)
  }
})

const getSingerName = (singerId) => {
  request.get(`/singer/selectById/${singerId}`).then(res => {
    if (res.code === '200') {
      singerName.value = res.data.name
    } else {
      singerName.value = '未知歌手'
    }
  })
}
const add = () => {
  request.get(`singer/selectType/${data.form.singerId}`)
      .then(res => {
        if (res.data === '站内') {
          ElMessage.error('不允许选择站内歌手');
          return;
        }
        request.get(`singer/selectById/${data.form.singerId}`)
            .then(res => {
              if (res.data == null) {
                ElMessage.error('不存在歌手');
                return;
              }
              const params = { ...data.form, type: '网络' };
              request.post('/song/add', params)
                  .then(res => {
                    if (res.code === '200') {
                      ElMessage.success('操作成功');
                      load();
                    } else {
                      ElMessage.error(`操作失败: ${res.msg || '未知错误'}`);
                    }
                  })
                  .catch(err => {
                    ElMessage.error(`请求失败: ${err.message}`);
                  });
            })
            .catch(err => {
              ElMessage.error(`获取歌手信息失败: ${err.message}`);
            });
      })
      .catch(err => {
        ElMessage.error(`获取歌手类型失败: ${err.message}`);
      });
};

const update = () => {
  console.log(data.form)
  request.put('/song/update', data.form).then(res =>{
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
        request.delete('/song/deleteById/' + id).then(res => {
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
        request.delete('/song/deleteBatch/' ,{data:data.ids}).then(res => {
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

const handlePicSuccess = (res) =>{
  data.form.pic = res.data
}

const handleUrlSuccess = (res) =>{
  data.form.url = res.data
}


const formatDate = (date) => {
  if (!date) return ''; // 处理空值

  const formattedDate = new Date(date);
  const year = formattedDate.getFullYear();
  const month = String(formattedDate.getMonth() + 1).padStart(2, '0'); // 月份是从0开始的，所以要加1
  const day = String(formattedDate.getDate()).padStart(2, '0');

  return `${year}-${month}-${day}`;
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

</style>