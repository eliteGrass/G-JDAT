package com.itsoku.lesson073;

import com.itsoku.lesson073.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/13 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootTest
@Slf4j
public class Lesson073ApplicationTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void placeOrder() throws InterruptedException {
        //使用线程池，对商品1，并发发起20个下单请求，每次下单数量是5
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                try {
                    String goodsId = "1";
                    int num = 5;
                    this.goodsService.placeOrder(goodsId, num);
                    log.info("下单成功");
                } catch (Exception e) {
                    log.info("下单失败:{}", e.getMessage());
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    public void placeOrder1() throws InterruptedException {
        //使用线程池，对商品2，并发发起20个下单请求，每次下单数量是5
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                try {
                    String goodsId = "2";
                    int num = 5;
                    this.goodsService.placeOrder1(goodsId, num);
                    log.info("下单成功");
                } catch (Exception e) {
                    log.info("下单失败:{}", e.getMessage());
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    public void placeOrder2() throws InterruptedException {
        //使用线程池，对商品3，并发发起20个下单请求，每次下单数量是5
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                try {
                    String goodsId = "3";
                    int num = 5;
                    this.goodsService.placeOrder2(goodsId, num);
                    log.info("下单成功");
                } catch (Exception e) {
                    log.info("下单失败:{}", e.getMessage());
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}
