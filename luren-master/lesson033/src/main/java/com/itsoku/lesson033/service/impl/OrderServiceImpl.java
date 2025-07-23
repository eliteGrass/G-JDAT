package com.itsoku.lesson033.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson033.config.RabbitMQConfiguration;
import com.itsoku.lesson033.dto.CreateOrderRequest;
import com.itsoku.lesson033.mapper.OrderMapper;
import com.itsoku.lesson033.mq.dto.Msg;
import com.itsoku.lesson033.mq.sender.IMsgSender;
import com.itsoku.lesson033.po.OrderPO;
import com.itsoku.lesson033.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:40 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderPO> implements IOrderService {

    @Autowired
    private IMsgSender msgSender;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(CreateOrderRequest req) {
        OrderPO orderPO = new OrderPO();
        orderPO.setId(IdUtil.fastSimpleUUID());
        orderPO.setGoods(req.getGoods());
        orderPO.setPrice(req.getPrice());
        this.save(orderPO);

        if (req.getDelaySeconds() == 0) {
            //投递订单消息（立即投递）
            log.info("***************投递普通消息：{}", orderPO);
            this.msgSender.sendWithBody(RabbitMQConfiguration.Order.EXCHANGE, RabbitMQConfiguration.Order.ROUTING_KEY, orderPO);
        } else {
            //投递订单消息（立即投递）
            log.info("***************投递延迟消息：{}", orderPO);
            this.msgSender.sendWithBody(RabbitMQConfiguration.Order.EXCHANGE, RabbitMQConfiguration.Order.ROUTING_KEY, req.getDelaySeconds(), TimeUnit.SECONDS, orderPO);
        }

        return orderPO.getId();
    }

}
