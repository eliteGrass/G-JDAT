package com.itsoku.lesson061;

import com.itsoku.lesson061.service.IdGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/6 14:45 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootTest
@Slf4j
public class Lesson061ApplicationTest {

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Test
    public void m1() {
        //生成100个订单id
        for (int j = 0; j < 100; j++) {
            System.out.println(this.idGeneratorService.getId("ORDER_ID"));
        }
    }

    @Test
    public void m2() {
        //一次性生成100个订单id
        System.out.println(this.idGeneratorService.getIdList("ORDER_ID", 100));
    }

    @Test
    public void m3() throws InterruptedException {
        // 测试多线程，是否会有重复的id，开启10个线程，跑100任务，每个任务重生成100个id，看是否有重复的数据
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int taskSize = 100;
        CountDownLatch countDownLatch = new CountDownLatch(taskSize);
        List<Long> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < taskSize; i++) {
            executorService.execute(() -> {
                list.addAll(this.idGeneratorService.getIdList("ORDER_ID", 100));
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        System.out.println("去重前：" + list.size());
        System.out.println("去重后：" + list.stream().distinct().collect(Collectors.toList()).size());
    }
}
