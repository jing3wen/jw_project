import axios from 'axios'
import Element from 'element-ui'
import store from '../store'
import router from "../router";

const request = axios.create({
    timeout: 5000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    let user = store.state.user.currentLoginUser
    if(user.token){
        config.headers['token'] = user.token;  // 设置请求头
    } else {
        // console.log("用户未登录")
    }
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        // 当后端传过来用户过期时，前端强制注销，清空所有用户信息
        if (res.code === 401){
            Element.Message.error(res.msg)
            store.commit('user/logout')
            router.push("/login")
        }
        // 权限不足
        if (res.code === 403){

        }
        //400 系统请求异常
        // if(res.code === 400){
        //     Element.Message.error(res.msg)
        // }
        return res;
    },
    error => {
        if (error.message.includes( 'timeout' )){
            
            Element.Message.error('网络请求超时');
        }
        else Element.Message.error('请求错误: '+error)
        return Promise.reject(error)
    }
)


export default request
