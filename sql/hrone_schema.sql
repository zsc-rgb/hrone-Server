-- ====================================
-- HROne 数据库初始化脚本
-- ====================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `hrone_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `hrone_db`;

-- ====================================
-- 用户表（测试用）
-- ====================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ====================================
-- 插入测试数据
-- ====================================
INSERT INTO `sys_user` VALUES 
(1, 'admin', '管理员', 'admin@hrone.com', '15888888888', '0', '', 'admin123', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', NULL, '管理员'),
(2, 'zhangsan', '张三', 'zhangsan@hrone.com', '15666666666', '0', '', '123456', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', NULL, '测试用户'),
(3, 'lisi', '李四', 'lisi@hrone.com', '15777777777', '1', '', '123456', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', NULL, '测试用户'),
(4, 'wangwu', '王五', 'wangwu@hrone.com', '15999999999', '0', '', '123456', '1', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', NULL, '停用用户'),
(5, 'zhaoliu', '赵六', 'zhaoliu@hrone.com', '13888888888', '0', '', '123456', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', NULL, '测试用户');

-- ====================================
-- 说明
-- ====================================
-- 1. 此脚本用于第3阶段学习
-- 2. 创建了测试用的用户表
-- 3. 插入了5条测试数据
-- 4. 可根据需要修改数据库名称和用户信息

select *from sys_user