package com.itsoku.lesson007.controller;

import com.itsoku.lesson007.service.Lesson007Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/31 22:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private Lesson007Service lesson007Service;

    /**
     * 声明式事务，事务范围比较大
     *
     * @throws InterruptedException
     */
    @GetMapping("/bigTransaction")
    public boolean bigTransaction() {
        try {
            this.lesson007Service.bigTransaction();
            return true;
        } catch (Exception e) {
            LOGGER.error("声明式事务 执行异常:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 使用 TransactionTemplate 编程式事务，可以灵活的控制事务的范围
     *
     * @throws InterruptedException
     */
    @GetMapping("/smallTransaction")
    public boolean smallTransaction() {
        try {
            this.lesson007Service.smallTransaction();
            return true;
        } catch (Exception e) {
            LOGGER.error("编程式事务 执行异常:{}", e.getMessage());
            return false;
        }
    }
}
