<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm">
      <el-select style="width: 200px" class="ml-5" placeholder="请选择弹幕状态"
                 v-model="queryForm.messageCheck" @keyup.enter.native="search">
        <el-option label="未审核" value="0"><el-tag type="info">未审核</el-tag></el-option>
        <el-option label="未通过" value="f"><el-tag type="danger">未通过</el-tag></el-option>
        <el-option label="通过" value="1"><el-tag type="success">通过</el-tag></el-option>
      </el-select>
      <el-button type="primary" plain class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="primary" plain :disabled="multiple" @click="updateCheckBatch(null, '1')">批量通过 <i class="el-icon-check"></i></el-button>
      <el-button type="danger" plain :disabled="multiple" @click="updateCheckBatch(null, 'f')">批量不通过 <i class="el-icon-close"></i></el-button>
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="messageNickname" label="用户昵称" width="120px" align="center"></el-table-column>
      <el-table-column prop="messageAvatar" label="用户头像" width="140px" align="center">
        <template slot-scope="scope">
          <el-avatar v-if="scope.row.messageAvatar" :src="scope.row.messageAvatar" shape="square"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="messageContent" label="弹幕内容" width="400px" :show-overflow-tooltip="true" align="center"></el-table-column>
      <el-table-column prop="messageCheck" label="评论审核" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.messageCheck==='f'">不通过</el-tag>
          <el-tag type="info" v-if="scope.row.messageCheck==='0'">未审核</el-tag>
          <el-tag type="success" v-if="scope.row.messageCheck==='1'">通过</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" min-width="200px" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right" width="250px" align="center">
        <template slot-scope="scope">
          <el-button type="text" v-if="scope.row.messageCheck!=='1'" class="el-icon-check pl-10" @click="updateCheckBatch(scope.row.messageId, '1')"> 通过</el-button>
          <el-button type="text" v-if="scope.row.messageCheck!=='f'" class="el-icon-close pl-10" @click="updateCheckBatch(scope.row.messageId, 'f')"> 不通过</el-button>
          <el-button type="text" class="el-icon-delete pl-10" @click="deleteRows(scope.row.messageId)">删除</el-button>
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
  name: "blogMessage",
  components:{Pagination },
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        messageCheck: ''
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
      this.request.get('/api/blogMessage/admin/getBlogMessagePage', {params: this.queryForm}).then(res =>{
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
      this.queryForm.messageCheck=''
      this.getPageList()
    },



    handleSelectionChange(selectedRows) {
      this.selectedRowIds = selectedRows.map(item => item.messageId)
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
        this.request.post('/api/blogMessage/admin/updateCheckBatch', data).then(res =>{
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
    // 批量删除
    deleteRows(id){
      let ids = []
      if(id == null)
        ids = this.selectedRowIds
      else
        ids = [id]
      this.$confirm('确定要删除此'+ids.length+'条数据吗?', '系统提示', {type : 'warning'}).then(() => {
        this.request.delete('/api/blogMessage/admin/deleteBatch', {data:ids}).then(res =>{
          if(res.code === 200){
            this.$message.success("删除成功")
            this.getPageList()
          }else this.$message.error(res.msg)
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      });
    },
  }
}
</script>

<style scoped>
.query-form{

}

</style>
