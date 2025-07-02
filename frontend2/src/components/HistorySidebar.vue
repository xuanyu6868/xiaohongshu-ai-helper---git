<template>
  <div class="history-sidebar" :class="{ 'collapsed': isCollapsed }">
    <!-- 侧边栏头部 -->
    <div class="sidebar-header">
      <h3 v-if="!isCollapsed" class="sidebar-title">历史记录</h3>
      <button class="toggle-btn" @click="toggleSidebar">
        <span v-if="isCollapsed">📋</span>
        <span v-else>✕</span>
      </button>
    </div>

    <!-- 历史记录列表 -->
    <div v-if="!isCollapsed" class="history-list">
      <div v-if="historyList.length === 0" class="empty-state">
        <div class="empty-icon">📝</div>
        <p>暂无历史记录</p>
      </div>
      
      <div v-else class="history-items">
        <div 
          v-for="(item, index) in historyList" 
          :key="item.id"
          class="history-item"
          :class="{ 'active': selectedHistoryId === item.id }"
          @click="selectHistory(item)"
        >
          <div class="item-header">
            <h4 class="item-title">{{ item.productName || '未命名产品' }}</h4>
            <span class="item-time">{{ formatTime(item.timestamp) }}</span>
          </div>
          <p class="item-preview">{{ item.preview }}</p>
          <div class="item-actions">
            <button class="delete-btn" @click.stop="deleteHistory(item.id)">
              🗑️
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 清空按钮 -->
    <div v-if="!isCollapsed && historyList.length > 0" class="sidebar-footer">
      <button class="clear-all-btn" @click="clearAllHistory">
        清空历史记录
      </button>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'

export default {
  name: 'HistorySidebar',
  emits: ['restore-history'],
  setup(props, { emit }) {
    const isCollapsed = ref(false)
    const historyList = ref([])
    const selectedHistoryId = ref(null)

    // 切换侧边栏显示/隐藏
    const toggleSidebar = () => {
      isCollapsed.value = !isCollapsed.value
      localStorage.setItem('historySidebarCollapsed', isCollapsed.value)
    }

    // 添加历史记录
    const addHistory = (data) => {
      const historyItem = {
        id: Date.now(),
        timestamp: new Date(),
        productName: data.productForm.name,
        productForm: { ...data.productForm },
        generatedContent: { ...data.generatedContent },
        preview: data.generatedContent.note ? 
          data.generatedContent.note.substring(0, 50) + '...' : 
          '生成的内容预览...'
      }
      
      historyList.value.unshift(historyItem)
      
      // 限制历史记录数量，最多保存50条
      if (historyList.value.length > 50) {
        historyList.value = historyList.value.slice(0, 50)
      }
      
      saveToLocalStorage()
    }

    // 选择历史记录
    const selectHistory = (item) => {
      selectedHistoryId.value = item.id
      emit('restore-history', {
        productForm: item.productForm,
        generatedContent: item.generatedContent
      })
      ElMessage.success('历史记录已还原')
    }

    // 删除单条历史记录
    const deleteHistory = async (id) => {
      try {
        await ElMessageBox.confirm(
          '确定要删除这条历史记录吗？',
          '确认删除',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        )
        
        historyList.value = historyList.value.filter(item => item.id !== id)
        if (selectedHistoryId.value === id) {
          selectedHistoryId.value = null
        }
        saveToLocalStorage()
        ElMessage.success('删除成功')
      } catch {
        // 用户取消删除
      }
    }

    // 清空所有历史记录
    const clearAllHistory = async () => {
      try {
        await ElMessageBox.confirm(
          '确定要清空所有历史记录吗？此操作不可恢复。',
          '确认清空',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        )
        
        historyList.value = []
        selectedHistoryId.value = null
        saveToLocalStorage()
        ElMessage.success('历史记录已清空')
      } catch {
        // 用户取消清空
      }
    }

    // 格式化时间
    const formatTime = (timestamp) => {
      const now = new Date()
      const time = new Date(timestamp)
      const diff = now - time
      
      if (diff < 60000) { // 1分钟内
        return '刚刚'
      } else if (diff < 3600000) { // 1小时内
        return `${Math.floor(diff / 60000)}分钟前`
      } else if (diff < 86400000) { // 24小时内
        return `${Math.floor(diff / 3600000)}小时前`
      } else {
        return time.toLocaleDateString('zh-CN', {
          month: 'short',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit'
        })
      }
    }

    // 保存到本地存储
    const saveToLocalStorage = () => {
      localStorage.setItem('xiaohongshu-history', JSON.stringify(historyList.value))
    }

    // 从本地存储加载
    const loadFromLocalStorage = () => {
      try {
        const saved = localStorage.getItem('xiaohongshu-history')
        if (saved) {
          historyList.value = JSON.parse(saved)
        }
        
        const collapsed = localStorage.getItem('historySidebarCollapsed')
        if (collapsed !== null) {
          isCollapsed.value = collapsed === 'true'
        }
      } catch (error) {
        console.error('加载历史记录失败:', error)
      }
    }

    // 组件挂载时加载历史记录
    onMounted(() => {
      loadFromLocalStorage()
    })

    return {
      isCollapsed,
      historyList,
      selectedHistoryId,
      toggleSidebar,
      addHistory,
      selectHistory,
      deleteHistory,
      clearAllHistory,
      formatTime
    }
  }
}
</script>

<style scoped>
.history-sidebar {
  width: 300px;
  height: calc(100vh - 80px);
  background: white;
  border-left: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  position: relative;
  z-index: 100;
}

.history-sidebar.collapsed {
  width: 50px;
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fafafa;
}

.sidebar-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.toggle-btn {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.toggle-btn:hover {
  background: #f0f0f0;
}

.history-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.history-items {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.history-item {
  padding: 12px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: white;
}

.history-item:hover {
  border-color: #ff6b6b;
  box-shadow: 0 2px 8px rgba(255, 107, 107, 0.1);
}

.history-item.active {
  border-color: #ff6b6b;
  background: #fff5f5;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.item-title {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-time {
  font-size: 12px;
  color: #999;
  margin-left: 8px;
  flex-shrink: 0;
}

.item-preview {
  margin: 0;
  font-size: 12px;
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.item-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
  opacity: 0;
  transition: opacity 0.2s;
}

.history-item:hover .item-actions {
  opacity: 1;
}

.delete-btn {
  background: none;
  border: none;
  font-size: 14px;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.delete-btn:hover {
  background: #fee;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid #f0f0f0;
}

.clear-all-btn {
  width: 100%;
  padding: 8px 16px;
  background: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 6px;
  color: #666;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.clear-all-btn:hover {
  background: #fee;
  border-color: #ff6b6b;
  color: #ff6b6b;
}

/* 滚动条样式 */
.history-list::-webkit-scrollbar {
  width: 4px;
}

.history-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 2px;
}

.history-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.history-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>