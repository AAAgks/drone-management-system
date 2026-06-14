<!--
  App.vue — 应用根组件
  现在的职责只是路由出口，业务逻辑已拆分到路由页面中。
-->
<template>
  <router-view />
</template>

<script>
export default { name: 'App' }
</script>

<style>
/* 全局样式：导航栏下方留出空白 */
body { padding-top: 70px; }
.label { font-size: 12px; padding: 3px 8px; }
</style>
