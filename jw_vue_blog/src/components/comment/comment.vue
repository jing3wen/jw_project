<template>
  <div>
    <!-- 评论框 -->
    <div style="margin-bottom: 40px">
      <div class="comment-head">
        <i class="el-icon-edit-outline" style="font-weight: bold;font-size: 22px;"></i> 留言
      </div>
      <div>
        <!-- 文字评论 -->
        <div v-show="!isGraffiti">
          <commentBox @showGraffiti="isGraffiti = !isGraffiti"
                      @submitComment="submitComment">
          </commentBox>
        </div>
        <!-- 画笔 -->
<!--        <div v-show="isGraffiti">-->
<!--          <graffiti @showComment="isGraffiti = !isGraffiti"-->
<!--                    @addGraffitiComment="addGraffitiComment">-->
<!--          </graffiti>-->
<!--        </div>-->
      </div>
    </div>

    <!-- 评论内容 -->
    <div v-if="comments.length > 0">
      <!-- 评论数量 -->
      <div class="commentInfo-title">
        <span style="font-size: 1.15rem">Comments | </span>
        <span>{{ commentTotal }} 条留言</span>
      </div>
      <!-- 评论详情 -->
      <div id="comment-content" class="commentInfo-detail"
           v-for="(item, index) in comments"
           :key="index">
        <!-- 头像 -->
        <el-avatar shape="square" class="commentInfo-avatar" :size="35" :src="item.avatar"></el-avatar>

        <div style="flex: 1;padding-left: 12px">
          <!-- 评论信息 -->
          <div style="display: flex;justify-content: space-between">
            <div>
              <span class="commentInfo-username">{{ item.nickname }}</span>
              <span class="commentInfo-master" v-if="item.userId === userId">主人翁</span>
              <span class="commentInfo-other">{{ $common.getDateDiff(item.createTime) }}</span>
            </div>
            <div class="commentInfo-reply" @click="replyDialog(item, item)">
              <span v-if="item.replyCommentCounts > 0">{{item.replyCommentCounts}} </span><span>回复</span>
            </div>
          </div>
          <!-- 评论内容 -->
          <div class="commentInfo-content">
            <span v-html="item.commentContent"></span>
          </div>
          <!-- 回复模块 -->
          <div v-if="!$common.isEmpty(item.replyCommentList)">
            <div class="commentInfo-detail" v-for="(childItem, i) in item.replyCommentList" :key="i">
              <!-- 头像 -->
              <el-avatar shape="square" class="commentInfo-avatar" :size="30" :src="childItem.avatar"></el-avatar>

              <div style="flex: 1;padding-left: 12px">
                <!-- 评论信息 -->
                <div style="display: flex;justify-content: space-between">
                  <div>
                    <span class="commentInfo-username-small">{{ childItem.nickname }}</span>
                    <span class="commentInfo-master" v-if="childItem.userId === userId">主人翁</span>
                    <span class="commentInfo-other">{{ $common.getDateDiff(childItem.createTime) }}</span>
                  </div>
                  <div>
                    <span class="commentInfo-reply" @click="replyDialog(childItem, item)">回复</span>
                  </div>
                </div>
                <!-- 评论内容 -->
                <div class="commentInfo-content">
                  <template v-if="childItem.replyCommentId !== item.commentId &&
                                  childItem.replyUserId !== childItem.userId">
                    <span style="color: var(--blue)">@{{ childItem.replyNickname }} </span>:
                  </template>
                  <span v-html="childItem.commentContent"></span>
                </div>
              </div>
            </div>
            <!-- 分页 -->
            <div class="pagination-wrap" v-if="item.replyCommentList.length < item.replyCommentCounts">
              <div class="pagination"
                   @click="toChildPage(item)">
                展开
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 分页 -->
      <proPage :current="pagination.pageNum"
               :size="pagination.pageSize"
               :total="floorCommentTotal"
               :buttonSize="6"
               :color="$constant.commentPageColor"
               @toPage="toPage">
      </proPage>
    </div>

    <div v-else class="myCenter" style="color: var(--greyFont)">
      <i>来发第一个留言啦~</i>
    </div>

    <el-dialog title="留言"
               :visible.sync="replyDialogVisible"
               width="30%"
               :before-close="handleClose"
               :append-to-body="true"
               destroy-on-close
               center>
      <div>
        <commentBox :disableGraffiti="true"
                    @submitComment="submitReply">
        </commentBox>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  // const graffiti = () => import( "./graffiti");
  const commentBox = () => import( "./commentBox");
  const proPage = () => import( "../common/proPage");

  export default {
    components: {
      // graffiti,
      commentBox,
      proPage
    },
    props: {
      source: {
        type: Number
      },
      type: {
        type: String
      },
      userId: {
        type: Number
      }
    },
    data() {
      return {
        isGraffiti: false,
        //所有评论总数
        commentTotal: 0,
        replyDialogVisible: false,
        floorComment: {},
        replyComment: {},
        comments: [],
        pagination: {
          commentType: this.type,
          articleId: this.source,
          floorCommentId: 0,
          pageNum : 1,
          pageSize : 10,
        },
        //一级评论总数
        floorCommentTotal:0,
      };
    },
    computed: {},
    created() {
      this.getComments(this.pagination);
      this.getTotal();
    },
    methods: {
      //获取一级(二级)评论
      getComments(pagination, floorComment = {}, isToPage = false) {
        this.$http.post("/api/blogComment/front/getFrontComment", pagination)
          .then((res) => {
            if (!this.$common.isEmpty(res.data) && !this.$common.isEmpty(res.data.records)) {
              if (this.$common.isEmpty(floorComment)) {
                //查询一级评论
                this.comments = res.data.records;
                this.floorCommentTotal = res.data.total
                this.emoji(this.comments, true);
              } else {
                //查询floorComment的二级评论分页
                if (isToPage === false) {
                  //查询二级评论分页
                  floorComment.replyCommentCounts = res.data.total;
                  floorComment.replyCommentList = res.data.records;
                } else {
                  //二级评论翻页(展开)
                  floorComment.replyCommentCounts = res.data.total;
                  floorComment.replyCommentList = floorComment.replyCommentList.concat(res.data.records);
                }
                this.emoji(floorComment.replyCommentList, false);
              }
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      },
      //获取评论总数
      getTotal() {
        this.$http.get("/api/blogComment/front/getFrontCommentCounts", {articleId: this.source})
          .then((res) => {
            if (!this.$common.isEmpty(res.data)) {
              this.commentTotal = res.data;
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      },
      //一级评论翻页
      toPage(page) {
        this.pagination.pageNum = page;
        this.pagination.floorCommentId = 0;
        window.scrollTo({
          top: document.getElementById('comment-content').offsetTop
        });
        this.getComments(this.pagination);
      },
      //二级评论翻页
      toChildPage(floorComment) {
        //根据total, length，和pageSize计算出当前的pageNum
        //计算出当前current  pageSize=5
        let current = Math.floor(floorComment.replyCommentList.length / 5)
        // 下一页 current+1
        let pagination = {
          articleId: this.source,
          floorCommentId: floorComment.commentId,
          pageNum: current+1,
          pageSize: 5,
        }
        this.getComments(pagination, floorComment, true);
      },
      //渲染评论
      emoji(comments, flag) {
        comments.forEach(c => {
          c.commentContent = c.commentContent.replace(/\n/g, '<br/>');
          //渲染表情包
          c.commentContent = this.$common.faceReg(c.commentContent);
          //渲染图片
          c.commentContent = this.$common.pictureReg(c.commentContent);
          if (flag) {
            if (!this.$common.isEmpty(c.replyCommentList)) {
              c.replyCommentList.forEach(cc => {
                c.commentContent = c.commentContent.replace(/\n/g, '<br/>');
                //渲染表情包
                cc.commentContent = this.$common.faceReg(cc.commentContent);
                //渲染图片
                cc.commentContent = this.$common.pictureReg(cc.commentContent);
              });
            }
          }
        });
      },

      //回复涂鸦功能
      addGraffitiComment(graffitiComment) {
        this.submitComment(graffitiComment);
      },
      //提交评论
      submitComment(commentContent) {
        let comment = {
          commentType: this.type,
          articleId: this.source,
          userId: this.$store.state.currentUser.id,
          commentContent: commentContent,
          commentCheck: this.$store.state.webInfo.commentCheck,
        };
        this.$http.post("/api/blogComment/front/addComment", comment)
          .then((res) => {
            //重置分页数据
            this.pagination = {
              commentType: this.type,
              articleId: this.source,
              floorCommentId: 0,
              pageNum : 1,
              pageSize : 10,
            }
            this.getComments(this.pagination);
            this.getTotal();
            if(this.$store.state.webInfo.commentCheck === '0'){
              this.$message({
                type: 'warning',
                message: '管理员已开启评论审核, 审核通过后才能展示'
              });
            }else {
              this.$message({
                type: 'success',
                message: '评论成功！'
              });
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      },
      //提交回复
      submitReply(commentContent) {
        let comment = {
          commentType: this.type,
          articleId: this.source,
          userId: this.$store.state.currentUser.id,
          floorCommentId: this.floorComment.commentId,
          replyCommentId: this.replyComment.commentId,
          replyUserId: this.replyComment.userId,
          commentContent: commentContent,
          commentCheck: this.$store.state.webInfo.commentCheck,
        };
        let floorComment = this.floorComment;

        this.$http.post("/api/blogComment/front/addComment", comment)
          .then((res) => {
            //提交回复后，查询(回复评论)二级评论
            let pagination = {
              commentType: this.type,
              articleId: this.source,
              floorCommentId: floorComment.commentId,
              pageNum: 1,
              pageSize: 5,
            }
            this.getComments(pagination, floorComment);
            this.getTotal();
            if(this.$store.state.webInfo.commentCheck === '0'){
              this.$message({
                type: 'warning',
                message: '管理员已开启评论审核, 审核通过后才能展示'
              });
            }else {
              this.$message({
                type: 'success',
                message: '评论成功！'
              });
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
        this.handleClose();
      },
      replyDialog(comment, floorComment) {
        this.replyComment = comment;
        this.floorComment = floorComment;
        this.replyDialogVisible = true;
      },
      handleClose() {
        this.replyDialogVisible = false;
        this.floorComment = {};
        this.replyComment = {};
      }
    }
  }
</script>

<style scoped>

  .comment-head {
    display: flex;
    align-items: center;
    font-size: 20px;
    font-weight: bold;
    margin: 40px 0 20px 0;
    user-select: none;
    color: var(--themeBackground);
  }

  .commentInfo-title {
    margin-bottom: 20px;
    color: var(--greyFont);
    user-select: none;
  }

  .commentInfo-detail {
    display: flex;
  }

  .commentInfo-avatar {
    border-radius: 5px;
  }

  .commentInfo-username {
    color: var(--orangeRed);
    font-size: 16px;
    font-weight: 600;
    margin-right: 5px;
  }

  .commentInfo-username-small {
    color: var(--orangeRed);
    font-size: 14px;
    font-weight: 600;
    margin-right: 5px;
  }

  .commentInfo-master {
    color: var(--green);
    border: 1px solid var(--green);
    border-radius: 0.2rem;
    font-size: 12px;
    padding: 2px 4px;
    margin-right: 5px;
  }

  .commentInfo-other {
    font-size: 12px;
    color: var(--greyFont);
    user-select: none;
  }

  .commentInfo-reply {
    font-size: 12px;
    cursor: pointer;
    user-select: none;
    color: var(--white);
    background: var(--themeBackground);
    border-radius: 0.2rem;
    padding: 3px 6px;
  }

  .commentInfo-content {
    margin: 15px 0 25px;
    padding: 18px 20px;
    background: var(--commentContent);
    border-radius: 12px;
    color: var(--black);
    word-break: break-word;
  }

  .pagination-wrap {
    display: flex;
    justify-content: center;
    margin-bottom: 10px;
  }

  .pagination {
    padding: 6px 20px;
    border: 1px solid var(--lightGray);
    border-radius: 3rem;
    color: var(--greyFont);
    user-select: none;
    cursor: pointer;
    text-align: center;
    font-size: 12px;
  }

  .pagination:hover {
    border: 1px solid var(--themeBackground);
    color: var(--themeBackground);
    box-shadow: 0 0 5px var(--themeBackground);
  }
</style>
