<template>
  <el-card >
    <!-- 搜索表单 -->
    <el-form class="query-form pl-10" :model="queryForm">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入文章类别"
                v-model="queryForm.categoryName" @keyup.enter.native="search"></el-input>
      <el-select style="width: 200px" class="ml-5" placeholder="请选择审核状态"
                 v-model="queryForm.articleCheck" @keyup.enter.native="search">
        <el-option label="待审核" value="0" style="color: dodgerblue">待审核</el-option>
        <el-option label="已通过" value="1" style="color: green">已通过</el-option>
        <el-option label="未通过" value="f" style="color: red">未通过</el-option>
      </el-select>
      <el-button type="primary" plain class="ml-5" @click="search">搜索</el-button>
      <el-button type="warning" plain class="ml-5"  @click="reset">重置</el-button>
    </el-form>

    <!-- 新增，批量删除，数据导入，导出按钮组 -->
    <div class="table-head-operate-buttons">
      <el-button type="danger" plain class="mr-5" :disabled="multiple" @click="deleteRows(null)"
                 v-permission="['blog:blogArticle:deleteArticle']">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  表格和分页  stripe斑马纹， border边框    -->
    <el-table :data="tableData"
              v-loading="tableLoading"
              stripe
              header-cell-class-name="tableHeader-style"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="nickName" label="作者名" width="120px"></el-table-column>
      <el-table-column prop="articleTitle" label="标题" width="150px" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="articleCover" label="文章封面" width="100px">
        <template slot-scope="scope">
          <el-image :src="scope.row.articleCover"
                    style="height: 50px"
                    :fit="'contain'"
                    :preview-src-list="[scope.row.articleCover]">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="categoryName" label="文章类别" width="120px"></el-table-column>
      <el-table-column prop="isTop" label="顶置">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.isTop==='1'">顶置</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="审核" prop="articleCheck" align="center" width="120px">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-s-check" v-if="scope.row.articleCheck==='0'" @click="checkArticle(scope.row)"
                     v-permission="['blog:blogArticle:checkArticle']">待审核</el-button>
          <el-tag type="success" v-if="scope.row.articleCheck==='1'">通过</el-tag>
          <el-tag type="danger" v-if="scope.row.articleCheck==='f'">未通过</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否可见" prop="articleVisible" align="center" width="120px">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.articleVisible==='0'">私密</el-tag>
          <el-tag type="success" v-if="scope.row.articleVisible==='1'">公开</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="viewCounts" label="浏览量" align="center" width="80px"></el-table-column>
      <el-table-column label="是否可评论" prop="commentAllowed" align="center" width="120px">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.commentAllowed==='0'">不能评论</el-tag>
          <el-tag type="success" v-if="scope.row.commentAllowed==='1'">可评论</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="commentCounts" label="评论数" align="center" width="80px"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180px" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作" fixed="right" width="240px" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="el-icon-top" @click="updateArticleTop(scope.row)" v-permission="['blog:blogArticle:editArticleTop']">
            <span v-if="scope.row.isTop==='0'">顶置</span>
            <span v-else>取消顶置</span>
          </el-button>
          <el-button type="text" class="el-icon-edit pl-10" @click="editArticle(scope.row.articleId)" v-permission="['blog:blogArticle:editArticle']"> 编辑</el-button>
          <el-button type="text" class="el-icon-delete pl-10" @click="deleteRows(scope.row.articleId)"
                     v-permission="['blog:blogArticle:deleteArticle']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
        :total="total"
        :f_pageNum.sync="queryForm.pageNum"
        :f_pageSize.sync="queryForm.pageSize"
        @pageList = "getPageList">
    </Pagination>

    <!--  预览文章  -->
    <el-dialog title="预览文章" :visible.sync="openDialog" width="60%">
      <mavon-editor
          v-model="previewArticle.articleContent"
          codeStyle="rainbow"
          :subfield="false"
          :defaultOpen="'preview'"
          :toolbarsFlag="false"
          style="min-height: 400px"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="danger" plain class="el-icon-close" @click="updateArticleCheck('f')">不通过</el-button>
        <el-button type="primary" plain class="el-icon-check" @click="updateArticleCheck('1')">通 过</el-button>
      </div>
    </el-dialog>
  </el-card>


