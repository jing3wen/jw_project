<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm" v-permission="['system:sysLoginLog:query']">
      <el-input style="width: 200px" class="ml-5" suffix-icon="el-icon-search" placeholder="请输入操作人姓名"
                v-model="queryForm.username" @keyup.enter.native="search"></el-input>
      <el-select style="width: 200px" class="ml-5" placeholder="请选择日志状态"
                 v-model="queryForm.status" @keyup.enter.native="search">
        <el-option label="正常" value="0">正常</el-option>
        <el-option label="异常" value="1">异常</el-option>
      </el-select>
      <el-button type="primary" class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" class="ml-5" @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      <el-button class="ml-5" plain type="primary" disabled @click="excelExport">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="username" label="登录账号名" width="100px" align="center"></el-table-column>
      <el-table-column prop="loginIp" label="登录ip" width="150px" align="center"></el-table-column>
      <el-table-column prop="loginLocation" label="登陆地址"  width="200px" align="center"></el-table-column>
      <el-table-column prop="loginBrowser" label="登陆浏览器"  width="100px" align="center"></el-table-column>
      <el-table-column prop="loginOs" label="操作系统" :show-overflow-tooltip="true" width="200px" align="center"></el-table-column>
      <el-table-column prop="status" label="日志状态" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status===0">正常</el-tag>
          <el-tag type="danger" v-if="scope.row.status===1">异常</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="loginMsg" label="登录信息" width="200px" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.status===0">{{scope.row.loginMsg}}</span>
          <span v-if="scope.row.status===1" style="color:red;">{{scope.row.loginMsg}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="登录时间" min-width="180px" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" width="180px" fixed="right" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-view pl-10" @click="openAddOrEditDialog(scope.row)">详情</el-button>
          <el-button type="text" class="el-icon-delete pl-10" @click="deleteRows(scope.row.id)"
                     v-permission="['system:sysLoginLog:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
        :total="total"
        :f_pageNum.sync="queryForm.pageNum"
        :f_pageSize.sync="queryForm.pageSize"
        @pageList = "getPageList">
    </Pagination>

    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="600px" @close="clickCancel">
      <el-card class="border-radius-25">
        <el-form :model="dialogForm" ref="dialogForm" label-width="90px" label-position="right">
          <el-row>
            <el-col :span="12">
              <el-form-item label="登录账户:">
                <span>{{dialogForm.username}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-form-item label="登录ip:">
                <span>{{dialogForm.loginIp}}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="登录地址:">
            <span>{{dialogForm.loginLocation}}</span>
          </el-form-item>
          <el-form-item label="登录浏览器:">
            <span >{{dialogForm.loginBrowser}}</span>
          </el-form-item>
          <el-form-item label="操作系统:">
            <span >{{dialogForm.loginOs}}</span>
          </el-form-item>
          <el-form-item label="登录状态:">
            <el-tag type="success" v-if="dialogForm.status===0">正常</el-tag>
            <el-tag type="danger" v-if="dialogForm.status===1">异常</el-tag>
          </el-form-item>
          <el-form-item label="登录结果:">
            <span class="span-hidden-text">{{dialogForm.loginMsg}}</span>
          </el-form-item>
          <el-form-item label="操作时间:">
            <span>{{dialogForm.createTime}}</span>
          </el-form-item>
        </el-form>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="clickCancel">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import Pagination from "@/components/pagination/Pagination";

export default {
  name: "sysOperationLog",
  components:{Pagination },
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        username: '',
        status:'',
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
        username: '',
        loginIp:'',
        loginLocation:'',
        loginBrowser:'',
        loginOs:'',
        status:'',
        loginMsg:'',
        createTime:'',
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
      this.request.post('/api/sysLoginLog/getPageList',this.queryForm).then(res =>{
        if(res.code === 200) {
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
      this.queryForm.username=''
      this.queryForm.status=''
      this.getPageList()
    },
    //批量删除，数据导出 按钮组相关操作
    openAddOrEditDialog(row){
      this.dialogTitle='登录日志详情'
      this.dialogForm = row
      this.openDialog = true
    },
    clickCancel(){
      this.resetDialogForm()
      this.openDialog = false
    },
    //记录勾选的行
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
        this.request.post('/api/sysLoginLog/deleteBatch', ids).then(res =>{
          if(res.code === 200) {
            this.$message.success("删除成功")
            this.getPageList()
          }else this.$message.error(res.msg)
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      });
    },
    //重置表单
    resetDialogForm(){
      //重置表单
      this.dialogForm = this.$options.data().dialogForm
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

