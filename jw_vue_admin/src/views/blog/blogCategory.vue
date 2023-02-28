<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入类别名"
                v-model="queryForm.categoryName" @keyup.enter.native="search"></el-input>
      <el-button type="primary" plain class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="primary" plain @click="openAddOrEditDialog(null)">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="categoryName" label="类别名称" width="200px" align="center"></el-table-column>
      <el-table-column prop="remark" label="备注" width="300px" :show-overflow-tooltip="true" align="center"></el-table-column>
      <el-table-column prop="articleCount" label="文章数量" width="150px" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right" width="200px" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-edit pl-10" @click="openAddOrEditDialog(scope.row)"> 编辑</el-button>
          <el-button type="text" class="el-icon-delete pl-10" @click="deleteRows(scope.row.categoryId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
        :total="total"
        :f_pageNum.sync="queryForm.pageNum"
        :f_pageSize.sync="queryForm.pageSize"
        @pageList = "getPageList">
    </Pagination>

    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="40%" @close="clickCancel">
      <el-form :model="dialogForm" :rules="dialogFormRules" ref="dialogForm" label-width="100px">
        <el-form-item label="类别名" prop="categoryName">
          <el-input v-model="dialogForm.categoryName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dialogForm.remark" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="clickCancel">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import Pagination from "@/components/pagination/Pagination";

export default {
  name: "blogCategory",
  components:{Pagination },
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        categoryName: ''
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
      //Dialog对话框, 可以换成Drawer抽屉
      openDialog:false,
      dialogTitle:'',
      dialogForm: {
        categoryId:null,
        categoryName: '',
        remark: '',
      },
      dialogFormRules: {
        categoryName: [
          {required: true, message: "请输入类别名称", trigger: "blur"},
          {min: 2, max: 50, message: '用户名称长度必须介于 2 和 50 之间', trigger: 'blur'}
        ],
        remark: [
          {max: 50, message: '备注长度不能超过50', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.getPageList()
    this.queryForm.pageNum=0
  },
  methods: {
    getPageList(){
      this.request.post('/api/blogCategory/admin/getBlogCategoryPageList', this.queryForm).then(res =>{
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
      this.queryForm.categoryName=''
      this.getPageList()
    },

    //新增，批量删除，数据导入，导出 按钮组相关操作
    openAddOrEditDialog(data){
      //重置表单状态
      this.resetDialogForm()
      if(data == null ){  //新增
        this.dialogForm.categoryId=null
        this.dialogTitle = '添加类别'
      }
      else {
        this.dialogForm = JSON.parse(JSON.stringify(data))
        this.dialogTitle = '编辑类别'
      }
      this.openDialog = true
    },
    addOrUpdate(){
      this.$refs['dialogForm'].validate((valid) => {
        if (valid) {
          let url = this.dialogForm.categoryId == null ?
              '/api/blogCategory/admin/addBlogCategory' : '/api/blogCategory/admin/updateBlogCategory'
          this.request.post(url, this.dialogForm).then(res => {
            if (res.code === 200) {
              this.$message.success("保存成功")
              this.getPageList()
              this.openDialog = false
            } else this.$message.error(res.msg)
          })
        } else {
          return false;
        }
      })
    },
    clickCancel(){
      this.resetDialogForm()
      this.openDialog = false
    },

    handleSelectionChange(selectedRows) {
      this.selectedRowIds = selectedRows.map(item => item.categoryId)
      this.multiple = !selectedRows.length
    },
    deleteRows(id){
      let ids = []
      if(id == null)
        ids = this.selectedRowIds
      else
        ids = [id]
      this.$confirm('确定要删除此'+ids.length+'条数据吗?', '系统提示', {type : 'warning'}).then(() => {
        this.request.delete('/api/blogCategory/admin/deleteBatch', {data:ids}).then(res =>{
          if(res.code === 200){
            this.$message.success("删除成功")
            this.getPageList()
          }else this.$message.error(res.msg)
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      });
    },
    //重置表单, 重置验证状态
    resetDialogForm(){
      //重置验证状态
      if(this.$refs['dialogForm']){
        this.$refs['dialogForm'].resetFields()
      }
      //重置表单
      this.dialogForm = this.$options.data().dialogForm
    },

  }
}
</script>

<style scoped>
.query-form{

}

</style>
