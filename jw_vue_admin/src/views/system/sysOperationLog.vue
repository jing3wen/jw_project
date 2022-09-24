<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm" v-permission="['system:sysOperationLog:query']">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入模块名"
                v-model="queryForm.optModule" @keyup.enter.native="search"></el-input>
      <el-input style="width: 200px" class="ml-5" suffix-icon="el-icon-search" placeholder="请输入操作类型"
                v-model="queryForm.optType" @keyup.enter.native="search"></el-input>
      <el-input style="width: 200px" class="ml-5" suffix-icon="el-icon-search" placeholder="请输入操作人姓名"
                v-model="queryForm.optUser" @keyup.enter.native="search"></el-input>
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
      <el-table-column prop="optModule" label="操作模块" width="100px" align="center"></el-table-column>
      <el-table-column prop="optType" label="操作类型" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.optType==='新增'">{{scope.row.optType}}</el-tag>
          <el-tag type="warning" v-else-if="scope.row.optType==='更新'">{{scope.row.optType}}</el-tag>
          <el-tag type="danger" v-else-if="scope.row.optType==='删除'">{{scope.row.optType}}</el-tag>
          <el-tag type="info" v-else>{{scope.row.optType}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="optDesc" label="操作描述" width="150px" align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="requestMethod" label="请求类型" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.requestMethod==='GET'">{{scope.row.requestMethod}}</el-tag>
          <el-tag type="success" v-else-if="scope.row.requestMethod==='POST'">{{scope.row.requestMethod}}</el-tag>
          <el-tag type="danger" v-else-if="scope.row.requestMethod==='DELETE'">{{scope.row.requestMethod}}</el-tag>
          <el-tag type="info" v-else>{{scope.row.requestMethod}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="响应状态" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status===0">正常</el-tag>
          <el-tag type="danger" v-if="scope.row.status===1">异常</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="optUser" label="操作人" width="100px" align="center"></el-table-column>
      <el-table-column prop="optIp" label="操作ip" width="180px" align="center"></el-table-column>
      <el-table-column prop="optLocation" label="操作地点"  width="200px" align="center"></el-table-column>
      <el-table-column prop="createTime" label="操作时间" min-width="180px" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" width="180px" fixed="right" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-view pl-10" @click="openAddOrEditDialog(scope.row)">详情</el-button>
          <el-button type="text" class="el-icon-delete pl-10" @click="deleteRows(scope.row.id)"
                     v-permission="['system:sysOperationLog:delete']">删除</el-button>
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
              <el-form-item label="操作模块:">
                <span>{{dialogForm.optModule}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-form-item label="操作类型:">
                <span>{{dialogForm.optType}}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="操作描述:">
            <span>{{dialogForm.optDesc}}</span>
          </el-form-item>
          <el-form-item label="请求url:">
            <span class="span-hidden-text">{{dialogForm.requestUrl}}</span>
          </el-form-item>
          <el-form-item label="请求方式:">
            <el-tag v-if="dialogForm.requestMethod==='GET'">{{dialogForm.requestMethod}}</el-tag>
            <el-tag type="success" v-else-if="dialogForm.requestMethod==='POST'">{{dialogForm.requestMethod}}</el-tag>
            <el-tag type="danger" v-else-if="dialogForm.requestMethod==='DELETE'">{{dialogForm.requestMethod}}</el-tag>
            <el-tag type="info" v-else>{{dialogForm.requestMethod}}</el-tag>
          </el-form-item>
          <el-form-item label="请求方法:">
            <span class="span-hidden-text">{{dialogForm.optMethod}}</span>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="请求参数:">
                <span class="span-hidden-text" style="width: 180px"
                      :title="dialogForm.requestParam">{{dialogForm.requestParam}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-form-item label="响应结果:">
                <span class="span-hidden-text" style="width: 180px"
                      :title="dialogForm.responseResult">{{dialogForm.responseResult}}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="响应状态:">
            <el-tag type="success" v-if="dialogForm.status===0">正常</el-tag>
            <el-tag type="danger" v-if="dialogForm.status===1">异常</el-tag>
          </el-form-item>
          <el-form-item label="异常信息:" v-if="dialogForm.status===1">
            <span class="span-hidden-text">{{dialogForm.errorMsg}}</span>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="操作人:">
                <span>{{dialogForm.optUser}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-form-item label="操作ip:">
                <span>{{dialogForm.optIp}}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="操作地址:">
                <span>{{dialogForm.optLocation}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-form-item label="操作时间:">
                <span>{{dialogForm.createTime}}</span>
              </el-form-item>
            </el-col>
          </el-row>
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
        optModule: '',
        optType: '',
        optUser:'',
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
        optModule: '',
        optType: '',
        optDesc: '',
        requestUrl: '',
        requestMethod: '',
        optMethod:'',
        requestParam:'',
        responseResult:'',
        status:'',
        errorMsg:'',
        optUser:'',
        optIp:'',
        optLocation:'',
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
      this.request.post('/api/sysOperationLog/getPageList',this.queryForm).then(res =>{
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
      this.queryForm.optModule=''
      this.queryForm.optType=''
      this.queryForm.optUser=''
      this.queryForm.status=''
      this.getPageList()
    },
    //批量删除，数据导出 按钮组相关操作
    openAddOrEditDialog(row){
      this.dialogTitle='操作日志详情'
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
        this.request.post('/api/sysOperationLog/deleteBatch', ids).then(res =>{
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

