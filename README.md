# 小红书AI营销助手

一个基于Spring Boot + Vue 3的智能小红书文案生成工具，帮助用户快速生成高质量的小红书营销内容。

## 🌟 功能特性

### 核心功能
- **智能文案生成**: 基于产品信息自动生成小红书风格的营销文案
- **流式输出**: 实时显示AI生成过程，提升用户体验
- **多维度内容**: 生成笔记正文、吸引标题、热门标签、卖点分析
- **历史记录**: 保存生成历史，方便回顾和复用
- **一键复制**: 快速复制生成的内容到剪贴板

### 技术特性
- **前后端分离**: Spring Boot后端 + Vue 3前端
- **AI集成**: 支持阿里云DashScope（DeepSeek模型）
- **响应式设计**: 适配不同屏幕尺寸
- **数据持久化**: MySQL数据库存储
- **跨域支持**: 完善的CORS配置

## 🚀 快速开始

### 环境要求
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 后端启动

1. **配置数据库**
   ```sql
   CREATE DATABASE xiaohongshu_ai;
   ```

2. **修改配置文件**
   编辑 `src/main/resources/application.yml`：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/xiaohongshu_ai
       username: your_username
       password: your_password
     ai:
       openai:
         api-key: your_dashscope_api_key
   ```

3. **启动后端服务**
   ```bash
   mvn spring-boot:run
   ```
   服务将在 http://localhost:8081 启动

### 前端启动

1. **安装依赖**
   ```bash
   cd frontend2
   npm install
   ```

2. **启动开发服务器**
   ```bash
   npm run serve
   ```
   前端将在 http://localhost:8080 启动

## 📱 使用指南

### 1. 产品信息输入
- **产品名称**: 输入要推广的产品名称
- **产品类别**: 选择合适的产品分类
- **价格范围**: 填写产品价格信息
- **产品描述**: 详细描述产品特点和功效
- **目标用户**: 定义目标用户群体（可选）
- **使用场景**: 描述产品使用场景（可选）

### 2. 内容生成
点击"一键生成小红书文案"按钮，系统将：
- 实时流式生成笔记正文
- 自动生成5个吸引人的标题
- 推荐8个热门标签
- 提供详细的卖点分析

### 3. 内容管理
- **复制功能**: 点击任意内容旁的复制按钮
- **历史记录**: 查看和重新加载之前生成的内容
- **快速示例**: 使用预设示例快速体验功能

## 🛠️ 技术架构

### 后端技术栈
- **Spring Boot 3.2.4**: 主框架
- **Spring AI**: AI集成框架
- **Spring Data JPA**: 数据访问层
- **MySQL**: 数据库
- **Lombok**: 代码简化
- **Jackson**: JSON处理

### 前端技术栈
- **Vue 3**: 前端框架
- **Element Plus**: UI组件库
- **Axios**: HTTP客户端
- **Vue Router**: 路由管理

### AI服务
- **阿里云DashScope**: AI服务提供商
- **DeepSeek-V3**: 大语言模型
- **流式输出**: 实时响应用户

## 📁 项目结构

```
xiaohongshu-ai-helper/
├── src/main/java/com/example/xiaohongshu_ai_helper/
│   ├── ai/                 # AI相关组件
│   │   ├── AiClient.java   # AI客户端
│   │   └── PromptBuilder.java # 提示词构建器
│   ├── config/             # 配置类
│   │   ├── WebConfig.java  # Web配置
│   │   └── GlobalExceptionHandler.java # 异常处理
│   ├── controller/         # 控制器层
│   │   └── NoteController.java
│   ├── model/              # 数据模型
│   │   ├── NoteEntity.java
│   │   ├── NoteRequest.java
│   │   └── NoteResponse.java
│   ├── repository/         # 数据访问层
│   │   └── NoteRepository.java
│   └── service/            # 业务逻辑层
│       └── NoteService.java
├── frontend2/              # 前端项目
│   ├── src/
│   │   ├── App.vue         # 主组件
│   │   └── main.js         # 入口文件
│   └── package.json
└── pom.xml                 # Maven配置
```

## 🔧 配置说明

### API密钥配置
1. 注册阿里云账号并开通DashScope服务
2. 获取API Key
3. 在 `application.yml` 中配置：
   ```yaml
   spring:
     ai:
       openai:
         base-url: https://dashscope.aliyuncs.com/compatible-mode
         api-key: your-api-key-here
         chat:
           options:
             model: deepseek-v3
   ```

### 数据库配置
确保MySQL服务运行，并创建对应数据库：
```sql
CREATE DATABASE xiaohongshu_ai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [阿里云DashScope](https://dashscope.aliyun.com/)

## 📞 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 Issue
- 发送邮件
- 微信群讨论

---

**让AI助力你的小红书营销，轻松创作爆款内容！** 🎉