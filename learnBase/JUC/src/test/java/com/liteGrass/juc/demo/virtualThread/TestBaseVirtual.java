package com.liteGrass.juc.demo.virtualThread;


import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @Description 虚拟线程测试
 * @Author liteGrass
 * @Date 2024/11/28 16:06
 */
public class TestBaseVirtual {

    /**
     * Java中虚拟线程的简单使用的几种方式
     */
    @Test
    public void testMethod1() {
        Thread virtualThread = Thread.ofVirtual().unstarted(() -> {
            System.out.println(StrUtil.format("虚拟线程({})进行调用", Thread.currentThread().getName()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        virtualThread.start();
        System.out.println("平台线程");
    }


    @Test
    public void testMethod2() {
        Thread.startVirtualThread(() -> {
            System.out.println(StrUtil.format("虚拟线程({})进行调用", Thread.currentThread().getName()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("平台线程");
    }

    @Test
    public void testMethod3() {
        ThreadFactory factory = Thread.ofVirtual().factory();
        Thread thread = factory.newThread(() -> {
            System.out.println(StrUtil.format("虚拟线程({})进行调用", Thread.currentThread().getName()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        System.out.println("平台线程");
    }

    @Test
    public void testMethod4() {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        executorService.execute(() -> {
            System.out.println(StrUtil.format("虚拟线程({})进行调用", Thread.currentThread().getName()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("平台线程");
    }

}
