<template>
  <!-- 卡片/搜索功能 -->
  <el-card class="search_card">
    <div class="search_img_box">
      <img :src="url">
    </div>
    <div class="search_input_box">
      <!-- 搜索框 -->
      <el-input v-model="input" class="w-50 m-2 search_input" size="large" placeholder="请输入关键字进行搜索"
                @keyup.enter.native="searchContent('')">
        <template #prefix>
          <el-icon class="el-input__icon"><Search /></el-icon>
        </template>
      </el-input>
    </div>
    <div class="search_link_box">
      <p>
        <el-link type="info" :underline="false" @click="searchContent('JAVA')">JAVA</el-link>
        <el-link type="info" :underline="false" @click="searchContent('资源')">资源</el-link>
        <el-link type="info" :underline="false" @click="searchContent('VUE')">VUE</el-link>
        <el-link type="info" :underline="false" @click="searchContent('技术')">技术</el-link>
      </p>
    </div>
  </el-card>
</template>

<script>
import router from "../../router";

export default {
  name: "SearchCard",
  data(){
    return{
      url:'',
      input:'',
    }
  },
  created() {
    this.showSearchCardImg()
  },
  methods: {
    //生成随机数，用户改变搜索框上方的背景图
    showSearchCardImg(){
      const nonce = Math.floor(Math.random() * 7+1);
      const nonce2 = (nonce < 10 ? '0' + nonce : nonce);
      this.url = require('../../assets/image/banner/banner'+ nonce2 +'.png')
    },
    //搜索
    searchContent(val){
      if (val == "") {
        if (this.input !== "") {
          router.push({ path: "/searchPage", query: {keyword: this.input}})
        } else {
          this.$message.error('请输入关键字进行搜索')
        }
      } else {
        router.push({ path: "/searchPage", query: {keyword: val}})
      }
    },
  }

}
</script>

<style scoped>
.el-card {
  border-radius: 10px;
  margin-bottom: 20px;
}

:deep(.el-card__body) {
  padding: 0;
}

.search_img_box {
  width: 100%;
  overflow: hidden;
  margin-bottom: 20px;
}

.search_img_box img {
  width: 100%;
  height: 100%;
  -o-object-fit: cover;
  object-fit: cover;
}

.search_input_box {
  text-align: center;
  font-size: 12px;
  margin-bottom: 8px;
}

.search_input {
  width: 80%;
  border-radius: 50px;
}

.search_link_box {
  height: 18%;
  padding: 0px 15%;
  margin-bottom: 30px;
}

.search_link_box p {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

:deep(.el-input--large .el-input__wrapper) {
  margin-top: 0px;
  border-radius: 50px;
}

.el-link {
  margin-right: 10px;
}

.el-link .el-icon--right.el-icon {
  vertical-align: text-bottom;
}
</style>
