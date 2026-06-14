/**
 * Vite 构建工具配置文件（前后端不分离模式）。
 *
 * 核心配置：
 * - 开发模式：端口 3000 + /api 代理到后端 localhost:8080
 * - 生产构建：输出到 ../src/main/resources/static/，由 Spring Boot 直接托管
 * - base: '/' —— 所有资源路径从根路径引用，与后端同源
 */
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],          // 启用 Vue 3 SFC 编译插件

  // 资源基础路径（同源部署，使用根路径）
  base: '/',

  // 生产构建输出目录（Spring Boot classpath:/static/）
  build: {
    outDir: '../src/main/resources/static',
    emptyOutDir: true,
  },

  // 开发服务器（前后端分离开发时可用）
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 代理到 Spring Boot 后端
        changeOrigin: true,
      },
    },
  },
})
