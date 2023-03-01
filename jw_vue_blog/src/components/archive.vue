<template>
  <div>
    <!-- 封面 -->
    <div class="article-head my-animation-slide-top">
      <!-- 背景图片 -->
      <el-image class="article-image my-el-image"
                v-once
                lazy
                :src="require('@/assets/file/backimage/archiveBG.jpg')"
                fit="cover">
        <div slot="error" class="image-slot">
          <div class="article-image"></div>
        </div>
      </el-image>
    </div>
    <!-- 文章归档 -->
    <div style="background: var(--background);">
      <div class="article-container my-animation-slide-bottom">
        <!--  -->
        <div v-if="!$common.isEmpty(archiveList)" class="process-wrap">
          <el-collapse accordion value="1">
            <el-collapse-item title="文章归档" name="1">
              <div class="process-line">
                <div class="process-item"
                     v-for="(archive, index) in archiveList"
                     :key="index">
                  <div class="timeline-item-time">
                    <span>
                      <el-tag type="warning" v-if="archive.articleVisible === '0' && $store.state.currentUser!=null">(私密)🤫</el-tag>
                      {{$common.getDateDiff(archive.createTime)}}
                    </span>
                  </div>
                  <div class="item-content"
                       @click="$router.push({path: '/article', query: {id: archive.articleId}})">
                    <div class="timeline-item-content" v-html="archive.articleTitle"></div>
                  </div>

                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
          <hr>
        </div>
      </div>
    </div>
    
    <div style="background: var(--background)">
      <myFooter></myFooter>
    </div>
  </div>
</template>

<script>
const myFooter = () => import( "./common/myFooter");
const process = () => import( "./common/process");


export default {
  components: {
    myFooter,

  },

  data() {
    return {
      archiveList: [],
      weiYanDialogVisible: false,
      newsTime: "",
      pagination: {
        pageNum: 1,
        pageSize: 10
      },
      total:0,
    };
  },
  created() {
    this.getArchive();
  },
  mounted() {
    // window.addEventListener("scroll", this.onScrollPage);
  },
  methods: {
    getArchive() {
      this.$http.get("http://localhost:9090/blogArticle/front/getArticleArchive", this.pagination)
        .then((res) => {
          this.archiveList = res.data.records;
          this.total = res.data.total;
        })
        .catch((error) => {
          this.$message({
            message: error.message,
            type: "error"
          });
        });
    },

  }
}
</script>

<style scoped>

.article-head {
  height: 40vh;
  position: relative;
}

.article-image::before {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: var(--miniMask);
  content: "";
}

.article-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 10px 20px;
}


blockquote {
  line-height: 2;
  border-left: 0.2rem solid var(--blue);
  padding: 10px 1rem;
  background-color: var(--azure);
  border-radius: 4px;
  margin: 0 0 40px 0;
  user-select: none;
  color: var(--black);
}


.process-wrap {
  margin: 0 0 40px;
}

.process-wrap hr {
  position: relative;
  margin: 10px auto 60px;
  border: 2px dashed var(--lightGreen);
  overflow: visible;
}

.process-wrap hr:before {
  position: absolute;
  top: -14px;
  left: 5%;
  color: var(--lightGreen);
  content: '❄';
  font-size: 30px;
  line-height: 1;
  transition: all 1s ease-in-out;
}

.process-wrap hr:hover:before {
  left: calc(95% - 20px);
}

.process-wrap >>> .el-collapse-item__header {
  border-bottom: unset;
  font-size: 20px;
  background-color: var(--background);
  color: var(--lightGreen);
}

.process-wrap >>> .el-collapse-item__wrap {
  background-color: var(--background);
}

.process-wrap .el-collapse {
  border-top: unset;
  border-bottom: unset;
}

.process-wrap >>> .el-collapse-item__wrap {
  border-bottom: unset;
}

@media screen and (max-width: 700px) {
  .article-info-container {
    left: 20px;
    max-width: 320px;
  }

  .article-info-news {
    right: 20px;
  }
}


.process-line {
  border-left: 2px solid var(--lightGreen);
  padding: 50px 20px 10px;
  margin-left: 20px;
  position: relative;
}

.process-line:before {
  content: '';
  width: 8px;
  height: 8px;
  border: 4px solid var(--maxLightRed);
  border-radius: 50%;
  position: absolute;
  top: 15px;
  left: -1px;
  transform: translateX(-50%);
  background-color: var(--white);
  animation: weiYanShadowFlashing 1.5s linear infinite;
}

.process-item {
  position: relative;
  margin: 10px;
  color: var(--fontColor);
}


.timeline-item-time::before {
  position: absolute;
  top: 5px;
  left: -37px;
  width: 6px;
  height: 6px;
  border: 3px solid var(--blue);
  border-radius: 50%;
  background: var(--white);
  content: '';
}

.item-content :hover{
  cursor: pointer;/*鼠标变成手指样式*/
  transition: all 1s;/*所有属性变化在0.6秒内执行动画*/
  transform: scale(1.05);
}
.timeline-item-content {
  padding: 12px 15px;
  margin: 10px 0 15px;
  border-radius: 10px;
  background: rgba(66, 139, 202, 0.2);
}
</style>
