<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入用户名"
                v-model="queryForm.fileName" @keyup.enter.native="search"></el-input>
      <el-select style="width: 200px" class="ml-5" suffix-icon="el-icon-search" placeholder="请选择检测状态"
                v-model="queryForm.detectStatus" @keyup.enter.native="search">
        <el-option label="检测完成" value="2">检测完成</el-option>
        <el-option label="检测中" value="1">检测中</el-option>
        <el-option label="未检测" value="0">未检测</el-option>
        <el-option label="检测异常" value="-1">检测异常</el-option>
      </el-select>
      <el-button type="primary" class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" class="ml-5"  @click="reset">重置</el-button>
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
      <el-table-column prop="fileName" label="文件名称" width="100px" align="center"></el-table-column>
      <el-table-column prop="fileType" label="文件类型" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.fileType === 'image' "><i class="el-icon-picture mr-5"/>图片</el-tag>
          <el-tag v-else-if="scope.row.fileType === 'video'"><i class="el-icon-video-camera mr-5"/>视频</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="fileAddress" label="检测文件" width="100px" align="center">
        <template slot-scope="scope">
          <el-image v-if="scope.row.fileType === 'image'"
                    :src="scope.row.fileAddress"
                    style="height: 50px"
                    :fit="'contain'"
                    :preview-src-list="[scope.row.fileAddress]">
          </el-image>
          <el-button type="text" v-if="scope.row.fileType==='video'" @click="openVideoDialog(scope.row.fileAddress)">
            <i class="el-icon-video-play"/> 播放视频
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="detectStatus" label="检测状态" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.detectStatus === 2" style="font-weight: bold">检测完成</el-tag>
          <el-tag type="success" v-if="scope.row.detectStatus === 1" style="font-weight: bold">检测中</el-tag>
          <el-tag type="info" v-if="scope.row.detectStatus === 0" style="font-weight: bold">未检测</el-tag>
          <el-tag type="danger" v-if="scope.row.detectStatus === -1" style="font-weight: bold">检测异常</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="saveResult" label="是否保存检测结果" width="140px" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.saveResult === '1'" style="font-weight:bold; color:green">是</span>
          <span v-if="scope.row.saveResult === '0'" style="font-weight:bold; color:orange">否</span>
        </template>
      </el-table-column>
      <el-table-column prop="resultFileAddress" label="检测结果文件" width="200px" align="center">
        <template slot-scope="scope">
          <el-image :src="scope.row.resultFileAddress"
                    style="height: 50px"
                    :fit="'contain'"
                    :preview-src-list="[scope.row.resultFileAddress]"
                    v-if="scope.row.detectStatus === 2 && scope.row.fileType === 'image'">
          </el-image>
          <el-button type="text" v-if="scope.row.detectStatus === 2 && scope.row.fileType==='video' " @click="openVideoDialog(scope.row.resultFileAddress)">
            <i class="el-icon-video-play"/> 播放视频
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="resultMsg" label="检测结果描述" width="300px" align="center"></el-table-column>
      <el-table-column prop="remark" label="备注" width="200px" align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="createBy" label="创建者" width="100px" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180px" align="center"></el-table-column>
      <el-table-column prop="updateBy" label="更新者" width="100px" align="center"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="180px" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right" width="200px" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-edit pl-10" @click="openAddOrEditDialog(scope.row)"> 编辑</el-button>
          <el-button type="text" class="el-icon-tickets pl-10" @click="detectFaceFile(scope.row)"> 检测</el-button>
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


    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="600px" @close="clickCancel">
      <el-card class="border-radius-25">
        <el-form :model="dialogForm" ref="dialogForm" :rules="dialogFormRules" label-width="100px">
          <el-form-item label="检测文件:" prop="fileAddress" v-if="dialogTitle === '添加文件'">
            <image-video-upload
                ref="image-upload"
                :f_action.sync="action"
                :f_file-url.sync="dialogForm.fileAddress"
                :f_remove-file-url="removeDetectFileUrl"
                :f_fileTypeLimit="detectFileTypeList"
                :f_fileSizeLimit="detectFileSizeLimit"
            >
            </image-video-upload>
          </el-form-item>
          <el-form-item label="文件类型:">
            <el-tag v-if="dialogForm.fileType === 'image' ">图片</el-tag>
            <el-tag v-else-if="dialogForm.fileType === 'video'">视频</el-tag>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="文件名:" prop="fileName">
                <el-input v-model="dialogForm.fileName" autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">

            </el-col>
          </el-row>

          <el-form-item label="保存检测结果:">
            <el-radio-group v-model="dialogForm.saveResult">
              <el-radio :label="'1'">保存(建议)</el-radio>
              <el-radio :label="'0'">不保存</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="dialogForm.remark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-form>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="clickCancel">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <video-player :f_video-url="f_videoUrl"
                  :f_open-video.sync="f_openVideo">
    </video-player>
  </el-card>
