<!-- ================================================================
     DroneTable.vue — 无人机数据表格组件。
     展示分页列表，包含详情、编辑、删除操作按钮和分页控件。
     ================================================================ -->
<template>
  <div class="panel panel-default">
    <div class="panel-heading">无人机列表</div>
    <div class="panel-body table-responsive">
      <!-- 加载状态 -->
      <div v-if="loading" class="text-center" style="padding:20px;">
        <span class="text-muted">加载中...</span>
      </div>
      <!-- 数据表格 -->
      <table v-else class="table table-striped table-bordered table-hover">
        <thead>
          <tr>
            <th>ID</th><th>名称</th><th>型号</th><th>序列号</th>
            <th>制造商</th><th>类型</th><th>重量(kg)</th>
            <th>最大速度</th><th>续航(min)</th><th>状态</th><th>来源</th><th>操作</th>
          </tr>
        </thead>
        <tbody>
          <!-- 空数据提示 -->
          <tr v-if="drones.length === 0">
            <td colspan="12" class="text-center">暂无数据</td>
          </tr>
          <!-- 数据行 -->
          <tr v-for="d in drones" :key="d.id">
            <td>{{ d.id }}</td>
            <td>{{ d.name }}</td>
            <td>{{ d.model }}</td>
            <td>{{ d.serialNumber }}</td>
            <td>{{ d.manufacturer }}</td>
            <td>{{ d.type }}</td>
            <td>{{ d.weight }}</td>
            <td>{{ d.maxSpeed }}</td>
            <td>{{ d.endurance }}</td>
            <td>
              <!-- 状态标签（正常=绿色，停用=红色） -->
              <span :class="statusClass(d.status)">{{ d.statusText }}</span>
            </td>
            <td>
              <!-- 来源标签 -->
              <span v-if="d.aiGenerated === 1" class="label label-info">AI生成</span>
              <span v-else class="label label-default">手动录入</span>
            </td>
            <td>
              <button class="btn btn-info btn-xs" @click="$emit('detail', d)">详情</button>
              <button class="btn btn-warning btn-xs" @click="$emit('edit', d)">编辑</button>
              <button class="btn btn-danger btn-xs" @click="$emit('delete', d.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <!-- ===== 分页控件 ===== -->
      <nav v-if="totalPages > 0">
        <ul class="pagination pagination-sm" style="margin:0;">
          <!-- 上一页 -->
          <li :class="{ disabled: pageNum <= 1 }">
            <a href="#" @click.prevent="go(pageNum - 1)" aria-label="上一页">&laquo;</a>
          </li>
          <!-- 页码按钮（最多显示 7 页） -->
          <li v-for="p in visiblePages" :key="p" :class="{ active: p === pageNum }">
            <a href="#" @click.prevent="go(p)">{{ p }}</a>
          </li>
          <!-- 下一页 -->
          <li :class="{ disabled: pageNum >= totalPages }">
            <a href="#" @click.prevent="go(pageNum + 1)" aria-label="下一页">&raquo;</a>
          </li>
        </ul>
        <!-- 总数信息 -->
        <span class="pull-right text-muted" style="line-height:34px;padding-right:15px;">
          共 {{ total }} 条，第 {{ pageNum }}/{{ totalPages }} 页
        </span>
      </nav>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'

export default {
  name: 'DroneTable',
  props: {
    drones: Array,        // 当前页数据
    loading: Boolean,     // 加载状态
    pageNum: Number,      // 当前页
    pageSize: Number,     // 每页大小
    total: Number,        // 总记录数
    totalPages: Number    // 总页数
  },
  emits: ['edit', 'detail', 'delete', 'page-change'],

  setup(props, { emit }) {
    /**
     * 计算可视页码（最多显示 7 页按钮）。
     * 例如第 5 页，总 20 页，显示 2~8。
     */
    const visiblePages = computed(() => {
      const max = 7
      let start = Math.max(1, props.pageNum - Math.floor(max / 2))
      let end = start + max - 1
      if (end > props.totalPages) {
        end = props.totalPages
        start = Math.max(1, end - max + 1)
      }
      const pages = []
      for (let i = start; i <= end; i++) pages.push(i)
      return pages
    })

    /** 跳转到指定页 */
    function go(p) {
      if (p < 1 || p > props.totalPages || p === props.pageNum) return
      emit('page-change', p)
    }

    /** 根据状态值返回 CSS 类 */
    function statusClass(s) {
      return 'label label-' + (s === 1 ? 'success' : 'danger')
    }

    return { visiblePages, go, statusClass }
  }
}
</script>
