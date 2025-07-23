package com.itsoku.lesson050;

import com.itsoku.lesson050.service.ITestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 第50节 SpringBoot多线程事务工具类，太好用了
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 11:58 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootTest
@Slf4j
public class Lesson050ApplicationTest {

    @Autowired
    private ITestService userService;

    /**
     * 单线程插入
     */
    @Test
    public void singleThreadInsert() {
        for (int i = 0; i < 5; i++) {
            //清理数据
            this.userService.delete();

            // 向 5 张测试表各插入 10 万条记录
            long startTime = System.currentTimeMillis();
            this.userService.singleThreadInsert();
            System.out.println("耗时(ms)：" + (System.currentTimeMillis() - startTime));

            System.out.println("===============");
            System.out.println();
        }
    }

    /**
     * 多线程事务批量插入
     */
    @Test
    public void moreThreadInsert() {
        for (int i = 0; i < 5; i++) {
            //清理数据
            this.userService.delete();

            // 向 5 张测试表各插入 10 万条记录
            long startTime = System.currentTimeMillis();
            this.userService.moreThreadInsert();
            System.out.println("耗时(ms)：" + (System.currentTimeMillis() - startTime));

            System.out.println("===============");
            System.out.println();
        }
    }

    /**
     * 多线程事务批量插入，模拟失败效果
     */
    @Test
    public void moreThreadInsertFail() {
        //清理数据
        this.userService.delete();

        // 向 5 张测试表各插入 10 万条记录，最后一个表的插入会报异常，导致 5 张表数据都会被回滚
        long startTime = System.currentTimeMillis();
        this.userService.moreThreadInsertFail();
        System.out.println("耗时(ms)：" + (System.currentTimeMillis() - startTime));
    }
}
