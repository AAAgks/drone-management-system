<!--
  DashboardPage.vue — 主控制台页（路由页面，需登录）
  提取自 App.vue 的业务部分
-->
<template>
  <div>
    <Navbar @logout="onLogout" />
    <div class="container">
      <SearchBar @search="handleSearch" @add="showAdd" />

      <!-- 错误提示条 -->
      <div v-if="errorMsg" class="alert alert-danger alert-dismissible" style="margin-top:10px;">
        <button type="button" class="close" @click="errorMsg = ''">&times;</button>
        <strong>请求失败：</strong> {{ errorMsg }}
      </div>

      <DroneTable
        :drones="drones"
        :loading="loading"
        :pageNum="pageNum"
        :pageSize="pageSize"
        :total="total"
        :totalPages="totalPages"
        @edit="showEdit"
        @detail="showDetail"
        @delete="handleDelete"
        @page-change="handlePageChange"
      />
    </div>

    <!-- 详情弹窗 -->
    <DroneDetail v-if="detailVisible" :drone="currentDrone" @close="detailVisible = false" />
    <!-- 表单弹窗 -->
    <DroneForm
      v-if="formVisible"
      :drone="editingDrone"
      :isEdit="isEdit"
      @cancel="closeForm"
      @saved="onSaved"
    />
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth'
import { useDrones } from '../composables/useDrones'
import Navbar from '../components/Navbar.vue'
import SearchBar from '../components/SearchBar.vue'
import DroneTable from '../components/DroneTable.vue'
import DroneDetail from '../components/DroneDetail.vue'
import DroneForm from '../components/DroneForm.vue'

export default {
  name: 'DashboardPage',
  components: { Navbar, SearchBar, DroneTable, DroneDetail, DroneForm },
  setup() {
    const router = useRouter()
    const { logout } = useAuth()
    const { drones, loading, errorMsg, pageNum, pageSize, total, totalPages,
            fetchList, search: doSearch, goToPage, remove, getDetail } = useDrones()
    // ↑ useDrones 把列表/分页/CRUD 全部封装进去了，页面只管传数据给模板

    // ---- UI 状态（弹窗开关、表单模式） ----
    const formVisible   = ref(false)
    const detailVisible = ref(false)
    const editingDrone  = ref(null)
    const isEdit        = ref(false)
    const currentDrone  = ref({})

    // ---- 事件处理（桥接模板和 composable） ----
    function handleSearch(p)     { doSearch(p) }
    function handlePageChange(p) { goToPage(p) }
    async function handleDelete(id) { await remove(id) }

    async function showDetail(d) {
      try {
        currentDrone.value = await getDetail(d.id)
        detailVisible.value = true
      } catch (e) {
        errorMsg.value = e.message
      }
    }

    function showAdd()  { editingDrone.value = null; isEdit.value = false; formVisible.value = true }
    function showEdit(d) { editingDrone.value = { ...d }; isEdit.value = true; formVisible.value = true }
    function closeForm() { formVisible.value = false }
    function onSaved()   { formVisible.value = false; fetchList() }

    async function onLogout() {
      await logout()
      router.replace({ name: 'Login' })
    }

    fetchList() // 进入页面时加载数据

    return {
      drones, loading, errorMsg, pageNum, pageSize, total, totalPages,
      formVisible, detailVisible, editingDrone, isEdit, currentDrone,
      handleSearch, handlePageChange, handleDelete, showDetail,
      showAdd, showEdit, closeForm, onSaved, onLogout,
    }
  },
}
</script>
