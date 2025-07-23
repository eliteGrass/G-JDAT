package com.itsoku.lesson042.service1;

import cn.hutool.json.JSONUtil;
import com.itsoku.lesson042.common.context.SystemContextHolder;
import com.itsoku.lesson042.common.thread.CommonThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
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
            return SystemContextHolder.getSystemContext().getContextMap();
        }

        @Override
        public void setContext(Object context) {
            SystemContextHolder.getSystemContext().getContextMap().putAll((Map<? extends String, ? extends String>) context);
        }
    }, 5, 5, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));

    @GetMapping("/service1/test1")
    public String test1() {
        /**
         * 下面会验证5种场景，看看是否都可以读取到请求头传递过来的上下文
         */
        //1、直接在当前线程中读取上下文
        Map<String, String> contextMap = SystemContextHolder.getSystemContext().getContextMap();
        log.info("contextMap:{}", JSONUtil.toJsonPrettyStr(contextMap));

        //2、使用restTemplate调用service2，注意看serivce2中输出的上下文，看下上下文是否传递过去了
        String callService2ByRestTemplate = this.restTemplate.getForObject("http://localhost:8082/service2/test1?callType=RestTemplate", String.class);
        log.info("callService2ByRestTemplate:{}", callService2ByRestTemplate);

        //3、使用feign客户端调用service2，注意看serivce2中输出的上下文，看下上下文是否传递过去了
        String callService2ByFeign = this.service2FeignClient.test1("OpenFeign");
        log.info("callService2ByFeign:{}", callService2ByFeign);

        //4、使用线程池执行任务，调用的是 execute 方法
        this.threadPoolExecutor.execute(() -> {
            log.info("通过线程池 execute 执行任务,{}", SystemContextHolder.getSystemContext().getContextMap());
        });

        //5、使用线程池执行任务，调用的是 submit 方法
        this.threadPoolExecutor.submit(() -> {
            log.info("通过线程池 submit 执行任务,{}", SystemContextHolder.getSystemContext().getContextMap());
        });
        return "ok";
    }
}
