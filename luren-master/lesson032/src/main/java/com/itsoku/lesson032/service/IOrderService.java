package com.itsoku.lesson032.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itsoku.lesson032.dto.CreateOrderRequest;
import com.itsoku.lesson032.po.OrderPO;

/**
 * <b>description</b>：用户服务 <br>
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
