<template>
  <div class="home">
    <!-- 这里可以放置主页内容，目前App.vue包含了所有功能 -->
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style scoped>
.home {
  min-height: 100vh;
}
</style>