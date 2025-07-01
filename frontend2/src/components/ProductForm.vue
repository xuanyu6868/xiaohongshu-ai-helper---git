<template>
  <el-aside class="input-section">
    <div class="input-card">
      <h3>产品信息输入</h3>

      <!-- 产品基本信息 -->
      <el-form :model="productForm" label-width="80px" class="product-form">
        <el-form-item label="产品名称">
          <el-input :model-value="productForm.name" @input="updateForm('name', $event)" placeholder="请输入产品名称" />
        </el-form-item>

        <el-form-item label="产品类别">
          <el-select :model-value="productForm.category" @change="updateForm('category', $event)" placeholder="请选择产品类别" style="width: 100%">
            <el-option label="美妆护肤" value="beauty" />
            <el-option label="服装配饰" value="fashion" />
            <el-option label="美食饮品" value="food" />
            <el-option label="生活用品" value="lifestyle" />
            <el-option label="数码科技" value="tech" />
            <el-option label="运动健身" value="sports" />
            <el-option label="母婴用品" value="baby" />
            <el-option label="家居装饰" value="home" />
          </el-select>
        </el-form-item>

        <el-form-item label="价格范围">
          <el-input :model-value="productForm.price" @input="updateForm('price', $event)" placeholder="如：99-199元" />
        </el-form-item>

        <el-form-item label="产品描述">
          <el-input
              :model-value="productForm.description"
              @input="updateForm('description', $event)"
              type="textarea"
              :rows="4"
              placeholder="请详细描述产品特点、功效、使用感受等"
          />
        </el-form-item>

        <el-form-item label="目标用户">
          <el-input :model-value="productForm.targetUser" @input="updateForm('targetUser', $event)" placeholder="如：20-30岁女性，学生党等" />
        </el-form-item>

        <el-form-item label="使用场景">
          <el-input :model-value="productForm.scenario" @input="updateForm('scenario', $event)" placeholder="如：日常护肤、约会妆容、运动健身等" />
        </el-form-item>
      </el-form>

      <!-- 生成按钮 -->
      <div class="generate-buttons">
        <el-button type="primary" @click="handleGenerate" :loading="loading" class="generate-btn">
          <el-icon><Magic /></el-icon>
          一键生成小红书文案
        </el-button>
      </div>
    </div>
  </el-aside>
</template>

<script>
import { Magic } from '@element-plus/icons-vue'

export default {
  name: 'ProductForm',
  components: {
    Magic
  },
  props: {
    productForm: {
      type: Object,
      required: true
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  emits: ['generate', 'update-form'],
  methods: {
    handleGenerate() {
      this.$emit('generate')
    },
    updateForm(field, value) {
      this.$emit('update-form', field, value)
    }
  }
}
</script>

<style scoped>
.input-section {
  width: 400px;
  padding: 20px;
  background: #f8f9fa;
}

.input-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.input-card h3 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 18px;
  font-weight: 600;
}

.product-form {
  margin-bottom: 20px;
}

.generate-buttons {
  text-align: center;
}

.generate-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, #ff6b6b, #ff8e8e);
  border: none;
  transition: all 0.3s ease;
}

.generate-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(255, 107, 107, 0.3);
}

.generate-btn:active {
  transform: translateY(0);
}
</style>