package com.itsoku.lesson034.mq.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson034.mq.enums.MsgStatusEnum;
import com.itsoku.lesson034.mq.mapper.MsgMapper;
import com.itsoku.lesson034.mq.po.MsgPO;
import com.itsoku.lesson034.mq.service.IMsgService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 12:07 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class MsgServiceImpl extends ServiceImpl<MsgMapper, MsgPO> implements IMsgService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<MsgPO> batchInsert(String exchange, String routingKey, LocalDateTime expectSendTime, List<String> msgBodyJsonList) {
        //批量插入消息到db中
        List<MsgPO> msgPOList = new ArrayList<>(msgBodyJsonList.size());
        for (String bodyJson : msgBodyJsonList) {
            MsgPO msgPO = new MsgPO();
            msgPO.setId(IdUtil.fastSimpleUUID());
            msgPO.setExchange(exchange);
            msgPO.setRoutingKey(routingKey);
            msgPO.setBodyJson(bodyJson);
            msgPO.setStatus(MsgStatusEnum.INIT.getStatus());
            msgPO.setExpectSendTime(expectSendTime);
            msgPO.setFailMsg(null);
            msgPO.setFailCount(0);
            msgPO.setSendRetry(0);
            msgPO.setCreateTime(LocalDateTime.now());
            msgPOList.add(msgPO);
        }

        this.saveBatch(msgPOList);
        return msgPOList;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateStatusSuccess(MsgPO msgPO) {
        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<MsgPO> updateWrapper = Wrappers.lambdaUpdate(MsgPO.class)
                .eq(MsgPO::getId, msgPO.getId())
                .set(MsgPO::getStatus, MsgStatusEnum.SUCCESS.getStatus())
                .set(MsgPO::getSendRetry, 0)
                .set(MsgPO::getNextRetryTime, null)
                .set(MsgPO::getActualSendTime, now)
                .set(MsgPO::getUpdateTime, now);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateStatusFail(MsgPO msgPO, String failMsg, boolean sendRetry, LocalDateTime nextRetryTime) {
        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<MsgPO> updateWrapper = Wrappers.lambdaUpdate(MsgPO.class)
                .eq(MsgPO::getId, msgPO.getId())
                .ne(MsgPO::getStatus, MsgStatusEnum.SUCCESS.getStatus())
                .set(MsgPO::getStatus, MsgStatusEnum.FAIL.getStatus())
                .set(MsgPO::getFailMsg, failMsg)
                .set(MsgPO::getSendRetry, sendRetry ? 1 : 0)
                .set(MsgPO::getNextRetryTime, nextRetryTime)
                .setSql("fail_count = fail_count + 1")
                .set(MsgPO::getActualSendTime, now)
                .set(MsgPO::getUpdateTime, now);
        this.update(updateWrapper);
    }
}
