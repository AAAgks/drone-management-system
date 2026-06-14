<!-- ================================================================
     LoginForm.vue — 登录页面组件。
     在全屏居中卡片中显示用户名和密码输入框，
     点击"登录"向 /api/login 发送 POST 请求。
     ================================================================ -->
<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="text-center">无人机信息管理系统</h2>
      <hr />
      <!-- 登录错误提示 -->
      <div v-if="error" class="alert alert-danger">{{ error }}</div>
      <div class="form-group">
        <label>用户名</label>
        <input type="text" class="form-control" v-model="username" @keyup.enter="login" placeholder="请输入用户名" />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input type="password" class="form-control" v-model="password" @keyup.enter="login" placeholder="请输入密码" />
      </div>
      <button class="btn btn-primary btn-block" @click="login" :disabled="loading">
        <span v-if="loading">登录中...</span>
        <span v-else>登录</span>
      </button>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { login as apiLogin } from '../api/auth'

export default {
  name: 'LoginForm',
  emits: ['login-success'],  // 登录成功后通知父组件
  setup(props, { emit }) {
    const username = ref('')
    const password = ref('')
    const error = ref('')
    const loading = ref(false)

    /** 执行登录请求 */
    async function login() {
      if (!username.value || !password.value) {
        error.value = '请输入用户名和密码'
        return
      }
      loading.value = true
      error.value = ''
      try {
        await apiLogin(username.value, password.value)
        emit('login-success', username.value)  // 通知 App 登录成功
      } catch (e) {
        error.value = e.message || '登录失败'
      } finally {
        loading.value = false
      }
    }

    return { username, password, error, loading, login }
  }
}
</script>

<style scoped>
/* 全屏居中布局 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f5f5f5;
}
/* 登录卡片 */
.login-card {
  width: 380px;
  padding: 30px;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}
.login-card h2 {
  margin-top: 0;
  font-size: 20px;
}
</style>
