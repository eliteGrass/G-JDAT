package com.itsoku.lesson037.consume;

import com.itsoku.lesson037.config.RabbitMQConfiguration;
import com.itsoku.lesson037.dto.CashOutMsg;
import com.itsoku.lesson037.mq.consumer.AbstractRetryConsumer;
import com.itsoku.lesson037.mq.dto.Msg;
import com.itsoku.lesson037.mq.po.MsgConsumePO;
import com.itsoku.lesson037.service.ICashOutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/10 15:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
@Slf4j
public class CashOutMsgConsumer extends AbstractRetryConsumer<CashOutMsg, Msg<CashOutMsg>> {

    @Autowired
    private ICashOutService cashOutService;

    @RabbitListener(queues = RabbitMQConfiguration.CashOut.QUEUE)
    public void dispose(Message message) {
        super.dispose(message);
    }

    @Override
    protected void consume(Message message, Msg<CashOutMsg> msg, MsgConsumePO msgConsumerPO) {
        CashOutMsg cashOutMsg = msg.getBody();
        //处理提现消息
        this.cashOutService.disposeCashOutMsg(cashOutMsg);
    }

}
