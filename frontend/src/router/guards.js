/**
 * router/guards.js — 全局路由守卫
 *
 * 职责：
 * 1. 登录鉴权：受保护页面未登录 → 重定向到 /login
 * 2. 已登录反跳：已登录访问 /login → 重定向到首页
 * 3. 复用 useAuth() 状态：只在首次进入时调一次 API，后续导航不重复请求
 * 4. 记录跳转来源：登录成功后回到目标页
 */
import { useAuth } from '../composables/useAuth'

/**
 * 注册全局前置守卫。
 * @param {import('vue-router').Router} router
 */
export function setupGuards(router) {
  const { authenticated, checked, checkAuth } = useAuth()

  router.beforeEach(async (to, from, next) => {
    // ========== 公开页面 ==========
    if (!to.meta.requiresAuth) {
      // 已登录用户访问 /login → 直接跳到首页
      if (to.name === 'Login' && authenticated.value) {
        return next({ name: 'Dashboard' })
      }
      return next()
    }

    // ========== 受保护页面 ==========
    // 首次进入（页面刷新）才请求后端验证 session
    if (!checked.value) {
      await checkAuth()
    }

    if (authenticated.value) {
      next()
    } else {
      // 未登录：跳转登录页，带上目标地址供登录成功后回跳
      next({
        name: 'Login',
        query: { redirect: to.fullPath },
        // 替换当前历史记录，避免回退键回到空白页
        replace: true,
      })
    }
  })
}
