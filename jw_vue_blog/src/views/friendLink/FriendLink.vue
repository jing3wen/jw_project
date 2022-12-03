<template>
  <div class="main_centent" v-loading="loading" element-loading-text="玩命加载中...">
    <div>
      <div class="centent_title">
        <h3>友情链接</h3>
      </div>
      <div class="centent_box friendshipLink">
        <el-row :gutter="20">
          <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
            <a href="http://www.bpvank.com">
              <el-card class="card_centent">
                <div class="card_img">
                  <img src="../../assets/image/friendLink/search_bg.jpg" />
                </div>
                <div class="link_text">
                  <p>Bpvank - 每天都是不一样</p>
                  <span>一个分享学习笔记、个人生活的秘密基地.</span>
                </div>
              </el-card>
            </a>
          </el-col>
          <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6" v-for="item in friendshipLinkInfo">
            <a :href="item.urlAddres" target="_blank">
              <el-card class="card_centent">
                <div class="card_img">
                  <img :src="url+item.urlLitimg" />
                </div>
                <div class="link_text">
                  <p>{{item.urlName}}</p>
                  <span>{{item.urlReferral}}</span>
                </div>
              </el-card>
            </a>
          </el-col>
        </el-row>
      </div>
    </div>
    <div>
      <div class="centent_title">
        <h3>推荐链接</h3>
      </div>
      <div class="centent_box recommendLink">
        <el-row :gutter="20">
          <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6" v-for="item in recommendLinkInfo">
            <a :href="item.urlAddres" target="_blank">
              <el-card class="card_centent">
                <div class="card_img">
                  <img :src="url+item.urlLitimg" />
                </div>

                <div class="link_text">
                  <p>{{item.urlName}}</p>
                  <span>{{item.urlReferral}}</span>
                </div>
              </el-card>
            </a>
          </el-col>
        </el-row>
      </div>
    </div>
    <div>
      <div class="centent_title">
        <h3>申请说明</h3>
      </div>
      <div class="centent_box">
        <h4>推荐链接申请：</h4>
        <p>1. 不强制要求互链；</p>
        <p>2. 对Google Pagerank和百度权重等SEO指标有要求；</p>
        <p>3. 网站内容健康积极向上，全技术、全资源类网站。</p>
        <p><strong>注</strong>：申请结果系统将会自动下发至站长邮箱，注意查收。</p>

        <h4>友情链接申请：</h4>
        <p>1. 申请前请将本站添加为友链，符合要求的本站会在12小时内跟上友链；</p>
        <p>2. 对Google Pagerank和百度权重等SEO指标无要求；</p>
        <p>3. 不接受内容含有淘宝客、色情、纯商业以及违反中华人民共和国法律的网站；</p>
        <p>4. 优先考虑收录原创个人博客，日记，技术类和生活类博客；</p>
        <p>5. 如若发现互换友链后未经通知私自撤换友链者，本站将拉黑并永不交换；</p>
        <p>6. 不定时互访，对于长时间无法访问的站点，会撤下友链，恢复后重新申请。</p>
        <p><strong>注</strong>：申请结果系统将会自动下发至站长邮箱，注意查收。</p>
      </div>
    </div>

    <div>
      <div class="centent_title">
        <h3>申请链接</h3>
      </div>
      <div class="centent_box">
        <el-form ref="ruleFormRef" :model="form" :rules="rules">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
              <el-row :gutter="20">
                <el-col :span="24">
                  <el-form-item prop="urlName" label="网站名称：">
                    <el-input v-model="form.urlName" placeholder="请输入网站名称" />
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-form-item prop="urlAddres" label="网站地址：">
                    <el-input v-model="form.urlAddres" placeholder="请输入网站地址" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item prop="urlReferral" label="网站介绍：">
                    <el-input v-model="form.urlReferral" placeholder="请输入网站介绍" maxlength="50"
                              show-word-limit />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item prop="urlType" label="网站类型：">
                    <el-select v-model="form.urlType" placeholder="请选择网站类型" style="width: 100%;">
                      <el-option label="推荐链接" value="1" />
                      <el-option label="友情链接" value="2" />
                    </el-select>
                  </el-form-item>
                </el-col>

              </el-row>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
              <el-row>
                <el-col :span="24">
                  <el-form-item prop="urlLitimg" label="网站首页：">

<!--                    <el-upload ref="uploadEle" style="width:100%;height: 130px;" class="upload-demo"-->
<!--                               drag :action="uploadURL" :on-success="handleAvatarSuccess"-->
<!--                               :before-upload="beforeUploadFunction" :on-progress="handleProgress"-->
<!--                               :on-preview="handlePictureCardPreview" :on-remove="handleRemove">-->
<!--                      <img w-full v-if="imageUrl" :src="imageUrl" class="avatar"-->
<!--                           style="height: 100%;object-fit: cover;" />-->

<!--                      <el-icon class="el-icon&#45;&#45;upload" v-if="!imageUrl">-->
<!--                        <upload-filled />-->
<!--                      </el-icon>-->
<!--                      <div class="el-upload__text" v-if="!imageUrl">-->
<!--                        图片拖拽此处或者<em>点击上传</em>-->
<!--                      </div>-->
<!--                      <el-dialog v-model="dialogVisible">-->
<!--                        <img w-full :src="imageUrl" alt="Preview Image" />-->
<!--                      </el-dialog>-->
<!--                    </el-upload>-->

                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item prop="webmasterEmail" label="站长邮箱：">
                    <el-input v-model="form.webmasterEmail" placeholder="请输入站长邮箱" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-col>

            <el-col :span="24">
              <el-form-item>
                <el-button style="margin: 0 auto;" plain color="#2fa7b9" @click="submitAdd">立即添加
                </el-button>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import {
  validateUrlName,
  validateUrlAddress,
  validateUrlReferral,
  validateUrlType,
  validateUrlLitimg,
  validateWebmasterEmail
} from "../../utils/ValidationRules"


