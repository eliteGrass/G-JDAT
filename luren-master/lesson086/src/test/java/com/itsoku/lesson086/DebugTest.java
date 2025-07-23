package com.itsoku.lesson086;

import org.junit.jupiter.api.Test;

/**
 * 86.idea中的必备debug技巧，高手必备
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/7 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class DebugTest {

    /**
     * 1、条件断点
     */
    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }

    /**
     * 2、单次断点
     */
    @Test
    public void test2() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }

    /**
     * 3、异常断点
     */
    @Test
    public void test3() {
        Integer age = null;
        String s = age.toString();
    }

    /**
     * 4、强制返回
     */
    @Test
    public void test4() {
        m1();
        System.out.println("over");
    }

    public void m1() {
        System.out.println("a");
        System.out.println("b");
        //假如后面是一些其他的操作，比如数据操作，我们不希望执行，就可以使用debug中的强制返回，此方法中，断点后面的代码就不会执行
        System.out.println("c");
    }

    /**
     * 抛出异常
     */
    @Test
    public void test5() {
        try {
            System.out.println("a");
            System.out.println("b");
            System.out.println("c");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset Frame
     */
    @Test
    public void test6() {
        m2();
    }

    public void m2() {
        System.out.println("a");
        System.out.println("b");
        System.out.println("c");
    }

}