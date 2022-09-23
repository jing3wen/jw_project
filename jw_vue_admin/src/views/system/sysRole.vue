<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm" v-permission="['system:sysRole:query']">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入角色编码"
                v-model="queryForm.roleCode" @keyup.enter.native="search"></el-input>
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入用户名" class="ml-5"
                v-model="queryForm.roleName" @keyup.enter.native="search"></el-input>
      <el-button type="primary" plain class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="primary" plain @click="openAddOrEditDialog(null)" v-permission="['system:sysRole:add']">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)" v-permission="['system:sysRole:delete']">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
<!--      <el-table-column prop="id" label="ID" width="80"></el-table-column>-->
      <el-table-column prop="roleCode" label="唯一标识" width="200px" align="center">
        <template slot-scope="scope">
          <el-tag>{{scope.row.roleCode}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleName" label="角色名" width="200px" align="center"></el-table-column>
      <el-table-column prop="remark" label="角色描述" width="300px" align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="status" label="角色状态" width="200px" align="center">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.status"
              :active-value="'1'"
              :inactive-value="'0'"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="启用"
              inactive-text="停用"
              @change="changeStatus(scope.row)"
              v-permission="['system:sysRole:update']">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createBy" label="创建人" width="100px" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180px" align="center"></el-table-column>
      <el-table-column prop="updateBy" label="更新人" width="100px" align="center"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="180px" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right" width="200px" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-edit"
                     :disabled="scope.row.status==='0'"
                     @click="openAddOrEditDialog(scope.row)"
                     v-permission="['system:sysRole:update']"> 编辑</el-button>
          <el-button type="text" class="el-icon-menu"
                     :disabled="scope.row.status==='0'"
                     @click="selectMenu(scope.row.id)"
                     v-permission="['system:sysRole:permission']"> 分配菜单</el-button>
          <el-button type="text" class="el-icon-delete"
                     :disabled="scope.row.status==='0'"
                     @click="deleteRows(scope.row.id)"
                     v-permission="['system:sysRole:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
        :total="total"
        :f_pageNum.sync="queryForm.pageNum"
        :f_pageSize.sync="queryForm.pageSize"
        @pageList = "getPageList">
    </Pagination>


    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="40%">
      <el-card class="border-radius-25">
        <el-form :model="dialogForm" ref="dialogForm" :rules="dialogFormRules" label-width="100px" label-position="right">
          <el-form-item label="唯一标识符:" prop="roleCode">
            <el-input v-model="dialogForm.roleCode" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="角色名:" prop="roleName">
            <el-input v-model="dialogForm.roleName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="角色描述:">
            <el-input type="textarea" v-model="dialogForm.remark" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="clickCancel('role')">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!--  给角色分配菜单弹窗  -->
    <el-dialog title="分配菜单" :visible.sync="menuDialogVisible" width="30%">
      <span style="margin-bottom: 10px; font-size: 15px; font-weight:bold" >
        注意: 建议给所有角色分配首页，因为用户登录后进入的初始页面为首页
      </span>
      <el-card class="border-radius-25 mt-10">
        <el-tree
            :props="menuProps"
            :data="menuData"
            show-checkbox
            check-strictly
            node-key="id"
            default-expand-all
            :default-checked-keys="menuDefaultChecks"
            ref="roleMenu"
        >
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span v-if="data.menuType==='button'">
            <el-tag type="danger"><i :class="data.icon"></i> {{ data.menuName }}</el-tag>
          </span>
          <span v-else-if="data.menuType==='menu'">
            <el-tag><i :class="data.icon"></i> {{ data.menuName }}</el-tag>
          </span>
          <span v-else><i :class="data.icon"></i> {{ data.menuName }}</span>
         </span>
        </el-tree>
      </el-card>
        <div slot="footer" class="dialog-footer">
          <el-button @click="clickCancel('roleMenu')">取 消</el-button>
          <el-button type="primary" @click="updateRoleMenu">确 定</el-button>
        </div>

    </el-dialog>

  </el-card>
</template>

<script>
import Pagination from "@/components/pagination/Pagination";
export default {
  name: "SysRole",
  components:{Pagination },
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        roleCode: '',
        roleName: '',
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
        roleCode: '',
        roleName: '',
        remark: '',
      },
      dialogFormRules:{
        roleCode: [
            { required: true, message: "请输入角色标识符", trigger: "blur" },
            { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        roleName: [
            { required: true, message: "请输入角色名", trigger: "blur" },
            { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
      },
      //Menu菜单
      checkRoleId: 0,
      menuDialogVisible: false,
      menuData:[],
      menuProps:{
        label: 'name'
      },
      menuDefaultChecks:[],
    }
  },
  computed:{
  },
  created() {
    this.getPageList()
    this.getMenuList()
    this.queryForm.pageNum=0
  },
  methods: {
    //获取所有角色列表
    getPageList(){
      this.request.post('/api/sysRole/getPageList',this.queryForm).then(res =>{
        if(res.code === 200) {
          this.tableData = res.data.records
          this.total = res.data.total
          this.tableLoading = false
        }else this.$message.error(res.msg)
      });
    },
    // 获取所有菜单列表
    getMenuList(){
      this.request.get('/api/sysMenu/findAll').then(res =>{
        if(res.code === 200) {
          this.menuData = res.data
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
      this.queryForm.roleCode =''
      this.queryForm.roleName =''
      this.getPageList()
    },
    //编辑角色状态
    changeStatus(row){
      // 注意新增前将表单置空
      this.resetDialogForm()
      this.dialogForm = JSON.parse(JSON.stringify(row))
      this.request.post('/api/sysRole/update',this.dialogForm).then(res =>{
        if(res.code === 200) {
          this.$message.success("操作成功")
          this.userPageList()
        }
        else this.$message.error(res.msg)
      })
    },
    //新增，批量删除，数据导入，导出 按钮组相关操作
    openAddOrEditDialog(data){
      // 注意新增前将表单置空
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
      this.$refs.dialogForm.validate((valid) => {
        if(valid){
          let url = this.dialogForm.id == null ? '/api/sysRole/add':'/api/sysRole/update'
          this.request.post(url,this.dialogForm).then(res =>{
            if(res.code === 200) {
              this.$message.success("保存成功")
              this.getPageList()
              this.openDialog = false
            }
            else this.$message.error(res.msg)
          })
        }else {
          return false
        }
      })
    },
    clickCancel(dialog){
      this.$message.info('数据未保存')
      if(dialog === 'role') {
        this.openDialog = false
        this.resetDialogForm()
      }
      else if(dialog === 'roleMenu') this.menuDialogVisible=false
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
      if(ids.length === 0){
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确定要删除此'+ids.length+'条数据吗?', '系统提示', {type : 'warning'}).then(() => {
        this.request.post('/api/sysRole/deleteBatch', ids).then(res =>{
          if(res.code === 200) {
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

    selectMenu(roleId){
      this.menuDialogVisible = true;
      this.checkRoleId= roleId;

      //渲染该角色已分配的菜单
      this.request.get('/api/sysRole/getRoleMenu',{params:{roleId: roleId}}).then(res=>{
        this.$nextTick(() =>{
          this.$refs.roleMenu.setCheckedKeys(res.data);
        });
      })
    },
    updateRoleMenu(){
      let data = {
        roleId: this.checkRoleId,
        menuIds :this.$refs.roleMenu.getCheckedKeys()
      }
      this.request.post('/api/sysRole/updateRoleMenu', data).then(res =>{
        if(res.code === 200) {
          this.$message.success("更新权限成功，请重新登录")
          this.checkRoleId = 0; // 此处可写可不写
          this.menuDialogVisible = false
        }else this.$message.error(res.msg)
      })
    },

  }
}
</script>

<style scoped>
.query-form{

}

</style>
