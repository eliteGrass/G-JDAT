package com.itsoku.lesson013.controller;

import com.itsoku.lesson013.common.BusinessException;
import com.itsoku.lesson013.dto.UserRegisterRequest;
import com.itsoku.lesson013.common.BusinessExceptionUtils;
import com.itsoku.lesson013.common.Result;
import com.itsoku.lesson013.common.ResultUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TestController {

    @GetMapping("/hello")
    public Result<String> hello() {
        return ResultUtils.success("欢迎大家学习《高并发 & 微服务 & 性能调优实战案例 100 讲》");
    }

    /**
     * 登录校验，用户名为路人的时候，登录成功，否则提示用户名错误
     *
     * @param name
     * @return
     */
    @GetMapping("/login")
    public Result<String> login(String name) {
        if (!"路人".equals(name)) {
            throw BusinessExceptionUtils.businessException("1001", "用户名错误");
        } else {
            return ResultUtils.success("登录成功");
        }
    }

    /**
     * 下面是一个注册接口，注册需要用户名和密码，这两个参数不能为空，这里我们使用springboot自带的校验功能
     *
     * @param req
     * @return
     */
    @PostMapping("/userRegister")
    public Result<Void> userRegister(@Validated @RequestBody UserRegisterRequest req) {
        return ResultUtils.success();
    }

}
