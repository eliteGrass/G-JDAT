-- 幂等调用辅助表
drop table if exists t_idempotent_call;
create table t_idempotent_call
(
    id            varchar(50) primary key comment 'id，主键',
    request_id    varchar(128) not null comment '请求id，唯一',
    status        smallint     not null default 0 comment '状态，0：处理中，1：处理成功，-1：处理失败',
    request_json  mediumtext comment '请求参数json格式',
    response_json mediumtext comment '响应数据json格式',
    version       bigint       not null default 0 comment '版本号，用于乐观锁，每次更新+1',
    create_time   datetime comment '创建时间',
    update_time   datetime comment '最后更新时间',
    unique key uq_request_id (request_id)
) comment '幂等调用辅助表';


-- 创建账户表
drop table if exists t_account_lesson043;
create table t_account_lesson043
(
    id      varchar(32)    not null primary key comment '用户id',
    name    varchar(50)    not null comment '用户名',
    balance decimal(12, 2) not null comment '账户余额'
) comment '账户表';

insert ignore into t_account_lesson043 value ('1','路人1','100.00');
insert ignore into t_account_lesson043 value ('2','路人2','0.00');