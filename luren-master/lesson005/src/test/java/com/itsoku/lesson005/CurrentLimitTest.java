package com.itsoku.lesson005;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用 Semaphore 实现限流功能
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/30 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class CurrentLimitTest {
    public static void main(String[] args) throws InterruptedException {
        // 记录成功量、失败量
        AtomicInteger successNum = new AtomicInteger(0);
        AtomicInteger failNum = new AtomicInteger(0);

        //下面模拟200个人同时下单，运行，大家看结果
        RestTemplate restTemplate = new RestTemplate();
        Runnable requestPlaceOrder = () -> {
            String result = restTemplate.getForObject("http://localhost:8080/placeOrder", String.class);
            System.out.println(result);
            if ("下单成功".equals(result)) {
                successNum.incrementAndGet();
            } else {
                failNum.incrementAndGet();
            }
        };

        //模拟100个人同时发送100个请求，待请求结束，看成功量、失败量
        LoadRunnerUtils.LoadRunnerResult loadRunnerResult = LoadRunnerUtils.run(100, 100, requestPlaceOrder);
        System.out.println(loadRunnerResult);

        System.out.println("下单成功数：" + successNum.get());
        System.out.println("下单失败数：" + failNum.get());
    }
}
