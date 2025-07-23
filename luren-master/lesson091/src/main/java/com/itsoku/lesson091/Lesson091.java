package com.itsoku.lesson091;

/**
 * 91.idea多线程调试，这个技巧也太棒了吧，你会么？
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/9 22:11 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class Lesson091 {
    public static void main(String[] args) {
        //下面创建3个线程：thread1、thread2、thread3
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread() + " " + i);
            }
        }, "thread1");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread() + " " + i);
            }
        }, "thread2");

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread() + " " + i);
            }
        }, "thread3");

        thread1.start();
        thread2.start();
        thread3.start();
    }

}
