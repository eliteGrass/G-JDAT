package com.itsoku.lesson030.controller;

import com.itsoku.lesson030.common.Result;
import com.itsoku.lesson030.common.ResultUtils;
import com.itsoku.lesson030.dto.UserRegisterRequest;
import com.itsoku.lesson030.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 演示：用户注册成功，并发送注册消息
     *
     * @param req
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@Validated @RequestBody UserRegisterRequest req) {
        return ResultUtils.success(this.userService.register(req));
    }


    /**
     * 演示：用户注册的事务中有异常，消息投递会被自动取消
     *
     * @param req
     * @return
     */
    @PostMapping("/registerError")
    public Result<String> registerError(@Validated @RequestBody UserRegisterRequest req) {
        return ResultUtils.success(this.userService.registerError(req));
    }

}
