-- 创建订单表
drop table if exists t_order_lesson034;
create table if not exists t_order_lesson034
(
    id    varchar(32)    not null primary key comment '订单id',
    goods varchar(100)   not null comment '商品',
    price decimal(12, 2) comment '订单金额'
) comment '订单表';

-- 创建本地消息表
drop table if exists t_msg_lesson034;
create table if not exists t_msg_lesson034
(
    id               varchar(32) not null primary key comment '消息id',
    exchange         varchar(100) comment '交换机',
    routing_key      varchar(100) comment '路由key',
    body_json        text        not null comment '消息体,json格式',
    status           smallint    not null default 0 comment '消息状态，0：待投递到mq，1：投递成功，2：投递失败',
    expect_send_time datetime    not null comment '消息期望投递时间，大于当前时间，则为延迟消息，否则会立即投递',
    actual_send_time datetime comment '消息实际投递时间',
    create_time      datetime comment '创建时间',
    fail_msg         text comment 'status=2 时，记录消息投递失败的原因',
    fail_count       int         not null default 0 comment '已投递失败次数',
    send_retry       smallint    not null default 1 comment '投递MQ失败了，是否还需要重试？1：是，0：否',
    next_retry_time  datetime comment '投递失败后，下次重试时间',
    update_time      datetime comment '最近更新时间',
    key idx_status (status)
) comment '本地消息表';


-- 创建消息和消费者关联表
drop table if exists t_msg_consume_lesson034;
create table if not exists t_msg_consume_lesson034
(
    id              varchar(32)  not null primary key comment '消息id',
    producer        varchar(100) not null comment '生产者名称',
    producer_bus_id varchar(100) not null comment '生产者这边消息的唯一标识',
    consumer_class_name        varchar(300) not null comment '消费者完整类名',
    queue_name      varchar(100) not null comment '队列名称',
    body_json       text         not null comment '消息体,json格式',
    status          smallint     not null default 0 comment '消息状态，0：待消费，1：消费成功，2：消费失败',
    create_time     datetime comment '创建时间',
    fail_msg        text comment 'status=2 时，记录消息消费失败的原因',
    fail_count      int          not null default 0 comment '已投递失败次数',
    consume_retry   smallint     not null default 1 comment '消费失败后，是否还需要重试？1：是，0：否',
    next_retry_time datetime comment '投递失败后，下次重试时间',
    update_time     datetime comment '最近更新时间',
    key idx_status (status),
    unique uq_msg (producer, producer_bus_id, consumer_class_name)
) comment '消息和消费者关联表';

drop table if exists t_msg_consume_log_lesson034;
create table if not exists t_msg_consume_log_lesson034
(
    id              varchar(32)  not null primary key comment '消息id',
    msg_consume_id        varchar(32) not null comment '消息和消费者关联记录',
    status          smallint     not null default 0 comment '消费状态，1：消费成功，2：消费失败',
    create_time     datetime comment '创建时间',
    fail_msg        text comment 'status=2 时，记录消息消费失败的原因',
    key idx_msg_consume_id (msg_consume_id)
) comment '消息消费日志';

-- 幂等辅助表
drop table if exists t_idempotent_lesson034;
create table if not exists t_idempotent_lesson034
(
    id             varchar(50) primary key comment 'id，主键',
    idempotent_key varchar(500) not null comment '需要确保幂等的key',
    unique key uq_idempotent_key (idempotent_key)
) comment '幂等辅助表';

-- 顺序消息编号生成器
drop table if exists t_sequential_msg_number_generator_lesson034;
create table if not exists t_sequential_msg_number_generator_lesson034
(
    id        varchar(50) primary key comment 'id，主键',
    group_id  varchar(256) not null comment '组id',
    numbering bigint       not null comment '消息编号',
    version   bigint       not null default 0 comment '版本号，每次更新+1',
    unique key uq_group_id (group_id)
) comment '顺序消息排队表';

-- 顺序消息消费信息表，(group_id、queue_name)中的消息消费到哪里了？
drop table if exists t_sequential_msg_consume_position_lesson034;
create table if not exists t_sequential_msg_consume_position_lesson034
(
    id         varchar(50) primary key comment 'id，主键',
    group_id   varchar(256) not null comment '组id',
    queue_name varchar(100) not null comment '队列名称',
    consume_numbering  bigint   default 0   not null comment '当前消费位置的编号',
    version   bigint       not null default 0 comment '版本号，每次更新+1',
    unique key uq_group_queue (group_id, queue_name)
) comment '顺序消息消费信息表';


-- 顺序消息排队表
drop table if exists t_sequential_msg_queue_lesson034;
create table if not exists t_sequential_msg_queue_lesson034
(
    id          varchar(50) primary key comment 'id，主键',
    group_id    varchar(256) not null comment '组id',
    numbering   bigint       not null comment '消息编号',
    queue_name  varchar(100) not null comment '队列名称',
    msg_json    text         not null comment '消息json格式',
    create_time datetime comment '创建时间',
    unique key uq_group_number_queue (group_id, numbering, queue_name)
) comment '顺序消息排队表';