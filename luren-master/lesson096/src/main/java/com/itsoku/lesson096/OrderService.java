package com.itsoku.lesson096;

import java.util.Objects;

/**
 * 订单服务
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/12 21:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class OrderService {

    /**
     * 订单支付
     *
     * @param orderModel
     */
    public void pay(OrderModel orderModel) {
        //验证订单状态，订单当前状态必须是待支付状态，即新创建的订单
        if (!Objects.equals(orderModel.getStatus(), OrderStatus.INIT.getStatus())) {
            throw new RuntimeException("订单状态不支持当前操作");
        }
        //将订单状态置为已支付状态
        OrderStatus toStatus = OrderStatus.PAID;
        orderModel.setStatus(toStatus.getStatus());

        //todo: 其他操作，比如将订单数据保存到db
    }

    /**
     * 卖家发货
     *
     * @param orderModel
     */
    public void ship(OrderModel orderModel) {
        //验证订单状态，订单当前状态必须是已支付状态
        if (!Objects.equals(orderModel.getStatus(), OrderStatus.PAID.getStatus())) {
            throw new RuntimeException("订单状态不支持当前操作");
        }
        //将订单状态置为已发货状态
        OrderStatus toStatus = OrderStatus.SHIPPED;
        orderModel.setStatus(toStatus.getStatus());

        //todo: 其他操作，比如将订单数据保存到db
    }

    /**
     * 买家确认收货
     *
     * @param orderModel
     */
    public void deliver(OrderModel orderModel) {
        //验证订单状态，订单当前状态必须是已支付状态
        if (!Objects.equals(orderModel.getStatus(), OrderStatus.PAID.getStatus())) {
            throw new RuntimeException("订单状态不支持当前操作");
        }
        //将订单状态置为已完成状态
        OrderStatus toStatus = OrderStatus.FINISHED;
        orderModel.setStatus(toStatus.getStatus());

        //todo: 其他操作，比如将订单数据保存到db
    }
}
