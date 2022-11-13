import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

//自定义样式
import './assets/css/index.css'
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
app.config.globalProperties.request = request
app.use(store).use(router).mount('#app')
