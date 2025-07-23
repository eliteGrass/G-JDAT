package com.itsoku.lesson085;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/3 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class Lesson085Test {
    @Test
    public void test1() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //使用线程池异步执行step1
        Future<String> step1 = executorService.submit(() -> {
            sleep(500);
            return "获取商品基本信息";
        });
        //使用线程池异步执行step2
        Future<String> step2 = executorService.submit(() -> {
            //这里需要等到step1执行完毕
            step1.get();
            sleep(500);
            return "获取商品折扣信息";
        });
        //使用线程池异步执行step3
        Future<String> step3 = executorService.submit(() -> {
            sleep(500);
            return "获取商品描述信息";
        });

        //这里需要等到3个步骤都执行完成，这里可以不用写step1.get()，因为step2依赖于step1
        step2.get();
        step3.get();
        System.out.println("耗时(ms)：" + (System.currentTimeMillis() - startTime));
    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //使用线程池异步执行step1
        CompletableFuture<String> step1 = CompletableFuture.supplyAsync(() -> {
            sleep(500);
            return "获取商品基本信息";
        }, executorService);

        //使用线程池异步执行step2
        CompletableFuture<String> step2 = step1.thenApplyAsync((goodsInfo) -> {
            sleep(500);
            return "获取商品折扣信息";
        }, executorService);

        //使用线程池异步执行step3
        CompletableFuture<String> step3 = CompletableFuture.supplyAsync(() -> {
            sleep(500);
            return "获取商品描述信息";
        }, executorService);

        //这里需要等到3个步骤都执行完成，这里可以不用写step1.get()，因为step2依赖于step1
        CompletableFuture.allOf(step2, step3).get();

        System.out.println("耗时(ms)：" + (System.currentTimeMillis() - startTime));
    }

    private static void sleep(long timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
