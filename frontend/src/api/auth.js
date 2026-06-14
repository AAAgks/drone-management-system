/**
 * auth.js — 认证相关 API
 *
 * 登录 / 登出 / 当前用户查询
 */
import { request, get, post } from './request'

/** 登录 */
export function login(username, password) {
  return post('/api/login', { username, password })
}

/** 登出 */
export function logout() {
  return request('/api/logout', { method: 'POST' })
}

/** 获取当前登录用户（用于页面刷新时恢复会话） */
export function getCurrentUser() {
  return get('/api/current-user')
}
