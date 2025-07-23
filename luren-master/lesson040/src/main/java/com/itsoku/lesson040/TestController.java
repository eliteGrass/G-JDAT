package com.itsoku.lesson040;

import com.itsoku.lesson040.lock.DistributeLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/23 23:09 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@Slf4j
public class TestController {
    @Autowired
    private DistributeLock distributeLock;

    @GetMapping("/tryLockRun")
    public String tryLockRun() {
        long startTime = System.currentTimeMillis();
        log.info("tryLockRun start");
        // 获取锁的过程不会阻塞，成功获取锁后将执行业务，业务执行完成后自动释放锁，并返回true；若无法获取锁，则会立即返回false
        boolean lockResult = this.distributeLock.tryLockRun("lock1", () -> {
            log.info("获取锁成功，执行业务");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        log.info("耗时：{},{}", (System.currentTimeMillis() - startTime), lockResult ? "获取锁成功" : "获取锁失败");
        return lockResult ? "获取锁成功" : "获取锁失败";
    }

    @GetMapping("/tryLockRunWaitTime")
    public String tryLockRunWaitTime() {
        long startTime = System.currentTimeMillis();
        log.info("tryLockRunWaitTime start");

        //这个尝试获取锁，最多等待1秒，成功获取锁后将执行业务，业务执行完成后自动释放锁，并返回true；若1秒无法获取锁，则返回false
        boolean lockResult = this.distributeLock.tryLockRun("lock1", () -> {
            log.info("获取锁成功，执行业务");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 1, TimeUnit.SECONDS);

        log.info("耗时：{},{}", (System.currentTimeMillis() - startTime), lockResult ? "获取锁成功" : "获取锁失败");
        return lockResult ? "获取锁成功" : "获取锁失败";
    }

    @GetMapping("/lockRun")
    public String lockRun() {
        long startTime = System.currentTimeMillis();
        log.info("lockRun start");
        //获取锁的过程会阻塞，直到成功获取锁，成功获取锁后将执行业务，业务执行完成后自动释放锁
        this.distributeLock.lockRun("lock1", () -> {
            log.info("获取锁成功，执行业务");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("获取锁成功,耗时：{}", (System.currentTimeMillis() - startTime));
        return "获取锁成功";
    }
}
