/**
 * Vue 应用入口文件。
 *
 * 1. 导入 Bootstrap 样式（全局生效）
 * 2. 创建 Vue 3 应用实例
 * 3. 注册路由，挂载到 #app 元素
 */
import 'bootstrap/dist/css/bootstrap.min.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

createApp(App).use(router).mount('#app')
