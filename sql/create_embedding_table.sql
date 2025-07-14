-- 创建嵌入模型资源表
USE `ry-vue`;

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

-- 插入测试数据
INSERT INTO `embedding_model_resource` (`resource_name`, `resource_type`, `api_key`, `secret_key`, `api_url`, `dimension`, `frequency_limit`, `active`, `operation`, `create_by`, `remark`) VALUES
('OpenAI Text Embedding', 'TEXT_EMBEDDING', 'sk-test-embedding-key', NULL, 'https://api.openai.com/v1/embeddings', 1536, 100, '0', '高质量文本嵌入模型，支持多语言', 'admin', 'OpenAI官方嵌入模型'),
('百度嵌入模型', 'TEXT_EMBEDDING', 'test-baidu-key', 'test-secret', 'https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/embedding-v1', 384, 60, '0', '中文优化嵌入模型，适合中文场景', 'admin', '百度文心嵌入模型'),
('智谱嵌入模型', 'TEXT_EMBEDDING', 'test-zhipu-key', NULL, 'https://open.bigmodel.cn/api/paas/v4/embeddings', 1024, 80, '0', '国产嵌入模型，性能优异', 'admin', '智谱AI嵌入模型');


-- VDB向量数据库资源
-- use `ry-vue`;
DROP TABLE IF EXISTS `vector_db_resource`;
CREATE TABLE `VDB_vector_db_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `resource_name` varchar(100) NOT NULL COMMENT '资源名称',
  `resource_type` varchar(50) NOT NULL COMMENT '资源类型',
    `embedding` varchar(100) NOT NULL COMMENT 'embedding',
 `url` varchar(500) NOT NULL COMMENT 'url',
`db_name` varchar(100) NOT NULL COMMENT '数据库名',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `active` char(1) DEFAULT '0' COMMENT '是否启用（0正常 1停用）',
  `operation` text COMMENT '操作说明',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resource_name` (`resource_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='向量数据库资源表';