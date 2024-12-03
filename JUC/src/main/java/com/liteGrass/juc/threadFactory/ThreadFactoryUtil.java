package com.liteGrass.juc.threadFactory;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 线程封装类
 * @Author liteGrass
 * @Date 2024/11/28 16:47
 */
public class ThreadFactoryUtil {

    // 虚拟线程
    private static ExecutorService virtualThreadTaskExecutor = Executors.newVirtualThreadPerTaskExecutor();


    /**
     * 获取相应的虚拟线程
     * @return
     */
    public static ExecutorService getVirtualThreadTaskExecutor() {
        return virtualThreadTaskExecutor;
    }

}
