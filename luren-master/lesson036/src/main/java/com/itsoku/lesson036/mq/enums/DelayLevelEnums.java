package com.itsoku.lesson036.mq.enums;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum DelayLevelEnums {
    SECOND_1(TimeUnit.SECONDS.toMillis(1), "1s"),
    SECOND_2(TimeUnit.SECONDS.toMillis(2), "2s"),
    SECOND_3(TimeUnit.SECONDS.toMillis(3), "3s"),
    SECOND_5(TimeUnit.SECONDS.toMillis(5), "5s"),
    SECOND_10(TimeUnit.SECONDS.toMillis(10), "10s"),
    SECOND_20(TimeUnit.SECONDS.toMillis(20), "20s"),
    SECOND_30(TimeUnit.SECONDS.toMillis(30), "30s"),
    MINUTE_1(TimeUnit.MINUTES.toMillis(1), "1m"),
    MINUTE_2(TimeUnit.MINUTES.toMillis(2), "2m"),
    MINUTE_3(TimeUnit.MINUTES.toMillis(3), "3m"),
    MINUTE_5(TimeUnit.MINUTES.toMillis(5), "5m"),
    MINUTE_10(TimeUnit.MINUTES.toMillis(10), "10m"),
    HOUR_1(TimeUnit.HOURS.toMillis(1), "1h"),
    HOUR_2(TimeUnit.HOURS.toMillis(2), "2h"),
    DAY_1(TimeUnit.DAYS.toMillis(1), "1d");

    private final long delayTimeInMills;
    private final String desc;

    DelayLevelEnums(long delayTimeInMills, String desc) {
        this.delayTimeInMills = delayTimeInMills;
        this.desc = desc;
    }

    public long getDelayTimeInMills() {
        return delayTimeInMills;
    }

    public String getDesc() {
        return desc;
    }
}
