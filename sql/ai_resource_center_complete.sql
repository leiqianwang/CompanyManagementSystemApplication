-- ================================================
-- AI资源中心完整建表和菜单配置脚本
-- 数据库：ry 或 ruoyi (RuoYi系统数据库)
-- ================================================

-- 切换到RuoYi系统数据库
-- USE ry; 

-- ================================================
-- 1. 创建AI模型资源表
-- ================================================
DROP TABLE IF EXISTS `ai_model_resource`;
CREATE TABLE `ai_model_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_name` varchar(100) NOT NULL COMMENT '资源名称',
  `resource_type` varchar(50) NOT NULL COMMENT '资源类型',
  `default_model` varchar(100) DEFAULT NULL COMMENT '默认模型',
  `api_key` varchar(255) NOT NULL COMMENT 'API密钥',
  `secret_key` varchar(255) DEFAULT NULL COMMENT '密钥',
  `api_url` varchar(500) NOT NULL COMMENT 'API地址',
  `frequency_limit` int(11) DEFAULT 100 COMMENT '频率限制(次/分钟)',
  `creation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `active` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `operation` text COMMENT '操作说明',
  `extra_key` text COMMENT '扩展字段',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resource_name` (`resource_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='AI模型资源表';

-- ================================================
-- 3. 创建功能资源表 (预留)
-- ================================================
DROP TABLE IF EXISTS `ai_function_resource`;
CREATE TABLE `ai_function_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `function_name` varchar(100) NOT NULL COMMENT '功能名称',
  `function_type` varchar(50) NOT NULL COMMENT '功能类型',
  `api_url` varchar(500) NOT NULL COMMENT 'API地址',
  `active` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `operation` text COMMENT '操作说明',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='功能资源表';

-- ================================================
-- 4. 创建嵌入模型资源表
-- ================================================
DROP TABLE IF EXISTS `embedding_model_resource`;
CREATE TABLE `embedding_model_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `resource_name` varchar(100) NOT NULL COMMENT '资源名称',
  `resource_type` varchar(50) NOT NULL COMMENT '资源类型',
  `api_key` varchar(255) NOT NULL COMMENT 'apiKey',
  `secret_key` varchar(255) DEFAULT NULL COMMENT 'secretKey',
  `api_url` varchar(500) NOT NULL COMMENT 'apiUrl',
  `dimension` int(11) DEFAULT 1536 COMMENT 'dimension',
  `frequency_limit` int(11) DEFAULT 100 COMMENT '频率限制(次/分钟)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `active` char(1) DEFAULT '0' COMMENT '是否启用（0正常 1停用）',
  `operation` text COMMENT '操作说明',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resource_name` (`resource_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='嵌入模型资源表';

-- ================================================
-- 5. 插入测试数据
-- ================================================
INSERT INTO `ai_model_resource` (`resource_name`, `resource_type`, `default_model`, `api_key`, `secret_key`, `api_url`, `frequency_limit`, `active`, `operation`, `extra_key`, `create_by`, `remark`) VALUES
('OpenAI GPT-4', 'LLM', 'gpt-4', 'sk-test-key-123', NULL, 'https://api.openai.com/v1/chat/completions', 60, '0', '测试OpenAI GPT-4模型', '{"temperature": 0.7, "max_tokens": 1000}', 'admin', 'OpenAI GPT-4测试资源'),
('百度文心一言', 'LLM', 'ernie-bot', 'test-api-key', 'test-secret-key', 'https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions', 100, '0', '测试百度文心一言', '{"temperature": 0.5}', 'admin', '百度文心一言测试资源');

-- 嵌入模型资源测试数据
INSERT INTO `embedding_model_resource` (`resource_name`, `resource_type`, `api_key`, `secret_key`, `api_url`, `dimension`, `frequency_limit`, `active`, `operation`, `create_by`, `remark`) VALUES
('OpenAI Text Embedding', 'TEXT_EMBEDDING', 'sk-test-embedding-key', NULL, 'https://api.openai.com/v1/embeddings', 1536, 100, '0', '高质量文本嵌入模型，支持多语言', 'admin', 'OpenAI官方嵌入模型'),
('百度嵌入模型', 'TEXT_EMBEDDING', 'test-baidu-key', 'test-secret', 'https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/embedding-v1', 384, 60, '0', '中文优化嵌入模型，适合中文场景', 'admin', '百度文心嵌入模型'),
('智谱嵌入模型', 'TEXT_EMBEDDING', 'test-zhipu-key', NULL, 'https://open.bigmodel.cn/api/paas/v4/embeddings', 1024, 80, '0', '国产嵌入模型，性能优异', 'admin', '智谱AI嵌入模型');

-- ================================================
-- 6. 菜单配置
-- ================================================

-- 主菜单：AI资源中心
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('AI资源中心', '0', '4', 'ai', null, 1, 0, 'M', '0', '0', '', 'ai', 'admin', sysdate(), '', null, 'AI资源中心目录');

-- 获取刚插入的主菜单ID
SET @parentId = LAST_INSERT_ID();

-- 子菜单：AI模型资源
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('AI模型资源', @parentId, '1', 'model-resource', 'ai/model-resource/index', 1, 0, 'C', '0', '0', 'ai:modelResource:list', 'model', 'admin', sysdate(), '', null, 'AI模型资源菜单');

SET @modelResourceMenuId = LAST_INSERT_ID();

-- 子菜单：向量数据库
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('向量数据库', @parentId, '2', 'vector-database', 'ai/vector-database/index', 1, 0, 'C', '0', '0', 'ai:vectorDatabase:list', 'database', 'admin', sysdate(), '', null, '向量数据库菜单');

SET @vectorDatabaseMenuId = LAST_INSERT_ID();

-- 子菜单：功能资源
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('功能资源', @parentId, '3', 'function-resource', 'ai/function-resource/index', 1, 0, 'C', '0', '0', 'ai:functionResource:list', 'function', 'admin', sysdate(), '', null, '功能资源菜单');

SET @functionResourceMenuId = LAST_INSERT_ID();

-- 子菜单：嵌入模型
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('嵌入模型', @parentId, '4', 'embedding-model', 'ai/embedding-model/index', 1, 0, 'C', '0', '0', 'ai:embeddingModel:list', 'embed', 'admin', sysdate(), '', null, '嵌入模型菜单');

SET @embeddingModelMenuId = LAST_INSERT_ID();

-- AI模型资源权限按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('AI模型资源查询', @modelResourceMenuId, '1', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:query', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('AI模型资源新增', @modelResourceMenuId, '2', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:add', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('AI模型资源修改', @modelResourceMenuId, '3', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:edit', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('AI模型资源删除', @modelResourceMenuId, '4', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:remove', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('AI模型资源导出', @modelResourceMenuId, '5', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:export', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('AI模型资源测试', @modelResourceMenuId, '6', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:test', '', 'admin', sysdate(), '', null, '');

-- 向量数据库权限按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('向量数据库查询', @vectorDatabaseMenuId, '1', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:query', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('向量数据库新增', @vectorDatabaseMenuId, '2', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:add', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('向量数据库修改', @vectorDatabaseMenuId, '3', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:edit', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('向量数据库删除', @vectorDatabaseMenuId, '4', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:remove', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('向量数据库测试', @vectorDatabaseMenuId, '5', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:test', '', 'admin', sysdate(), '', null, '');


-- 嵌入模型权限按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('嵌入模型查询', @embeddingModelMenuId, '1', '', '', 1, 0, 'F', '0', '0', 'ai:embeddingModel:query', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('嵌入模型新增', @embeddingModelMenuId, '2', '', '', 1, 0, 'F', '0', '0', 'ai:embeddingModel:add', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('嵌入模型修改', @embeddingModelMenuId, '3', '', '', 1, 0, 'F', '0', '0', 'ai:embeddingModel:edit', '', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('嵌入模型删除', @embeddingModelMenuId, '4', '', '', 1, 0, 'F', '0', '0', 'ai:embeddingModel:remove', '', 'admin', sysdate(), '', null, '');

-- ================================================
-- 7. 验证脚本 (可选执行)
-- ================================================
-- 查看AI资源中心菜单
-- SELECT menu_id, menu_name, parent_id, path, component, perms 
-- FROM sys_menu 
-- WHERE menu_name = 'AI资源中心' OR parent_id IN 
-- (SELECT menu_id FROM sys_menu WHERE menu_name = 'AI资源中心');

-- 查看AI模型资源表数据
-- SELECT * FROM ai_model_resource;

COMMIT;
