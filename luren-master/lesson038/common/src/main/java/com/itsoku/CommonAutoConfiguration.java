package com.itsoku;

import com.itsoku.config.MyBatisPlusConfiguration;
import com.itsoku.web.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/16 22:19 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
@AutoConfigureBefore({TransactionAutoConfiguration.class})
@Import({MyBatisPlusConfiguration.class, GlobalExceptionHandler.class})
@EnableTransactionManagement(proxyTargetClass = true, order = Ordered.LOWEST_PRECEDENCE - 10)
public class CommonAutoConfiguration {
    public CommonAutoConfiguration() {
        System.out.println("哈哈哈");
    }
}
