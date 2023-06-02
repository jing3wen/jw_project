//配置代理服务器信息
module.exports = {
    publicPath: process.env.NODE_ENV === 'production' ? '/admin/':'/',
    outputDir: 'dist',
    devServer:{
        //设置跳过host检查
        disableHostCheck: true,
        port: 10090,
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
