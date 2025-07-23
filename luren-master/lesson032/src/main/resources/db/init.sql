-- 创建订单表
drop table if exists t_order_lesson032;
create table if not exists t_order_lesson032
(
    id    varchar(32)    not null primary key comment '订单id',
    goods varchar(100)   not null comment '商品',
    price decimal(12, 2) not null comment '订单金额'
) comment '订单表';

-- 创建本地消息表
drop table if exists t_msg_lesson032;
create table if not exists t_msg_lesson032
(
    id               varchar(32) not null primary key comment '消息id',
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
