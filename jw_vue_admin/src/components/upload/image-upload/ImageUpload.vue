<template>
  <div>
    <el-card class="border-radius-25" style="width:200px; text-align: center">
      <el-upload
          :action= "f_action"
          :headers="config"
          :show-file-list="false"
          list-type="picture-card"
          :before-upload="onBeforeUpload"
          :on-success="handleUploadSuccess"
          :on-progress="uploadVideoProcess"
          v-if="!imageUrl">
        <i class="el-icon-plus"></i>
      </el-upload>
      <div class="el-upload-list el-upload-list--picture-card" v-if="imageUrl">
        <div class="el-upload-list__item is-success" style="display:flex; justify-content: center;">
          <el-image v-if="imageUrl && fileType === 'image'" :src="imageUrl" :fit="'contain'"
                    style="justify-content: center; align-items: center;"></el-image>
          <video v-if="imageUrl && fileType === 'video'" :src="imageUrl"></video>
          <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" >
              <i class="el-icon-zoom-in" @click="handleImagePreview()"></i>
            </span>
            <span class="el-upload-list__item-delete" v-if="f_removeImageUrl">
              <i class="el-icon-delete" @click="handleRemoveImage()"></i>
            </span>
          </span>
        </div>
      </div>
      <!-- 进度条 -->
      <el-progress v-if="progressFlag" :percentage="loadProgress"></el-progress>
    </el-card>
    <span v-if="f_fileTypeLimit.length !== 0">只能上传  {{f_fileTypeLimit.toString()}}  文件，且大小不超过 {{f_fileSizeLimit}} MB</span>
    <el-dialog :visible="dialogImgVisible" :append-to-body="true" @close="dialogImgVisible = false">
      <el-image :src="imageUrl" fit="'contain'" style="width:100%"></el-image>
    </el-dialog>

    <video-player :f_video-url="f_videoUrl"
                  :f_open-video.sync="f_openVideo">
    </video-player>
  </div>
</template>

<script>
import {getFileType} from "@/assets/js/common"
import VideoPlayer from "@/components/video-player/VideoPlayer";
/**
 * 图片上传组件
 */
export default {
  name: "ImageUpload",
  components:{VideoPlayer},
  props:{
    f_action: '',
    f_fileTypeLimit: {
      type: Array,
      default:[],
    },
    f_fileSizeLimit: {
      type: Number,
      default: 0,
    },
    f_imageUrl:'',
    f_removeImageUrl:'',
  },
  data() {
    return {
      dialogImgVisible : false,
      dialogImageUrl:'',
      loadProgress: 0, // 动态显示进度条
      progressFlag: false, // 关闭进度条
      fileType:'',

      //视频预览
      f_videoUrl:'',
      f_openVideo:false
    }
  },
  computed:{
    imageUrl: {
      get() {
        return this.f_imageUrl
      },
      set(val) {
        this.$emit('update:f_imageUrl', val)
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
      //上传文件没有类型限制
      if(this.f_fileTypeLimit.length === 0){
        typeLimit = true
      }else{
        let index = file.type.indexOf('/')
        let fileType = file.type.substr(index + 1,file.type.length)
        // console.log(fileType)
        typeLimit = this.f_fileTypeLimit.indexOf(fileType) !== -1 ;
      }
      //上传文件没有大小限制
      if (this.f_fileSizeLimit === 0){
        sizeLimit = true
      }else {
        sizeLimit = file.size / 1024 / 1024 < this.f_fileSizeLimit;
      }

      if (!typeLimit) {
        this.$message.error('上传文件只能是 '+this.f_fileTypeLimit.toString()+' 格式!');
        return false
      }
      if (!sizeLimit) {
        this.$message.error('上传文件大小不能超过'+this.f_fileSizeLimit+'MB!');
        return false
      }
      return typeLimit && sizeLimit;
    },
    handleUploadSuccess(res){
      this.$message.success('上传成功')
      this.fileType = getFileType(res.data)
      this.imageUrl = res.data
    },
    handleRemoveImage(){
      this.request.get(this.f_removeImageUrl,  {params:{imageUrl: this.imageUrl}}).then(res =>{
        if(res.code === 200){
          this.$message.info("已清空上传图片")
          this.imageUrl = ''
        }else this.$message.error(res.msg)
      })
    },
    //图片预览
    handleImagePreview(){
      if(this.fileType==='image') this.dialogImgVisible = true
      else if(this.fileType === 'video') {
        this.f_videoUrl = this.imageUrl
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
