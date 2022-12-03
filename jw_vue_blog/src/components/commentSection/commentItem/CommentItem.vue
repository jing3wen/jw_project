<template>
  <div class="comment_child" v-for="(item,index) in commentChild" :key="index">
    <div class="comment_child_content">
      <div class="comment_child_left">
        <!-- 用户没设置头像的情况下使用系统默认的 -->
        <img v-if="item.userIcon !== null" :src="url+item.userIcon" />
        <img v-if="item.userIcon === null" src="../../../assets/image/avatar/default_avatar.svg" alt="">
      </div>
      <div class="comment_child_right">
        <div class="comment_child_name">
          <p>{{item.userName}}<span v-if="item.userId == authorId" class="comment_author">作者</span>
            <span style="margin:0 5px;">回复</span>@{{reply}}</p>
        </div>
        <div class="comment_content">
          <p>{{item.content}}</p>
        </div>
        <div class="comment_else">
          <p>
            <span>{{item.commentDate}}</span>
            <!-- 显示删除功能   1、当前文章为当前登录用户的 2、管理员用户 -->
            <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" :icon="Delete"
                           icon-color="#F56C6C" confirm-button-type="danger" :title="'确定删除该评论吗？'"
                           @confirm="deleteComment(item.commentId)">
              <template #reference>
                <span class="reply" v-if="userInfo.userId === authorId || userInfo.userType ===0 || item.userId === userInfo.userId">
                  <el-icon><Delete /></el-icon>
                  删除
                </span>
              </template>
            </el-popconfirm>
            <span class="reply" @click="getValue(item.commentId)">
              <el-icon><ChatDotSquare /></el-icon>
              <span v-if="item.commentId != replyIds">回复</span>
              <span v-if="item.commentId == replyIds">取消回复</span>
            </span>
          </p>
        </div>
        <div v-show="item.commentId == replyIds" class="comment_reply_box">
          <el-input v-model="replyContent" maxlength="100" :placeholder="'回复@' + item.userName+ ':'"
                    :autosize="{ minRows: 4, maxRows: 6 }" show-word-limit type="textarea"
                    style="margin-bottom: 10px;" />
          <div style=" text-align:right">
            <el-button plain color="#2fa7b9" @click="getReplyIdContent(item.commentId,replyContent)">提交
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="item.child">
      <CommentItem :commentChild="item.child" :reply="item.userName" :authorId="authorId" :replyId="replyIds"
                   @onGetValue="getValue" @onGetReplyIdContent="getReplyIdContent" :userInfo="userInfo"
                   @onDeleteComment="deleteComment"></CommentItem>
    </div>
  </div>
</template>

<script>
export default {
  name: "CommentItem",
  props: {
    commentChild: { // 子评论
      type: Array
    },
    reply: { // 回复  ##
      type: String
    },
    authorId: { // 作者id
      type: Number
    },
    replyId: { // 点击回复id

    },
    articleInfo: { // 当前文章信息  ##

    }
  },
  data() {
    return{
      replyIds: false,
      replyContent: '',
      userInfo: {},
      articleInfo: {
        userId: '',
      },
      // 服务器路径
      url: process.env.VUE_APP_URL,
    }
  },
  created() {
    var tokenStr = sessionStorage.getItem("userInfo");
    if (tokenStr) {
      this.userInfo = JSON.parse(tokenStr).data[0]
    }
  },
  methods: {

    getValue(id){
      this.$emit('onGetValue', id)
    },
    getReplyIdContent(parentId, replyContent) {
      this.$emit('onGetReplyIdContent', parentId, replyContent)
      this.replyContent = ""
    },
    deleteComment(commentId){
      this.$emit('onDeleteComment', commentId)
    }

  }

}
</script>

<style scoped>

</style>
