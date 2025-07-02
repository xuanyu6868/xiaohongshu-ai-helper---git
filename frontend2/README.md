# 小红书文案生成工具 - 前端

这是一个基于Vue 3 + Element Plus开发的小红书营销内容生成工具前端界面。

## 功能特性

- 🎨 **现代化UI设计** - 参考小红书官方设计风格
- 📝 **智能文案生成** - 根据产品信息生成吸引人的小红书笔记
- 💡 **标题推荐** - 提供多个吸引眼球的标题选项
- 🏷️ **热门标签** - 智能推荐相关热门标签
- 🎯 **卖点分析** - 深度分析产品卖点和营销策略
- 📱 **响应式设计** - 支持桌面端和移动端

## 技术栈

- **Vue 3** - 渐进式JavaScript框架
- **Element Plus** - Vue 3组件库
- **Vue Router** - 官方路由管理器
- **Axios** - HTTP客户端

## 快速开始

### 安装依赖

```bash
npm install
```

### 开发环境运行

```bash
npm run serve
```

项目将在 `http://localhost:8080` 启动

### 生产环境构建

```bash
npm run build
```

## 项目结构

```
frontend/
├── public/
│   └── index.html          # HTML模板
├── src/
│   ├── components/         # 组件目录
│   ├── views/             # 页面目录
│   ├── router/            # 路由配置
│   ├── App.vue            # 主应用组件
│   └── main.js            # 入口文件
├── package.json           # 项目配置
└── vue.config.js          # Vue CLI配置
```

## 主要功能模块

### 1. 产品信息输入
- 产品名称、类别、价格
- 详细描述和特点
- 目标用户群体
- 使用场景

### 2. 内容生成
- 小红书笔记内容
- 吸引人的标题（5个选项）
- 热门标签推荐
- 产品卖点分析

### 3. 交互功能
- 一键复制生成内容
- 快速填充示例数据
- 实时预览效果

## 样式特色

- 🎨 小红书品牌色彩搭配
- 💫 渐变背景和阴影效果
- 🎭 悬停动画和过渡效果
- 📱 移动端适配

## API接口

前端通过代理访问后端API：
- 开发环境：`http://localhost:8081/api`
- 生产环境：根据部署配置

## 浏览器支持

- Chrome >= 87
- Firefox >= 78
- Safari >= 14
- Edge >= 88

## 开发说明

1. 确保Node.js版本 >= 14
2. 推荐使用npm或yarn管理依赖
3. 开发时建议安装Vue DevTools浏览器扩展
4. 代码遵循ESLint规范

## 部署

### 静态部署
1. 运行 `npm run build`
2. 将 `dist` 目录部署到Web服务器

### Docker部署
```dockerfile
FROM nginx:alpine
COPY dist/ /usr/share/nginx/html/
EXPOSE 80
```

## 许可证

MIT License