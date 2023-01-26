<template>
  <div style="width: 400px;">
    <el-upload
        :action= "f_action"
        :headers="config"
        :show-file-list="false"
        list-type="picture-card"
        :before-upload="onBeforeUpload"
        :on-success="handleUploadSuccess"
        :on-progress="uploadVideoProcess"
        v-if="!fileUrl">
      <i class="el-icon-plus"></i>
    </el-upload>
    <div class="el-upload-list el-upload-list--picture-card" v-if="fileUrl">
      <div class="el-upload-list__item is-success" style="display:flex; justify-content: center;">
        <el-image v-if="fileUrl && fileType === 'image'" :src="fileUrl" :fit="'contain'"
                  style="justify-content: center; align-items: center;"></el-image>
        <video v-if="fileUrl && fileType === 'video'" :src="fileUrl"></video>
        <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" >
              <i class="el-icon-zoom-in" @click="handleImagePreview()"></i>
            </span>
            <span class="el-upload-list__item-delete" v-if="f_removeFileUrl">
              <i class="el-icon-delete" @click="handleRemoveImage()"></i>
            </span>
          </span>
      </div>
    </div>
    <!-- 进度条 -->
    <el-progress v-if="progressFlag" :percentage="loadProgress" style="width: 300px"></el-progress>
    <span v-if="f_fileTypeLimit.length !== 0">只能上传  {{f_fileTypeLimit.toString()}}  文件，且大小不超过 {{f_fileSizeLimit}} MB</span>
    <el-dialog :visible="dialogImgVisible" :append-to-body="true" @close="dialogImgVisible = false">
      <el-image :src="fileUrl" fit="'contain'" style="width:100%"></el-image>
    </el-dialog>

    <video-player :f_video-url="f_videoUrl"
                  :f_open-video.sync="f_openVideo">
    </video-player>
  </div>
</template>

<script>
import {getFileType, getUploadFileSizeLimitByFilename, getUploadFileTypeLimitByFilename} from "@/assets/js/common"
import VideoPlayer from "@/components/file/video-player/VideoPlayer";
/**
 * 图片上传组件
 */
export default {
  name: "ImageVideoUpload",
  components:{VideoPlayer},
  props:{
    f_action: '',  //上传文件的请求url
    f_fileTypeLimit: {  //上传文件类型限制
      type: Array,
      default:()=>([])
    },
    f_fileSizeLimit: {  //上传文件大小限制
      type: Number,
      default: -1,
    },
    f_fileUrl:'',  //文件上传成功后后端返回的文件访问路径
    f_removeFileUrl:'',  //删除该上传文件的请求url
  },
  data() {
    return {
      loadProgress: 0, // 动态显示进度条
      progressFlag: false, // 关闭进度条
      fileType:'',
      //图片预览
      dialogImgVisible : false,
      //视频预览
      f_videoUrl:'',
      f_openVideo:false
    }
  },
  computed:{
    fileUrl: {
      get() {
        this.fileType = getFileType(this.f_fileUrl);
        return this.f_fileUrl
      },
      set(val) {
        this.$emit('update:f_fileUrl', val)
      }
    },
    config(){
      return {'token' : this.$store.state.user.currentLoginUser.token};
    }
  },
  methods: {
    //上传前校验类型和大小
    onBeforeUpload(file){
      let typeLimit = false
      let sizeLimit = false
      let fileSizeLimit, fileTypeLimit;

      //没有设置上传文件类型限制时, 根据文件名加载默认配置
      if(this.f_fileTypeLimit.length === 0){
        fileTypeLimit = getUploadFileTypeLimitByFilename(file.name)
      }else {
        fileTypeLimit = this.f_fileTypeLimit
      }
      let index = file.type.indexOf('/')
      let fileType = file.type.substr(index + 1,file.type.length)
      // console.log(fileType)
      typeLimit = fileTypeLimit.indexOf(fileType) !== -1 ;

      //没有设置上传文件大小限制时, 根据文件名加载默认配置
      if (this.f_fileSizeLimit === -1){
        fileSizeLimit = getUploadFileSizeLimitByFilename(file.name)
      }else {
        fileSizeLimit = this.f_fileSizeLimit
      }
      sizeLimit = file.size / 1024 / 1024 < fileSizeLimit;

      if (!typeLimit) {
        this.$message.error('该类文件只能是 '+fileTypeLimit.toString()+' 格式!');
        return false
      }
      if (!sizeLimit) {
        this.$message.error('该类文件大小不能超过'+fileSizeLimit+'MB!');
        return false
      }
      return typeLimit && sizeLimit;
    },
    handleUploadSuccess(res){
      this.$message.success('上传成功')
      this.fileType = getFileType(res.data)
      this.fileUrl = res.data
    },
    handleRemoveImage(){
      this.request.delete(this.f_removeFileUrl,  {data:this.fileUrl}).then(res =>{
        if(res.code === 200){
          this.$message.info("已清空上传文件")
          this.fileUrl = ''
        }else this.$message.error(res.msg)
      })
    },
    //图片预览
    handleImagePreview(){
      if(this.fileType==='image') this.dialogImgVisible = true
      else if(this.fileType === 'video') {
        this.f_videoUrl = this.fileUrl
        this.f_openVideo = true
      }
    },
    //进度条
    uploadVideoProcess(event, file, fileList) {
      this.progressFlag = true; // 显示进度条
      this.loadProgress = parseInt(event.percent); // 动态获取文件上传进度
      if (this.loadProgress >= 100) {
        this.loadProgress = 100
        setTimeout( () => {this.progressFlag = false}, 1000) // 一秒后关闭进度条
      }
    },

  },
}
</script>

<style scoped>

</style>
