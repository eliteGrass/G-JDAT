package com.itsoku.lesson033.controller;

import com.itsoku.lesson033.common.Result;
import com.itsoku.lesson033.common.ResultUtils;
import com.itsoku.lesson033.dto.CreateOrderRequest;
import com.itsoku.lesson033.service.IOrderService;
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
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService userService;

    /**
     * 演示：创建订单，模拟投递消息
     *
     * @param req
     * @return
     */
    @PostMapping("/createOrder")
    public Result<String> createOrder(@Validated @RequestBody CreateOrderRequest req) {
        return ResultUtils.success(this.userService.createOrder(req));
    }


}
