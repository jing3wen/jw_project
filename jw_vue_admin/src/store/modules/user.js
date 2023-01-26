// 登陆用户相关设置
import store from '../index'
const state = {

    currentLoginUser:{

    },  //存储当前登录的用户信息

    menuAndDirectoryTree:[]  //存储当前登录用户的目录和子菜单
}

const getters = {

}

const mutations = {

    loginUser(state, loginUserInfo){
        state.currentLoginUser = loginUserInfo
    },
    logout(state) {
        //重置登陆用户状态
        state.currentLoginUser = {}
        state.menuAndDirectoryTree = []
            //重置标签栏
        store.commit('layout/resetTab')
    }
}

const actions = {

}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}
