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
            <div class="analysis-layout">
              <!-- 左侧：卖点分析 -->
              <div class="analysis-left">
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
              
              <!-- 右侧：图片分析 -->
              <div class="analysis-right">
                <div class="content-header">
                  <h4>📱 图片分析</h4>
                </div>
                <div class="content-body">
                  <div class="image-analysis-container">
                    <!-- 手机框架 -->
                    <div class="phone-frame">
                      <!-- 手机顶部状态栏 -->
                      <div class="phone-status-bar">
                        <div class="status-left">
                          <span class="time">09:41</span>
                        </div>
                        <div class="status-right">
                          <div class="signal-bars">
                            <div class="bar"></div>
                            <div class="bar"></div>
                            <div class="bar"></div>
                            <div class="bar active"></div>
                          </div>
                          <div class="wifi-icon">📶</div>
                          <div class="battery">🔋</div>
                        </div>
                      </div>
                      
                      <!-- 小红书导航栏 -->
                      <div class="xiaohongshu-header">
                        <div class="header-left">
                          <span class="back-arrow">‹</span>
                          <span class="app-logo">
                            <img src="/logo.png" alt="Logo" style="width: 25px; height: 25px;">
                          </span>
                          <span class="app-name">小红书</span>
                        </div>
                        <div class="header-right">
                          <button class="follow-btn">关注</button>
                          <span class="share-icon">📤</span>
                        </div>
                      </div>
                      
                      <!-- 内容区域 -->
                      <div class="phone-content">
                        <!-- 图片区域 -->
                        <div class="image-area">
                          <div class="product-image">
                            <!-- 模拟产品图片 -->
                            <div class="image-placeholder">
                              <div class="image-content">
                                <div class="product-showcase">
                                  <img src="/xiaohongshuBackground.jpeg" alt="小红书背景" style="width: 100%; height: 100%; object-fit: cover;">
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        
                        <!-- 文案区域 -->
                         <div class="text-area">
                           <div class="note-text" v-if="generatedContent.note">
                             {{ generatedContent.note }}
                           </div>
                           <div class="note-text" v-else>
                             💝心动好物推荐💝
                             
                             🌟超值好物分享🌟
                             ✨品质生活必备✨
                             
                             #好物推荐 #生活好物 #种草
                           </div>
                         </div>
                         
                         <!-- 卖点分析区域 -->
                          <div class="analysis-preview" v-if="generatedContent.analysis">
                            <div class="analysis-title">📊 产品卖点</div>
                            <div class="analysis-text">
                              {{ generatedContent.analysis }}
                            </div>
                          </div>
                        
                        <!-- 底部互动区域 -->
                        <div class="interaction-area">
                          <div class="user-info">
                            <div class="avatar">👤</div>
                            <span class="username">AI助手</span>
                          </div>
                          <div class="action-buttons">
                            <div class="action-btn">
                              <span class="icon">❤️</span>
                              <span class="count">100w</span>
                            </div>
                            <div class="action-btn">
                              <span class="icon">💬</span>
                              <span class="count">55.5w</span>
                            </div>
                            <div class="action-btn">
                              <span class="icon">⭐</span>
                              <span class="count">收藏</span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
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
  height: calc(100vh - 150px);
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

/* 卖点分析布局 */
.analysis-layout {
  display: flex;
  height: 100%;
  gap: 20px;
}

.analysis-left {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.analysis-right {
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 图片分析容器 */
.image-analysis-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 20px;
}

/* 手机框架 - 苹果手机样式 */
.phone-frame {
  width: 280px;
  height: 500px;
  background: #000;
  border-radius: 30px;
  padding: 4px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.4), inset 0 0 0 2px #333;
  position: relative;
  overflow: hidden;
}

/* 苹果手机刘海屏 */
.phone-frame::before {
  content: '';
  position: absolute;
  top: 4px;
  left: 50%;
  transform: translateX(-50%);
  width: 120px;
  height: 20px;
  background: #000;
  border-radius: 0 0 15px 15px;
  z-index: 10;
}

