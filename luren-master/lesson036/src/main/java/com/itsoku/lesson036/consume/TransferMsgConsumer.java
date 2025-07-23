package com.itsoku.lesson036.consume;

import cn.hutool.json.JSONUtil;
import com.itsoku.lesson036.config.RabbitMQConfiguration;
import com.itsoku.lesson036.dto.TransferMsg;
import com.itsoku.lesson036.mq.consumer.AbstractIdempotentConsumer;
import com.itsoku.lesson036.mq.consumer.AbstractRetryConsumer;
import com.itsoku.lesson036.mq.dto.Msg;
import com.itsoku.lesson036.mq.po.MsgConsumePO;
import com.itsoku.lesson036.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/10 15:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
@Slf4j
public class TransferMsgConsumer extends AbstractIdempotentConsumer<TransferMsg, Msg<TransferMsg>> {

    @Autowired
    private IAccountService accountService;
    @RabbitListener(queues = RabbitMQConfiguration.Transfer.QUEUE)
    public void dispose(Message message) {
        super.dispose(message);
    }

    @Override
    protected void idempotentConsume(TransferMsg transferMsg) {
        log.info("处理转账消息：{}",JSONUtil.toJsonStr(transferMsg));

        this.accountService.balanceAdd(transferMsg.getToAccountId(),transferMsg.getTransferPrice());
    }


}
