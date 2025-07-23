package com.itsoku.lesson043.controller;

import com.itsoku.lesson043.common.Result;
import com.itsoku.lesson043.common.ResultUtils;
import com.itsoku.lesson043.dto.TransferRequest;
import com.itsoku.lesson043.idempotent.IdempotentCallRequest;
import com.itsoku.lesson043.idempotent.IdempotentCallResponse;
import com.itsoku.lesson043.service.AccountTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 23:05 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class AccountController {
    @Autowired
    private AccountTransferService accountTransferService;

    @PostMapping("/account/transfer")
    public Result<IdempotentCallResponse<Void>> transfer(@RequestBody IdempotentCallRequest<TransferRequest> request) {
        IdempotentCallResponse<Void> transferResponse = this.accountTransferService.call(request);
        return ResultUtils.success(transferResponse);
    }
}
