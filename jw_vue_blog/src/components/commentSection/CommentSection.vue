<template>
  <div class="commentBox">
    <h3>文章评论</h3>
    <p>评论出精彩，精彩原与你！</p>
    <!-- 评论回复框 -->
    <div class="comment_issue">
      <el-input @click="verifyingLogin(ref)" :readonly="readonly" v-model="commentContent" maxlength="100"
                placeholder="留下你的精彩评论吧" :autosize="{ minRows: 4, maxRows: 6 }" show-word-limit type="textarea"
                style="margin-bottom: 10px;" />
      <div style=" text-align:right">
        <el-button plain color="#2fa7b9" @click="submitComment(0, null)">提交</el-button>
      </div>
    </div>

    <!-- 显示评论 -->
    <h3>最新评论</h3>
    <p>一共有{{commentCounts}}条评论</p>
    <div class="all_comment">
      <div class="card-hover-transition comment" v-for="(item,index) in commentList" :key="index">
        <!-- 第一级评论 -->
        <div class="comment_avatar">
          <!-- 用户没设置头像的情况下使用系统默认的 -->
          <img v-if="item.avatar !== null" :src="url+item.avatar" />
          <img v-else src="../../assets/image/avatar/default_avatar.svg" alt="">
        </div>
        <div class="comment_right">
          <div class="comment_name">
            <p>{{item.nickname}}
              <span v-if="item.userId === articleInfo.userId" class="comment_author">作者</span>
            </p>
          </div>
          <div class="comment_content">
            <p>{{item.commentContent}}</p>
          </div>
          <div class="comment_else">
            <p>
              <span>{{item.createTime}}</span>
              <!-- 删除按钮   1、当前文章为当前登录用户的 2、管理员用户 -->
              <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="Delete"
                             icon-color="#F56C6C" confirm-button-type="danger" :title="'确定删除该评论吗？'"
                             @confirm="deleteComment(item.commentId)">
                <template #reference>
                  <span class="reply" v-if="userInfo.userId === articleInfo.userId || userInfo.userType === 0 || item.userId === userInfo.userId">
                    <el-icon><Delete /></el-icon>
                    删除
                  </span>
                </template>
              </el-popconfirm>
              <!-- 第一级评论的回复按钮 -->
              <span class="reply" @click="reply(item.commentId)">
                <el-icon><ChatDotSquare /></el-icon>
                <span v-if="item.commentId !== replyId">回复</span>
                <span v-if="item.commentId === replyId">取消回复</span>
              </span>
            </p>
          </div>
          <div v-show="item.commentId === replyId" class="comment_reply_box">
            <el-input v-model="replyIdContent" maxlength="100" :placeholder="'回复@' + item.nickname+ ':'"
                      :autosize="{ minRows: 4, maxRows: 6 }" show-word-limit type="textarea"
                      style="margin-bottom: 10px;" />
            <div style=" text-align:right">
              <el-button plain color="#2fa7b9" @click="submitComment(item.commentId, item.userId)">提交</el-button>
            </div>
          </div>


          <!-- 子评论 -->
          <div class="comment_child" v-if="item.toCommentList.length > 0" v-for="(child,itemIndex) in item.toCommentList"
               :key="itemIndex">
            <div class="comment_child_content">
              <div class="comment_child_left">
                <!-- 用户没设置头像的情况下使用系统默认的 -->
                <img v-if="child.avatar !== null" :src="url+child.avatar" alt=""/>
                <img v-else src="../../assets/image/avatar/default_avatar.svg" alt="">
              </div>
              <div class="comment_child_right">
                <div class="comment_child_name">
                  <p>{{child.nickname}}
                    <span v-if="child.userId === articleInfo.userId" class="comment_author">作者</span>
                    <span v-if="child.toUserId !== item.userId " style="margin:0 5px;">回复  @{{child.toNickname}}
                      <span v-if="child.toUserId === articleInfo.userId" class="comment_author">作者</span>
                    </span>
                  </p>

                </div>
                <div class="comment_content">
                  <p>{{child.commentContent}}</p>
                </div>
                <div class="comment_else">
                  <p>
                    <span>{{child.createTime}}</span>
                    <!-- 显示删除功能   1、当前文章为当前登录用户的 2、管理员用户 -->
                    <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="Delete"
                                   icon-color="#F56C6C" confirm-button-type="danger" :title="'确定删除该评论吗？'"
                                   @confirm="deleteComment(child.commentId)">
                      <template #reference>
                        <span class="reply" v-if="userInfo.userId === articleInfo.userId || userInfo.userType ===0 || child.userId === userInfo.userId">
                          <el-icon><Delete /></el-icon>
                          删除
                        </span>
                      </template>
                    </el-popconfirm>
                    <span class="reply" @click="reply(child.commentId)">
                      <el-icon><ChatDotSquare /></el-icon>
                      <span v-if="child.commentId !== replyId">回复</span>
                      <span v-if="child.commentId === replyId">取消回复</span>
                    </span>
                  </p>
                </div>
                <div v-show="child.commentId === replyId" class="comment_reply_box">
                  <el-input v-model="replyIdContent" maxlength="100"
                            :placeholder="'回复@' + child.nickname+ ':'" :autosize="{ minRows: 4, maxRows: 6 }"
                            show-word-limit type="textarea" style="margin-bottom: 10px;" />
                  <div style=" text-align:right">
                    <el-button plain color="#2fa7b9" @click="submitComment(child.parentId, child.userId)">提交</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="commentPage.commentPageSize < commentCounts" style="text-align:center;margin-top:14px">
        <el-button type="primary" @click="getMoreComment()">加载更多..</el-button>
      </div>

    </div>
  </div>
