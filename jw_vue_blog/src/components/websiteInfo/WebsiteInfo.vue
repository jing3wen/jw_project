<template>
  <div class="websiteInfo card-hover-transition">
    <div class="avatar">
      <el-avatar :size="80" :src="websiteInfo.webAvatar" ></el-avatar>
    </div>
    <div class="personal-signature">
      <span style="font-size: 28px; font-weight: bold;">{{ websiteInfo.webName }}</span>
      <br>
      <span style="font-size: 14px">{{websiteInfo.webSignature}}</span>
    </div>
    <div class="blog-info-wrapper">
      <div class="blog-info-data">
        <router-link to="">
          <div style="font-size: 0.875rem">文章</div>
          <div style="font-size: 1.125rem;">
            {{websiteInfo.articleCount}}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link to="">
          <div style="font-size: 0.875rem">分类</div>
          <div style="font-size: 1.125rem">
            {{websiteInfo.categoryCount}}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link to="">
          <div style="font-size: 0.875rem">标签</div>
          <div style="font-size: 1.125rem">
            {{websiteInfo.tagCount}}
          </div>
        </router-link>
      </div>
    </div>

  </div>
</template>

<script>
import fileUrl from "../../assets/image/avatar/nbxx.jpg"
export default {
  name: "WebsiteInfo",
  data(){
    return {
      //网站信息
      websiteInfo: {
        webAvatar:fileUrl,
        webName: 'jw博客',
        webSignature: 'jw博客子系统',
        articleCount: 4,
        webCreateTime:'',
        categoryCount:5,
        tagCount:10,
      },
      distance:'',
    }
  },
  created() {
  },
  methods: {
    showWebSite(){
      // 查询通过审核的文章总数量
      this.request.get("/article/showArticleCount").then(res => {
        this.articleCount.value = res.data < 10 ? "0" + res.data : res.data
      })


      // var day = Math.floor(new Date().getTime() / 1000) - (new Date('2022/07/20 00:00:00').getTime() / 1000),
      //     day2 = Math.floor(day / (24 * 3600)),
      //       day3 = day2 * 24 * 3600,
      //       day4 = day - day3,
      //       day5 = Math.floor(day4 / 3600),
      //       day6 = day4 - day5 * 3600,
      //       day7 = Math.floor(day6 / 60),
      //       day8 = day6 - day7 * 60;
      // this.distance = `${day2} 天 ${day5} 时 ${day7} 分 ${day8} 秒`

    }
  }
}
</script>

<style scoped>
.websiteInfo {
  padding: 20px;
  border-radius: 10px;
  background-color: white;
  margin-bottom: 20px;
  overflow: hidden;
}
.avatar{
  text-align: center;
  margin: auto;
  cursor: pointer;
  transition: all 0.6s;
}
.avatar:hover{
  transform: scale(1.2);
}
.personal-signature{
  margin-top: 10px;
  text-align: center;
  font-family: '华文楷体'
}

.blog-info-wrapper {
  display: flex;
  align-items: center;
  padding: 12px 10px 0;
}
.blog-info-data {
  flex: 1;
  line-height: 2;
  text-align: center;
}


.websiteInfo li {
  margin-bottom: 8px;
  font-size: 15px;
}

</style>
