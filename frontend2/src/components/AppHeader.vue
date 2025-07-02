<template>
  <el-header class="header">
    <div class="header-content">
      <div class="logo">
        <div class="logo-icon">小红书</div>
        <span class="logo-text">小红书文案</span>
      </div>
      <div class="greeting">
        哈喽，小红书小伙伴！👋 有什么可以帮到你的吗？#回归日常 #小红书互动
      </div>
    </div>
  </el-header>
</template>

<script>
export default {
  name: 'AppHeader'
}
</script>

<style scoped>
/* 顶部导航栏 */
.header {
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 80px;
  display: flex;/* 弹性布局 */
  justify-content:flex-start;/* 内容水平靠左对齐 */
  align-items: center;/* 垂直居中 */
}
/* 内容容器 */
.header-content {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}
/* 品牌logo */
.logo {
  display: flex;/* 弹性布局 */
  align-items:baseline;
  gap: 12px;/* 品牌logo间距 */
}
/* 品牌logo图标 */
.logo-icon {
  width: 40px;/* 图标宽度 */
  height: 40px;/* 图标高度 */
  background: linear-gradient(135deg, #ff6b6b, #e80f0f);
  border-radius: 12px;/* 图标圆角 */
  display: flex;/* 弹性布局 */
  align-items: center;/* 垂直居中 */
  justify-content: center;/* 水平居中 */
  color: white;
  font-weight: bold;
  font-size: 12px;/* 图标字体大小 */
}
/* 品牌logo文字 */
.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.greeting {
  color: #666;
  font-size: 14px;
}
</style>