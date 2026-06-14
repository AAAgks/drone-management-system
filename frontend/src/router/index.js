/**
 * router/index.js — 路由表定义
 *
 * 所有路由跳转和访问权限控制逻辑请见 guards.js。
 *
 * 路由结构：
 *   /login      → LoginPage（公开，已登录用户自动跳转到 /）
 *   /            → DashboardPage（需登录）
 *   其他         → 301 → /
 */
import { createRouter, createWebHashHistory } from 'vue-router'
import { setupGuards } from './guards'

// 懒加载页面组件
const LoginPage = () => import('../pages/LoginPage.vue')
const DashboardPage = () => import('../pages/DashboardPage.vue')

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
    // 公开页面，不设 requiresAuth
  },
  {
    path: '/',
    name: 'Dashboard',
    component: DashboardPage,
    meta: { requiresAuth: true },
  },
  // 未匹配路径 → 301 到首页（由守卫决定是否需要登录）
  { path: '/:pathMatch(.*)*', redirect: '/' },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

// 注册全局守卫
setupGuards(router)

export default router
