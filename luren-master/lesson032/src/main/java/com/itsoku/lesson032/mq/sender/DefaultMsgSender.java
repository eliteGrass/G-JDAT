package com.itsoku.lesson032.mq.sender;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import com.itsoku.lesson032.lock.DistributeLock;
import com.itsoku.lesson032.mq.delay.DelayTaskProcessor;
import com.itsoku.lesson032.mq.enums.MsgStatusEnum;
import com.itsoku.lesson032.mq.po.MsgPO;
import com.itsoku.lesson032.mq.retry.MqSendRetry;
import com.itsoku.lesson032.mq.retry.MqSendRetryResult;
import com.itsoku.lesson032.mq.service.IMsgService;
import com.itsoku.lesson032.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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

    //分布式锁
    private DistributeLock distributeLock;

    //延迟队列：处理延迟消息的投递
    private DelayTaskProcessor delayMsgProcessor;

    //在延迟队列（delayMsgProcessor）等待被投递的延迟消息
    private Map<String, MsgPO> delayMsgWaitingMap = new ConcurrentHashMap<>();

    //延迟队列：处理投递失败后需要延迟投递重试的消息
    private DelayTaskProcessor delaySendRetryProcessor;

    //在延迟队列（sendRetryProcessor）等待被投递重试的消息
    private Map<String, MsgPO> delaySendRetryWaitingMap = new ConcurrentHashMap<>();


    public DefaultMsgSender(IMsgService msgService,
                            MqSendRetry mqSendRetry,
                            ThreadPoolTaskExecutor mqExecutor,
                            DelayTaskProcessor delayMsgProcessor,
                            DelayTaskProcessor delaySendRetryProcessor,
                            DistributeLock distributeLock) {
        this.msgService = msgService;
        this.mqSendRetry = mqSendRetry;
        this.mqExecutor = mqExecutor;
        this.delayMsgProcessor = delayMsgProcessor;
        this.delaySendRetryProcessor = delaySendRetryProcessor;
        this.distributeLock = distributeLock;
    }

    @Override
    public void send(List<Object> msgList) {
        this.send(LocalDateTime.now(), msgList);
    }

    @Override
    public void send(long delayTime, TimeUnit delayTimeUnit, List<Object> msgList) {
        if (delayTime <= 0) {
            throw new IllegalArgumentException("Invalid parameter: delayTimeUnit, must be greater than 0");
        }
        Objects.nonNull(delayTimeUnit);
        //计算期望发送时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expectSendTime = now.plusSeconds(TimeUnit.SECONDS.convert(delayTime, delayTimeUnit));
        //发送消息
        this.send(expectSendTime, msgList);
    }

    private void send(LocalDateTime expectSendTime, List<Object> msgList) {
        Objects.nonNull(expectSendTime);

        //将消息转换为json格式
        List<String> msgBodyJsonList = CollUtils.convertList(msgList, JSONUtil::toJsonStr);

        //消息是否需要先入库？（事务消息 || 延迟消息）需要先入库；延迟消息为什么需要入库？有可能延迟10天，那么这个数据会先存储到db，会依赖数据库
        if (this.isNeedStoreToDb(expectSendTime, msgList)) {
            //有事务，则先入库
            List<MsgPO> msgPOList = this.msgService.batchInsert(expectSendTime, msgBodyJsonList);

            //当前是否有事务？
            boolean hasTransaction = hasTransaction();
            if (!hasTransaction) {
                //无事务
                mqExecutor.execute(() -> deliverMsg(msgPOList));
            } else {
                /**
                 * 若有事务，则在事务执行完毕之后，进行投递
                 *
                 * spring事务扩展点，通过TransactionSynchronizationManager.registerSynchronization添加一个事务同步器TransactionSynchronization，
                 * 事务执行完成之后，不管事务成功还是失败，都会调用TransactionSynchronization#afterCompletion 方法
                 */
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        /**
                         * 代码走到这里时，事务已经完成了（可能是回滚了、或者是提交了）
                         * 看下本地消息记录是否存在？如果存在，说明事务是成功的，业务是执行成功的，则投递消息 & 并将消息状态置为成功
                         */
                        MsgPO msgPO = msgService.getById(msgPOList.get(0).getId());
                        if (msgPO == null) {
                            return;
                        }
                        //为了提升性能：事务消息的投递消息这里异步去执行，即使失败了，会有补偿JOB进行重试
                        mqExecutor.execute(() -> deliverMsg(msgPOList));
                    }
                });
            }
        } else {
            //不需要入库的，直接投递
            for (String msgBodyJson : msgBodyJsonList) {
                this.deliverMsgToMq(msgBodyJson);
            }
        }
    }

    /**
     * 消息是否需要先存储到db？
     *
     * @param expectSendTime
     * @param msgList
     * @return
     */
    private boolean isNeedStoreToDb(LocalDateTime expectSendTime, List<Object> msgList) {
        //事务消息 || 延迟消息
        return hasTransaction() || expectSendTime.isAfter(LocalDateTime.now());
    }

    @Override
    public void sendRetry(MsgPO msgPO) {
        if (msgPO == null) {
            return;
        }
        if (MsgStatusEnum.INIT.getStatus().equals(msgPO.getStatus())) {
            // 待投递的
            this.deliverMsg(msgPO);
        } else if (MsgStatusEnum.FAIL.getStatus().equals(msgPO.getStatus()) && msgPO.getSendRetry().equals(1)) {
            // 投递失败的，需要重试的
            this.delaySendRetry(msgPO);
        }
    }


    /**
     * 批量投递消息
     *
     * @param msgPOList
     */
    private void deliverMsg(List<MsgPO> msgPOList) {
        for (MsgPO msgPO : msgPOList) {
            this.deliverMsg(msgPO);
        }
    }

    /**
     * 投递消息
     *
     * @param msgPO
     */
    private void deliverMsg(MsgPO msgPO) {
        //如果期望发送时间>当前时间，则走延迟消息发送处理逻辑
        if (msgPO.getExpectSendTime().isAfter(LocalDateTime.now())) {
            //延迟消息的投递
            this.deliverDelayMsg(msgPO);
        } else {
            //投递消息
            this.deliverImmediateMsg(msgPO.getId());
        }
    }

    /**
     * 延迟投递的逻辑（这里会将2分中内待发送的消息，放到java本地延迟队列排队处理，排队时间到后，会立即投递
     *
     * @param msgPO
     */
    private void deliverDelayMsg(MsgPO msgPO) {
        //计算延迟时间（s）
        LocalDateTime now = LocalDateTime.now();
        long delayTimeMills = ChronoUnit.MILLIS.between(now, msgPO.getExpectSendTime());

        //这里会将2分中内待发送的消息，放到java本地延迟队列排队处理，非2分钟内的消息，交给job去触发
        if (delayTimeMills <= TimeUnit.MINUTES.toMillis(2)) {
            //为了防止消息重复排队，这里使用了一个map，map中没有的时候，才放入队列进行排队，从队列中移除的时候，会从map中移除
            this.delayMsgWaitingMap.computeIfAbsent(msgPO.getId(), msgId -> {
                boolean putSuccess = this.delayMsgProcessor.put(delayTimeMills, TimeUnit.MILLISECONDS, () -> {
                    //从map中移除
                    this.delayMsgWaitingMap.remove(msgId);
                    //立即投递消息
                    this.deliverImmediateMsg(msgPO.getId());
                });
                if (!putSuccess) {
                    log.error("本地延迟队列已满：{}", this.delayMsgProcessor);
                    throw new RuntimeException("本地延迟队列已满,延迟消息排队失败");
                }
                return msgPO;
            });
        }
    }

    /**
     * 立即投递消息
     *
     * @param msgId
     */
    private void deliverImmediateMsg(String msgId) {
        //对消息添加分布式锁，防止服务集群部署时，避免重复投递
        this.distributeLock.accept("deliverImmediateMsg:" + msgId,
                lockKey -> {
                    //加锁成功后，重新从db中获取消息
                    MsgPO msgPO = this.msgService.getById(msgId);
                    //若消息已投递成功 || （投递失败且不需要重试了），则不处理
                    if (MsgStatusEnum.SUCCESS.getStatus().equals(msgPO.getStatus()) ||
                            (MsgStatusEnum.FAIL.getStatus().equals(msgPO.getStatus()) && msgPO.getSendRetry().equals(0))) {
                        return;
                    }
                    Exception exception = null;
                    try {
                        //投递消息到MQ
                        deliverMsgToMq(msgPO.getBodyJson());
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

                        if (retry) {
                            //投递失败，需要重试的，则延迟重试
                            this.delaySendRetry(this.msgService.getById(msgPO.getId()));
                        } else {
                            //如果走到这里，还是失败，且不需要重试了，建议告警，说明重试次数达到上限了，请求人工干预
                            //todo 请执行告警代码，人工干预
                        }
                    }
                });
    }

    /**
     * 延迟投递重试，对于投递失败的且需要投递重试的，调用此方法进行排队重试
     *
     * @param msgPO
     */
    private void delaySendRetry(MsgPO msgPO) {
        //失败了，还需要重试，则丢入延迟队列（delayRetryProcessor），稍后继续重试
        //计算延迟时间（s）
        LocalDateTime now = LocalDateTime.now();
        long delayTimeMills = ChronoUnit.MILLIS.between(now, msgPO.getNextRetryTime());
        //这里只处理2分钟内待重试投递消息，非2分钟内的消息，交给job去触发
        if (delayTimeMills <= TimeUnit.MINUTES.toMillis(2)) {
            //为了防止消息重复排队，这里使用了一个map，map中没有的时候，才放入队列进行排队，从队列中移除的时候，会从map中移除
            this.delaySendRetryWaitingMap.computeIfAbsent(msgPO.getId(), key -> {
                boolean putSuccess = this.delaySendRetryProcessor.put(delayTimeMills, TimeUnit.MILLISECONDS, () -> {
                    this.delaySendRetryWaitingMap.remove(msgPO.getId());
                    this.deliverMsg(this.msgService.getById(msgPO.getId()));
                });
                if (!putSuccess) {
                    log.error("本地延迟投递重试队列已满：{}", this.delaySendRetryProcessor);
                    throw new RuntimeException("本地延迟投递重试队列已满，延迟投递重试排队失败");
                }
                return msgPO;
            });
        }
    }

    /**
     * 投递消息到mq
     *
     * @param msgBodyJson
     */
    private void deliverMsgToMq(String msgBodyJson) {
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
