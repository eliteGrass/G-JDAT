package com.itsoku;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人 <br/>
 *
 * <b>description</b>： 简单的任务批处理<br>
 * <b>time</b>：2024/3/27 11:35 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SimpleBatchTask {

    public static void main(String[] args) {
        batchTaskTest();
    }

    /**
     * 使用线程池批量发送短信，发送完毕后，方法继续向下走
     */
    public static void batchTaskTest() {
        long startTime = System.currentTimeMillis();
        //待发送的短信列表
        List<String> taskList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            taskList.add("短信-" + i);
        }

        //使用线程池批量处理任务
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //创建CountDownLatch,构造器参数为任务数量
        CountDownLatch countDownLatch = new CountDownLatch(taskList.size());
        for (String task : taskList) {
            executorService.execute(() -> {
                try {
                    //交个线程池处理任务
                    disposeTask(task);
                } finally {
                    //处理完成后调用 countDownLatch.countDown()
                    countDownLatch.countDown();
                }
            });
        }
        try {
            //阻塞当前线程池
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("任务处理完毕,耗时(ms):" + (System.currentTimeMillis() - startTime));
        executorService.shutdown();
    }

    public static void disposeTask(String task) {
        System.out.println(String.format("【%s】发送成功", task));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
