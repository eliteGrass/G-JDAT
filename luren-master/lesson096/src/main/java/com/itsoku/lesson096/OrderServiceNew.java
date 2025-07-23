package com.itsoku.lesson096;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/12 21:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class OrderServiceNew {

    /**
     * 订单状态转换列表，相当于订单的状态图存储在这个列表中了，
     * 列表中每条记录对应状态图中一个流转步骤，（OrderStatusTransition：表示一个流转步骤，由（fromStatus、action、toStatus）组合而成）
     */
    public static List<OrderStatusTransition> orderStatusTransitionList = new ArrayList<>();

    static {
        //下面根据订单状态图，将订单流转步骤添加到 orderStatusTransitionList 中

        // 待支付 -- 支付 -->已支付
        orderStatusTransitionList.add(OrderStatusTransition.builder()
                .fromStatus(OrderStatus.INIT)
                .action(OrderStatusChangeAction.PAY)
                .toStatus(OrderStatus.PAID).build());

        // 已支付 -- 发货 -->已发货
        orderStatusTransitionList.add(OrderStatusTransition.builder()
                .fromStatus(OrderStatus.PAID)
                .action(OrderStatusChangeAction.SHIP)
                .toStatus(OrderStatus.SHIPPED).build());

        // 已发货 -- 买家收货 -->完成
        orderStatusTransitionList.add(OrderStatusTransition.builder()
                .fromStatus(OrderStatus.SHIPPED)
                .action(OrderStatusChangeAction.DELIVER)
                .toStatus(OrderStatus.FINISHED).build());
    }

    /**
     * 触发订单状态转换
     *
     * @param orderModel 订单
     * @param action     动作
     */
    private void statusTransition(OrderModel orderModel, OrderStatusChangeAction action) {

        //根据订单当前状态 & 动作，去 orderStatusTransitionList 中找，可以找到对应的记录，说明当前操作是允许的；否则抛出异常
        OrderStatus fromStatus = OrderStatus.get(orderModel.getStatus());
        Optional<OrderStatusTransition> first = orderStatusTransitionList.stream().
                filter(orderStatusTransition -> orderStatusTransition.getFromStatus().equals(fromStatus) && orderStatusTransition.getAction().equals(action))
                .findFirst();
        if (!first.isPresent()) {
            throw new RuntimeException("订单状态不支持当前操作");
        }
        OrderStatusTransition orderStatusTransition = first.get();
        //切换订单状态
        orderModel.setStatus(orderStatusTransition.getToStatus().getStatus());
    }

    /**
     * 订单支付
     *
     * @param orderModel
     */
    public void pay(OrderModel orderModel) {
        // 订单状态转换
        this.statusTransition(orderModel, OrderStatusChangeAction.PAY);

        //todo: 其他操作，比如将订单数据保存到db
    }

    /**
     * 卖家发货
     *
     * @param orderModel
     */
    public void ship(OrderModel orderModel) {
        // 订单状态转换
        this.statusTransition(orderModel, OrderStatusChangeAction.SHIP);

        //todo: 其他操作，比如将订单数据保存到db
    }

    /**
     * 买家确认收货
     *
     * @param orderModel
     */
    public void deliver(OrderModel orderModel) {
        // 订单状态转换
        this.statusTransition(orderModel, OrderStatusChangeAction.DELIVER);

        //todo: 其他操作，比如将订单数据保存到db
    }
}
