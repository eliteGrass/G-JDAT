-- 创建用户表
drop table if exists t_user_lesson047;
create table t_user_lesson047
(
    id        bigint not null auto_increment primary key,
    user_name varchar(100) comment '用户名',
    tenant_id       bigint comment '租户id'
);

insert into t_user_lesson047 (user_name, tenant_id)
values ('张学友', 1),
       ('刘德华', 1),
       ('郭富城', 2),
       ('黎明', 2);