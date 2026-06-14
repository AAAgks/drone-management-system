<!--
  Navbar.vue — 顶部导航栏。
  直接从 useAuth() 获取用户名（不依赖父组件 props 传递），
  退出事件仍通过 emit 发出，由页面组件处理路由跳转。
-->
<template>
  <nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
      <div class="navbar-header">
        <a class="navbar-brand" href="#">无人机信息管理系统</a>
      </div>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#"><span class="glyphicon glyphicon-user"></span> {{ username }}</a></li>
        <li><a href="#" @click.prevent="$emit('logout')"><span class="glyphicon glyphicon-log-out"></span> 退出</a></li>
      </ul>
    </div>
  </nav>
</template>

<script>
import { useAuth } from '../composables/useAuth'

export default {
  name: 'Navbar',
  emits: ['logout'],
  setup() {
    const { username } = useAuth()
    return { username }
  }
}
</script>
