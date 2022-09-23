<template>
  <el-dialog :visible.sync="openVideo" width="700px" :append-to-body="true" @close="clickCancel" >
    <el-card class="border-radius-25" style="display: flex;">
      <video id="videoID"
             ref="videoPlayer"
             autoplay
             loop
             controls
             width="600px"
             :src="videoUrl"></video>
    </el-card>
  </el-dialog>

</template>

<script>
export default {
  name: "VideoPlayer",
  props: {
    f_videoUrl: {
      type: String,
      default: '',
    },
    f_openVideo:{
      type: Boolean,
      default: false,
    }
  },
  computed:{
    videoUrl:{
      get() {
        return this.f_videoUrl
      },
      set(value) {
        this.$emit('update:f_videoUrl', value)
      }
    },
    openVideo:{
      get() {
        return this.f_openVideo
      },
      set(value) {
        this.$emit('update:f_openVideo', value)
      }
    }
  },
  created(){
    // this.$refs.videoPlayer.play()
  },
  data() {
    return{

    }
  },
  methods:{
    clickCancel(){
      this.$refs.videoPlayer.pause();
      this.videoUrl= ''
      this.openVideo = false
    },

  }
}
</script>

<style scoped>

</style>