</template>

<script>
import { mavonEditor  } from "mavon-editor";
import "mavon-editor/dist/css/index.css";
import Pagination from "@/components/pagination/Pagination";
import router from "../../router";

export default {
  name: "blogArticle",
  components:{Pagination, mavonEditor},
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum:0,
        pageSize:10,
        categoryName: '',
        articleCheck:'',
      },
      total:0,
      //表格数据
      tableData: [],
      //加载表格数据
      tableLoading:true,
      //选中的数据id
      selectedRowIds: [],
      // 非多个禁用
      multiple: true,

      openDialog: false,
      previewArticle:{
        articleId: null,
        articleContent:''
      },

    }
  },
  created() {
    this.getPageList()
    this.queryForm.pageNum=0
  },
  methods: {
    getPageList(){
      this.request.post('/api/blogArticle/admin/getAdminBlogArticlePage',this.queryForm).then(res =>{
        if(res.code === 200){
          this.tableData = res.data.records
          this.total = res.data.total
          this.tableLoading = false
        }else this.$message.error(res.msg)

      });
    },

    // 搜索表单相关操作
    search(){
      this.queryForm.pageNum=0
      this.getPageList()
    },
    reset(){
      this.queryForm.pageNum=0
      this.queryForm.pageSize=10
      this.queryForm.categoryName = ''
      this.queryForm.articleCheck = ''
      this.getPageList()
    },

    //编辑文章
    editArticle(articleId){
      this.$router.push({
        path:'/blog/blogUpdateArticle/'+articleId
      })
    },

    handleSelectionChange(selectedRows) {
      this.selectedRowIds = selectedRows.map(item => item.articleId)
      this.multiple = !selectedRows.length
    },
    deleteRows(id){
      let ids = []
      if(id == null)
        ids = this.selectedRowIds
      else
        ids = [id]
      this.$confirm('确定要删除此'+ids.length+'条数据吗?', '系统提示', {type : 'warning'}).then(() => {
        this.request.delete('/api/blogArticle/admin/deleteBatch', {data: ids}).then(res =>{
          if(res.code === 200){
            this.$message.success("删除成功")
            this.getPageList()
          }else this.$message.error(res.msg)
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      });
    },
    //修改文章顶置状态
    updateArticleTop(row){
      const data = {
        articleId: row.articleId,
        isTop: ''
      };
      if (row.isTop === '1') {
        data.isTop = '0'
      } else {
        data.isTop = '1'
      }
      this.request.post('/api/blogArticle/admin/updateArticleTop', data).then(res =>{
        if(res.code === 200){
          if(data.isTop === '1') {
            this.$message.success("已顶置该文章")
          } else {
            this.$message.info("已取消顶置该文章")
          }
          this.getPageList()
        }else this.$message.error(res.msg)
      })
    },
    //审核文章
    checkArticle(row){
      this.request.get("/api/blogArticle/admin/getUpdateArticle", {params: {articleId: row.articleId}}).then(res => {
        if(res.code === 200){
          this.previewArticle.articleId = res.data.articleId
          this.previewArticle.articleContent = res.data.articleContent
        }else {
          this.$message.error(res.msg)
        }
      })
      this.openDialog = true
    },
    //修改文章审核状态
    updateArticleCheck(status){
      const data = {
        articleId: this.previewArticle.articleId,
        articleCheck: status
      }
      this.request.post('/api/blogArticle/admin/updateArticleCheck', data).then(res =>{
        if(res.code === 200){
          if(data.articleCheck === '1') {
            this.$message.success("已通过该文章")
          } else {
            this.$message.info("已不通过该文章")
          }
          this.getPageList()
          this.openDialog = false
        }else this.$message.error(res.msg)
      })
    },
  }
}
</script>

<style scoped>
.query-form{

}

</style>