</template>

<script>
import {useRoute} from "vue-router";
import router from "@/router";


export default {
  name: "CommentSection",
  props: {
    f_articleInfo: Object
  },
  computed:{
    // 当前文章信息
    articleInfo(){
      return this.f_articleInfo
    }
  },
  data() {
    return{
      // 当前文章对应的评论信息
      commentList: [],
      commentCounts:0,
      // 评论内容
      commentContent: "",
      // 回复内容
      replyIdContent: '',
      // 回复状态
      replyId: false,
      // 评论框 只读
      readonly: true,
      // 总评论数
      // 登录信息
      userInfo: {
        userId: 7
      },
      // 服务器路径
      url: 'http://localhost:9090',

      //评论分页
      commentPage:{
        commentPageNum: 0,
        commentPageSize: 5,
      },


    }
  },
  created() {
    this.loadData()
    // 获取当前登录的用户信息
    const tokenStr = sessionStorage.getItem("userInfo");
    if (tokenStr) {
      this.userInfo = JSON.parse(tokenStr).data[0]
    }
  },
  methods: {
    loadData() {
      const params = {
        articleId: this.articleInfo.articleId,
        pageNum: this.commentPage.commentPageNum,
        pageSize: this.commentPage.commentPageSize,
      }
      this.request.get("http://localhost:9090/blogComment/front/getCommentByArticleId", {params}).then(res => {
        if (res.code === 200) {
          this.commentList = res.data.records;
          this.commentCounts = res.data.total;
        }else {
          this.$message.error(res.msg)
        }

      })
    },
    //提示登录的弹窗
    loginMessageBox(){
      let route = useRoute()
      this.$messageBox.confirm(
          '当前系统检测到您未登录，登录即可解锁更多精彩内容',
          '提示', {
            confirmButtonText: '前往登录',
            cancelButtonText: '取消',
            type: 'warning',
          }
      ).then(() => {
        router.push({
          path: '/login',
          query: {
            redirect: route.path
          } // 登录成功后返回当前页面
        })
      })
    },

    // 评论表单获得焦点 校验是否登录
    verifyingLogin(event){
      if (!this.userInfo) {
        this.loginMessageBox()
      } else {
        this.readonly = false
      }
    },

    // 回复评论
    reply(commentId){
      // 判断是否登录，没有登录前往登录界面
      if (!this.userInfo) {
        this.loginMessageBox()
      } else {
        // 关闭回复
        if (this.replyId === commentId) {
          this.replyId = false;
        }
        // 当前评论没有关闭又点击其他回复时
        else if (this.replyId !== commentId && this.replyId !== false) {
          this.replyId = commentId
        } else { // 为false时
          this.replyId = commentId
        }
      }
    },

    // 提交评论
    submitComment(commentId, toUserId){
      // 判断是否登录，没有登录前往登录界面
      if (!this.userInfo) {
        this.loginMessageBox()
      } else {
        if (this.commentContent !== "" || this.replyIdContent !== "") {
          const params = {
            // 当前文章id
            articleId: this.articleInfo.articleId,
            // 父评论  没有为0
            parentId: commentId,
            // 评论用户id
            userId: this.userInfo.userId,
            // 被评论用户id
            toUserId: toUserId,
            // 评论内容
            commentContent: commentId === 0 ? this.commentContent : this.replyIdContent,
            // TODO 审核状态： 默认通过
            commentCheck: '1'

          }
          this.request.post("http://localhost:9090/blogComment/front/addComment",
              params
          ).then(res => {
            if (res.code === 200) {
              if(params.commentCheck === '1'){
                this.$message.success("评论成功")
              }else{
                this.$message.info("评论成功, 请等待审核")
              }
              
              if (commentId === 0) {
                // 正常评论
                this.commentContent = ""
              } else {
                // 回复
                this.replyIdContent = ""
                // 回复表单
                this.replyId = false
              }
              // 刷新评论数据
              this.loadData();
            }else{
              this.$message.error(res.msg)
            }
          })
        } else {
          this.$message.error('表达你的态度再评论吧')
        }

      }
    },

    // 删除评论
    deleteComment(commentId) {
      this.request.delete("http://localhost:9090/blogComment/front/deleteComment", {params: {commentId: commentId}}).then(res => { // 删除成功
        if (res.code === 200) {
          // 刷新评论数据
          this.loadData();
          this.$message.success('删除成功')
        } else {
          this.$message.error('删除失败')
        }
      })
    },

    //查看更多评论
    getMoreComment(){
      this.commentPage.commentPageSize = this.commentPage.commentPageSize+5;
      this.loadData()
    }


  }
}
</script>

