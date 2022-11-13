<template>
  <div class="featured" v-loading="loading" element-loading-text="玩命加载中...">
    <div class="featured_title">
      <h3 class="title"><b>精选</b></h3>
    </div>
    <div class="featured_layout">
      <div v-for="(item,index) in articleInfo">
        <div class="contentBox" v-if="index===0">
          <!-- <span @click="articleDetails(item)" :title="item.articleTitle">
              <img v-if="item.articleImgLitimg" class="bannerImg" :src="item.articleImgLitimg"
                  :alt="item.articleTitle" @click="articleDetails(item)">
              <h4 class="title" @click="articleDetails(item)">{{item.articleTitle}}</h4>
              <p>{{item.articleClassifyName}} · {{item.publishTime}}</p>
          </span> -->
          <a :href="'/articleDetails/'+item.articleId" :title="item.articleTitle">
            <img v-if="item.articleImgLitimg" class="bannerImg" :src="item.articleImgLitimg"
                 :alt="item.articleTitle" @click="articleDetails(item)">
            <h4 class="title">{{item.articleTitle}}</h4>
            <p>{{item.articleClassifyName}} · {{item.publishTime}}</p>
          </a>
        </div>


        <div class="contentBox item" v-if="index>0">
          <a :href="'/articleDetails/'+item.articleId" :title="item.articleTitle">
            <div v-if="item.articleImgLitimg" class="banner">
              <img :src="item.articleImgLitimg" :alt="item.articleTitle">
            </div>
            <!-- 判断是否有缩略图，没有则样式不同 -->
            <div :class="[item.articleImgLitimg ? 'articleContent':'articleContent2']">
              <h4 class="title">{{item.articleTitle}}</h4>
              <p>{{item.articleClassifyName}} · {{item.publishTime}}</p>
            </div>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {formatDate} from "../../utils/common";

export default {
  name: "Featured",
  data() {
    return{
      // 精选文章的文章id
      featuredData: [],
      // 筛选出来的文章内容
      articleInfo: [],
      loading:false
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData(){
      //TODO 下列代码是请求  精选文章id 和 全部文章，然后从全部文章中筛选
      // 建议优化，将这部分工作在后端中完成

      this.loading = true
      // 查询系统设置中设置好的精选文章的文章id
      this.request.get("/api/showAllSystemSetup").then(res => {

        this.featuredData = JSON.parse(res.featuredArticle);
      })

      // 查询全部文章
      this.request.get("/api/showAllArticleInfo").then(res => {
        this.featuredData.forEach(val => {
          res.forEach(element => {// 筛选出系统设置中设置好的精选文章
            if (element.articleId === val) {
              // 时间戳格式化
              element.publishTime = formatDate(element.publishTime);
              // 图片 根url
              const url = process.env.VUE_APP_URL;
              // 缩略图 判断是点击上传的还是，网络图片
              if (element.articleImgLitimg != "" && !element.articleImgLitimg
                  .includes('http') && !element.articleImgLitimg
                  .includes('https')) {
                element.articleImgLitimg = url + element.articleImgLitimg
              }
              // 添加
              this.articleInfo.push(element)
            }
          });
          this.loading = false
        });
      })
    }
  }
}
</script>

<style scoped>
.featured {
  background: rgb(255, 255, 255);
  border-radius: 6px;
  padding: 20px;
  margin-bottom: 20px;
}

.featured_title {
  margin-bottom: 15px;
  margin-top: -10px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid rgb(238, 238, 238);
  overflow: hidden;
}

.featured_title.title {
  line-height: 40px;
  color: #474749;
}

.featured_title.title b {
  display: inline-block;
  border-bottom: 4px solid #2fa7b9;
}

.featured_layout .contentBox {
  margin-bottom: 15px;
  position: relative;
  overflow: hidden;
  font-size: 12px;
  color: #999;
  line-height: 18px;
}

.featured_layout .contentBox h4:hover {
  color: #2fa7b9;
}

.featured_layout .contentBox span {
  cursor: pointer;
}

.featured_layout .bannerImg {
  display: block;
  width: 100%;
  object-fit: cover;
  margin-bottom: 10px;
  border-radius: 6px;
}

.featured_layout .title {
  font-size: 15px;
  line-height: 24px;
  margin-bottom: 10px;
  color: #454545;
}

.featured_layout p {
  font-size: 12px;
  color: #999;
}

.featured_layout .contentBox.item {
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.featured_layout .contentBox.item .banner {
  width: 30%;
  height: 60px;
  overflow: hidden;
  float: left;
  border-radius: 4px;
  margin-right: 15px;
  cursor: pointer;
}

.featured_layout .contentBox.item .banner img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.featured_layout .contentBox.item .title {
  font-size: 12px;
  line-height: 18px;
  overflow: hidden;
  -webkit-box-orient: vertical;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  text-align: justify;
  cursor: pointer;
  line-height: 20px;
}

.articleContent {
  width: 55%;
  float: left;
  word-break: break-all;
  text-align: justify;
}

.articleContent2 {
  width: 100%;
  float: left;
  word-break: break-all;
  text-align: justify;
}
</style>
