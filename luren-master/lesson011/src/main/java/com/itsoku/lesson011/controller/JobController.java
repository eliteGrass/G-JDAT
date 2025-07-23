package com.itsoku.lesson011.controller;

import com.itsoku.lesson011.comm.Result;
import com.itsoku.lesson011.comm.ResultUtils;
import com.itsoku.lesson011.dto.Job;
import com.itsoku.lesson011.dto.JobCreateRequest;
import com.itsoku.lesson011.dto.JobUpdateRequest;
import com.itsoku.lesson011.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/3 0:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class JobController {
    @Autowired
    private JobService jobService;

    /**
     * 创建job
     *
     * @param request
     * @return 返回job的id
     */
    @PostMapping("/jobCreate")
    public Result<String> jobCreate(@RequestBody JobCreateRequest request) {
        return ResultUtils.ok(this.jobService.jobCreate(request));
    }

    /**
     * 更新job
     *
     * @param request
     */
    @PostMapping("/jobUpdate")
    public Result<Boolean> jobUpdate(@RequestBody JobUpdateRequest request) {
        return ResultUtils.ok(this.jobService.jobUpdate(request));
    }

    /**
     * 删除job
     *
     * @param id
     */
    @PostMapping("/jobDelete")
    public Result<Boolean> jobDelete(@RequestParam("id") String id) {
        return ResultUtils.ok(this.jobService.jobDelete(id));
    }

    /**
     * 启用job
     *
     * @param id
     */
    @PostMapping("/jobStart")
    public Result<Boolean> jobStart(@RequestParam("id") String id) {
        return ResultUtils.ok(this.jobService.jobStart(id));
    }

    /**
     * 停止job
     *
     * @param id
     */
    @PostMapping("/jobStop")
    public Result<Boolean> jobStop(@RequestParam("id") String id) {
        return ResultUtils.ok(this.jobService.jobStop(id));
    }

    /**
     * 所有的job
     *
     * @return
     */
    @GetMapping
    public Result<List<Job>> jobList() {
        return ResultUtils.ok(this.jobService.jobList());
    }

}