<style scoped>
.commentBox {
  height: auto;
  overflow: hidden;
  background-color: white;
  padding: 25px;
  color: #474749;
  border-radius: 10px;
  margin-bottom: 20px;
}

.commentBox>p {
  border-bottom: #dbdbdb 1px dashed;
  line-height: 30px;
  color: #888;
  font-size: 13px;
  padding-bottom: 5px;
  margin-bottom: 20px;
}

/* 评论区样式 */
.comment {
  /* width: 100%;
  float: left; */
  display: flex;
  margin-top: 14px;
  padding: 16px 20px;
  border: 1px ;
  border-radius: 10px;
}

.comment_left {
    width: 40px;
    height: auto;
    margin-right: 10px;
}

.comment_avatar>img {
  width: 40px;
  border-radius: 50px;
  object-fit: cover;
  overflow: hidden;
  margin-right: 10px;
}

.comment_right {
  /* width: 85%; */
  height: auto;
  /* float: left; */
  line-height: 25px;
}
/*第一级评论昵称*/
.comment_name {
  color: #6F6F6F;
  font-size: 14px;
  font-weight: bold;
}

.comment_author {
  padding: 0 3px;
  background: #2fa7b9;
  color: white;
  margin-left: 5px;
  border-radius: 3px;
  font-weight: normal;
  font-size: 13px;
}
/*第一级评论内容*/
.comment_content {
  font-size: 14px;
}


.comment_else {
  font-size: 12px;
  color: #b1b1b1;
  margin-bottom: 10px;
}

.comment_else span {
  margin-right: 15px;
  display: inline-flex;
  justify-content: center;
  align-items: center;
}

.comment_else span .el-icon {
  margin-right: 5px;
}

.comment_else .reply {
  color: #777777;
  cursor: pointer;
  font-size: 12px;
}

.comment_child_content {
  display: flex;
}

/* .comment_child_left {
    width: 30px;
    height: auto;
    margin-right: 8px;
} */

/*第二级评论昵称*/
.comment_child_name {
  color: #6F6F6F;
  font-size: 14px;
  font-weight: bold;
}

.comment_child_left img {
  width: 40px;
  border-radius: 50px;
  object-fit: cover;
  overflow: hidden;
  margin-right: 10px;
}

.comment_child_right {
  /* width: 90%;
  height: auto;
  float: left; */
  height: auto;
  line-height: 25px;
}
</style>
