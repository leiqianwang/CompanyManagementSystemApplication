-- ================================================
-- 嵌入模型资源表结构更新
-- ================================================
DROP TABLE IF EXISTS `embedding_model_resource`;
CREATE TABLE `embedding_model_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_name` varchar(100) NOT NULL COMMENT '资源名称',
  `model_name` varchar(100) NOT NULL COMMENT '模型名称',
  `model_type` varchar(50) NOT NULL COMMENT '模型类型',
  `api_key` varchar(255) NOT NULL COMMENT 'API密钥',
  `secret_key` varchar(255) DEFAULT NULL COMMENT '密钥',
  `api_url` varchar(500) NOT NULL COMMENT 'API地址',
  `dimensions` int(11) DEFAULT 1536 COMMENT '向量维度',
  `max_input_tokens` int(11) DEFAULT 8192 COMMENT '最大输入token数',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='嵌入模型资源表';

-- 插入测试数据
INSERT INTO `embedding_model_resource` (`resource_name`, `model_name`, `model_type`, `api_key`, `secret_key`, `api_url`, `dimensions`, `max_input_tokens`, `frequency_limit`, `active`, `operation`, `extra_key`, `create_by`, `remark`) VALUES
('OpenAI Text Embedding', 'text-embedding-ada-002', 'TEXT_EMBEDDING', 'sk-test-embedding-key', NULL, 'https://api.openai.com/v1/embeddings', 1536, 8192, 100, '0', '高质量文本嵌入模型', '{"encoding_format": "float"}', 'admin', 'OpenAI官方嵌入模型'),
('百度嵌入模型', 'ernie-text-embedding', 'TEXT_EMBEDDING', 'test-baidu-key', 'test-secret', 'https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/embedding-v1', 384, 1024, 60, '0', '中文优化嵌入模型', '{"batch_size": 16}', 'admin', '百度文心嵌入模型');
