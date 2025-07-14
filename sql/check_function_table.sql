-- 检查function_resource表是否存在
SELECT COUNT(*) as table_exists FROM information_schema.tables 
WHERE table_schema = 'ry-vue' AND table_name = 'function_resource';

-- 如果存在，显示表结构
DESCRIBE function_resource;

-- 显示现有数据
SELECT * FROM function_resource;
