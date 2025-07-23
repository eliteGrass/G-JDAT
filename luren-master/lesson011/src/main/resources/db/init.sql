-- 创建job表
create table if not exists t_job
(
    id          varchar(50) primary key comment 'id，主键',
    name        varchar(100) not null comment 'job名称，可以定义一个有意义的名称',
    cron        varchar(50)  not null comment 'job的执行周期，cron表达式',
    bean_name   varchar(100) not null comment 'job需要执行那个bean，对应spring中bean的名称',
    bean_method varchar(100) not null comment 'job执行的bean的方法',
    status      smallint     not null default 0 comment 'job的状态,0：停止，1：执行中'
);

-- 为了方便测试，清理数据
delete from t_job;
-- 创建2个测试用的job，job1每1秒执行1次，job2每2秒执行一次
insert ignore into t_job values ('1', '第1个测试job', '* * * * * *', 'job1', 'execute', 1);
insert ignore into t_job values ('2', '第2个测试job', '*/2 * * * * *', 'job2', 'execute', 1);