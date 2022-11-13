<template>
  <div class="footer_copyright">
    <div class="topWrapper hidden-xs-only">
      <div class="menuWrap">
        <div class="menuBox">
          <a href="/">首页</a>
          <a href="/article">精品文章</a>
          <a href="/website">学习网站</a>
          <a href="/about">关于</a>
        </div>
        <div class="copyright viaWrapper">
          <i class="via"></i>
          相信有一天, 理想主义终将所向披靡.
        </div>
      </div>
    </div>
    <div class="midWrapper">
      <div class="article">
        <p>站点声明：本站转载作品版权归原作者及来源网站所有，原创内容作品版权归作者所有，任何内容转载、商业用途等均须联系原作者并注明来源。</p>
        <p></p>
        <p>
          <span>
            Copyright © 2022 Bpvank
          </span>
          <a target="_blank" href="https://beian.miit.gov.cn"><img src="../../assets/image/badge.png" class="badeg" />赣ICP备2022008772号</a>
          <br>相关侵权、举报、投诉及建议等，请发E-mail：<a
            href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&amp;email=478292420@qq.com" target="_blank">478292420
          @qq.com</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Copyright"
}
</script>

<style scoped>
.footer_copyright .topWrapper {
  padding: 10px 0;
  border-bottom: 1px solid #ebebeb;
  color: #999;
  font-size: 12px;
  line-height: 20px;
  overflow: hidden;
}

.footer_copyright .menuWrap {
  display: flex;
  justify-content: space-between;
  width: 75%;
  margin: 0 auto;
}

.footer_copyright .menuBox {
  line-height: 30px;
}

.footer_copyright .menuBox a {
  margin-right: 10px;
  color: #999;
}

.footer_copyright .menuBox a:hover {
  color: #2FA7B9;
}

.footer_copyright .menuWrap .copyright {
  text-align: right;
  color: #777;
}

.footer_copyright .menuWrap .copyright .alink {
  padding-left: 5px;
  color: #2FA7B9;
}

.footer_copyright .viaWrapper {
  line-height: 30px;
}

.footer_copyright .viaWrapper .via {
  display: inline-block;
  width: 30px;
  height: 30px;
  background: url(../../assets/image/logo_gray.svg) no-repeat;
  background-size: 30px;
  margin-right: 6px;
  float: left;
}

.footer_copyright .midWrapper {
  margin: 0 auto;
  width: 75%;
  overflow: hidden;
}

.footer_copyright .midWrapper .article {
  padding: 10px 0;
  color: #666;
  font-size: 12px;
  overflow: hidden;
  line-height: 20px;
}

.footer_copyright .midWrapper p {
  color: #666;
  font-size: 12px;
  line-height: 20px;
  margin-bottom: 10px;
}

.footer_copyright .el-footer {
  --el-footer-height: 100%;
}

.badeg{
  vertical-align: middle;
  width: 15px;
  margin: 0 2px;
}
</style>
