package com.itsoku.lesson019.controller;

import com.itsoku.lesson019.common.BusinessExceptionUtils;
import com.itsoku.lesson019.common.Result;
import com.itsoku.lesson019.common.ResultUtils;
import com.itsoku.lesson019.log.NoLog;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/14 14:20 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class UserController {

    @GetMapping("/userAdd")
    public Result<Boolean> userAdd(@RequestParam("userName") String userName,
                                   @RequestParam("age") int age,
                                   @NoLog @RequestParam("password") String password) {
        return ResultUtils.success(true);
    }

    @GetMapping("/login")
    public Result<Void> login(@RequestParam("name") String name) {
        if (!"路人".equals(name)) {
            throw BusinessExceptionUtils.businessException("用户名无效");
        }
        return ResultUtils.success();
    }

    @GetMapping("/getUserPassword")
    @NoLog
    public Result<String> getUserPassword(@RequestParam("userId") String userId, HttpServletRequest request) {
        return ResultUtils.success("123456");
    }
}