</template>

<script>
import Pagination from "@/components/pagination/Pagination";
import ImageVideoUpload from "@/components/upload/imageVideoUpload/ImageVideoUpload";
import VideoPlayer from "@/components/video-player/VideoPlayer";
import {detectFileTypeList, detectFileSizeLimit} from "@/assets/js/config";
import {getFileType} from "@/assets/js/common"

export default {
  name: "dlFaceDetectFile",
  components:{Pagination, ImageVideoUpload, VideoPlayer},
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        fileName: '',
        detectStatus: '',
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
        fileName: '',
        fileAddress: '',
        fileType: '',
        detectStatus: '',
        saveResult: '1',
        resultFileAddress: '',
      },
      dialogFormRules: {
        fileAddress: [
          { required: true, message: '请上传检测文件', trigger: 'blur' }
        ],
        fileName: [
          { required: true, message: '请输入文件名', trigger: 'blur' }
        ]
      },

      //上传检测文件组件参数
      action:"/api/dlFaceDetectFile/uploadFaceDetectFile",
      removeDetectFileUrl:'/api/dlFaceDetectFile/deleteFaceDetectFile',
      detectFileTypeList: detectFileTypeList,
      detectFileSizeLimit: detectFileSizeLimit,

      //播放视频
      f_openVideo:false,
      f_videoUrl:'',
    }
  },
  computed:{
  },
  watch:{
    'dialogForm.fileAddress'(newValue, oldValue){
      if(newValue === ''){
        this.dialogForm.fileType = ''
      }
      else {
        let fileType = getFileType(newValue)
        if (fileType){
          this.dialogForm.fileType = fileType
        }
      }
    }
  },
  created() {
    this.getPageList()
    this.queryForm.pageNum=0
  },
  methods: {
    getPageList(){
      this.request.post('/api/dlFaceDetectFile/getPageList', this.queryForm).then(res =>{
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
      this.queryForm.fileName=''
      this.queryForm.detectStatus=''
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
        this.dialogForm.saveResult = data.saveResult
        this.dialogTitle = '编辑文件'
      }
      this.openDialog = true
    },
    addOrUpdate(){
      this.$refs['dialogForm'].validate((valid) => {
        if(valid) {
          let url = this.dialogForm.id == null ? '/api/dlFaceDetectFile/add':'/api/dlFaceDetectFile/update'
          this.request.post(url,this.dialogForm).then(res =>{
            if(res.code === 200) {
              this.$message.success("保存成功")
              // 提前重置文件地址, 防止文件触发clickCancel()中的删除图片
              this.dialogForm.fileAddress = ""
              this.getPageList()
              this.openDialog = false
            }else this.$message.error(res.msg)
          })
        }else
          return false
      })
    },
    clickCancel(){
      if(this.dialogForm.fileAddress !== "" && this.dialogTitle === '添加文件'){
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
        this.request.post('/api/dlFaceDetectFile/deleteBatch', ids).then(res =>{
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
    //检测文件
    detectFaceFile(data){
      this.request.post('/api/dlFaceDetectFile/detectFaceFile', data).then(res =>{
        if(res.code === 200){
          this.$message.success("文件检测完成")
          this.getPageList()
        }else if(res.code === 202){
          this.$message.info("文件正在检测中, 请稍等")
        }
        else this.$message.error(res.msg)
      })
    },
    //播放视频
    openVideoDialog(data){
      this.f_openVideo = true
      this.f_videoUrl = data
    }

  }
}
</script>

<style scoped>
.query-form{

}

</style>
