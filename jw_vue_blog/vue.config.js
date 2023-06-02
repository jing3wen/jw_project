module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/blog/':'/',
  devServer: {
    //设置跳过host检查
    disableHostCheck: true,
    port: 80,
    proxy: {
      "/api": {
          target: "http://localhost:9090",
          changeOrigin: true,
          pathRewrite: {
              "^/api": ""
          }
      },
      "/static": {
          target: "http://localhost:9090",
          changeOrigin: true,
      }
  }
  }
}
