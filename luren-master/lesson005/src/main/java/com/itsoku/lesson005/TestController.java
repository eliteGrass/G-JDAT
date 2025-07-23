package com.itsoku.lesson005;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 使用 Semaphore 实现限流功能
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/30 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TestController {

    /**
     * Juc中的Semaphore可以实现限流功能，可以将 Semaphore 想象成停车场入口的大爷，
     * 大爷手里面拥有一定数量的停车卡（也可以说是令牌），卡的数量是多少呢？就是Semaphore构造方法中指定的，如下就是50个卡，
     * 车主想进去停车，先要从大爷手中拿到一张卡，出来的时候，需要还给大爷，如果拿不到卡，就不能进去停车。
     * <p>
     * semaphore 内部提供了获取令牌，和还令牌的一些方法
     */
    private Semaphore semaphore = new Semaphore(50);

    /**
     * 来个案例，下面是一个下单的方法，这个方法最多只允许 50 个并发，若超过50个并发，则进来的请求，最多等待1秒，如果无法获取到令牌，则快速返回失败，请重试
     *
     * @return
     */
    @GetMapping("/placeOrder")
    public String placeOrder() throws InterruptedException {

        /**
         * semaphore 在上面定义的，里面有50个令牌，也就是同时可以支持50个并发请求
         * 下面的代码，尝试最多等待1秒去获取令牌，获取成功，则进入下单逻辑，获取失败，则返回系统繁忙，请稍后重试
         */
        boolean flag = this.semaphore.tryAcquire(1, 1L, TimeUnit.SECONDS);
        // 获取到令牌，则进入下单逻辑
        if (flag) {
            try {
                //这里休眠2秒，模拟下单的操作
                TimeUnit.SECONDS.sleep(2);
                return "下单成功";
            } finally {
                //这里一定不要漏掉了，令牌用完了，要还回去
                this.semaphore.release();
            }
        } else {
            return "系统繁忙，请稍后重试";
        }
    }

}
