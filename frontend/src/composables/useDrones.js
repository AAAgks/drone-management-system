/**
 * useDrones.js — 无人机列表数据管理 composable
 *
 * 从 DashboardPage 抽离的数据逻辑，包括：
 * - 分页列表查询
 * - 搜索 / 翻页 / 删除 / 详情
 * - 加载状态与错误提示
 *
 * 每个调用方都拥有独立的响应式状态（不是模块级单例）。
 */
import { ref } from 'vue'
import { getDrones, getDrone, deleteDrone } from '../api/drones'

export function useDrones() {
  // ---------- 响应式状态 ----------
  const drones     = ref([])       // 当前页数据
  const loading    = ref(false)    // 加载中
  const errorMsg   = ref('')       // 错误提示
  const pageNum    = ref(1)        // 当前页码
  const pageSize   = ref(10)       // 每页条数
  const total      = ref(0)        // 总记录数
  const totalPages = ref(0)        // 总页数

  // 非响应式变量（不需要触发 UI 更新）
  let lastSearchParams = {}

  // ---------- 方法 ----------

  /** 获取列表（核心方法） */
  async function fetchList(params = {}) {
    loading.value = true
    errorMsg.value = ''
    try {
      const data = await getDrones({ pageNum: pageNum.value, pageSize: pageSize.value, ...params })
      drones.value     = data && data.rows  ? data.rows  : []
      total.value      = data.total   || 0
      totalPages.value = data.pages   || 0
      pageNum.value    = data.pageNum || 1
    } catch (e) {
      errorMsg.value = e.message
      drones.value = []
    } finally {
      loading.value = false
    }
  }

  /** 搜索：保存条件 + 重置到第一页 */
  function search(params) {
    lastSearchParams = { ...params }
    pageNum.value = 1
    fetchList(params)
  }

  /** 翻页：用保存的搜索条件 */
  function goToPage(p) {
    pageNum.value = p
    fetchList(lastSearchParams)
  }

  /** 删除：确认后调 API，刷新列表 */
  async function remove(id) {
    if (!confirm('确定要删除这条记录吗？')) return
    try {
      await deleteDrone(id)
      fetchList(lastSearchParams)
    } catch (e) {
      errorMsg.value = e.message
    }
  }

  /** 查看详情（返回详情数据，由调用方决定如何展示） */
  async function getDetail(id) {
    return await getDrone(id)
  }

  // ---------- 返回 ----------
  return {
    // 状态
    drones, loading, errorMsg, pageNum, pageSize, total, totalPages,
    // 方法
    fetchList, search, goToPage, remove, getDetail,
  }
}
