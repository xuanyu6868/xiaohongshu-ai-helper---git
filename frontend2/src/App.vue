<template>
  <div id="app">
    <!-- 顶部导航栏 -->
    <AppHeader />

    <!-- 主要内容区域 -->
    <el-container class="main-container">
      <!-- 左侧输入区域 -->
      <ProductForm 
      :product-form="productForm" 
      :loading="loading" 
      @generate="generateContent" 
      @update-form="updateProductForm"
    />

      <!-- 右侧结果展示区域 -->
      <ResultTabs 
        v-model:active-tab="activeTab" 
        :generated-content="generatedContent" 
        @copy="copyContent" 
      />
    </el-container>

    <!-- 底部提示区域 -->
     <SuggestionArea @fill-example="fillExample" />
   </div>
 </template>

<script>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import AppHeader from './components/AppHeader.vue'
import ProductForm from './components/ProductForm.vue'
import ResultTabs from './components/ResultTabs.vue'
import SuggestionArea from './components/SuggestionArea.vue'

export default {
  name: 'App',
  components: {
    AppHeader,
    ProductForm,
    ResultTabs,
    SuggestionArea
  },
  setup() {
    const activeTab = ref('note')
    const loading = ref(false)

    const productForm = reactive({
      name: '',
      category: '',
      price: '',
      description: '',
      targetUser: '',
      scenario: ''
    })

    const generatedContent = reactive({
      note: '',
      titles: [],
      tags: [],
      analysis: ''
    })

    // 流式生成标题
    const streamGenerateTitles = () => {
      return new Promise((resolve, reject) => {
        let titlesStreamCompleted = false
        let hasReceivedTitlesData = false
        let titlesContent = ''

        const titlesEventSource = new EventSource(`http://localhost:8081/api/notes/stream-titles?name=${encodeURIComponent(productForm.name)}&description=${encodeURIComponent(productForm.description)}&category=${encodeURIComponent(productForm.category)}`)

        titlesEventSource.onmessage = (event) => {
          titlesContent += event.data
          hasReceivedTitlesData = true
        }

        titlesEventSource.onerror = (error) => {
          console.error('Titles EventSource failed:', error)
          if (!hasReceivedTitlesData && !titlesStreamCompleted) {
            reject(new Error('标题流式传输失败'))
          }
          titlesEventSource.close()
        }

        titlesEventSource.onopen = () => {
          console.log('Titles connection to server opened.')
        }

        // 监听标题流式传输结束
        const checkTitlesStreamEnd = setInterval(() => {
          if (titlesEventSource.readyState === EventSource.CLOSED) {
            clearInterval(checkTitlesStreamEnd)
            titlesStreamCompleted = true
            if (hasReceivedTitlesData) {
              // 解析标题内容
              generatedContent.titles = titlesContent.split('\n')
                .map(title => title.trim())
                .filter(title => title && !title.match(/^\d+[.、]?\s*/)) // 过滤掉编号
                .slice(0, 5) // 限制最多5个标题
              resolve()
            } else {
              reject(new Error('未接收到标题内容'))
            }
          }
        }, 1000)

        // 设置标题流式传输超时
        setTimeout(() => {
          if (titlesEventSource.readyState !== EventSource.CLOSED) {
            titlesStreamCompleted = true
            titlesEventSource.close()
            if (hasReceivedTitlesData) {
              generatedContent.titles = titlesContent.split('\n')
                .map(title => title.trim())
                .filter(title => title && !title.match(/^\d+[.、]?\s*/)) // 过滤掉编号
                .slice(0, 5) // 限制最多5个标题
              resolve()
            } else {
              reject(new Error('标题流式传输超时'))
            }
          }
        }, 15000) // 15秒超时
      })
    }

    // 流式生成标签
    const streamGenerateTags = () => {
      return new Promise((resolve, reject) => {
        let tagsStreamCompleted = false
        let hasReceivedTagsData = false
        let tagsContent = ''

        const tagsEventSource = new EventSource(`http://localhost:8081/api/notes/stream-tags?name=${encodeURIComponent(productForm.name)}&category=${encodeURIComponent(productForm.category)}`)

        tagsEventSource.onmessage = (event) => {
          tagsContent += event.data
          hasReceivedTagsData = true
        }

        tagsEventSource.onerror = (error) => {
          console.error('Tags EventSource failed:', error)
          if (!hasReceivedTagsData && !tagsStreamCompleted) {
            reject(new Error('标签流式传输失败'))
          }
          tagsEventSource.close()
        }

        tagsEventSource.onopen = () => {
          console.log('Tags connection to server opened.')
        }

        // 监听标签流式传输结束
        const checkTagsStreamEnd = setInterval(() => {
          if (tagsEventSource.readyState === EventSource.CLOSED) {
            clearInterval(checkTagsStreamEnd)
            tagsStreamCompleted = true
            if (hasReceivedTagsData) {
              // 解析标签内容
              generatedContent.tags = tagsContent.split(/[,，]/)
                .map(tag => tag.trim())
                .filter(tag => tag)
              resolve()
            } else {
              reject(new Error('未接收到标签内容'))
            }
          }
        }, 1000)

        // 设置标签流式传输超时
        setTimeout(() => {
          if (tagsEventSource.readyState !== EventSource.CLOSED) {
            tagsStreamCompleted = true
            tagsEventSource.close()
            if (hasReceivedTagsData) {
              generatedContent.tags = tagsContent.split(/[,，]/)
                .map(tag => tag.trim())
                .filter(tag => tag)
              resolve()
            } else {
              reject(new Error('标签流式传输超时'))
            }
          }
        }, 15000) // 15秒超时
      })
    }

    // 生成标题和标签（流式输出）
    const generateTitlesAndTags = async () => {
      try {
        // 并行生成标题和标签
        await Promise.all([
          streamGenerateTitles(),
          streamGenerateTags()
        ])
        
        // 开始流式生成分析内容
        streamGenerateAnalysis()
      } catch (error) {
        console.error('生成标题和标签失败:', error)
        ElMessage.error(error.message || '生成标题和标签失败')
        loading.value = false
      }
    }

    // 流式生成分析内容
    const streamGenerateAnalysis = () => {
      let analysisStreamCompleted = false
      let hasReceivedAnalysisData = false

      const analysisEventSource = new EventSource(`http://localhost:8081/api/notes/stream-analysis?name=${encodeURIComponent(productForm.name)}&description=${encodeURIComponent(productForm.description)}&category=${encodeURIComponent(productForm.category)}&price=${parseFloat(productForm.price.replace(/[^\d.]/g, '')) || 0}&targetUser=${encodeURIComponent(productForm.targetUser)}&scenario=${encodeURIComponent(productForm.scenario)}`)

      analysisEventSource.onmessage = (event) => {
        generatedContent.analysis += event.data
        hasReceivedAnalysisData = true
      }

      analysisEventSource.onerror = (error) => {
        console.error('Analysis EventSource failed:', error)
        if (!hasReceivedAnalysisData && !analysisStreamCompleted) {
          ElMessage.error('分析内容流式传输失败')
        }
        analysisEventSource.close()
        loading.value = false
      }

      analysisEventSource.onopen = () => {
        console.log('Analysis connection to server opened.')
      }

      // 监听分析流式传输结束
      const checkAnalysisStreamEnd = setInterval(() => {
        if (analysisEventSource.readyState === EventSource.CLOSED) {
          clearInterval(checkAnalysisStreamEnd)
          analysisStreamCompleted = true
          loading.value = false
          if (hasReceivedAnalysisData) {
            ElMessage.success('内容生成完成')
          } else {
            ElMessage.error('未接收到分析内容')
          }
        }
      }, 1000)

      // 设置分析流式传输超时
      setTimeout(() => {
        if (analysisEventSource.readyState !== EventSource.CLOSED) {
          analysisStreamCompleted = true
          analysisEventSource.close()
          loading.value = false
          if (hasReceivedAnalysisData) {
            ElMessage.success('内容生成完成')
          } else {
            ElMessage.error('分析内容流式传输超时')
          }
        }
      }, 30000) // 30秒超时
    }

    // 生成内容
    const generateContent = () => {
      if (!productForm.name || !productForm.description) {
        ElMessage.warning('请填写产品名称和描述')
        return
      }

      loading.value = true
      generatedContent.note = ''
      generatedContent.titles = []
      generatedContent.tags = []
      generatedContent.analysis = ''

      let streamCompleted = false
      let hasReceivedData = false

      const eventSource = new EventSource(`http://localhost:8081/api/notes/stream-generate?name=${encodeURIComponent(productForm.name)}&description=${encodeURIComponent(productForm.description)}&category=${encodeURIComponent(productForm.category)}&price=${parseFloat(productForm.price.replace(/[^\d.]/g, '')) || 0}&targetUser=${encodeURIComponent(productForm.targetUser)}&scenario=${encodeURIComponent(productForm.scenario)}`)

      eventSource.onmessage = (event) => {
        generatedContent.note += event.data
        hasReceivedData = true
      }

      eventSource.onerror = (error) => {
        console.error('EventSource failed:', error)
        // 只有在没有接收到数据且不是正常结束时才显示错误
        if (!hasReceivedData && !streamCompleted) {
          ElMessage.error('流式传输失败')
          loading.value = false
        }
        eventSource.close()
      }

      eventSource.onopen = () => {
        console.log('Connection to server opened.')
      }

      // 监听流式传输结束，然后生成其他内容
      const checkStreamEnd = setInterval(() => {
        if (eventSource.readyState === EventSource.CLOSED) {
          clearInterval(checkStreamEnd)
          streamCompleted = true
          // 流式传输结束后，流式生成标题和标签
          if (hasReceivedData) {
            generateTitlesAndTags()
          } else {
            loading.value = false
            ElMessage.error('未接收到笔记内容')
          }
        }
      }, 1000)

      // 设置超时，防止无限等待
      setTimeout(() => {
        if (eventSource.readyState !== EventSource.CLOSED) {
          streamCompleted = true
          eventSource.close()
          if (hasReceivedData) {
            generateTitlesAndTags()
          } else {
            loading.value = false
            ElMessage.error('流式传输超时')
          }
        }
      }, 30000) // 30秒超时
    }

    // 复制内容
    const copyContent = async (content) => {
      try {
        await navigator.clipboard.writeText(content)
        ElMessage.success('复制成功')
      } catch (error) {
        ElMessage.error('复制失败')
      }
    }

    // 更新表单数据
    const updateProductForm = (field, value) => {
      productForm[field] = value
    }

    // 填充示例
    const fillExample = (type) => {
      switch (type) {
        case 'outdoor':
          Object.assign(productForm, {
            name: '春季踏青套装',
            category: 'fashion',
            price: '199-299元',
            description: '轻薄透气的春季外套，配上舒适的运动鞋，完美的踏青装备。面料柔软亲肤，版型修身显瘦。',
            targetUser: '20-35岁热爱户外的女性',
            scenario: '春季踏青、户外运动、日常休闲'
          })
          break
        case 'skincare':
          Object.assign(productForm, {
            name: '玻尿酸保湿精华',
            category: 'beauty',
            price: '89-129元',
            description: '高浓度玻尿酸配方，深层补水保湿，质地清爽不粘腻，适合各种肌肤类型。坚持使用肌肤水润有光泽。',
            targetUser: '18-40岁注重护肤的女性',
            scenario: '日常护肤、干燥季节、熬夜后修复'
          })
          break
        case 'fitness':
          Object.assign(productForm, {
            name: '瑜伽垫套装',
            category: 'sports',
            price: '99-159元',
            description: 'TPE环保材质，防滑耐磨，厚度适中提供良好支撑。配送瑜伽带和收纳袋，居家健身必备。',
            targetUser: '喜欢居家健身的人群',
            scenario: '居家瑜伽、健身训练、拉伸运动'
          })
          break
      }
    }

    return {
      activeTab,
      loading,
      productForm,
      generatedContent,
      generateContent,
      streamGenerateTitles,
      streamGenerateTags,
      generateTitlesAndTags,
      streamGenerateAnalysis,
      updateProductForm,
      copyContent,
      fillExample
    }
  }
}
</script>

<style scoped>
/* 全局样式 */
#app {
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  background: linear-gradient(135deg, #ffeef8 0%, #fff5f5 100%);
  min-height: 100vh;
}

/* 主容器 */
.main-container {
  min-height: calc(100vh - 80px);
}




</style>