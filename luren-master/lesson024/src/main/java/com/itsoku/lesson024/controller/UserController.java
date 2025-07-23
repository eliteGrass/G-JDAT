package com.itsoku.lesson024.controller;

import com.itsoku.lesson024.common.BusinessExceptionUtils;
import com.itsoku.lesson024.common.Result;
import com.itsoku.lesson024.common.ResultUtils;
import com.itsoku.lesson024.dto.UserAddRequest;
import com.itsoku.lesson024.dto.UserModifyRequest;
import com.itsoku.lesson024.log.OperLog;
import com.itsoku.lesson024.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    @OperLog(log = "用户管理-新增用户")
    public Result<String> add(@Validated @RequestBody UserAddRequest req) {
        return ResultUtils.success(this.userService.add(req));
    }

    @PostMapping("/delete")
    @OperLog(log = "用户管理-删除用户")
    public Result<Boolean> delete(@RequestParam("userId") String userId) {
        //这里抛个异常，演示错误请求
        throw BusinessExceptionUtils.businessException("无权操作");
    }

}
