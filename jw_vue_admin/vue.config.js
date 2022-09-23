//配置代理服务器信息
module.exports = {
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
