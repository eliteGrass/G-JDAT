package com.ms.dts.business.service;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <b>description</b>： p-dts-business-service自动装配<br>
 * <b>time</b>：2019-1-10 14:46 <br>
 * <b>author</b>： ready likun_557@163.com
 */
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = TccBusinessProperties.PREFIX, name = "enabled", matchIfMissing = true)
@ComponentScan(basePackageClasses = {TccBusinessAutoConfiguration.class})
@EnableConfigurationProperties(TccBusinessProperties.class)
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@MapperScan(basePackageClasses = TccBusinessAutoConfiguration.class, annotationClass = Mapper.class)
public class TccBusinessAutoConfiguration {

    private TccBusinessProperties dtsBusinessProperties;

    public TccBusinessAutoConfiguration(TccBusinessProperties dtsBusinessProperties) {
        this.dtsBusinessProperties = dtsBusinessProperties;
    }

}
