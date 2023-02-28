<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入评论人"
                v-model="queryForm.nickname" @keyup.enter.native="search"></el-input>
      <el-select style="width: 200px" class="ml-5" placeholder="请选择评论状态"
                 v-model="queryForm.commentCheck" @keyup.enter.native="search">
        <el-option label="未审核" value="0"><el-tag type="info">未审核</el-tag></el-option>
        <el-option label="不通过" value="f"><el-tag type="danger">不通过</el-tag></el-option>
        <el-option label="通过" value="1"><el-tag type="success">通过</el-tag></el-option>
      </el-select>
      <el-select style="width: 200px" class="ml-5" placeholder="请选择评论类型"
                 v-model="queryForm.commentType" @keyup.enter.native="search">
        <el-option label="文章评论" value="article">
          <el-tag><i class="el-icon-tickets"/>文章评论</el-tag>
        </el-option>
        <el-option label="留言评论" value="message">
          <el-tag type="warning"><i class="el-icon-chat-dot-round"/>留言评论</el-tag>
        </el-option>
      </el-select>
      <el-button type="primary" plain class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="primary" plain :disabled="multiple" @click="updateCheckBatch(null, '1')">批量通过<i class="el-icon-check"></i></el-button>
      <el-button type="danger" plain :disabled="multiple" @click="updateCheckBatch(null, 'f')">批量不通过<i class="el-icon-close"></i></el-button>
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="nickname" label="评论用户名" width="120px" align="center"></el-table-column>
      <el-table-column prop="avatar" label="用户头像" width="120px" align="center">
        <template slot-scope="scope">
          <el-avatar v-if="scope.row.avatar" :src="scope.row.avatar" shape="square"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="toNickname" label="回复人" width="120px" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.toNickname">{{scope.row.toNickname}}</span>
          <span v-else >无</span>
        </template>
      </el-table-column>
      <el-table-column prop="commentType" label="评论类型" width="120px" align="center">
        <template slot-scope="scope">
          <el-tag type="warning" v-if="scope.row.commentType==='message'"><i class="el-icon-chat-dot-round"/>留言评论</el-tag>
          <el-tag v-if="scope.row.commentType==='article'"><i class="el-icon-tickets"/>文章评论</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="commentContent" label="评论内容" width="400px" :show-overflow-tooltip="true" align="center"></el-table-column>
      <el-table-column prop="commentCheck" label="评论审核" width="80px" align="center">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.commentCheck==='f'">未通过</el-tag>
          <el-tag type="info" v-if="scope.row.commentCheck==='0'">未审核</el-tag>
          <el-tag type="success" v-if="scope.row.commentCheck==='1'">通过</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="评论时间" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right" width="200px" align="center">
        <template slot-scope="scope">
          <el-button type="text" v-if="scope.row.commentCheck!=='1'" class="el-icon-check pl-10" @click="updateCheckBatch(scope.row.commentId, '1')">通过</el-button>
          <el-button type="text" v-if="scope.row.commentCheck!=='f'" class="el-icon-close pl-10" @click="updateCheckBatch(scope.row.commentId, 'f')">不通过</el-button>
          <el-button type="text" class="el-icon-delete pl-10" @click="deleteRows(scope.row.commentId)">删除</el-button>
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
  name: "blogComment",
  components:{Pagination },
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        commentCheck: '',
        commentType:'',
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
  computed:{
  },
  created() {
    this.getPageList()
    this.queryForm.pageNum=0
  },
  methods: {
    getPageList(){
      this.request.post('/api/blogComment/admin/getPageList',this.queryForm).then(res =>{
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
      this.queryForm.commentType=''
      this.queryForm.commentCheck =''
      this.getPageList()
    },

    clickCancel(){
      // this.$message.info('操作已取消')
      this.resetDialogForm()
      this.openDialog = false
    },

    handleSelectionChange(selectedRows) {
      this.selectedRowIds = selectedRows.map(item => item.commentId)
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
        this.request.post('/api/blogComment/admin/updateCheckBatch', data).then(res =>{
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
      this.$confirm('确定要删除此'+ids.length+'条数据吗? 该评论下的子评论也会被删除', '系统提示', {type : 'warning'}).then(() => {
        this.request.delete('/api/blogComment/admin/deleteBatch', {data:ids}).then(res =>{
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
