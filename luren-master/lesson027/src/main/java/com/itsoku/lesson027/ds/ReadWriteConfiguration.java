package com.itsoku.lesson027.ds;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/25 13:53 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Configuration
public class ReadWriteConfiguration {

    /**
     * 定义读写分离用到的AOP
     *
     * @return
     */
    @Bean
    public ReadWriteAspect readWriteAspect() {
        return new ReadWriteAspect();
    }

    /**
     * 定义主库数据源
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * 定义从库数据源
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource salveDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * 这里的@Primary一定不要漏掉，因为当前类中，我们定义了3个数据源，不加这个配置，spring不知道用哪个数据源
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        //路由策略和数据源映射关系，放在一个map中（key：路由策略，value：对应的实际数据源）
        Map<Object, Object> targetDataSourcesMap = new HashMap<>();
        //主库路由配置
        targetDataSourcesMap.put(ReadWriteRoutingStrategy.MASTER, this.masterDataSource());
        //从库路由配置
        targetDataSourcesMap.put(ReadWriteRoutingStrategy.SLAVE, this.salveDataSource());
        //强制路由主库的配置
        targetDataSourcesMap.put(ReadWriteRoutingStrategy.HIT_MASTER, this.masterDataSource());

        //创建我们自定义的路由数据源
        ReadWriteDataSource readWriteDataSource = new ReadWriteDataSource();
        //设置目标数据源配置
        readWriteDataSource.setTargetDataSources(targetDataSourcesMap);
        //设置兜底的数据源，这里使用主库兜底，也就是说没有路由策略或者根据路由策略找不到对应的数据源时，会使用这个数据源进行兜底
        readWriteDataSource.setDefaultTargetDataSource(this.masterDataSource());

        return readWriteDataSource;
    }
}
