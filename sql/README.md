# 数据库脚本

此目录用于存放数据库相关的SQL脚本。

## 文件说明

- `hrone_base.sql` - 数据库结构（表结构、索引等）
- `hrone_base_data.sql` - 初始数据（字典数据、菜单数据等）

## 使用说明

在第3阶段学习时，我们会创建这些SQL脚本。

到时候执行以下命令导入数据库：

```sql
-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS hrone_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 使用数据库
USE hrone_db;

-- 3. 导入表结构
SOURCE hrone_base.sql;

-- 4. 导入初始数据
SOURCE hrone_base_data.sql;
```

或使用工具（Navicat、DBeaver等）导入。

