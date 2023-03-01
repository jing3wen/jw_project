<template>
  <el-card>
    <el-form style="padding-left: 10px" :model="queryForm"  v-permission="['system:sysUser:query']">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入用户名"
                v-model="queryForm.username"  @keyup.enter.native="searchUserPageList"></el-input>
      <el-input style="width: 200px" suffix-icon="el-icon-message" placeholder="请输入昵称" class="ml-5"
                v-model="queryForm.nickname" @keyup.enter.native="searchUserPageList"></el-input>
      <el-button type="primary" plain class="ml-5" @click="searchUserPageList">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="resetSearchForm">重置</el-button>
    </el-form>
    <div style="margin: 10px 5px">
      <el-button type="primary" plain @click="openAddOrEditDialog(null)" v-permission="['system:sysUser:add']">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteUser(null)" v-permission="['system:sysUser:delete']">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--    stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="100px" align="center"></el-table-column>
      <el-table-column prop="avatar" label="头像" align="center">
        <template slot-scope="scope">
          <el-avatar v-if="scope.row.avatar" :src="scope.row.avatar" shape="square"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="roleList" label="角色" width="180px" align="center">
        <template slot-scope="scope">
          <el-tag
              v-for="(item, index) of scope.row.roleList"
              :key="index"
              style="margin-right:4px;margin-top:4px"
          >
            {{item.roleName}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120px" align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="userType" label="用户类型" width="140px" align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="sex" label="性别" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.sex!=='0'">{{scope.row.sex}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="150px" align="center"></el-table-column>
      <el-table-column prop="phone" label="电话" width="150px" align="center"></el-table-column>
      <el-table-column prop="status" label="用户状态" width="150px" align="center">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.status"
              :active-value="'1'"
              :inactive-value="'0'"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="启用"
              inactive-text="停用"
              @change="changeUserStatus(scope.row)"
              v-permission="['system:sysUser:update']">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" align="center" width="200px" :show-overflow-tooltip="true" ></el-table-column>
      <el-table-column prop="createBy" label="创建人" width="100px" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180px" align="center"></el-table-column>
      <el-table-column prop="updateBy" label="更新人" width="100px" align="center"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="180px" align="center"></el-table-column>
      <el-table-column fixed="right" prop="operate" label="操作" width="280px" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-edit"
                     :disabled="scope.row.status==='0'"
                     @click="openAddOrEditDialog(scope.row)"
                     v-permission="['system:sysUser:update']"> 编辑</el-button>
          <el-button type="text" class="el-icon-s-custom"
                     :disabled="scope.row.status==='0'"
                     @click="openEditUserRoleDialog(scope.row)"
                     v-permission="['system:sysUser:editRole']">分配角色</el-button>
          <el-button type="text" class="el-icon-edit-outline"
                     :disabled="scope.row.status==='0'"
                     @click="resetPassword(scope.row)"
                     v-permission="['system:sysUser:resetPassword']">重置密码</el-button>
          <el-button type="text" class="el-icon-delete"
                     :disabled="scope.row.status==='0'"
                     @click="deleteUser(scope.row.id)"
                     v-permission="['system:sysUser:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
        :total="total"
        :f_pageNum.sync="queryForm.pageNum"
        :f_pageSize.sync="queryForm.pageSize"
        @pageList = "userPageList">
    </Pagination>

    <el-dialog  :title="dialogTitle" :visible.sync="dialogFormVisible" width="40%" @close="cancelAddOrEditUser">
      <el-card class="border-radius-25">
        <el-form :model="userForm" :rules="userFormRules" ref="userForm" label-width="90px" label-position="right" >
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户名: " prop="username">
                <el-input v-model="userForm.username" autocomplete="off" :disabled="dialogTitle==='编辑用户'" ></el-input>
                <span style="color:red; margin-top: 10px" v-if="dialogTitle==='编辑用户'">*登录名不能修改</span>
              </el-form-item>
            </el-col>
            <el-col :span="12"  style="padding-left: 12px">
              <el-form-item label="昵称: " prop="nickname">
                <el-input v-model="userForm.nickname" autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户类型:">
                <el-input v-model="userForm.userType" autocomplete="off" :disabled="dialogTitle==='编辑用户'"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12"  style="padding-left: 12px">
              <el-form-item label="性别:" prop="sex">
                <el-radio-group v-model="userForm.sex">
                  <el-radio label="男">男</el-radio>
                  <el-radio label="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="邮箱:" prop="email" >
                <el-input v-model="userForm.email" autocomplete="off" :disabled="dialogTitle==='编辑用户'"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-form-item label="电话:" prop="phone">
                <el-input v-model="userForm.phone" autocomplete="off" :disabled="dialogTitle==='编辑用户'"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="初始密码:" v-if="dialogTitle==='添加用户'" prop="password">
                <el-input show-password v-model="userForm.password"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12"  style="padding-left: 12px">

            </el-col>
          </el-row>
          <el-form-item label="备注:">
            <el-input type="textarea" v-model="userForm.remark" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelAddOrEditUser">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!--  分配角色  -->
    <el-dialog  title="分配角色" :visible.sync="dialogRoleVisible" width="30%" @close="cancelEditRole">
      <div style="margin-bottom: 10px; font-size:Medium; ">为用户:
        <el-tag type="success" style="font-size:large; font-weight:bold">
          {{editUserName}}
        </el-tag>
        分配角色(可多选)
      </div>
      <el-card class="border-radius-25">
        <span style="margin-bottom: 10px; font-size:Medium; font-weight:bold" >角色列表如下:</span>
        <el-checkbox-group v-model="roleIds">
          <el-checkbox v-for="role in roleOptions"
                       :label="role.id"
                       :key="role.id"
                       :value="role.id" style="display: block; padding-top: 10px">
            <el-tag>{{role.roleName}}</el-tag>
          </el-checkbox>
        </el-checkbox-group>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelEditRole">取 消</el-button>
        <el-button type="primary" @click="updateUserRole">确 定</el-button>
      </div>
    </el-dialog>

    <!--  重置密码  -->
    <ResetPassword
        :f_userId.sync="resetPasswordUserId"
        :f_nickname="f_nickname"
        :f_openResetPassword.sync="f_openResetPassword">
    </ResetPassword>
  </el-card>
</template>

<script>
import {initPassword} from '@/assets/js/config'
import Pagination from "@/components/pagination/Pagination";
import ResetPassword from "@/components/resetPassword/ResetPassword";
export default {
  name: "sysUser",
  components:{Pagination, ResetPassword},
  data() {
    return {
      //查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        username: '',
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

      //编辑用户信息对话框数据
      dialogFormVisible:false,
      userForm: {
        id:null,
        username: '',
        nickname: '',
        userType:'',
        sex: '',
        email: '',
        phone: '',
        password: '',
        remark:'',
      },
      userFormRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 2, max: 50, message: '用户名称长度必须介于 2 和 50 之间', trigger: 'blur' }
        ],
        nickname:[
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        sex:[
          { required: true, message: '请选择用户性别', trigger: 'blur'}
        ],
        email: [
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"]}
        ],
        phone: [
          { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}
        ],
        password:[
          { required: true, message: '请输入初始密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
      },
      dialogTitle:'',

      roleOptions:[],
      //用户分配角色对话框数据
      dialogRoleVisible:false,
      dialogRoleTitle: '',
      editUserName: '',
      editUserId:'',
      roleIds:[],

      //重置密码表单
      resetPasswordUserId:'',
      f_nickname:'',
      f_openResetPassword: false
    }
  },
  created() {
    this.userPageList()
    this.getAllRoles()
    this.queryForm.pageNum=0
  },
  methods: {
    userPageList(){
      this.request.post('/api/sysUser/getPageList',this.queryForm).then(res =>{
        if(res.code === 200) {
          this.tableData = res.data.records
          this.total = res.data.total
          this.tableLoading = false
        }else this.$message.error(res.msg)
      });
    },
    getAllRoles(){
      this.request.get('/api/sysRole/findAll').then(res =>{
        this.roleOptions = res.data
      });
    },
    //搜索表单相关操作
    searchUserPageList(){
      this.queryForm.pageNum=0
      this.userPageList()
    },
    resetSearchForm(){
      this.queryForm.pageNum=0
      this.queryForm.pageSize=10
      this.queryForm.username=''
      this.queryForm.nickname=''
      this.userPageList()
    },
    //修改用户状态
    changeUserStatus(row){
      // 注意新增前将表单置空
      this.resetUserForm()
      this.userForm = JSON.parse(JSON.stringify(row))
      this.request.post('/api/sysUser/update',this.userForm).then(res =>{
        if(res.code === 200) {
          this.$message.success("操作成功")
          this.userPageList()
        }
        else this.$message.error(res.msg)
      })
    },
    //打开 (添加/编辑) 用户对话框
    openAddOrEditDialog(row){
      // 注意新增前将表单置空
      this.resetUserForm()
      if(row == null ){  //新增
        this.userForm.id=null
        this.userForm.password = initPassword
        this.dialogTitle = '添加用户'
      }
      else {
        this.userForm = JSON.parse(JSON.stringify(row))
        this.dialogTitle = '编辑用户'
      }
      this.dialogFormVisible = true
    },
    //点击 (添加/编辑) 用户按钮
    addOrUpdate(){
      this.$refs['userForm'].validate((valid) => {
        if(valid){
          let url = this.userForm.id == null ? '/api/sysUser/add':'/api/sysUser/update'
          this.request.post(url,this.userForm).then(res =>{
            if(res.code === 200) {
              this.$message.success("操作成功")
              this.userPageList()
              this.dialogFormVisible = false
            }
            else {
              this.$message.error(res.msg)
            }
          })
        }else {
          return false;
        }
      })
    },
    //取消 (添加/编辑) 用户
    cancelAddOrEditUser(){
      this.resetUserForm()
      this.dialogFormVisible = false
    },
    //重置表单状态
    resetUserForm(){
      //重置验证状态
      if(this.$refs['userForm'] !== undefined){
        this.$refs['userForm'].resetFields()
      }
      //重置表单
      this.userForm = this.$options.data().userForm
    },

    //删除数据相关操作
    handleSelectionChange(selectedRows) {
      this.selectedRowIds = selectedRows.map(item => item.id)
      this.multiple = !selectedRows.length
    },
    deleteUser(id){
      let ids = []
      if(id == null)
        ids = this.selectedRowIds
      else
        ids = [id]
      if(ids.length === 0){
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确定要删除此'+ids.length+'条数据吗? 建议对用户停用,而不是直接删除', '系统提示', {type : 'warning'}).then(() => {
        this.request.post('/api/sysUser/deleteBatch', ids).then(res =>{
          if(res.code === 200) {
            this.$message.success("删除成功")
            this.userPageList()
          }else this.$message.error(res.msg)
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      });
    },

    //打开分配角色对话框
    openEditUserRoleDialog(row){
      //先清空数据
      this.resetEditRoleDialog()
      //打开
      this.dialogRoleVisible = true

      this.editUserId = row.id
      this.editUserName = row.nickname
      //userRoleIds用来存储用户已经分配的角色id
      let userRoleIds = []
      if(row.roleList!=null && row.roleList.length > 0){
        row.roleList.forEach(role=>{
          userRoleIds.push(role.id)
        })
      }
      //将用户已经分配的角色渲染到页面
      this.roleIds = userRoleIds
    },
    //取消分配角色操作
    cancelEditRole(){
      this.dialogRoleVisible = false
      //清空数据
      this.resetEditRoleDialog()
    },
    //为用户分配角色
    updateUserRole(){
      let data={
        userId: this.editUserId,
        roleIds: this.roleIds
      }
      this.request.post('/api/sysUser/updateUserRole', data).then(res =>{
        if(res.code === 200){
          this.$message.success('分配角色成功')
          this.userPageList();
          this.dialogRoleVisible = false
        }
        else this.$message.error(res.msg)
      })
    },
    //重置分配角色对话框
    resetEditRoleDialog(){
      this.editUserName=''
      this.editUserId=''
      this.roleIds=[]
    },

    //重置密码
    resetPassword(row){
      this.resetPasswordUserId = row.id
      this.f_nickname = row.nickname
      this.f_openResetPassword = !this.f_openResetPassword
    }

  }
}
</script>

<style>


</style>
