package com.itsoku.lesson096;

import org.junit.Test;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/12 21:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class OrderTest {

    OrderService orderService = new OrderService();
    @Test
    public void m1(){
        OrderModel orderModel = OrderModel.builder().id("1").status(OrderStatus.INIT.getStatus()).build();
        this.orderService.pay(orderModel);
        System.out.println(orderModel);
    }

    @Test
    public void m2(){
        OrderModel orderModel = OrderModel.builder().id("1").status(OrderStatus.INIT.getStatus()).build();
        this.orderService.ship(orderModel);
        System.out.println(orderModel);
    }


    OrderServiceNew orderServiceNew = new OrderServiceNew();
    @Test
    public void m3(){
        OrderModel orderModel = OrderModel.builder().id("1").status(OrderStatus.INIT.getStatus()).build();
        this.orderServiceNew.pay(orderModel);
        System.out.println(orderModel);
    }

    @Test
    public void m4(){
        OrderModel orderModel = OrderModel.builder().id("1").status(OrderStatus.INIT.getStatus()).build();
        this.orderServiceNew.ship(orderModel);
        System.out.println(orderModel);
    }
}
