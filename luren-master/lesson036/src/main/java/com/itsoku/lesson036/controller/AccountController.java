package com.itsoku.lesson036.controller;

import com.itsoku.lesson036.common.Result;
import com.itsoku.lesson036.common.ResultUtils;
import com.itsoku.lesson036.dto.TransferRequest;
import com.itsoku.lesson036.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @RequestMapping("/transfer")
    public Result<Boolean> transfer(@RequestBody TransferRequest request) {
        this.accountService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getTransferPrice());
        return ResultUtils.success(Boolean.TRUE);
    }
}
