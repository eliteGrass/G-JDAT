package com.itsoku.lesson037.mq.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson037.common.BusinessExceptionUtils;
import com.itsoku.lesson037.mq.dto.Msg;
import com.itsoku.lesson037.mq.enums.MsgConsumerLogStatusEnum;
import com.itsoku.lesson037.mq.enums.MsgConsumerStatusEnum;
import com.itsoku.lesson037.mq.mapper.MsgConsumeLogMapper;
import com.itsoku.lesson037.mq.mapper.MsgConsumeMapper;
import com.itsoku.lesson037.mq.po.MsgConsumeLogPO;
import com.itsoku.lesson037.mq.po.MsgConsumePO;
import com.itsoku.lesson037.mq.service.IMsgConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:07 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
@Service
public class MsgConsumerServiceImpl extends ServiceImpl<MsgConsumeMapper, MsgConsumePO> implements IMsgConsumerService {

    @Autowired
    private MsgConsumeLogMapper msgConsumeLogMapper;

    @Override
    public MsgConsumePO getAndCreate(Msg<?> msg, String consumerClassName, String queueName) {
        LambdaQueryWrapper<MsgConsumePO> qw = Wrappers.lambdaQuery(MsgConsumePO.class)
                .eq(MsgConsumePO::getProducer, msg.getProducer())
                .eq(MsgConsumePO::getProducerBusId, msg.getProducerBusId())
                .eq(MsgConsumePO::getConsumerClassName, consumerClassName);
        MsgConsumePO msgConsumerPO = this.findOne(qw);
        if (msgConsumerPO == null) {
            msgConsumerPO = this.insert(msg, consumerClassName, queueName);
        }
        return msgConsumerPO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MsgConsumePO insert(Msg<?> msg, String consumer, String queueName) {
        String producer = msg.getProducer();
        String producerMsgId = msg.getProducerBusId();
        MsgConsumePO msgConsumerPO = new MsgConsumePO();
        msgConsumerPO.setId(IdUtil.fastSimpleUUID());
        msgConsumerPO.setProducer(producer);
        msgConsumerPO.setProducerBusId(producerMsgId);
        msgConsumerPO.setConsumerClassName(consumer);
        msgConsumerPO.setQueueName(queueName);
        msgConsumerPO.setStatus(MsgConsumerStatusEnum.INIT.getStatus());
        msgConsumerPO.setBodyJson(JSONUtil.toJsonStr(msg));
        msgConsumerPO.setCreateTime(LocalDateTime.now());
        msgConsumerPO.setFailCount(0);
        this.save(msgConsumerPO);
        return msgConsumerPO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusSuccess(String id, int curStatus) {
        LambdaUpdateWrapper<MsgConsumePO> updateWrapper = Wrappers.lambdaUpdate(MsgConsumePO.class);
        updateWrapper
                .eq(MsgConsumePO::getId, id)
                .eq(MsgConsumePO::getStatus, curStatus)
                .set(MsgConsumePO::getStatus, MsgConsumerStatusEnum.SUCCESS.getStatus())
                .set(MsgConsumePO::getUpdateTime, LocalDateTime.now())
                .set(MsgConsumePO::getConsumeRetry, 0)
                .set(MsgConsumePO::getNextRetryTime, null);

        boolean update = this.update(updateWrapper);
        if (!update) {
            MsgConsumePO msgConsumePO = this.getById(id);
            log.error("更新消息消费记录失败:{}", msgConsumePO);
            throw BusinessExceptionUtils.businessException("更新消息消费记录失败!");
        }

        //插入消费日志
        MsgConsumeLogPO msgConsumeLogPO = new MsgConsumeLogPO();
        msgConsumeLogPO.setId(IdUtil.fastSimpleUUID());
        msgConsumeLogPO.setMsgConsumeId(id);
        msgConsumeLogPO.setStatus(MsgConsumerLogStatusEnum.SUCCESS.getStatus());
        msgConsumeLogPO.setCreateTime(LocalDateTime.now());
        this.msgConsumeLogMapper.insert(msgConsumeLogPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusFail(String id, int curStatus, String failMsg, boolean consumerRetry, LocalDateTime nextRetryTime) {
        LambdaUpdateWrapper<MsgConsumePO> updateWrapper = Wrappers.lambdaUpdate(MsgConsumePO.class);
        updateWrapper
                .eq(MsgConsumePO::getId, id)
                .eq(MsgConsumePO::getStatus, curStatus)
                .set(MsgConsumePO::getFailMsg, failMsg)
                .set(MsgConsumePO::getStatus, MsgConsumerStatusEnum.FAIL.getStatus())
                .set(MsgConsumePO::getConsumeRetry, consumerRetry ? 1 : 0)
                .set(MsgConsumePO::getNextRetryTime, nextRetryTime)
                .setSql("fail_count = fail_count + 1")
                .set(MsgConsumePO::getUpdateTime, LocalDateTime.now());

        boolean update = this.update(updateWrapper);
        if (!update) {
            MsgConsumePO msgConsumePO = this.getById(id);
            log.error("更新消息消费记录失败:{}", msgConsumePO);
            throw BusinessExceptionUtils.businessException("更新消息消费记录失败!");
        }

        //插入消费日志
        MsgConsumeLogPO msgConsumeLogPO = new MsgConsumeLogPO();
        msgConsumeLogPO.setId(IdUtil.fastSimpleUUID());
        msgConsumeLogPO.setMsgConsumeId(id);
        msgConsumeLogPO.setStatus(MsgConsumerLogStatusEnum.FAIL.getStatus());
        msgConsumeLogPO.setFailMsg(failMsg);
        msgConsumeLogPO.setCreateTime(LocalDateTime.now());
        this.msgConsumeLogMapper.insert(msgConsumeLogPO);
    }
}
