/**
 * Vite 构建工具配置文件。
 *
 * 核心配置：
 * - 端口 3000 启动开发服务器
 * - /api 请求代理到后端 localhost:8080，解决跨域问题
 */
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],   // 启用 Vue 3 SFC 编译插件
  server: {
    port: 3000,       // 前端开发服务器端口
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 代理目标：Spring Boot 后端
        changeOrigin: true                  // 修改请求头 Origin 避免后端拒绝
      }
    }
  }
})
