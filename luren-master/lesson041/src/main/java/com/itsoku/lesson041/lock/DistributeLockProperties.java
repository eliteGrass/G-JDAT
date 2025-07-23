package com.itsoku.lesson041.lock;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <b>description</b>：分布式锁配置类 <br>
 * <b>time</b>：2024/5/23 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@ConfigurationProperties(prefix = "distribute.lock")
public class DistributeLockProperties {
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
