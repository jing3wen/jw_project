<template>
  <div>
    <el-image class="el-image" :src="imgUrlList[0]" style="width: 70px" :fit="'contain'" @click="openImgViewer"></el-image>
    <div class="el-image-viewer">
      <el-image-viewer
          v-if="imgViewerVisible"
          :on-close="closeImgViewer"
          :url-list="imgUrlList"></el-image-viewer>
    </div>
  </div>
</template>

<script>
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
export default {
  name: "ImageViewer",
  components:{ElImageViewer },
  props:{
    f_imgUrlList: [],
  },
  data() {
    return {
      imgViewerVisible: false,
    }
  },
  computed:{
    imgUrlList(){
      return this.f_imgUrlList
    }
  },
  methods:{
    openImgViewer(){
      this.imgViewerVisible  = true
    },
    closeImgViewer(){
      this.imgViewerVisible = false
    }
  }
}
</script>

<style scoped>
::v-deep .el-image-viewer__btn.el-image-viewer__close {
  color: #fff;
}
.el-image {
  -webkit-transition: opacity 0.6s ease-in-out;
  -moz-transition: opacity 0.6s ease-in-out;
  -o-transition: opacity 0.6s ease-in-out;
  -ms-transition: opacity 0.6s ease-in-out;
  transition: opacity 0.6s ease-in-out;
}
</style>
