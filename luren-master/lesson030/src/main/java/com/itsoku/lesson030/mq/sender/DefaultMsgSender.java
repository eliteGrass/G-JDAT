package com.itsoku.lesson030.mq.sender;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import com.itsoku.lesson030.mq.po.MsgPO;
import com.itsoku.lesson030.mq.retry.MqSendRetry;
import com.itsoku.lesson030.mq.retry.MqSendRetryResult;
import com.itsoku.lesson030.mq.service.IMsgService;
import com.itsoku.lesson030.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:25 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public class DefaultMsgSender implements IMsgSender {
    private IMsgService msgService;
    private MqSendRetry mqSendRetry;
    private ThreadPoolTaskExecutor mqExecutor;

    public DefaultMsgSender(IMsgService msgService, MqSendRetry mqSendRetry, ThreadPoolTaskExecutor mqExecutor) {
        this.msgService = msgService;
        this.mqSendRetry = mqSendRetry;
        this.mqExecutor = mqExecutor;
    }

    @Override
    public void send(List<Object> msgList) {
        //将消息转换为json格式
        List<String> msgBodyJsonList = CollUtils.convertList(msgList, JSONUtil::toJsonStr);

        //当前是否有事务？
        boolean hasTransaction = hasTransaction();
        if (hasTransaction) {
            //有事务，则先入库
            List<MsgPO> msgPOList = this.msgService.batchInsert(msgBodyJsonList);
            /**
             * spring事务扩展点，通过TransactionSynchronizationManager.registerSynchronization添加一个事务同步器TransactionSynchronization，
             * 事务执行完成之后，不管事务成功还是失败，都会调用TransactionSynchronization#afterCompletion 方法
             */
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    //为了提升性能：事务消息的投递消息这里异步去执行，即使失败了，会有补偿JOB进行重试
                    mqExecutor.execute(() -> transactionAfter(msgPOList));
                }
            });
        } else {
            //没有事务，则直接投递消息到MQ
            for (String msgBodyJson : msgBodyJsonList) {
                this.sendMsgToMQ(msgBodyJson);
            }
        }
    }

    @Override
    public void sendRetry(MsgPO msgPO) {
        this.sendMsg(msgPO);
    }

    /**
     * 事务后执行的方法
     *
     * @param msgPOList
     */
    private void transactionAfter(List<MsgPO> msgPOList) {
        /**
         * 代码走到这里时，事务已经完成了（可能是回滚了、或者是提交了）
         * 看下本地消息记录是否存在？如果存在，说明事务是成功的，业务是执行成功的，则投递消息 & 并将消息状态置为成功
         */
        MsgPO msgPO = msgService.getById(msgPOList.get(0).getId());
        if (msgPO != null) {
            //循环发送消息
            for (MsgPO msg : msgPOList) {
                this.sendMsg(msg);
            }
            log.info("事务执行成功，消息投递完毕");
        } else {
            //走到这里，说明事务执行失败了，消息未投递，由于插入消息和业务在一个事务中，事务执行失败，此时db中也是没有消息记录
            log.info("事务执行失败，消息未投递");
        }
    }


    private void sendMsg(MsgPO msgPO) {
        Exception exception = null;
        try {
            //投递消息到MQ
            sendMsgToMQ(msgPO.getBodyJson());
        } catch (Exception e) {
            exception = e;
        }
        //没有异常，说明消息投递成功
        if (exception == null) {
            //将消息状态置为成功
            msgService.updateStatusSuccess(msgPO);
        } else {
            //有异常，则获取重试信息，将信息更新到db中
            MqSendRetryResult retryResult = this.mqSendRetry.getRetryResult(msgPO);
            //异常信息
            String failMsg = ExceptionUtil.stacktraceToString(exception);
            //是否需要重试？
            boolean retry = retryResult.isRetry();
            //下次重试时间
            LocalDateTime nextRetryTime = retryResult.getNextRetryTime();
            //将消息状态置为失败（且记录失败信息、是否需要重试，下次重试时间）
            this.msgService.updateStatusFail(msgPO, failMsg, retry, nextRetryTime);

            //如果走到这里，还是失败，且不需要重试了，建议告警
            if (!retry) {
                //todo 请执行告警代码，人工干预
            }
        }
    }

    private void sendMsgToMQ(String msgBodyJson) {
        //todo 这里大家去实现将消息投递到mq的代码
        log.info("开始投递消息到MQ。。。。");
        log.info("消息体：{}", msgBodyJson);
        //这里为了测试出投递失败的效果，故意在这里加了一段代码
        if (msgBodyJson.length() > 100) {
            throw new RuntimeException("模拟投递失败的情况，故意失败：" + msgBodyJson);
        }
        log.info("消息已投递到MQ");
    }

    /**
     * 判断当前是否有事务？
     *
     * @return
     */
    private static boolean hasTransaction() {
        //开启了事务同步 && 并且当前确实有事务
        return TransactionSynchronizationManager.isSynchronizationActive() && TransactionSynchronizationManager.isActualTransactionActive();
    }


}
