// 布局相关的设置
import Element from 'element-ui'

const state = {
    isCollapse: false,
    currentPathName: '',
    tabList:[{ name: "主页", path: "/index" }],
    currentUiSize: "small",
}

const getters = {

}

const mutations = {
    //折叠
    collapse(state) {
        state.isCollapse = !state.isCollapse;
    },
    //面包屑导航栏
    setPath(state, currentPathName) {
        state.currentPathName = currentPathName;
    },
    // 保存标签
    saveTab(state, tab) {
        if (state.tabList.findIndex(item => item.path === tab.path) == -1) {
            state.tabList.push({name: tab.name, path: tab.path});
        }
    },
    // 移除标签
    removeTab(state, tab) {
        if(tab.name == "主页" || tab.path == '/index' || tab.path == '/Index'){
            Element.Message.warning('该标签为主页, 不能移除')

        }else {
            var index = state.tabList.findIndex(item => item.name === tab.name);
            state.tabList.splice(index, 1);
        }

    },
    // 重置标签
    resetTab(state) {
        state.tabList = [{name: "主页", path: "/index"}];
    },
    setElementUiSize(state, size) {
        state.currentUiSize = size
        location.reload()
    },
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