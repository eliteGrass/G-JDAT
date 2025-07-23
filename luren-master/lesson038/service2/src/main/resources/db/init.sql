-- tcc分布式事务->业务库->分支日志记录表
drop table if exists t_tcc_bus_branch_log;
create table if not exists t_tcc_bus_branch_log
(
    id                 varchar(32)  not null primary key comment '主键',
    tcc_record_id      varchar(32)  not null comment '事务记录id发，来源于t_tcc_record表的id',
    classname          varchar(500) not null comment '分支完整类名',
    method             varchar(100) not null comment '方法，0:try1，1:confirm，2:cancel',
    msg                varchar(200) comment '分支执行结果',
    status             smallint     not null default 0 comment '状态，0：待处理，1：处理成功，2：处理失败',
    context            longtext     not null comment '上下文信息,json格式',
    addtime            bigint       not null default 0 comment '创建时间',
    uptime             bigint       not null default 0 comment '最后更新时间',
    version            bigint       not null default 0 comment '版本号，每次更新+1',
    unique key uk_1 (tcc_record_id, classname, method),
    key idx_tcc_record_id (tcc_record_id)
) comment 'tcc分布式事务->业务库->分支日志记录表';


-- 创建账户表
drop table if exists t_account_lesson038;
create table if not exists t_account_lesson038
(
    id      varchar(32)    not null primary key comment '用户id',
    name    varchar(50)    not null comment '用户名',
    balance decimal(12, 2) not null comment '账户余额',
    frozen  decimal(12, 2) not null comment '冻结金额'
) comment '账户表';

insert ignore into t_account_lesson038 value ('2','B','0.00','0.00');