-- 创建账户资金表
drop table if exists t_account_funds_lesson048;
CREATE TABLE `t_account_funds_lesson048`
(
    `account_id`  bigint         NOT NULL COMMENT '账户id',
    `balance`     decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '余额',
    `frozen`      decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
    `version`     bigint                  DEFAULT '0' COMMENT '版本号，默认为0，每次+1',
    `create_time` datetime(6)             DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(6)             DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`account_id`)
) COMMENT ='账户资金表';

insert ignore into t_account_funds_lesson048 value (1, '100.00', '0.00', 0, now(), null);

-- 创建账户资金流水表
drop table if exists t_account_funds_data_lesson048;
CREATE TABLE `t_account_funds_data_lesson048`
(
    `id`                               bigint         NOT NULL auto_increment COMMENT '主键',
    `account_id`                       bigint         NOT NULL COMMENT '账号id,t_account_funds.account_id',
    `price`                            decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '交易金额',
    `income`                           tinyint        NOT NULL DEFAULT '0' COMMENT '进出标志位，0：账户总金额余额不变，1：账户总金额增加，-1：账户总余额减少 （总金额 = account_funds.balance+ account_funds.frozen）',
    `bus_type`                         smallint       NOT NULL COMMENT '业务关联的业务类型',
    `bus_id`                           bigint         NOT NULL COMMENT '交易关联的业务id',
    `before_account_funds_snapshot_id` bigint         NOT NULL COMMENT '本交易前资金快照id，快照是指交易时将（account_funds）当时的记录备份一份',
    `after_account_funds_snapshot_id`  bigint         NOT NULL COMMENT '本交易后资金快照id，快照是指交易后将（account_funds）当时的记录备份一份',
    `create_time`                      datetime(6)             DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) COMMENT ='账户资金流水表';

-- 创建账户资金快照表
drop table if exists t_account_funds_snapshot_lesson048;
CREATE TABLE `t_account_funds_snapshot_lesson048`
(
    `id`          bigint         NOT NULL auto_increment COMMENT '主键',
    `account_id`  bigint         NOT NULL COMMENT '账号id,t_account_funds.account_id',
    `balance`     decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '余额',
    `frozen`      decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
    `create_time` datetime(6)             DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) COMMENT ='账户资金快照表';


-- 创建提现记录表
drop table if exists t_cash_out_lesson048;
create table if not exists t_cash_out_lesson048
(
    `id`        bigint         NOT NULL auto_increment COMMENT '提现记录id',
    account_id  varchar(32)    not null comment '账号id',
    price       decimal(12, 2) not null comment '提现金额',
    status      smallint       not null comment '状态，0：处理中，1：提现成功，2：提现失败',
    fail_msg    varchar(1000) comment '提现失败原因',
    create_time datetime       not null comment '创建时间',
    update_time datetime comment '最后更新时间',
    PRIMARY KEY (`id`)
) comment '提现记录表';
