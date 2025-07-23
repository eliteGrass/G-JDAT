package com.itsoku.lesson090;

import com.itsoku.lesson090.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/13 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootTest
@Slf4j
public class Lesson090ApplicationTest {

    @Autowired
    private UserService userService;

    @Test
    public void m1() {
        this.userService.m1();
    }

    @Test
    public void m3() {
        this.userService.m3();
    }

    @Test
    public void m4() {
        this.userService.m4();
    }

    @Test
    public void m5() {
        this.userService.m5();
    }

    @Test
    public void m6() throws Exception {
        this.userService.m6();
    }

    @Test
    public void m7() throws Exception {
        this.userService.m7();
    }
}
