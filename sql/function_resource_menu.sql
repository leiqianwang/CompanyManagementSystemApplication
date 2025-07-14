-- Function资源菜单 SQL
-- 插入Function资源菜单

-- 获取AI资源中心的menu_id
SET @ai_parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = 'AI资源中心' AND parent_id = 0 LIMIT 1);

-- 插入Function资源菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Function资源', @ai_parent_id, 3, 'functionResource', 'ai/functionResource/index', '', 1, 0, 'C', '0', '0', 'ai:functionResource:list', 'function', 'admin', sysdate(), '', null, 'Function资源菜单');

-- 获取刚插入的菜单ID
SET @function_menu_id = LAST_INSERT_ID();

-- 插入Function资源子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Function资源查询', @function_menu_id, 1, '', '', '', 1, 0, 'F', '0', '0', 'ai:functionResource:query', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Function资源新增', @function_menu_id, 2, '', '', '', 1, 0, 'F', '0', '0', 'ai:functionResource:add', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Function资源修改', @function_menu_id, 3, '', '', '', 1, 0, 'F', '0', '0', 'ai:functionResource:edit', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Function资源删除', @function_menu_id, 4, '', '', '', 1, 0, 'F', '0', '0', 'ai:functionResource:remove', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Function资源导出', @function_menu_id, 5, '', '', '', 1, 0, 'F', '0', '0', 'ai:functionResource:export', '#', 'admin', sysdate(), '', null, '');
