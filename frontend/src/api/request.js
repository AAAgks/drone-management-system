/**
 * request.js — 统一 HTTP 请求封装（基于 axios）
 *
 * 职责：
 * 1. 默认 baseURL + JSON Content-Type
 * 2. 统一响应拦截——自动解包 { code, msg, data } → 返回 data
 * 3. 统一错误处理——code !== 200 时抛出 ApiError
 * 4. 请求/响应拦截器（基于 axios.interceptors）
 * 5. 10 秒超时
 */
import axios from 'axios'

// ---- axios 实例 ----
const http = axios.create({
  timeout: 10000,               // 10 秒超时
  headers: { 'Content-Type': 'application/json' },
})

// ================================================================
// 响应拦截器 —— 核心：自动解包 + 业务异常
// ================================================================
http.interceptors.response.use(
  // 2xx → 解包
  (res) => {
    const result = res.data           // axios 已自动 JSON.parse
    if (result.code !== 200) {
      throw new ApiError(result.msg || '请求失败', result.code)
    }
    return result.data                // 调用方拿到的是 data，不需要 .data
  },
  // 非 2xx → 统一转 ApiError
  (err) => {
    if (err.response) {
      // 后端返回了响应（401 / 404 / 500 等）
      const { status, data } = err.response
      throw new ApiError(
        (data && data.msg) || `服务器错误 (${status})`,
        data && data.code ? data.code : status
      )
    }
    if (err.code === 'ECONNABORTED') {
      throw new ApiError('请求超时，请检查网络或后端服务', -1)
    }
    throw new ApiError('网络连接失败，请检查后端服务是否启动', -1)
  }
)

// ================================================================
// ApiError —— 业务异常类
// ================================================================
export class ApiError extends Error {
  constructor(msg, code) {
    super(msg)
    this.name = 'ApiError'
    this.code = code
  }
}

// ================================================================
// 拦截器扩展方法（包装 axios interceptors）
// ================================================================
export function addRequestInterceptor(onFulfilled, onRejected) {
  return http.interceptors.request.use(onFulfilled, onRejected)
}

export function addResponseInterceptor(onFulfilled, onRejected) {
  return http.interceptors.response.use(onFulfilled, onRejected)
}

// ================================================================
// 快捷方法（保持和之前一样的签名，api/{auth,drones}.js 不用改）
// ================================================================
export function request(url, options = {}) {
  return http.request({ url, ...options })
}

export function get(url, params) {
  return http.get(url, { params })
}

export function post(url, data) {
  return http.post(url, data)
}

export function put(url, data) {
  return http.put(url, data)
}

export function del(url) {
  return http.delete(url)
}

export default http