/* Home指示器 */
.phone-frame::after {
  content: '';
  position: absolute;
  bottom: 8px;
  left: 50%;
  transform: translateX(-50%);
  width: 120px;
  height: 4px;
  background: #333;
  border-radius: 2px;
  z-index: 10;
}

/* 手机状态栏 */
.phone-status-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25px 16px 8px;
  background: #e34f4f;
  font-size: 12px;
  font-weight: 600;
  position: relative;
  z-index: 5;
}

.status-left .time {
  color: #000;
}

.status-right {
  display: flex;
  align-items: center;
  gap: 4px;
}

.signal-bars {
  display: flex;
  gap: 2px;
  align-items: flex-end;
}

.signal-bars .bar {
  width: 3px;
  height: 8px;
  background: #ccc;
  border-radius: 1px;
}

.signal-bars .bar.active {
  background: #000;
}

.wifi-icon, .battery {
  font-size: 10px;
}

/* 小红书导航栏 */
.xiaohongshu-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.back-arrow {
  font-size: 20px;
  color: #333;
}

.app-logo {
  font-size: 16px;
}

.app-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.follow-btn {
  background: #ff2442;
  color: white;
  border: none;
  border-radius: 16px;
  padding: 6px 16px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.follow-btn:hover {
  background: #e01e3c;
  transform: scale(1.05);
}

.share-icon {
  font-size: 16px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.share-icon:hover {
  transform: scale(1.1);
}

/* 手机内容区域 */
.phone-content {
  background: #fff;
  height: calc(100% - 95px);
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  overflow-x: hidden;
  margin-bottom: 12px;
}

/* 自定义滚动条样式 */
.phone-content::-webkit-scrollbar {
  width: 4px;
}

.phone-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 2px;
}

.phone-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.phone-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 图片区域 */
.image-area {
  flex: 0 0 180px;
  padding: 16px;
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 50%, #fecfef 100%);
}

.product-image {
  height: 100%;
  border-radius: 12px;
  overflow: hidden;
}

.image-placeholder {
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.image-content {
  text-align: center;
}

.product-showcase {
  position: relative;
}

.product-item {
  font-size: 60px;
  margin-bottom: 10px;
}

.decoration {
  position: absolute;
  top: -10px;
  right: -10px;
  font-size: 20px;
  animation: sparkle 2s infinite;
}

@keyframes sparkle {
  0%, 100% { transform: scale(1) rotate(0deg); }
  50% { transform: scale(1.2) rotate(180deg); }
}

/* 文案区域 */
.text-area {
  padding: 16px;
  background: #fff;
  border-top: 1px solid #f0f0f0;
}

.note-text {
  font-size: 13px;
  line-height: 1.6;
  color: #333;
  white-space: pre-line;
}

/* 卖点分析预览区域 */
.analysis-preview {
  padding: 12px 16px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-top: 1px solid #f0f0f0;
  margin-top: 8px;
}

.analysis-title {
  font-size: 12px;
  font-weight: 600;
  color: #495057;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.analysis-text {
  font-size: 11px;
  line-height: 1.5;
  color: #6c757d;
  white-space: pre-line;
  background: rgba(255, 255, 255, 0.7);
  padding: 8px;
  border-radius: 6px;
  border-left: 3px solid #ff6b6b;
}

/* 底部互动区域 */
.interaction-area {
  padding: 12px 16px;
  background: #fff;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #ff6b6b;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
}

.username {
  font-size: 12px;
  color: #666;
}

.action-buttons {
  display: flex;
  gap: 16px;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  cursor: pointer;
  transition: transform 0.2s;
}

.action-btn:hover {
  transform: scale(1.1);
}

.action-btn .icon {
  font-size: 16px;
}

.action-btn .count {
  font-size: 10px;
  color: #666;
}
</style>