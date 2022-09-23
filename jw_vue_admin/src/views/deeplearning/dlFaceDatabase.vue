<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输人脸名"
                v-model="queryForm.faceName" @keyup.enter.native="search"></el-input>
      <el-button type="primary" class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="primary" plain @click="openAddOrEditDialog(null)">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      <el-button type="warning" plain class="mr-5" @click="updateFaceDatabase">更新人脸库 <i class="el-icon-refresh"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="人脸名" prop="faceName" align="center" width="100px"></el-table-column>
      <el-table-column label="人脸性别" prop="faceSex" align="center" width="80px">
        <template slot-scope="scope">
          <span v-if="scope.row.sex!=='0'">{{scope.row.sex}}</span>
        </template>
      </el-table-column>
      <el-table-column label="人脸类别" prop="faceType" align="center" width="120px"></el-table-column>
      <el-table-column label="图片名称" prop="imageName" align="center" width="150px"></el-table-column>
      <el-table-column label="图片地址" prop="imageAddress" align="center" width="100px">
        <template slot-scope="scope">
          <el-image :src="scope.row.imageAddress"
                    style="height: 50px"
                    :fit="'contain'"
                    :preview-src-list="[scope.row.imageAddress]">

          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" width="200px" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="状态" prop="status" align="center" width="180px">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.status==='0'">正常</el-tag>
          <el-tag type="success" v-if="scope.row.status==='1'">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建者" prop="createBy" align="center" width="100px"></el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180px"></el-table-column>
      <el-table-column label="更新者" prop="updateBy" align="center" width="100px"></el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180px"></el-table-column>
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


    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="40%" @close="clickCancel()">
      <el-card class="border-radius-25">
        <el-form :model="dialogForm" ref="dialogForm" :rules="dialogFormRules" label-width="100px" label-position="right">
          <el-form-item label="上传人脸" prop="imageAddress">
            <image-upload
                ref="image-upload"
                :f_action.sync="action"
                :f_image-url.sync="dialogForm.imageAddress"
                :f_remove-image-url="removeImageUrl"
                :f_file-type-limit="faceTypeList"
                :f_file-size-limit="faceSizeLimit">
            </image-upload>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="人脸名" prop="faceName">
                <el-input v-model="dialogForm.faceName" placeholder="请输入人脸名" />
              </el-form-item>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-form-item label="图片名称" prop="imageName">
                <el-input v-model="dialogForm.imageName" placeholder="请输入图片名称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="dialogForm.remark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-form>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="clickCancel()">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import Pagination from "@/components/pagination/Pagination";
import {faceSizeLimit, faceTypeList} from "@/assets/js/config";
import ImageUpload from "@/components/upload/image-upload/ImageUpload";

export default {
  name: "dlFaceDatabase",
  components:{Pagination, ImageUpload},
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        faceName: ''
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
        faceName: '',
        imageName: '',
        imageAddress: '',
        remark: '',
      },
      dialogFormRules: {
        imageAddress: [
          { required: true, message: '请上传人脸图片', trigger: 'blur' }
        ],
        faceName:[
          { required: true, message: '请输入人脸名', trigger: 'blur' }
        ],
        imageName:[
          { required: true, message: '请输入图片名', trigger: 'blur' }
        ]
      },

      //上传图片组件参数
      action:"/api/dlFaceDatabase/uploadFaceDatabase",
      removeImageUrl:'/api/dlFaceDatabase/deleteFaceImage',
      faceSizeLimit:faceSizeLimit,
      faceTypeList:faceTypeList,
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
      this.request.post('/api/dlFaceDatabase/getPageList', this.queryForm).then(res =>{
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
      this.queryForm.name=''
      this.getPageList()
    },

    //新增，批量删除，数据导入，导出 按钮组相关操作
    openAddOrEditDialog(data){
      this.resetDialogForm()
      if(data == null ){  //新增
        this.dialogForm.id=null
        this.dialogTitle = '添加人脸'
      }
      else {
        this.dialogForm = JSON.parse(JSON.stringify(data))
        this.dialogTitle = '编辑人脸'
      }
      this.openDialog = true
    },
    addOrUpdate(){
      this.$refs['dialogForm'].validate((valid) => {
        if(valid) {
          let url = this.dialogForm.id == null ? '/api/dlFaceDatabase/add':'/api/dlFaceDatabase/update'
          this.request.post(url,this.dialogForm).then(res =>{
            if(res.code === 200) {
              this.$message.success("保存成功")
              this.getPageList()
              //提前重置图片地址, 防止文件触发clickCancel()中的删除图片
              this.dialogForm.imageAddress = ""
              this.openDialog = false
            }
            else this.$message.error(res.msg)
          })
        }else {
          return false
        }
      })
    },
    clickCancel(){
      if(this.dialogForm.imageAddress !== "" && this.dialogTitle === '添加人脸'){
        //取消添加人脸需要对该图片删除
        this.$refs["image-upload"].handleRemoveImage()
      }
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
        this.request.post('/api/dlFaceDatabase/deleteBatch', ids).then(res =>{
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
    //更新人脸库
    updateFaceDatabase(){
      this.request.get('/api/dlFaceDatabase/updateFaceDatabase').then(res =>{
        if(res.code === 200){
          this.$message.success("人脸库已更新")
        }else this.$message.error(res.msg)
      })
    },
  }
}
</script>

<style scoped>
.query-form{

}
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar {
  width: 220px;
  display: block;
}
</style>