export default {
  name: "FriendLink",
  data() {
    return {
      // 推荐链接所有信息
      recommendLinkInfo: [],
      // 友情链接所有信息
      friendshipLinkInfo: [],
      // 图片根路径
      url: process.env.VUE_APP_URL,
      form: {
        urlName: "",
        urlAddres: "",
        urlReferral: "",
        urlType: "",
        urlLitimg: "",
        webmasterEmail: ""
      },
      // 图片上传到服务器的路径
      uploadURL: process.env.VUE_APP_URL + "/fileUpload",
      // uploadURL: "http://localhost/fileUpload",
      imageUrl: '',
      loading: false,
      dialogVisible:false,

      rules:{
        urlName: [{validator: validateUrlName, trigger: 'blur'}],
        urlAddres: [{validator: validateUrlAddress, trigger: 'blur'}],
        urlReferral: [{validator: validateUrlReferral, trigger: 'blur'}],
        urlType: [{validator: validateUrlType, trigger: 'blur'}],
        urlLitimg: [{validator: validateUrlLitimg, trigger: 'blur'}],
        webmasterEmail: [{validator: validateWebmasterEmail, trigger: 'blur'}],
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      this.request.get("/api/url/showAllUrlInfo", {params: {'urlType': 1}}).then(res => {
        // console.log(res);
        this.recommendLinkInfo = res.data;
      })
      this.request.get("/api/url/showAllUrlInfo", {params: {'urlType': 2}}).then(res => {
        this.friendshipLinkInfo = res.data;
        this.loading = false
      })
    },

    // 链接添加
    submitAdd(){
      // 1：校验链接信息
      this.$refs.form.validate((valid) => {
        // 如果表单校验成功 提交数据到后台
        if (valid) {
          const imageUrl = this.imageUrl.replace(process.env.VUE_APP_URL, "");
          const params = {
            urlName: this.form.urlName,
            urlAddres: this.form.urlAddres,
            urlReferral: this.form.urlReferral,
            urlType: this.form.urlType,
            urlLitimg: imageUrl,
            webmasterEmail: this.form.webmasterEmail,
          }
          this.request.post("/api/url/insert", params).then((res) => {
            // console.log(res)
            if (res.data.code === 0) {
              // 重置表单项，将其值重置为初始值，并移除校验结果
              this.$refs.form.resetFields()
              // 清空已上传的文件列表
              uploadEle.value.clearFiles();
              this.imageUrl = ''
              this.$notify.success({
                title: '错误',
                message: '链接申请成功，等待审核，审核结果将会下发至您邮箱，请注意查收！'
              })
            }
          })

        }
      })
    },


    // 图片格式的文件
    beforeUploadFunction(rawFile){
      // console.log(rawFile.type);
      if (rawFile.type !== 'image/jpeg' &&
          rawFile.type !== 'image/jpg' &&
          rawFile.type !== 'image/png' &&
          rawFile.type !== 'image/gif') {
        this.$message.error('仅支持图片格式.png | .jpg | .jpeg | .gif ')
        return false
      } else if (rawFile.size / 1024 / 1024 > 5) {
        this.$message.error('仅支持大小不超过5MB的图片!')
        return false
      }
      return true
    },


    // // 图片上传成功后执行的函数
    // handleAvatarSuccess(response){
    //   this.imageUrl = process.env.VUE_APP_URL + response;
    //   uploadEle.value.clearFiles();
    //
    //   this.urlLitimg = this.imageUrl
    // },
    // handleRemove(uploadFile, uploadFiles){
    //   console.log(uploadFile, uploadFiles)
    // },
    //
    // handlePictureCardPreview(uploadFile){
    //   uploadEle.value = uploadFile.url
    //   this.dialogVisible= true
    // },

  }
}
</script>

<style scoped>
.friendshipLink .el-col,
.recommendLink .el-col {
  margin-bottom: 20px;
}

.main_centent {
  width: 100%-50px;
  height: auto;
  overflow: hidden;
  background-color: white;
  padding: 25px;
  line-height: 30px;
  color: #474749;
  border-radius: 10px;
}

.centent_box {
  width: 100%;
}

.centent_box>h4 {
  margin-left: 10px;
}

.centent_box>p {
  text-indent: 2em;
  line-height: 35px;
}

.centent_title>h3 {
  font-size: 20px;
  font-weight: 700;
  padding-left: 10px;
  margin: 15px 0;
  border-left: 4px solid #2FA7B9;
}

.el-card {
  --el-card-padding: 0px;
}

.el-card:hover {
  background: rgb(240 240 240);
}

.card_centent {
  padding: 0px;
}

.card_img {
  width: 100%;
  height: 100px;
  overflow: hidden;
}

.card_img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}


.link_text {
  padding: 5px 15px;
  height: 75px;
}

.link_text p {
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-box-orient: vertical;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  line-height: 25px;
}

.link_text span {
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-box-orient: vertical;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-height: 20px;
  color: #989898;
}

:deep(.el-upload-dragger) {
  padding: 3px;
}

:deep(.el-upload-dragger) {
  width: 100%;
  height: 130px;
}
</style>
