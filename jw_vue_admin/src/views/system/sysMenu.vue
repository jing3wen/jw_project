<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm" v-permission="['system:sysMenu:query']">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入菜单名"
                v-model="queryForm.menuName" @keyup.enter.native="search"></el-input>
      <el-button type="primary" plain class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="primary" plain @click="openAddOrEditDialog(null)" v-permission="['system:sysMenu:add']">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)" v-permission="['system:sysMenu:delete']">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              row-key="id"
              @selection-change="handleSelectionChange"
              >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="menuName" label="名称" width="170px">
        <template slot-scope="scope">
          <span v-if="scope.row.menuType==='directory'" >{{scope.row.menuName}}</span>
          <el-tag type="primary" v-if="scope.row.menuType ==='menu'" style="font-weight:bolder">{{scope.row.menuName}}</el-tag>
          <el-tag type="danger" v-if="scope.row.menuType ==='button'" style="font-weight:bold">{{scope.row.menuName}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="图标" width="50px" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon" class="icon-size"></i>
        </template>
      </el-table-column>
      <el-table-column prop="menuSort" label="排序" width="80px" align="center">
      </el-table-column>
      <el-table-column prop="path" label="路由地址" width="200px" align="center"></el-table-column>
      <el-table-column prop="component" label="组件路径" width="200px" align="center"></el-table-column>
      <el-table-column prop="perms" label="权限按钮" width="300px" align="center">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.menuType ==='button'" style="font-weight:bold">{{scope.row.perms}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" width="300px" align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="createBy" label="创建人" width="100px" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180px" align="center"></el-table-column>
      <el-table-column prop="updateBy" label="更新人" width="100px" align="center"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="180px" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right"  width="200px" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-edit" @click="openAddOrEditDialog(scope.row)" v-permission="['system:sysMenu:update']"> 编辑</el-button>
          <el-button type="text"
                     class="el-icon-plus"
                     @click="addChild(scope.row)"
                     v-if="scope.row.menuType ==='directory'" v-permission="['system:sysMenu:add']"> 新增子菜单</el-button>
          <el-button type="text"
                     class="el-icon-plus"
                     @click="addChild(scope.row)"
                     v-if="scope.row.menuType ==='menu'" v-permission="['system:sysMenu:add']"> 新增按钮</el-button>
          <el-button type="text" class="el-icon-delete" @click="deleteRows(scope.row.id)" v-permission="['system:sysMenu:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="40%" @close="clickCancel">
      <el-card class="border-radius-25">
        <el-form :model="dialogForm" :rules="dialogFormRules" ref="dialogForm" label-width="100px" label-position="right">
          <el-form-item label="父级菜单:">
            <treeselect
                v-model="dialogForm.parentId"
                :options="menuOptions"
                :defaultExpandLevel="1"
                :searchable="true"
                :show-count="true"
                placeholder="选择上级菜单"
            />
          </el-form-item>
          <el-form-item label="名称:" prop="menuName">
            <el-input v-model="dialogForm.menuName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="类型:" prop="menuType">
            <template>
              <el-radio v-model="dialogForm.menuType" label="directory" >目录</el-radio>
              <el-radio v-model="dialogForm.menuType" label="menu">菜单</el-radio>
              <el-radio v-model="dialogForm.menuType" label="button">按钮</el-radio>
            </template>
          </el-form-item>
          <el-form-item label="显示排序:">
            <el-input-number v-model="dialogForm.menuSort" :min="1" :max="100"></el-input-number>
          </el-form-item>
          <el-form-item label="图标:" v-if="dialogForm.menuType !=='button'">
            <div>
              <el-select clearable v-model="dialogForm.icon" placeholder="请选择">
                <el-row v-for="(iconGroup, index) in formatIcons" :key="index">
                  <el-col :span="4" v-for="(item, index) in iconGroup" :key="index">
                    <el-option :key="item" :value="item">
                      <i :class="item" style="font-size:25px"></i>
                    </el-option>
                  </el-col>
                </el-row>
              </el-select>
              <span style="padding-left: 12px; font-weight: bold">图标预览:
                <i :class="dialogForm.icon"  class="icon-size pl-10"></i>
              </span>
            </div>
          </el-form-item>

          <el-row>
            <el-col :span="12">
              <el-form-item label="路由地址:" v-if="dialogForm.menuType === 'menu'">
                <el-input v-model="dialogForm.path" autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="组件地址:" v-if="dialogForm.menuType === 'menu'">
                <el-input v-model="dialogForm.component" autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="权限按钮" v-if="dialogForm.menuType === 'button'">
            <el-input v-model="dialogForm.perms" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea" v-model="dialogForm.remark" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="clickCancel">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>


<script>

import formatIcons from '@/assets/js/iconList'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {menuListToMenuOptions} from '@/assets/js/readTree'
export default {
  name: "model",
  components:{Treeselect},
  data() {
    return {
      icon:'',
      // 查询表单
      queryForm: {
        menuName: ''
      },
      formatIcons:formatIcons,
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
        parentId: 0,
        menuName: '',
        menuSort: 1,
        menuType:'',
        icon: '',
        path: '',
        component:'',
        perms:'',
        remark: '',
      },
      dialogFormRules:{
        menuName:[
          { required: true, message: "请输入菜单名", trigger: "blur" },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        menuType:[
          { required: true, message: "请选择菜单类型", trigger: "blur" },
        ],
      },
      menuOptions: [],
    }
  },
  computed:{
  },
  created() {
    this.getPageList()
    this.getMenuList()
  },
  methods: {
    getPageList(){
      this.request.post('/api/sysMenu/getPageList', this.queryForm).then(res =>{
        if(res.code === 200) {
          this.tableData = res.data
          this.tableLoading = false
        }else this.$message.error(res.msg)
      });
    },
    getMenuList(){
      this.request.get('/api/sysMenu/findAll').then(res =>{
        if(res.code === 200) {
          this.menuOptions = menuListToMenuOptions(res.data)
        }else this.$message.error(res.msg)
      });
    },

    // 搜索表单相关操作
    search(){
      this.getPageList()
    },
    reset(){
      this.queryForm.menuName=''
      this.getPageList()
    },

    //新增，批量删除 按钮组相关操作
    openAddOrEditDialog(data){
      // 注意新增前将表单置空
      this.resetDialogForm()
      if(data == null ){  //新增
        this.dialogForm.id=null
        this.dialogTitle = '添加菜单'
      }
      else {  // 编辑
        this.dialogForm = JSON.parse(JSON.stringify(data))
        this.dialogTitle = '编辑菜单'
      }
      this.openDialog = true
    },
    addOrUpdate(){
      this.$refs['dialogForm'].validate((valid)=>{
        if(valid){
          let url = this.dialogForm.id == null ? '/api/sysMenu/add':'/api/sysMenu/update'
          this.request.post(url,this.dialogForm).then(res =>{
            if(res.code === 200) {
              this.$message.success("保存成功")
              this.getMenuList()
              this.getPageList()
              this.openDialog = false
            }else this.$message.error(res.msg)
          })

        }else {
          return false
        }

      })

    },
    clickCancel(){
      this.openDialog = false
      this.resetDialogForm()
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
        this.request.post('/api/sysMenu/deleteBatch', ids).then(res =>{
          if(res.code === 200){
            this.$message.success("删除成功")
            this.getMenuList()
            this.getPageList()
          }
          else this.$message.error(res.msg)
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      });
    },

    //添加子菜单
    addChild(row){
      // this.resetDialogForm()
      this.openDialog = true
      if(row.menuType === 'directory'){
        // console.log('新增菜单:')
        this.dialogTitle = '新增菜单: '
        this.dialogForm.menuType= 'menu'
        this.dialogForm.parentId = row.id
      }else {
        // console.log('新增按钮: ')
        this.dialogTitle = '新增按钮: '
        this.dialogForm.menuType= 'button'
        this.dialogForm.parentId = row.id
      }

    },
    //重置表单
    resetDialogForm(){
      //重置验证状态
      if(this.$refs['dialogForm']){
        this.$refs['dialogForm'].resetFields()
      }
      //重置表单
      this.dialogForm = this.$options.data().dialogForm
    }

  }
}
</script>

<style scoped lang="scss">
.query-form{

}
.icon-size{
  font-size: 18px
}


</style>
