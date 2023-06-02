import axios from "axios";
//处理url参数
import qs from "qs";
import store from "../store";


// 添加请求拦截器
axios.interceptors.request.use(function (config) {

  //检查完整是否进入维护状态, 只有查询博客配置的请求"/blogWeb/front/getWebInfo"可以放行
  if(!config.url.includes("/blogWeb/front/getWebInfo")){
    if(store.state.webInfo.status==='0'){
      return Promise.reject(new Error("网站维护中"))
    }
  }

  // 在发送请求之前做些什么
  config.headers['Content-Type'] = 'application/json;charset=utf-8';

  let user = store.state.currentUser
  if(user.token){
    config.headers['token'] = user.token;  // 设置请求头
  } else {
    // console.log("用户未登录")
  }
  return config;
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error);
});

// 添加响应拦截器
axios.interceptors.response.use(function (response) {
  if (response.data !== null && response.data.hasOwnProperty("code") && response.data.code !== 200) {

    //用户token过期情况
    if (response.data.code === 401) {
      store.commit("loadCurrentUser", {});
    }

    return Promise.reject(new Error(response.data.msg));
  } else {
    return response;
  }
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});

// 当data为URLSearchParams对象时设置为application/x-www-form-urlencoded;charset=utf-8
// 当data为普通对象时，会被设置为application/json;charset=utf-8

export default {
  post(url, params = {}, json = true) {
    return new Promise((resolve, reject) => {
      axios
        .post(url, json ? params : qs.stringify(params))
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  },

  get(url, params = {}) {
    return new Promise((resolve, reject) => {
      axios.get(url, {params: params}).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err)
      })
    });
  },

  delete(url, data = {}) {
    return new Promise((resolve, reject) => {
      axios.delete(url, {data: data,}).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err)
      })
    });
  },
}

