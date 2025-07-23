drop table if exists t_article_lesson089;
create table if not exists t_article_lesson089(
    id bigint primary key,
    user_id bigint not null comment '作者id',
    title varchar(256) not null comment '文章标题'
) comment = '博客表';

insert into t_article_lesson089 values (1,1,'Java 高并发');
insert into t_article_lesson089 values (2,1,'Java 微服务');
insert into t_article_lesson089 values (3,2,'Java 性能调优');
insert into t_article_lesson089 values (4,2,'Java 从入门到精通');
insert into t_article_lesson089 values (5,3,'Java Web开发');

