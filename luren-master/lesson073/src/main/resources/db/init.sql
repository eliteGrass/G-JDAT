-- 商品表
drop table if exists t_goods_073;
create table if not exists t_goods_073
(
    id   varchar(32) primary key comment '商品id',
    goods_name varchar(256) not null comment '商品名称',
    stock      int          not null comment '库存'
) comment = '商品表';

insert into t_goods_073 values ('1', '高并发实战案例100讲', 20);
insert into t_goods_073 values ('2', 'java教程', 20);
insert into t_goods_073 values ('3', 'spring教程', 20);

drop table if exists t_order_073;
create table if not exists t_order_073
(
    id       varchar(32) primary key comment '订单id',
    goods_id varchar(32) comment '商品id',
    num      int not null comment '商品数量'
) comment = '订单表';