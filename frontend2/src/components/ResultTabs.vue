<template>
  <el-main class="result-section">
    <div class="result-tabs">
      <el-tabs :model-value="activeTab" @tab-change="updateActiveTab" class="content-tabs">
        <!-- 笔记内容 -->
        <el-tab-pane label="📝 小红书笔记" name="note">
          <div class="content-card">
            <div class="content-header">
              <h4>生成的小红书笔记</h4>
              <el-button size="small" @click="handleCopy(generatedContent.note)">
                <el-icon><DocumentCopy /></el-icon>
                复制
              </el-button>
            </div>
            <div class="content-body">
              <div v-if="generatedContent.note" class="note-content">
                {{ generatedContent.note }}
              </div>
              <div v-else class="placeholder">
                <el-icon class="placeholder-icon"><Document /></el-icon>
                <p>请在左侧输入产品信息，点击生成按钮</p>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 标题推荐 -->
        <el-tab-pane label="💡 吸引标题" name="title">
          <div class="content-card">
            <div class="content-header">
              <h4>推荐标题</h4>
            </div>
            <div class="content-body">
              <div v-if="generatedContent.titles && generatedContent.titles.length" class="titles-list">
                <div
                    v-for="(title, index) in generatedContent.titles"
                    :key="index"
                    class="title-item"
                    @click="handleCopy(title)"
                >
                  <span class="title-number">{{ index + 1 }}</span>
                  <span class="title-text">{{ title }}</span>
                  <el-icon class="copy-icon"><DocumentCopy /></el-icon>
                </div>
              </div>
              <div v-else class="placeholder">
                <el-icon class="placeholder-icon"><Star /></el-icon>
                <p>生成中，请稍候...</p>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 热门标签 -->
        <el-tab-pane label="🏷️ 热门标签" name="tags">
          <div class="content-card">
            <div class="content-header">
              <h4>推荐标签</h4>
            </div>
            <div class="content-body">
              <div v-if="generatedContent.tags && generatedContent.tags.length" class="tags-container">
                <el-tag
                    v-for="tag in generatedContent.tags"
                    :key="tag"
                    class="tag-item"
                    @click="handleCopy('#' + tag)"
                >
                  #{{ tag }}
                </el-tag>
              </div>
              <div v-else class="placeholder">
                <el-icon class="placeholder-icon"><PriceTag /></el-icon>
                <p>标签生成中...</p>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 卖点分析 -->
        <el-tab-pane label="🎯 卖点分析" name="analysis">
          <div class="content-card">
            <div class="content-header">
              <h4>产品卖点分析</h4>
            </div>
            <div class="content-body">
              <div v-if="generatedContent.analysis" class="analysis-content">
                {{ generatedContent.analysis }}
              </div>
              <div v-else class="placeholder">
                <el-icon class="placeholder-icon"><TrendCharts /></el-icon>
                <p>分析生成中...</p>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </el-main>
</template>

<script>
import { DocumentCopy, Document, Star, PriceTag, TrendCharts } from '@element-plus/icons-vue'

export default {
  name: 'ResultTabs',
  components: {
    DocumentCopy,
    Document,
    Star,
    PriceTag,
    TrendCharts
  },
  props: {
    activeTab: {
      type: String,
      default: 'note'
    },
    generatedContent: {
      type: Object,
      required: true
    }
  },
  emits: ['update:activeTab', 'copy'],
  methods: {
    handleCopy(content) {
      this.$emit('copy', content)
    },
    updateActiveTab(newVal) {
      this.$emit('update:activeTab', newVal)
    }
  }
}
</script>

<style scoped>
/* 结果区域 */
.result-section {
  flex: 1;
  padding: 20px 10px 100px;
  background: #f8f9fa;
}

/* 结果选项卡 */
.result-tabs {
  height: 100%;
}

/* 选项卡内容区域 */
.content-tabs {
  height: 100%;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}
/* 选项卡内容卡片 */
.content-card {
  height: calc(100vh - 300px);
  display: flex;
  flex-direction: column;
}
/* 选项卡内容卡片头部 */
.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px 0;/* 卡片头部内边距 */
  border-bottom: 1px solid #f0f0f0;/* 卡片底部边框 */
  margin-bottom: 30px;/* 卡片底部间距 */
}
/* 选项卡内容卡片头部标题 */
.content-header h4 {
  margin: -15px -10px 20px;/* 标题外边距 */
  color: #060606;
  font-size: 17px;/* 标题字体大小 */
  font-weight: 600;/* 标题字体加粗 */
}
/* 选项卡内容卡片主体 */
.content-body {
  flex: 1;
  padding: 0 24px 24px;
  overflow-y: auto;
}
/* 注意事项内容 */
.note-content,
.analysis-content {
  line-height: 1.8;
  color: #333;
  font-size: 14px;
  white-space: pre-wrap;
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  border-left: 4px solid #ff6b6b;
}
/* 标题列表 */
.titles-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
/* 标题项 */
.title-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  cursor: pointer;/* 鼠标悬停时改变样式 */
  transition: all 0.4s ease;/* 过渡效果 */
  border: 2px solid transparent;/* 初始透明边框 */
}

.title-item:hover {
  background: #e3f2fd;
  border-color: #2196f3;
  transform: translateY(-2px);
}
/* 标题项数字 */
.title-number {
  width: 24px;
  height: 24px;
  background: #ff6b6b;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  margin-right: 12px;
  flex-shrink: 0;
}

/* 标题文本 */
.title-text {
  flex: 1;
  color: #333;
  font-size: 14px;
  line-height: 1.5;
}
/* 复制图标 */
.copy-icon {
  color: #999;
  margin-left: 8px ;/* 图标间距 */
  font-size: 16px;/* 图标大小 */
  cursor: pointer;/* 鼠标悬停时改变样式 */
  opacity: 0;/* 初始透明 */
  transition: opacity 0.3s ease;
}

/* 标题项悬停时显示图标 */
.title-item:hover .copy-icon {
  opacity: 1;
}
/* 标签容器 */
.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
/* 标签项 */
.tag-item {
  cursor: pointer;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #ff6b6b, #ff8e8e);
  color: white;
  border: none;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 20px;
}
/* 标签项悬停效果 */
.tag-item:hover {
  transform: translateY(20px);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
}
/* 占位符容器 */
.placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #999;
}
/* 占位符图标 */
.placeholder-icon {
  font-size: 48px;
  margin-bottom: 16px;
  color: #ddd;
}

.placeholder p {
  margin: 0;
  font-size: 14px;
}
</style>