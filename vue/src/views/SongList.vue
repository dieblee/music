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
        <el-table-column type="selection" width="55" />

        <!-- 自适应宽度，去除固定宽度 -->
        <el-table-column label="id" prop="id" min-width="80" />
        <el-table-column label="歌单名称" prop="title" min-width="100" />
        <el-table-column label="图片" min-width="120">
          <template #default="scope">
            <el-image
                style="width: 50px; height: 50px;"
                :src="scope.row.pic"
                fit="cover">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="歌单介绍" prop="introduction" min-width="100" />
        <el-table-column label="歌单风格" prop="style" min-width="100" />
        <el-table-column label="创建人" prop="username" min-width="100" >
          <template #default="scope">
            <span>{{ scope.row.username || '管理员' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="收藏歌曲" min-width="150">
          <template #default="scope">
            <el-select
                placeholder="请选择"
                @visible-change="(visible) => visible && loadallsongs(scope.row.id)"
            >
              <el-option
                  v-for="song in data.songOptions"
                  :key="song.id"
                  :value="song.id"
                  style="padding-left: 10px;padding-right: 10px">
                <div >
                  <span style="margin-right: 15px">{{ song.name }}</span>
                  <div style="float: right">
                    <el-icon
                        v-if="!scope.row.createId"
                        @click.stop="handleDeleteSong(scope.row.id,song.id)"
                        style="cursor: pointer; color: #999;"
                    >
                      <Close />
                    </el-icon>
                  </div>
                </div>
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <!-- 固定的操作列 -->
        <el-table-column label="操作" style="text-align: center;" fixed="right" min-width="200">
          <template #default="scope">
            <el-button link type="primary" @click="handleupdate(scope.row)" :icon="Edit">编辑</el-button>
            <el-button link type="danger" @click="del(scope.row.id)" :icon="Delete">删除</el-button>
            <el-button
                link
                type="success"
                @click="check(scope.row.id)"
                :icon="Plus"
                :disabled="scope.row.createId !== null"
            >
              添加
            </el-button>
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
      <el-dialog v-model="data.formVisible" title="歌单信息" width="600px" destory-on-close>
        <el-form ref="formRef"  :model="data.form"  label-width="80px" style="padding-right:50px">
          <el-form-item label="歌单名称" prop="title">
            <el-input v-model="data.form.title" style="width:250px" autocomplete="off" ></el-input>
          </el-form-item>
          <el-form-item label="图片">
            <el-upload
                action="http://localhost:8080/files/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
            >
              <el-button type="primary">上传图片</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="个人简介" prop="introduction">
            <el-input v-model="data.form.introduction"  style="width: 500px" :rows="4" type="textarea" placeholder="请输入歌曲简介"/>
          </el-form-item>
          <el-form-item label="歌单风格" prop="style">
            <el-input v-model="data.form.style" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="margin-top:15px; display: flex; justify-content: center;">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>
    </div>

    <el-dialog v-model="data.songsVisible" title="选择要添加的歌曲" width="600px" destroy-on-close>
      <div v-for="item in data.songData" :key="item.id" >
        <el-button style="width: 400px;height: 30px;margin-bottom: 10px;margin-left: 70px" @click="addsongs(item.id)">{{ item.name }}</el-button>
      </div>
      <template #footer>
        <div class="dialog-footer" style="text-align:center;">
          <el-button @click="data.songsVisible = false">取 消</el-button>
          <el-button type="primary" @click="data.songsVisible = false">完成</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {onMounted, reactive, ref} from "vue";
import  { Search,Edit,Delete,Plus } from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage,ElMessageBox } from "element-plus";
import warning from "@element-plus/icons/lib/Warning.js";
import { Close } from '@element-plus/icons-vue'

const data = reactive({
  username:null,
  songOptions:{},
  tableData:[],
  pageNum:1,
  pageSize:10,
  total:0,
  form:{},
  formVisible:false,
  songsVisible:false,
  songsData:{},
})

const formRef = ref()

const load = () => {
  request.get('/songList/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username: data.username
    }
  }).then(res => {
    console.log("后端返回数据：", res);
    if (res && res.data && Array.isArray(res.data.list)) {
      data.tableData = res.data.list;
      data.total = res.data.total || 0;  // 这里是if内的操作
      console.log("tableData:", data.tableData);
    } else {
      console.warn("后端数据异常或 list 不是数组：", res);
      data.tableData = [];
      data.total = 0;
    }
  }).catch(error => {
    console.error("请求失败：", error);
    data.tableData = [];
    data.total = 0;
  });
};

const loadallsongs = (id) => {
  request.get(`/listsong/selectAllSongs/${id}`).then(res => {
    data.songOptions = res.data
  })
}

const loadsongs = () => {
  request.get(`/song/selectAll`).then(res => {
    data.songData = res.data
  })
}

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
  request.post('/songList/add', data.form).then(res =>{
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    }else{
      ElMessage.error(`操作失败: ${res.msg || '未知错误'}`);
    }
  })
}

const check = (id) => {
  data.songsVisible = true
  localStorage.setItem('check', JSON.stringify(id))
  loadsongs()
}

const update = () => {
  console.log(data.form)
  request.put('/songList/update', data.form).then(res =>{
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const addsongs = (id) => {
  let songListId = JSON.parse(localStorage.getItem('check'));
  request.post(`/listsong/add`, {
    song_list_id: songListId,
    song_id: id
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('添加成功');
    } else {
      ElMessage.error(res.msg);
    }
  })
}

const handleupdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row))//深度复制
  data.formVisible = true
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗?', '删除确认', { type: 'warning' }).then(() => {
        request.delete('/songList/deleteById/' + id).then(res => {
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
        request.delete('/songList/deleteBatch/' ,{data:data.ids}).then(res => {
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

const handleDeleteSong = async (songlistId, songId) => {
  try {
    await ElMessageBox.confirm(
        '确定要删除这首歌吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
    )

    const res = await request.delete(`/listsong/manageSongs/${songlistId}/${songId}`)
    if (res) {
      loadallsongs(songlistId)
      ElMessage.success('删除成功')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}
const handleSelectionChange = (rows) =>{
  console.log(rows)
  data.ids = rows.map(row => row.id)
  console.log(data.ids)
}

const handleAvatarSuccess = (res) =>{
  data.form.pic = res.data
}



onMounted(() => {
  load();
});

</script>
<style>
.el-select-dropdown {
  box-sizing: border-box;
  list-style: none;
  margin: 0;
  position: relative;
}
</style>