-- Function资源表
-- 用于管理AI资源中心的功能资源信息

-- 删除表（如果存在）
DROP TABLE IF EXISTS `function_resource`;

-- 创建Function资源表
CREATE TABLE `function_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号（主键）',
  `function_code` varchar(100) NOT NULL COMMENT '编号',
  `function_description` text COMMENT '功能描述',
  `category_class` varchar(200) DEFAULT NULL COMMENT '所属类',
  `execution_method` varchar(500) DEFAULT NULL COMMENT '执行方法',
  `parameter_management` text COMMENT '参数管理',
  `is_enabled` char(1) DEFAULT '1' COMMENT '是否启用（1启用 0停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_function_code` (`function_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Function资源表';

-- 插入测试数据
INSERT INTO `function_resource` (`function_code`, `function_description`, `category_class`, `execution_method`, `parameter_management`, `is_enabled`, `create_by`, `remark`) VALUES
('FUNC_001', '文本向量化处理', 'com.ruoyi.ai.service.TextProcessingService', 'vectorizeText', '{"input": {"type": "string", "required": true}, "model": {"type": "string", "default": "text-embedding-ada-002"}}', '1', 'admin', '将文本转换为向量表示'),
('FUNC_002', '图像特征提取', 'com.ruoyi.ai.service.ImageProcessingService', 'extractFeatures', '{"image_url": {"type": "string", "required": true}, "feature_type": {"type": "enum", "values": ["basic", "advanced"]}}', '1', 'admin', '从图像中提取特征向量'),
('FUNC_003', '语义相似度计算', 'com.ruoyi.ai.service.SimilarityService', 'calculateSimilarity', '{"text1": {"type": "string", "required": true}, "text2": {"type": "string", "required": true}, "algorithm": {"type": "string", "default": "cosine"}}', '1', 'admin', '计算两个文本的语义相似度'),
('FUNC_004', '知识图谱查询', 'com.ruoyi.ai.service.KnowledgeGraphService', 'queryGraph', '{"query": {"type": "string", "required": true}, "limit": {"type": "integer", "default": 10}}', '0', 'admin', '在知识图谱中执行查询操作'),
('FUNC_005', '数据预处理', 'com.ruoyi.ai.service.DataPreprocessingService', 'preprocessData', '{"data": {"type": "object", "required": true}, "operations": {"type": "array", "items": "string"}}', '1', 'admin', '对输入数据进行预处理');
