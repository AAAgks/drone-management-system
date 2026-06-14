/**
 * useAuth.js — 认证状态 composable（模块级单例）
 *
 * 不依赖 Vuex/Pinia，用模块级 ref 实现跨组件状态共享。
 * LoginPage、DashboardPage、Router 守卫共用同一份状态。
 */
import { ref } from 'vue'
import { getCurrentUser, logout as apiLogout } from '../api/auth'

// 模块级响应式状态（所有 import 此模块的组件共享）
const authenticated = ref(false)
const username = ref('')
const checked = ref(false)  // 是否已完成初始检查（防闪烁）

export function useAuth() {
  /** 检查登录状态（页面刷新时恢复会话） */
  async function checkAuth() {
    try {
      username.value = await getCurrentUser()
      authenticated.value = true
    } catch {
      authenticated.value = false
      username.value = ''
    } finally {
      checked.value = true
    }
  }

  /** 登录成功：设置状态 */
  function setLogin(user) {
    username.value = user
    authenticated.value = true
  }

  /** 退出登录 */
  async function logout() {
    try { await apiLogout() } catch { /* 忽略网络错误 */ }
    authenticated.value = false
    username.value = ''
  }

  return { authenticated, username, checked, checkAuth, setLogin, logout }
}
