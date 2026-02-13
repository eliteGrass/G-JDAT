-- 创建数据库
CREATE DATABASE IF NOT EXISTS mybatis_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mybatis_demo;

-- 创建用户表
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `age` INT(3) DEFAULT NULL COMMENT '年龄',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入测试数据
INSERT INTO `user` (`username`, `password`, `email`, `phone`, `age`) VALUES
('zhangsan', '123456', 'zhangsan@example.com', '13800138000', 25),
('lisi', '123456', 'lisi@example.com', '13800138001', 28),
('wangwu', '123456', 'wangwu@example.com', '13800138002', 30);
