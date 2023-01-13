<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入用户名"
                v-model="queryForm.name" @keyup.enter.native="search"></el-input>
      <el-button type="primary" plain class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="primary" plain @click="openAddOrEditDialog(null)">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      <el-upload
          action="/api/sysFile/excelImport"
          :show-file-list="false"
          style="display: inline-block;"
          accept="xlsx"
          :on-success="handleExcelImportSuccess"
      >
        <el-button class="ml-5" type="primary">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button class="ml-5" type="primary" @click="excelExport">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="文件名称"></el-table-column>
      <el-table-column prop="type" label="文件类型"></el-table-column>
      <el-table-column prop="size" label="文件大小(kb)"></el-table-column>
      <el-table-column label="下载">
        <template slot-scope="scope">
          <el-button type="text" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right" width="200px" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-edit pl-10" @click="openAddOrEditDialog(scope.row)"> 编辑</el-button>
          <el-button type="text" class="el-icon-delete pl-10" @click="deleteRows(scope.row.id)">删除</el-button>
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
      <el-form :model="dialogForm" rules="dialogFormRules" ref="dialogForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="dialogForm.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="dialogForm.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="dialogForm.email" autocomplete="off"></el-input>
        </el-form-item>

<!--        <el-row>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="操作模块">-->
<!--              <el-input v-model="dialogForm.optModule" autocomplete="off"></el-input>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12" style="padding-left: 12px">-->
<!--            <el-form-item label="操作类型">-->
<!--              <el-input v-model="dialogForm.optType" autocomplete="off"></el-input>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
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
  name: "model",
  components:{Pagination },
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        name: ''
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
        id:null,
        username: '',
        nickname: '',
        email:'',
      },
      dialogFormRules:{
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 2, max: 50, message: '用户名称长度必须介于 2 和 50 之间', trigger: 'blur' }
        ],
        nickname:[
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        email: [
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"]}
        ],
      },
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
      this.request.post('/api/sysFile/getPageList',this.queryForm).then(res =>{
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
      this.queryForm.name=''
      this.getPageList()
    },

    //新增，批量删除，数据导入，导出 按钮组相关操作
    openAddOrEditDialog(data){
      //重置表单状态
      this.resetDialogForm()
      if(data == null ){  //新增
        this.dialogForm.id=null
        this.dialogTitle = '添加文件'
      }
      else {
        this.dialogForm = JSON.parse(JSON.stringify(data))
        this.dialogTitle = '编辑文件'
      }
      this.openDialog = true
    },
    addOrUpdate(){
      this.$refs['dialogForm'].validate((valid) => {
        if (valid){
          let url = this.dialogForm.id == null ? '/api/sysFile/add':'/api/sysFile/update'
          this.request.post(url,this.dialogForm).then(res =>{
            if(res.code === 200) {
              this.$message.success("保存成功")
              this.getPageList()
              this.openDialog = false
            } else this.$message.error(res.msg)
          })
        }else {
          return false;
        }
      })
    },
    clickCancel(){
      // this.$message.info('操作已取消')
      this.resetDialogForm()
      this.openDialog = false
    },

    handleSelectionChange(selectedRows) {
      this.selectedRowIds = selectedRows.map(item => item.id)
      this.multiple = !selectedRows.length
    },
    deleteRows(id){
      let ids = []
      if(id == null)
        ids = this.selectedRowIds
      else
        ids = [id]
      this.$confirm('确定要删除此'+ids.length+'条数据吗?', '系统提示', {type : 'warning'}).then(() => {
        this.request.post('/api/sysFile/deleteBatch', ids).then(res =>{
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

    handleExcelImportSuccess(){
      this.$message.success("文件导入成功")
      this.getPageList()
    },
    excelExport(){
      window.open('/sysFile/excelExport')
    },

  }
}
</script>

<style scoped>
.query-form{

}

</style>
