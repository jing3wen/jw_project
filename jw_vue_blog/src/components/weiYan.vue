<template>
  <div>
    <!-- 两句诗 -->
    <div class="my-animation-slide-top">
      <twoPoem :isHitokoto="false"></twoPoem>
    </div>

    <div style="background: var(--background);animation: hideToShow 2.5s">
      <div>
        <treeHole :treeHoleList="treeHoleList" @launch="launch" @deleteTreeHole="deleteMoments"></treeHole>
        <proPage :current="pagination.pageNum"
                 :size="pagination.pageSize"
                 :total="total"
                 :buttonSize="3"
                 :color="$constant.pageColor"
                 @toPage="toPage">
        </proPage>
      </div>

      <!-- 页脚 -->
      <myFooter :showFooter="showFooter"></myFooter>
    </div>

    <el-dialog title="微言"
               :visible.sync="weiYanDialogVisible"
               width="40%"
               :before-close="handleClose"
               :append-to-body="true"
               destroy-on-close
               center>
      <div>
        <div class="myCenter" style="padding-bottom: 20px">
          <el-radio-group v-model="isPublic">
            <el-radio-button :label="'1'">公开</el-radio-button>
            <el-radio-button :label="'0'">私密</el-radio-button>
          </el-radio-group>
        </div>
        <commentBox :disableGraffiti="true"
                    @submitComment="submitMoments">
        </commentBox>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  const twoPoem = () => import( "./common/twoPoem");
  const myFooter = () => import( "./common/myFooter");
  const treeHole = () => import( "./common/treeHole");
  const proPage = () => import( "./common/proPage");
  const commentBox = () => import( "./comment/commentBox");

  export default {
    components: {
      twoPoem,
      myFooter,
      treeHole,
      proPage,
      commentBox
    },

    data() {
      return {
        treeHoleList: [],
        pagination: {
          viewMe:false,
          pageNum: 1,
          pageSize: 10
        },
        total:0,
        weiYanDialogVisible: false,
        isPublic: '1',
        showFooter: false
      }
    },

    computed: {},

    watch: {},

    created() {
      this.getMoments();
    },

    mounted() {

    },

    methods: {
      toPage(page) {
        this.pagination.pageNum = page;
        window.scrollTo({
          top: 240,
          behavior: "smooth"
        });
        this.getMoments();
      },
      launch() {
        if (this.$common.isEmpty(this.$store.state.currentUser)) {
          this.$message({
            message: "请先登录！",
            type: "error"
          });
          return;
        }

        if (this.$common.isEmpty(this.$store.state.currentUser.email)) {
          this.$message({
            message: "请先绑定邮箱！",
            type: "error"
          });
          return;
        }

        this.weiYanDialogVisible = true;
      },
      handleClose() {
        this.weiYanDialogVisible = false;
      },
      submitMoments(content) {
        let moments = {
          userId: this.$store.state.currentUser.id,
          momentsContent: content,
          isPublic: this.isPublic
        };
        this.$http.post("http://localhost:9090/blogMoments/front/addMoments", moments)
          .then((res) => {
            this.$message({
              type: 'success',
              message: '发布成功!'
            });
            this.getMoments();
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
        this.handleClose();
      },
      deleteMoments(id) {
        if (this.$common.isEmpty(this.$store.state.currentUser)) {
          this.$message({
            message: "请先登录！",
            type: "error"
          });
          return;
        }

        if (this.$common.isEmpty(this.$store.state.currentUser.email)) {
          this.$message({
            message: "请先绑定邮箱！",
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
          this.$http.delete("http://localhost:9090/blogMoments/front/deleteBatch", [id])
            .then((res) => {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.pagination.pageNum = 1;
              this.getMoments();
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
      getMoments() {
        this.$http.post("http://localhost:9090/blogMoments/front/getFrontMomentsPage", this.pagination)
          .then((res) => {
            this.showFooter = false;
            if (!this.$common.isEmpty(res.data)) {
              res.data.records.forEach(c => {
                c.momentsContent = c.momentsContent.replace(/\n{2,}/g, '<div style="height: 12px"></div>');
                c.momentsContent = c.momentsContent.replace(/\n/g, '<br/>');
                c.momentsContent = this.$common.faceReg(c.momentsContent);
                c.momentsContent = this.$common.pictureReg(c.momentsContent);
              });
              this.treeHoleList = res.data.records;
              this.total = res.data.total;
            }
            this.$nextTick(() => {
              this.showFooter = true;
            });
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      }
    }
  }
</script>

<style scoped>

</style>
