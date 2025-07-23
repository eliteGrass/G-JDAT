package com.itsoku.lesson033.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itsoku.lesson033.dto.CreateOrderRequest;
import com.itsoku.lesson033.po.OrderPO;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>： 13:38 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IOrderService extends IService<OrderPO> {
    /**
     * 创建订单
     *
     * @param req
     * @return
     */
    String createOrder(CreateOrderRequest req);
}
