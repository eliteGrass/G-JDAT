-- 这里面写数据库 ddl 脚本的内容（如创建表结构、索引等）
create table if not exists t_user_051
(
    id        bigint  primary key comment '用户id',
    user_name varchar(64) not null comment '用户名'
);