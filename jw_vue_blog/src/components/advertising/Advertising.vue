<template>
  <div class="adBox">
    <a :href="advertisingLink" target="_blank">
      <img :src="advertisingImg" alt="">
    </a>
  </div>
</template>

<script>
export default {
  name: "Advertising",
  props:{
    f_advertisingLink:'',
    f_advertisingImg:''
  },
  computed:{
    advertisingLink(){
      return this.f_advertisingLink
    },
    advertisingImg(){
      return this.f_advertisingImg
    }
  }
}
</script>

<style scoped>
.adBox {
  width: 100%;
  background: white;
  height: auto;
  border-radius: 6px;
  margin-bottom: 20px;
}

.adBox img {
  display: block;
  width: 100%;
  height: auto;
  border-radius: 6px;
}
</style>
