<template>
  <div v-if="!$common.isEmpty(article)">
    <!-- 封面 -->
    <div class="article-head my-animation-slide-top">
      <!-- 背景图片 -->
      <el-image class="article-image my-el-image"
                v-once
                lazy
                :src="$constant.random_image+new Date()+Math.floor(Math.random()*10)"
                fit="cover">
        <div slot="error" class="image-slot">
          <div class="article-image"></div>
        </div>
      </el-image>
    </div>
    <!-- 文章 -->
    <div style="background: var(--background);">
      <div class="article-container my-animation-slide-bottom">
        <!-- 最新进展 -->
        <div v-if="!$common.isEmpty(treeHoleList)" class="process-wrap">
          <el-collapse accordion value="1">
            <el-collapse-item title="最新进展" name="1">
              <process :treeHoleList="treeHoleList" @deleteTreeHole="deleteTreeHole"></process>
            </el-collapse-item>
          </el-collapse>

          <hr>
        </div>
      </div>
    </div>

    <div style="background: var(--background)">
      <myFooter></myFooter>
    </div>

    <el-dialog title="最新进展"
               :visible.sync="weiYanDialogVisible"
               width="40%"
               :append-to-body="true"
               destroy-on-close
               center>
      <div>
        <div class="myCenter" style="margin-bottom: 20px">
          <el-date-picker
            v-model="newsTime"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            align="center"
            placeholder="选择日期时间">
          </el-date-picker>
        </div>
        <commentBox :disableGraffiti="true"
                    @submitComment="submitWeiYan">
        </commentBox>
      </div>
    </el-dialog>
  </div>
</template>

<script>
const myFooter = () => import( "./common/myFooter");
const process = () => import( "./common/process");
const commentBox = () => import( "./comment/commentBox");

export default {
  components: {
    myFooter,
    commentBox,
    process
  },

  data() {
    return {
      article: {},
      articleContentHtml: "",
      treeHoleList: [],
      weiYanDialogVisible: false,
      newsTime: ""
    };
  },
  created() {
    this.getArticle();
  },
  mounted() {
    // window.addEventListener("scroll", this.onScrollPage);
  },
  methods: {
    deleteTreeHole(id) {
      if (this.$common.isEmpty(this.$store.state.currentUser)) {
        this.$message({
          message: "请先登录！",
          type: "error"
        });
        return;
      }

      this.$confirm('确认删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success',
        center: true
      }).then(() => {
        this.$http.get(this.$constant.baseURL + "/weiYan/deleteWeiYan", {id: id})
          .then((res) => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.getNews();
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      }).catch(() => {
        this.$message({
          type: 'success',
          message: '已取消删除!'
        });
      });
    },
    submitWeiYan(content) {
      let weiYan = {
        content: content,
        createTime: this.newsTime,
        source: this.article.articleId
      };

      this.$http.post(this.$constant.baseURL + "/weiYan/saveNews", weiYan)
        .then((res) => {
          this.weiYanDialogVisible = false;
          this.newsTime = "";
          this.getNews();
        })
        .catch((error) => {
          this.$message({
            message: error.message,
            type: "error"
          });
        });
    },
    getNews() {
      this.$http.post(this.$constant.baseURL + "/weiYan/listNews", {
        current: 1,
        size: 9999,
        source: this.article.id
      })
        .then((res) => {
          if (!this.$common.isEmpty(res.data)) {
            res.data.records.forEach(c => {
              c.content = c.content.replace(/\n{2,}/g, '<div style="height: 12px"></div>');
              c.content = c.content.replace(/\n/g, '<br/>');
              c.content = this.$common.faceReg(c.content);
              c.content = this.$common.pictureReg(c.content);
            });
            this.treeHoleList = res.data.records;
          }
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
  padding: 40px 20px;
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
</style>
