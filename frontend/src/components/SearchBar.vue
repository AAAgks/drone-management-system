<!-- ================================================================
     SearchBar.vue — 搜索栏组件。
     提供名称、型号、状态的筛选条件，以及查询/新增/重置按钮。
     ================================================================ -->
<template>
  <div class="panel panel-default">
    <div class="panel-heading">搜索条件</div>
    <div class="panel-body">
      <div class="row">
        <!-- 名称输入 -->
        <div class="col-md-3">
          <input type="text" class="form-control" v-model="s.name" placeholder="名称" />
        </div>
        <!-- 型号输入 -->
        <div class="col-md-3">
          <input type="text" class="form-control" v-model="s.model" placeholder="型号" />
        </div>
        <!-- 状态下拉 -->
        <div class="col-md-3">
          <select class="form-control" v-model="s.status">
            <option value="">全部状态</option>
            <option value="1">正常</option>
            <option value="0">停用</option>
          </select>
        </div>
        <!-- 操作按钮 -->
        <div class="col-md-3">
          <button class="btn btn-primary" @click="doSearch">查询</button>
          <button class="btn btn-success" @click="$emit('add')">新增</button>
          <button class="btn btn-default" @click="reset">重置</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive } from 'vue'

export default {
  name: 'SearchBar',
  emits: ['search', 'add'],  // search：查询事件，add：新增事件
  setup(props, { emit }) {
    const s = reactive({ name: '', model: '', status: '' })  // 搜索条件

    /** 触发查询，传递当前搜索条件 */
    function doSearch() { emit('search', { ...s }) }

    /** 重置所有搜索条件并触发空搜索（显示全部） */
    function reset() {
      s.name = '' ; s.model = '' ; s.status = ''
      emit('search', {})
    }

    return { s, doSearch, reset }
  }
}
</script>
