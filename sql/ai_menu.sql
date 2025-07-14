-- AI资源中心模块菜单SQL
-- 菜单 SQL

-- 主菜单：AI资源中心
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI资源中心', '0', '4', 'ai', null, 1, 0, 'M', '0', '0', '', 'ai', 'admin', sysdate(), '', null, 'AI资源中心目录');

-- 获取刚插入的主菜单ID (需要根据实际情况调整)
SET @parentId = LAST_INSERT_ID();

-- 子菜单：AI模型资源
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI模型资源', @parentId, '1', 'model-resource', 'ai/model-resource/index', 1, 0, 'C', '0', '0', 'ai:modelResource:list', 'model', 'admin', sysdate(), '', null, 'AI模型资源菜单');

SET @modelResourceMenuId = LAST_INSERT_ID();

-- 子菜单：向量数据库
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('向量数据库', @parentId, '2', 'vector-database', 'ai/vector-database/index', 1, 0, 'C', '0', '0', 'ai:vectorDatabase:list', 'database', 'admin', sysdate(), '', null, '向量数据库菜单');

SET @vectorDatabaseMenuId = LAST_INSERT_ID();

-- 子菜单：功能资源
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('功能资源', @parentId, '3', 'function-resource', 'ai/function-resource/index', 1, 0, 'C', '0', '0', 'ai:functionResource:list', 'function', 'admin', sysdate(), '', null, '功能资源菜单');

SET @functionResourceMenuId = LAST_INSERT_ID();

-- 子菜单：嵌入模型
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('嵌入模型', @parentId, '4', 'embedding-model', 'ai/embedding-model/index', 1, 0, 'C', '0', '0', 'ai:embeddingModel:list', 'embed', 'admin', sysdate(), '', null, '嵌入模型菜单');

SET @embeddingModelMenuId = LAST_INSERT_ID();

-- AI模型资源权限按钮
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI模型资源查询', @modelResourceMenuId, '1', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:query', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI模型资源新增', @modelResourceMenuId, '2', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:add', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI模型资源修改', @modelResourceMenuId, '3', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:edit', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI模型资源删除', @modelResourceMenuId, '4', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:remove', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI模型资源导出', @modelResourceMenuId, '5', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:export', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI模型资源测试', @modelResourceMenuId, '6', '', '', 1, 0, 'F', '0', '0', 'ai:modelResource:test', '', 'admin', sysdate(), '', null, '');

-- 向量数据库权限按钮
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('向量数据库查询', @vectorDatabaseMenuId, '1', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:query', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('向量数据库新增', @vectorDatabaseMenuId, '2', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:add', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('向量数据库修改', @vectorDatabaseMenuId, '3', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:edit', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('向量数据库删除', @vectorDatabaseMenuId, '4', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:remove', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('向量数据库测试', @vectorDatabaseMenuId, '5', '', '', 1, 0, 'F', '0', '0', 'ai:vectorDatabase:test', '', 'admin', sysdate(), '', null, '');

-- 功能资源权限按钮
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('功能资源查询', @functionResourceMenuId, '1', '', '', 1, 0, 'F', '0', '0', 'ai:functionResource:query', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('功能资源新增', @functionResourceMenuId, '2', '', '', 1, 0, 'F', '0', '0', 'ai:functionResource:add', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('功能资源修改', @functionResourceMenuId, '3', '', '', 1, 0, 'F', '0', '0', 'ai:functionResource:edit', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('功能资源删除', @functionResourceMenuId, '4', '', '', 1, 0, 'F', '0', '0', 'ai:functionResource:remove', '', 'admin', sysdate(), '', null, '');

-- 嵌入模型权限按钮
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('嵌入模型查询', @embeddingModelMenuId, '1', '', '', 1, 0, 'F', '0', '0', 'ai:embeddingModel:query', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('嵌入模型新增', @embeddingModelMenuId, '2', '', '', 1, 0, 'F', '0', '0', 'ai:embeddingModel:add', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('嵌入模型修改', @embeddingModelMenuId, '3', '', '', 1, 0, 'F', '0', '0', 'ai:embeddingModel:edit', '', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('嵌入模型删除', @embeddingModelMenuId, '4', '', '', 1, 0, 'F', '0', '0', 'ai:embeddingModel:remove', '', 'admin', sysdate(), '', null, '');
