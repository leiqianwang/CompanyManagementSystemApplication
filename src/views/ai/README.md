# AI Resource Center Module

AI资源中心模块是RuoYi-Vue系统的扩展模块，用于管理各种AI相关资源。

## 🎯 **推荐开发方式：使用代码生成器 + 自定义组织**

基于RuoYi框架的最佳实践，推荐以下开发方式：

### 1. 使用代码生成器生成基础代码
- 在数据库中创建AI相关表（ai_model_resource, vdb_vector_database等）
- 使用RuoYi的代码生成器生成Controller、Service、Mapper和前端代码
- 生成的API路径遵循 `/system/[tableName]/` 模式

### 2. 自定义前端组织结构
- 将生成的Vue组件移动到 `src/views/ai/` 目录下
- 保持API路径不变（`/system/modelResource/`等）
- 这样既利用了代码生成器，又实现了更好的模块组织

## 模块结构

```
src/views/ai/                             # AI模块主目录（自定义组织）
├── model-resource/                       # AI模型资源管理
│   └── index.vue                        # 基于生成代码优化的组件
├── vector-database/                      # 向量数据库管理  
│   └── index.vue                        # 基于生成代码优化的组件
├── function-resource/                    # 函数资源管理
│   └── index.vue                        # 基于生成代码优化的组件
├── embedding-model/                      # 嵌入模型资源管理
│   └── index.vue                        # 基于生成代码优化的组件
└── README.md                            # 模块说明文档

src/api/ai/                              # AI模块API目录（自定义组织）
├── modelResource.js                     # API路径: /system/modelResource/*
├── vectorDatabase.js                    # API路径: /system/vectorDb/*
├── functionResource.js                  # API路径: /system/functionResource/*
└── embeddingModel.js                    # API路径: /system/embeddingModel/*
```

## 🔄 **开发流程**

### 步骤1：数据库表设计
根据ERD创建数据库表：
- `ai_model_resource`
- `vdb_vector_database` 
- `function_resource`
- `embedding_model_resource`

### 步骤2：使用代码生成器
1. 在RuoYi管理后台进入"系统工具" -> "代码生成"
2. 导入数据库表
3. 配置生成信息（模块名、包路径等）
4. 生成代码并下载

### 步骤3：整合到AI模块
1. 将生成的Controller放到backend对应位置
2. 将生成的Vue组件移动到 `src/views/ai/` 对应子目录
3. 将生成的API文件移动到 `src/api/ai/` 目录
4. 根据需要优化Vue组件的UI和功能

### 步骤4：配置路由和菜单
按照标准流程配置系统菜单和权限

## 🔧 **API路径设计**

虽然前端组织在 `/ai/` 目录下，但API路径保持生成器的标准：

| 功能模块 | API路径 | 权限标识 |
|---------|---------|----------|
| AI模型资源 | `/system/modelResource/*` | `system:modelResource:*` |
| 向量数据库 | `/system/vectorDb/*` | `system:vectorDb:*` |
| 函数资源 | `/system/functionResource/*` | `system:functionResource:*` |
| 嵌入模型 | `/system/embeddingModel/*` | `system:embeddingModel:*` |

## 💡 **这种方式的优势**

1. **充分利用框架特性**: 使用代码生成器，减少重复工作
2. **标准化开发**: 遵循RuoYi的开发规范和最佳实践
3. **便于维护**: 生成的代码结构标准，易于维护
4. **快速开发**: 大幅减少开发时间
5. **灵活组织**: 前端可以自定义组织结构，提升可读性

## 🎨 **自定义增强功能**

在生成的基础代码上，可以添加AI特有的功能：

1. **连接测试**: 测试AI模型、向量数据库连接
2. **函数执行**: 测试AI函数的执行效果
3. **向量化测试**: 测试嵌入模型的向量化能力
4. **配置验证**: 验证API Key、连接参数等
5. **监控面板**: AI资源的使用情况监控

## 🔐 **权限配置**

系统菜单配置示例：
```
AI资源中心 (父菜单, 路由: /ai)
├── AI模型资源 (system:modelResource:list)
├── 向量数据库 (system:vectorDb:list)  
├── 函数资源 (system:functionResource:list)
└── 嵌入模型 (system:embeddingModel:list)
```

## 📝 **注意事项**

1. **保持API路径一致**: 前端移动后，确保API导入路径正确
2. **权限标识更新**: 使用生成器标准的权限标识
3. **数据字典配置**: 及时配置相关的数据字典类型
4. **测试功能**: 生成代码基础上添加的测试功能需要后端支持

这种混合方式既发挥了RuoYi代码生成器的优势，又实现了良好的模块组织结构。
