-- 清理重复的AI资源中心菜单
-- 此脚本用于删除重复的菜单项，只保留一个AI资源中心主菜单

-- 1. 查看当前重复的AI资源中心菜单
SELECT menu_id, menu_name, parent_id, path, order_num, create_time 
FROM sys_menu 
WHERE menu_name = 'AI资源中心' 
ORDER BY create_time ASC;

-- 2. 删除重复的AI资源中心菜单及其子菜单
-- 首先删除子菜单（根据parent_id）
DELETE FROM sys_menu 
WHERE parent_id IN (
    SELECT menu_id FROM (
        SELECT menu_id 
        FROM sys_menu 
        WHERE menu_name = 'AI资源中心' 
        AND menu_id NOT IN (
            SELECT MIN(menu_id) 
            FROM sys_menu 
            WHERE menu_name = 'AI资源中心'
        )
    ) AS duplicate_parents
);

-- 然后删除重复的主菜单，只保留最早创建的那个
DELETE FROM sys_menu 
WHERE menu_name = 'AI资源中心' 
AND menu_id NOT IN (
    SELECT * FROM (
        SELECT MIN(menu_id) 
        FROM sys_menu 
        WHERE menu_name = 'AI资源中心'
    ) AS keep_menu
);

-- 3. 验证清理结果
SELECT menu_id, menu_name, parent_id, path, order_num 
FROM sys_menu 
WHERE menu_name = 'AI资源中心' 
   OR parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name = 'AI资源中心')
ORDER BY parent_id, order_num;

COMMIT;
