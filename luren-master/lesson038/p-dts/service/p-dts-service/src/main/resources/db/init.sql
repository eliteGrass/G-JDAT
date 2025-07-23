-- tcc分布式事务记录表
drop table if exists t_tcc_record;
create table if not exists t_tcc_record
(
    id                       varchar(32)   not null primary key comment '订单id',
    bus_type                 varchar(100)  not null comment '业务类型',
    bus_id                   varchar(100)  not null comment '业务id',
    classname                varchar(500)  not null comment '事务发起者',
    status                   smallint      not null default 0 comment '状态，0：待处理，1：处理成功，2：处理失败',
    service_application_name varchar(1000) not null comment '事务发起者服务名称，也可能是服务地址',
    beanname                 varchar(500)  not null comment '事务发起者bean名称',
    request_data             longtext      not null comment '请求参数json格式',
    next_dispose_time        bigint        not null default 0 comment '下次处理时间',
    max_failure              int           not null default 50 comment '最大允许失败次数',
    failure                  int           not null default 0 comment '当前已失败次数',
    addtime                  bigint        not null default 0 comment '创建时间',
    uptime                   bigint        not null default 0 comment '最后更新时间',
    version                  bigint        not null default 0 comment '版本号，每次更新+1',
    unique key uk_1 (bus_type, bus_id)
) comment 'tcc分布式事务记录表';

-- tcc分布式事务->分支日志记录表
drop table if exists t_tcc_branch_log;
create table if not exists t_tcc_branch_log
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
) comment 'tcc分布式事务->分支日志记录表';

-- tcc分布式事务->补偿日志表
drop table if exists t_tcc_dispose_log;
create table if not exists t_tcc_dispose_log
(
    id            varchar(32) not null primary key comment '主键',
    tcc_record_id varchar(32) not null comment '事务记录id发，来源于t_tcc_record表的id',
    msg           longtext comment '执行结果',
    starttime     bigint      not null default 0 comment '开始时间（时间戳）',
    endtime       bigint      not null default 0 comment '结束时间（时间戳）',
    addtime       bigint      not null default 0 comment '创建时间（时间戳）',
    key idx_tcc_record_id (tcc_record_id)
) comment 'tcc分布式事务->补偿日志表';