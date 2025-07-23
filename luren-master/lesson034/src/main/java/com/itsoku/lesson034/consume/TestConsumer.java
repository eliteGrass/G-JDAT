package com.itsoku.lesson034.consume;

import cn.hutool.json.JSONUtil;
import com.itsoku.lesson034.config.RabbitMQConfiguration;
import com.itsoku.lesson034.dto.OrderMsg;
import com.itsoku.lesson034.mq.consumer.AbstractSequentialMsgConsumer;
import com.itsoku.lesson034.mq.dto.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/10 15:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
@Slf4j
public class TestConsumer extends AbstractSequentialMsgConsumer<OrderMsg, Msg<OrderMsg>> {

    //消费方启动了5个消费者，也可以确保消息消费的顺序性
    @Override
    @RabbitListener(queues = RabbitMQConfiguration.Order.QUEUE, concurrency = "5")
    public void dispose(Message message) {
        /*Msg<OrderMsg> msg = getMsg(message);
        String log = "☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆没有确保顺序消费的情况,消息编号：" + msg.getSequentialMsgNumbering() + ",消息体：" + JSONUtil.toJsonStr(msg.getBody());
        System.err.println(log);*/
        super.dispose(message);
    }

    @Override
    protected void sequentialMsgConsume(Msg<OrderMsg> msg) {
        String log = "☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆顺序消息消费,消息编号：" + msg.getSequentialMsgNumbering() + ",消息体：" + JSONUtil.toJsonStr(msg.getBody());
        System.err.println(log);
        //这里休眠500ms，模拟耗时的业务操作
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
