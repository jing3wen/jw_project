<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入用户名"
                v-model="queryForm.nickname" @keyup.enter.native="search"></el-input>
      <el-button type="primary" plain class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="nickname" label="用户" width="200px" align="center"></el-table-column>
      <el-table-column prop="avatar" label="用户头像" align="center">
        <template slot-scope="scope">
          <el-avatar v-if="scope.row.avatar" :src="scope.row.avatar" shape="square"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="momentsContent" label="朋友圈内容" width="150px" align="center"></el-table-column>
      <el-table-column prop="isPublic" label="公开状态" width="80px" align="center">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.isPublic==='0'">私密</el-tag>
          <el-tag type="success" v-if="scope.row.isPublic==='1'">公开</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发表" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right" width="200px" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-delete pl-10" @click="deleteRows(scope.row.momentsId)">删除</el-button>
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
  name: "blogMoments",
  components:{Pagination },
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        nickname: ''
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
      this.request.get('/api/blogMoments/admin/getAdminMomentsPage', {params: this.queryForm}).then(res =>{
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
      this.queryForm.nickname=''
      this.getPageList()
    },



    handleSelectionChange(selectedRows) {
      this.selectedRowIds = selectedRows.map(item => item.momentsId)
      this.multiple = !selectedRows.length
    },
    // 批量删除
    deleteRows(id){
      let ids = []
      if(id == null)
        ids = this.selectedRowIds
      else
        ids = [id]
      this.$confirm('确定要删除此'+ids.length+'条数据吗?', '系统提示', {type : 'warning'}).then(() => {
        this.request.delete('/api/blogMoments/admin/deleteBatch', {data:ids}).then(res =>{
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
