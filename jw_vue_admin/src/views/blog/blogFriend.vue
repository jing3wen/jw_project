<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm">
      <el-select style="width: 200px" class="ml-5" placeholder="请选择友链状态"
                 v-model="queryForm.status" @keyup.enter.native="search">
        <el-option label="待定" value="0"><el-tag type="info">待定</el-tag></el-option>
        <el-option label="停用" value="f"><el-tag type="danger">停用</el-tag></el-option>
        <el-option label="启用" value="1"><el-tag type="success">启用</el-tag></el-option>
      </el-select>
      <el-button type="primary" plain class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="primary" plain :disabled="multiple" @click="updateCheckBatch(null, '1')">批量启用<i class="el-icon-check"></i></el-button>
      <el-button type="danger" plain :disabled="multiple" @click="updateCheckBatch(null, 'f')">批量停用<i class="el-icon-close"></i></el-button>
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="friendTitle" label="友链标题" width="200px" :show-overflow-tooltip="true" align="center"></el-table-column>
      <el-table-column prop="friendCover" label="封面" width="100px">
        <template slot-scope="scope">
          <el-image :src="scope.row.friendCover"
                    style="height: 50px"
                    :fit="'contain'"
                    :preview-src-list="[scope.row.friendCover]">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="friendUrl" label="地址" width="200px" align="center">
        <template slot-scope="scope">
          <el-button type="text" @click="openFriendUrl(scope.row.friendUrl)">{{scope.row.friendUrl}}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="friendIntroduction" label="友链介绍" width="300px" :show-overflow-tooltip="true" align="center"></el-table-column>
      <el-table-column prop="status" label="友链状态" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.status==='f'">停用</el-tag>
          <el-tag type="info" v-if="scope.row.status==='0'">待定</el-tag>
          <el-tag type="success" v-if="scope.row.status==='1'">启用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="申请时间" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right" width="250px" align="center">
        <template slot-scope="scope">
          <el-button type="text" v-if="scope.row.status!=='1'" class="el-icon-check pl-5" @click="updateCheckBatch(scope.row.friendId, '1')">启用</el-button>
          <el-button type="text" v-if="scope.row.status!=='f'" class="el-icon-close pl-5" @click="updateCheckBatch(scope.row.friendId, 'f')">停用</el-button>
          <el-button type="text" class="el-icon-delete pl-10" @click="deleteRows(scope.row.friendId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
        :total="total"
        :f_pageNum.sync="queryForm.pageNum"
        :f_pageSize.sync="queryForm.pageSize"
        @pageList = "getPageList">
    </Pagination>
  </el-card>
</template>

<script>
import Pagination from "@/components/pagination/Pagination";

export default {
  name: "blogFriend",
  components:{Pagination },
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        status: ''
      },
      total:0,
      //表格数据
      tableData: [],
      //加载表格数据
      tableLoading:true,
      //选中的数据id
      selectedRowIds: [],
      // 非多个禁用
      multiple: true,
    }
  },
  created() {
    this.getPageList()
    this.queryForm.pageNum=0
  },
  methods: {
    getPageList(){
      this.request.get('/api/blogFriend/admin/getBlogFriendPage', {params: this.queryForm}).then(res =>{
        if(res.code === 200){
          this.tableData = res.data.records
          this.total = res.data.total
          this.tableLoading = false
        }else this.$message.error(res.msg)
      });
    },

    // 搜索表单相关操作
    search(){
      this.queryForm.pageNum=0
      this.getPageList()
    },
    reset(){
      this.queryForm.pageNum=0
      this.queryForm.pageSize=10
      this.queryForm.status=''
      this.getPageList()
    },

    //批量审核,批量删除
    handleSelectionChange(selectedRows) {
      this.selectedRowIds = selectedRows.map(item => item.friendId)
      this.multiple = !selectedRows.length
    },
    //批量审核
    updateCheckBatch(id, checkStatus){
      let ids = []
      if(id == null)
        ids = this.selectedRowIds
      else
        ids = [id]
      let data = {
        ids: ids,
        checkStatus: checkStatus
      }
      this.$confirm('确定要审核此'+ids.length+'条数据吗?', '系统提示', {type : 'warning'}).then(() => {
        this.request.post('/api/blogFriend/admin/updateCheckBatch', data).then(res =>{
          if(res.code === 200) {
            this.$message.success("编辑成功")
            this.getPageList()
            this.openDialog = false
          } else this.$message.error(res.msg)
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      });
    },
    //批量删除
    deleteRows(id){
      let ids = []
      if(id == null)
        ids = this.selectedRowIds
      else
        ids = [id]
      this.$confirm('确定要删除此'+ids.length+'条数据吗?', '系统提示', {type : 'warning'}).then(() => {
        this.request.delete('/api/blogFriend/admin/deleteBatch', {data:ids}).then(res =>{
          if(res.code === 200){
            this.$message.success("删除成功")
            this.getPageList()
          }else this.$message.error(res.msg)
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      });
    },

    //跳转友链
    openFriendUrl(url){
      if(url===null || url===''){
        return false;
      }
      if(!url.match("http://") && !url.match("https://")){
        url = "https://"+url
      }
      window.open(url, '_blank')
    }
  }
}
</script>

<style scoped>
.query-form{

}

</style>
