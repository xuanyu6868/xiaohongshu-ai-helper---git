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

    // 生成内容
    const generateContent = async () => {
      if (!productForm.name || !productForm.description) {
        ElMessage.warning('请填写产品名称和描述')
        return
      }

      loading.value = true

      try {
        // 调用后端API
        const response = await axios.post('http://localhost:8080/api/notes/generate', {
          name: productForm.name,
          description: productForm.description,
          category: productForm.category,
          price: parseFloat(productForm.price.replace(/[^\d.]/g, '')) || 0,
          targetUser: productForm.targetUser,
          scenario: productForm.scenario
        })

        // 更新生成的内容
        const data = response.data
        generatedContent.note = data.note
        generatedContent.titles = data.titles || []
        generatedContent.tags = data.tags || []
        generatedContent.analysis = data.analysis

        ElMessage.success('生成成功！')

      } catch (error) {
        console.error('API调用失败:', error)
        ElMessage.error('生成失败，请检查后端服务是否启动')
      } finally {
        loading.value = false
      }
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