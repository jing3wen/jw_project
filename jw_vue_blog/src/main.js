import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

//自定义样式
import './assets/css/index.css'
import './assets/css/common.css'
//插件
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/lib/locale/lang/zh-cn'
import * as ELIcons from '@element-plus/icons-vue'
import request from "./utils/request";

const app = createApp(App)

app.use(ElementPlus, { locale })
// 全局导入plus图标
for (let iconName in ELIcons) {
    app.component(iconName, ELIcons[iconName])
}


//创建v-highlight全局指令
app.directive('highlight', function (el) {
    let blocks = el.querySelectorAll('pre code');
    blocks.forEach((block) => {
        hljs.highlightBlock(block)
        // 从这开始是设置行号
        block.innerHTML = `<ol><li>${block.innerHTML.replace(/\n/g, `</li><li class="line">`)}</li></ol>`;
    })
})
app.config.globalProperties.request = request
app.use(store).use(router).mount('#app')
