package com.ms.dts.service.tcc.job;

import com.ms.dts.service.tcc.bus.ITccBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/17 16:07 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
public class TccJob {
    @Autowired
    private ITccBus tccBus;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void recover() {
        tccBus.disposeNeedRecover();
    }
}
