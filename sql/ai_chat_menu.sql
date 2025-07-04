-- AI聊天菜单 SQL
-- 插入AI聊天主菜单和子菜单

-- 插入AI聊天主菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('AI聊天', 0, 4, 'aiChat', null, '', 1, 0, 'M', '0', '0', '', 'message', 'admin', sysdate(), '', null, 'AI聊天菜单');

-- 获取刚插入的AI聊天菜单ID
SET @ai_chat_parent_id = LAST_INSERT_ID();

-- 插入AI聊天子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('聊天窗口', @ai_chat_parent_id, 1, 'chat', 'aiChat/chat/index', '', 1, 0, 'C', '0', '0', 'ai:chat:list', 'chat', 'admin', sysdate(), '', null, 'AI聊天窗口菜单');

-- 获取聊天窗口菜单ID
SET @chat_menu_id = LAST_INSERT_ID();

-- 插入AI聊天功能权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('发送消息', @chat_menu_id, 1, '', '', '', 1, 0, 'F', '0', '0', 'ai:chat:send', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('查询会话', @chat_menu_id, 2, '', '', '', 1, 0, 'F', '0', '0', 'ai:chat:query', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('新增会话', @chat_menu_id, 3, '', '', '', 1, 0, 'F', '0', '0', 'ai:chat:add', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('编辑会话', @chat_menu_id, 4, '', '', '', 1, 0, 'F', '0', '0', 'ai:chat:edit', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('删除会话', @chat_menu_id, 5, '', '', '', 1, 0, 'F', '0', '0', 'ai:chat:remove', '#', 'admin', sysdate(), '', null, '');
