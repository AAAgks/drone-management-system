<!--
  LoginPage.vue — 登录页（路由页面）
  登录成功后跳转到 Dashboard
-->
<template>
  <LoginForm @login-success="onLoginSuccess" />
</template>

<script>
import { useRouter, useRoute } from 'vue-router'
import { useAuth } from '../composables/useAuth'
import LoginForm from '../components/LoginForm.vue'

export default {
  name: 'LoginPage',
  components: { LoginForm },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const { setLogin } = useAuth()

    function onLoginSuccess(user) {
      setLogin(user)
      // 登录后回跳到之前想去的页面，或默认到首页
      router.replace(route.query.redirect || { name: 'Dashboard' })
    }

    return { onLoginSuccess }
  },
}
</script>
