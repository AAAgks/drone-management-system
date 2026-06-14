/**
 * drones.js — 无人机 CRUD API
 *
 * 列表查询 / 详情 / 新增 / 编辑 / 删除 / AI 生成
 */
import { get, post, put, del } from './request'

const BASE = '/api/drones'

/** 分页列表 + 搜索 */
export function getDrones(params) {
  return get(BASE, params)
}

/** 单条详情 */
export function getDrone(id) {
  return get(BASE + '/' + id)
}

/** 新增 */
export function createDrone(data) {
  return post(BASE, data)
}

/** 编辑 */
export function updateDrone(id, data) {
  return put(BASE + '/' + id, data)
}

/** 删除 */
export function deleteDrone(id) {
  return del(BASE + '/' + id)
}

/** AI 智能填充 */
export function aiGenerate(data) {
  return post(BASE + '/ai-generate', data)
}
