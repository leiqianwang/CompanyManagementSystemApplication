-- 创建向量数据库资源表
USE `ry-vue`;

DROP TABLE IF EXISTS `vector_db_resource`;
CREATE TABLE `vector_db_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `db_name` varchar(100) NOT NULL COMMENT '数据库名称',
  `db_type` varchar(50) NOT NULL COMMENT '数据库类型(CHROMA/PINECONE/MILVUS/WEAVIATE)',
  `connection_url` varchar(500) NOT NULL COMMENT '连接地址',
  `api_key` varchar(255) DEFAULT NULL COMMENT 'API密钥',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `database_name` varchar(100) DEFAULT NULL COMMENT '数据库名',
  `collection_name` varchar(100) DEFAULT NULL COMMENT '集合名称',
  `dimension` int(11) DEFAULT 1536 COMMENT '向量维度',
  `max_connections` int(11) DEFAULT 10 COMMENT '最大连接数',
  `timeout` int(11) DEFAULT 30000 COMMENT '超时时间(毫秒)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `active` char(1) DEFAULT '0' COMMENT '是否启用（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_db_name` (`db_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='向量数据库资源表';

-- 插入测试数据
INSERT INTO `vector_db_resource` (`db_name`, `db_type`, `connection_url`, `api_key`, `username`, `password`, `database_name`, `collection_name`, `dimension`, `max_connections`, `timeout`, `active`, `create_by`, `remark`) VALUES
('本地Chroma数据库', 'CHROMA', 'http://localhost:8000', NULL, NULL, NULL, 'default', 'documents', 1536, 10, 30000, '0', 'admin', '本地Chroma向量数据库'),
('Pinecone生产环境', 'PINECONE', 'https://your-index.svc.gcp-starter.pinecone.io', 'test-pinecone-api-key', NULL, NULL, 'production', 'embeddings', 1536, 20, 60000, '0', 'admin', 'Pinecone云端向量数据库'),
('Milvus集群', 'MILVUS', 'localhost:19530', NULL, 'milvus', 'password123', 'vector_db', 'text_collection', 1024, 15, 45000, '0', 'admin', 'Milvus分布式向量数据库'),
('Weaviate实例', 'WEAVIATE', 'http://localhost:8080', 'test-weaviate-key', NULL, NULL, 'default', 'Document', 768, 10, 30000, '1', 'admin', 'Weaviate图形向量数据库');
