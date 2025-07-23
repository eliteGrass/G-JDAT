drop table if exists t_user_090;
create table if not exists t_user_090
(
    id   varchar(32) primary key comment 'id',
    name varchar(64) not null comment '用户名'
) comment = '用户表';