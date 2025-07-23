package com.itsoku.lesson009.controller;

import com.itsoku.lesson009.comm.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人 <br/>
 * <b>time</b>：2024/4/2 15:12 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@RequestMapping("/threadPoolManager")
public class ThreadPoolManagerController {
    /**
     * 获取所有的线程池信息
     *
     * @return
     */
    @GetMapping("/threadPoolInfoList")
    public Result<List<ThreadPoolInfo>> threadPoolInfoList() {
        return ResultUtils.ok(ThreadPoolManager.threadPoolInfoList());
    }

    /**
     * 线程池扩缩容
     *
     * @return
     */
    @PostMapping("/threadPoolChange")
    public Result<Boolean> threadPoolChange(@RequestBody ThreadPoolChange threadPoolChange) {
        ThreadPoolManager.changeThreadPool(threadPoolChange);
        return ResultUtils.ok(true);
    }
}
