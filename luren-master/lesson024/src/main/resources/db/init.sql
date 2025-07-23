-- 创建账户表
create table if not exists t_user_lesson024
(
    id   varchar(50) primary key comment '用户id',
    name varchar(50) not null comment '用户名',
    age  smallint    not null comment '年龄'
) comment '用户表';

-- 操作日志表
create table if not exists t_oper_log_lesson024
(
    id              varchar(50) primary key comment 'id，主键',
    log             varchar(500) not null comment '操作日志',
    status          smallint     not null default 1 comment '状态，0：异常，1：正常',
    param_json      text comment '请求参数json',
    result_json     text comment '响应结果json',
    error_msg       text comment '错误信息(status=0时，记录错误信息)',
    cost_time       long comment '耗时（毫秒）',
    oper_ip         varchar(50) comment '操作ip地址',
    oper_ip_address varchar(50) comment '操作ip地址归属地',
    oper_user_name  varchar(50) comment '操作人用户名',
    oper_time       datetime comment '操作时间'
) comment '操作日志表';