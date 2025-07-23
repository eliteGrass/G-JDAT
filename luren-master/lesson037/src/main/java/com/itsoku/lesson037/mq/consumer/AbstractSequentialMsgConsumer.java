package com.itsoku.lesson037.mq.consumer;

import cn.hutool.json.JSONUtil;
import com.itsoku.lesson037.common.BusinessExceptionUtils;
import com.itsoku.lesson037.mq.dto.Msg;
import com.itsoku.lesson037.mq.po.MsgConsumePO;
import com.itsoku.lesson037.mq.po.SequentialMsgConsumePositionPO;
import com.itsoku.lesson037.mq.po.SequentialMsgQueuePO;
import com.itsoku.lesson037.mq.service.ISequentialMsgConsumePositionService;
import com.itsoku.lesson037.mq.service.ISequentialMsgQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 幂等消费者，子类集成该类，便拥有了幂等消费 && 消费失败衰减式重试的能力
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/7 15:16 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public abstract class AbstractSequentialMsgConsumer<B, M extends Msg<B>> extends AbstractRetryConsumer<B, M> {

    @Autowired
    protected ISequentialMsgConsumePositionService sequentialMsgConsumeInfoService;

    @Autowired
    protected ISequentialMsgQueueService sequentialMsgQueueService;


    @Override
    protected void consume(Message message, M msg, MsgConsumePO msgConsumerPO) {
        //1、消息过来后，会将其放到db中搞的一个队列中进行排队
        this.pushQueue(message, msg);

        //2、从db中队列表中拉取消息进行消费（循环拉取最小的一条记录，看看是不是要消费的记录，如果是，则进行消费）
        this.pullMsgFromQueueConsume(message, msg);
    }

    /**
     * 从db中消息排队表拉取消息进行消费
     *
     * @param message
     * @param msg
     */
    protected void pullMsgFromQueueConsume(Message message, M msg) {
        String groupId = msg.getSequentialMsgGroupId();
        String queueName = this.getQueueName(message);
        String lockKey = String.format("consumeOrderMessage:%s:%s", groupId, queueName);

        //加分布式锁
        boolean lockResult = this.distributeLock.accept(lockKey, lk -> {
            //从db中队列表中拉取消息进行消费（循环拉取最小的一条记录，和当前消费位置对比下？看看是不是要消费的记录，如果是，则进行消费）
            while (true) {
                //从队列中拿到第一条消息进行消费
                SequentialMsgQueuePO firstMsg = this.sequentialMsgQueueService.getFirst(groupId, queueName);
                //队列中没有消息，退出循环
                if (firstMsg == null) {
                    break;
                }
                //获取队列当前消费的位置
                SequentialMsgConsumePositionPO sequentialMsgConsumePositionPO = this.sequentialMsgConsumeInfoService.getAndCreate(groupId, queueName);

                //轮到自己消费了？
                if (firstMsg.getNumbering() == sequentialMsgConsumePositionPO.getConsumeNumbering() + 1) {
                    //消费,由子类实现
                    this.sequentialMsgConsume(this.getMsg(firstMsg.getMsgJson()));
                    this.transactionTemplate.executeWithoutResult(action -> {
                        //更新消费位置
                        sequentialMsgConsumePositionPO.setConsumeNumbering(firstMsg.getNumbering());
                        boolean update = this.sequentialMsgConsumeInfoService.updateById(sequentialMsgConsumePositionPO);
                        if (!update) {
                            throw BusinessExceptionUtils.businessException("系统繁忙，请稍后重试");
                        }
                        //从队列中移除消息
                        this.sequentialMsgQueueService.delete(firstMsg.getId());
                    });
                } else if (msg.getSequentialMsgNumbering() < sequentialMsgConsumePositionPO.getConsumeNumbering()) {
                    //编号小于当前位置，说明已经消费过了，从队列中移除消息
                    this.sequentialMsgQueueService.delete(firstMsg.getId());
                } else {
                    //还未轮到自己，退出循环
                    break;
                }
            }
        });
        //加锁失败，抛个异常，触发重试
        if (!lockResult) {
            throw BusinessExceptionUtils.businessException("顺序消息消费加锁失败");
        }
    }

    /**
     * 将消息先压入队列
     *
     * @param message
     * @param msg
     */
    private void pushQueue(Message message, M msg) {
        String groupId = msg.getSequentialMsgGroupId();
        String queueName = this.getQueueName(message);
        Long numbering = msg.getSequentialMsgNumbering();
        this.sequentialMsgQueueService.push(groupId, numbering, queueName, JSONUtil.toJsonStr(msg));
    }

    protected abstract void sequentialMsgConsume(M msg);
}
