package com.itsoku.lesson043.idempotent;

import com.itsoku.lesson043.idempotent.mapper.IdempotentCallMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
@MapperScan(basePackageClasses = IdempotentCallMapper.class)
public class IdempotentConfiguration {
}
