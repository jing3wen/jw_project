<template>
  <!-- 精选 -->
  <div class="featured card-hover-transition" v-loading="loading" element-loading-text="玩命加载中...">
    <div class="featured_title">
      <h3 class="title"><b>热门文章</b></h3>
    </div>
    <div class="featured_layout">
      <div v-for="(item,index) in hotArticleList">
        <div class="contentBox" v-if="index===0">
          <a :href="'/articleDetails/'+item.articleId" :title="item.articleTitle">
            <img v-if="item.articleCover" class="bannerImg" :src="item.articleCover"
                 :alt="item.articleTitle" >
              <!--        @click="articleDetails(item)"    -->
            <h4 class="title">{{item.articleTitle}}</h4>
            <div>
              <el-icon style="vertical-align: -10%"><Calendar /></el-icon> {{item.createTime}}
            </div>
          </a>
        </div>

        <div class="contentBox item" v-if="index>0">
          <a :href="'/articleDetails/'+item.articleId" :title="item.articleTitle">
            <div v-if="item.articleCover" class="banner">
              <img :src="item.articleCover" :alt="item.articleTitle">
            </div>
            <!-- 判断是否有封面，没有则样式不同 -->
            <div :class="[item.articleCover ? 'articleContent':'articleContent2']">
              <h4 class="title">{{item.articleTitle}}</h4>
              <p>
                <el-icon style="vertical-align: -10%"><Calendar /></el-icon> {{item.createTime}}
              </p>
            </div>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "HotArticle",
  data() {
    return{
      // 热门文章
      hotArticleList: [],
      loading:false
    }
  },
  created() {
    this.getHotArticle()
  },
  methods: {
    getHotArticle(){
      //TODO 下列代码是请求  精选文章id 和 全部文章，然后从全部文章中筛选
      // 建议优化，将这部分工作在后端中完成
      this.loading = true
      // 查询系统设置中查询热门文章id
      this.request.get("/api/blogArticle/front/getHotArticle").then(res => {
        if(res.code === 200) {
          this.hotArticleList = res.data
        }else {
          this.$message.error(res.msg)
        }
        this.loading = false
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
  text-overflow: ellipsis;
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
  height: 120px;
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
