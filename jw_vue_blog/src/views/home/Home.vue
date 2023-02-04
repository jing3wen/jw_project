<template>
  <el-row :gutter="20">
    <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
      <!-- 左侧间隙 -->
    </el-col>
    <el-col :xs="24" :sm="22" :md="22" :lg="18" :xl="16">
      <el-row :gutter="20">
        <!-- 左侧——走马灯+文章列表  -->
        <el-col :xs="24" :sm="24" :md="18" :lg="18" :xl="18">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
              <!-- 走马灯/轮播图 -->
              <el-carousel trigger="hover" :autoplay="true"
                           class="card-hover-transition"
                           style="border-radius: 20px;">
                <el-carousel-item v-for="(img,index) in imgs" :key="index">
                  <img :src="img.url" style="width:100%;"/>
                </el-carousel-item>
              </el-carousel>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
              <!-- 文章列表 -->
              <Article></Article>
            </el-col>
          </el-row>
        </el-col>
        <!-- 右侧  -->
        <el-col class="hidden-sm-and-down" :md="6" :lg="6" :xl="6">
          <!-- 站点信息 -->
          <WebsiteInfo></WebsiteInfo>
          <!-- 搜索功能 -->
          <SearchCard></SearchCard>
          <!-- 文章分类 -->
          <ArticleCategories></ArticleCategories>
          <!-- 精选 -->
          <HotArticle></HotArticle>
        </el-col>
      </el-row>
    </el-col>
    <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
      <!-- 右侧间隙 -->
    </el-col>
  </el-row>
</template>

<script>
import SearchCard from "../../components/searchCard/SearchCard";
import ArticleCategories from "../../components/articleCategories/ArticleCategories";
import WebsiteInfo from "../../components/websiteInfo/WebsiteInfo";
import Article from "../../components/articleList/ArticleList";
import HotArticle from "../../components/hotArticle/HotArticle";

export default {
  name: "Home",
  components:{HotArticle, Article, WebsiteInfo,SearchCard,ArticleCategories},
  data(){
    return {
      imgs : [
        {name: "banner01", url: require('../../assets/image/banner/banner02.png')},
        {name: "banner02", url: require('../../assets/image/banner/banner03.png')},
        {name: "banner03", url: require('../../assets/image/banner/banner04.png')},
        {name: "banner04", url: require('../../assets/image/banner/banner05.png')},
        {name: "banner05", url: require('../../assets/image/banner/banner06.png')}
      ],

    }
  },
  created() {

  },
  methods:{

  },
}
</script>

<style scoped>
.el-col {
  overflow: hidden;
}

.el-col>div {
  border-radius: 10px;
}

.el-row {
  margin-bottom: 20px;
}
</style>
