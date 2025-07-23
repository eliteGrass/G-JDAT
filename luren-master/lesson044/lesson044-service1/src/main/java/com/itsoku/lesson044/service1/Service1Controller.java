package com.itsoku.lesson044.service1;

import com.itsoku.lesson044.common.Result;
import com.itsoku.lesson044.common.ResultUtils;
import com.itsoku.lesson044.common.thread.CommonThreadPoolExecutor;
import com.itsoku.lesson044.common.trace.TraceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 15:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@Slf4j
public class Service1Controller {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Service2FeignClient service2FeignClient;

    private ThreadPoolExecutor threadPoolExecutor = new CommonThreadPoolExecutor(new CommonThreadPoolExecutor.ThreadLocalContext() {
        @Override
        public Object getContext() {
            return TraceUtils.getTraceId();
        }

        @Override
        public void setContext(Object traceId) {
            TraceUtils.setTraceId((String) traceId);
        }
    }, 5, 5, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));

    @GetMapping("/service1/test1")
    public Result<String> test1() {
        /**
         * 下面会验证5种场景，看看日志中的traceId是不是一样的？
         */
        //1、这里输出一条日志，注意看日志中的traceId
        log.info("微服务链路日志追踪");

        //2、使用restTemplate调用service2，注意看service2中输出日志中的traceId
        String callService2ByRestTemplate = this.restTemplate.getForObject("http://localhost:8082/service2/test1?callType=RestTemplate", String.class);
        log.info("callService2ByRestTemplate:{}", callService2ByRestTemplate);

        //3、使用feign客户端调用service2，注意看service2中输出日志中的traceId
        String callService2ByFeign = this.service2FeignClient.test1("OpenFeign");
        log.info("callService2ByFeign:{}", callService2ByFeign);

        //4、使用线程池执行任务，调用的是 execute 方法，注意看日志中的traceId
        this.threadPoolExecutor.execute(() -> {
            log.info("通过线程池 execute 执行任务");
        });

        //5、使用线程池执行任务，调用的是 submit 方法，注意看日志中的traceId
        this.threadPoolExecutor.submit(() -> {
            log.info("通过线程池 submit 执行任务");
        });
        return ResultUtils.error("ok");
    }
}
