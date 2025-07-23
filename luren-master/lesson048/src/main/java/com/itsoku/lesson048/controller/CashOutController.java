package com.itsoku.lesson048.controller;

import com.itsoku.lesson048.common.Result;
import com.itsoku.lesson048.common.ResultUtils;
import com.itsoku.lesson048.dto.CashOutCallbackRequest;
import com.itsoku.lesson048.dto.CashOutRequest;
import com.itsoku.lesson048.service.ICashOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/13 15:32 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class CashOutController {
    @Autowired
    private ICashOutService cashOutService;

    /**
     * 用户发起提现到微信钱包的接口
     *
     * @param request
     * @return
     */
    @PostMapping("/cashOut")
    public Result<Long> cashOut(@RequestBody CashOutRequest request) {
        Long cashOutId = this.cashOutService.cashOut(request.getAccountId(), request.getPrice());
        return ResultUtils.success(cashOutId);
    }

    /**
     * 微信支付提现回调接口
     *
     * @param request
     * @return
     */
    @PostMapping("/cashOutCallback")
    public Result<Boolean> cashOutCallback(@RequestBody CashOutCallbackRequest request) {
        this.cashOutService.cashOutCallback(request);
        return ResultUtils.success(true);
    }
}
