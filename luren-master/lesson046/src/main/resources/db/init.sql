-- 创建用户表
drop table if exists t_user_lesson046;
create table t_user_lesson046
(
    id        bigint not null auto_increment primary key,
    user_name varchar(100),
    age       int
);

insert into t_user_lesson046 (user_name, age)
values ('路人1', 10),
       ('路人2', 11),
       ('路人3', 20),
       ('路人4', 21),
       ('路人5', 22),
       ('路人6', 23),
       ('路人7', 30),
       ('路人8', 31),
       ('路人9', 32);

-- 创建账户表
drop table if exists t_account_lesson046;
create table if not exists t_account_lesson046
(
    id      varchar(32)    not null primary key comment '用户id',
    name    varchar(50)    not null comment '用户名',
    balance decimal(12, 2) not null comment '账户余额',
    version bigint         not null default 0 comment '版本号，默认为0，每次更新+1'
) comment '账户表';

insert ignore into t_account_lesson046 value ('1', '路人1', '1000.00', 0);