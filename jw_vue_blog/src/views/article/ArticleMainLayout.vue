<template>
  <el-row :gutter="20">
    <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
      <!-- 左侧间隙 -->
    </el-col>
    <el-col :xs="24" :sm="22" :md="22" :lg="18" :xl="16">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="24" :md="18" :lg="18" :xl="18">
          <!-- 主要内容 -->
          <Article></Article>
        </el-col>
        <el-col class="hidden-sm-and-down" :md="6" :lg="6" :xl="6">
          <!-- 其他内容 -->
          <!--&lt;!&ndash; 站点信息 &ndash;&gt;-->
          <WebsiteInfo></WebsiteInfo>
          <!-- 搜索功能 -->
          <SearchCard></SearchCard>
          <!-- 文章分类 -->
          <ArticleCategories></ArticleCategories>
          <!-- 精选文章 -->
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
import SearchCard from "@/components/searchCard/SearchCard";
import ArticleCategories from "@/components/articleCategories/ArticleCategories";
import WebsiteInfo from "@/components/websiteInfo/WebsiteInfo";
import Article from "../../components/articleList/ArticleList";
import HotArticle from "../../components/hotArticle/HotArticle";

export default {
  name: "ArticleMain",
  components:{HotArticle, Article, WebsiteInfo, ArticleCategories, SearchCard}
}
</script>

<style scoped>

</style>
