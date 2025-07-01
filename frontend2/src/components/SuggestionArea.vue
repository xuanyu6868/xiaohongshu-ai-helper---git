<template>
  <div class="suggestion-area">
    <div class="suggestion-buttons">
      <el-button class="suggestion-btn" @click="handleFillExample('outdoor')">
        能帮我写一篇关于春天户外郊游的文案吗？
      </el-button>
      <el-button class="suggestion-btn" @click="handleFillExample('skincare')">
        我需要一条关于日常护肤分享的文案。
      </el-button>
      <el-button class="suggestion-btn" @click="handleFillExample('fitness')">
        请为我做一条健身日记的文案内容。
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SuggestionArea',
  emits: ['fillExample'],
  methods: {
    handleFillExample(type) {
      this.$emit('fillExample', type)
    }
  }
}
</script>

<style scoped>
/* 建议区域 */
.suggestion-area {
  padding: 0 10px ;
  margin-top: -50px;
  background: white;
  border-top: -20px solid #f0f0f0;
}
/* 建议按钮容器 */
.suggestion-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
  max-width: 1200px;
  margin: 0 auto;
}
/* 建议按钮 */
.suggestion-btn {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  color: #666;
  border-radius: 20px;
  padding: 12px 24px;
  font-size: 14px;
  transition: all 0.3s ease;
  white-space: nowrap;
}
/* 建议按钮悬停效果 */
.suggestion-btn:hover {
  background: #e3f2fd;
  border-color: #2196f3;
  color: #2196f3;
  transform: translateY(-15px);
}
/* 建议按钮点击效果 */
.suggestion-btn:active {
  transform: translateY(0);
}
/* 建议按钮点击效果 */
@media (max-width: 768px) {
  .suggestion-buttons {
    flex-direction: column;
    align-items: center;
  }
  /* 建议按钮点击效果 */
  .suggestion-btn {
    width: 100%;
    max-width: 300px;
  }
}
</style>