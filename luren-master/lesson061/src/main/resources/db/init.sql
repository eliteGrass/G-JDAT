CREATE TABLE if not exists `t_id_generator_lesson061`
(
    `code`      varchar(128) not null primary key COMMENT '业务编码（唯一）',
    `max_id` bigint       not null default 1 comment '当前最大id'
) COMMENT ='id生成器';

