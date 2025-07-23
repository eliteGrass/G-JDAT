package com.itsoku.lesson033.service;

import com.itsoku.lesson033.common.BusinessExceptionUtils;
import com.itsoku.lesson033.config.RabbitMQConfiguration;
import com.itsoku.lesson033.mq.consumer.AbstractIdempotentConsumer;
import com.itsoku.lesson033.mq.dto.Msg;
import com.itsoku.lesson033.po.OrderPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 订单消息消费者
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/7 15:27 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
@Component
public class OrderConsumer extends AbstractIdempotentConsumer<OrderPO, Msg<OrderPO>> {

    @RabbitListener(queues = RabbitMQConfiguration.Order.QUEUE)
    public void dispose(Message message) {
        super.dispose(message);
    }

    @Override
    protected void disposeIn(Message message, OrderPO body) {
        log.info("***************收到订单消息：{}", body);

        //这里是为了演示消费异常重试的场景，这里加了个判断，金额为空的时候，弹出异常
        if (body.getPrice() == null) {
            throw BusinessExceptionUtils.businessException("订单金额有误啊!");
        }
    }
}
