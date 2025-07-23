package com.itsoku.lesson037.mq.sender;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itsoku.lesson037.mq.enums.MsgStatusEnum;
import com.itsoku.lesson037.mq.po.MsgPO;
import com.itsoku.lesson037.mq.service.IMsgService;
import com.itsoku.lesson037.utils.CollUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 消息发送补偿job
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 14:55 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class MqSendRetryJob implements DisposableBean {
    private IMsgService msgService;
    private IMsgSender msgSender;
    private volatile boolean stop;

    public MqSendRetryJob(IMsgService msgService, IMsgSender msgSender) {
        this.msgService = msgService;
        this.msgSender = msgSender;
    }

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void sendRetry() {
        /**
         * 查询出需要重试的消息（状态为0 and 期望投递时间 <= 当前时间 + 2分钟） || （投递失败的 and 需要重试 and 下次重试时间小于等于当前时间 + 2分钟）
         * select * from t_msg where ((status = 0 and expect_send_time<=当前时间+2分钟) or (status = 2 and send_retry = 1 and next_retry_time <= 当前时间 + 2分钟))
         */
        LocalDateTime time = LocalDateTime.now().plusMinutes(2);
        LambdaQueryWrapper<MsgPO> qw = Wrappers.lambdaQuery(MsgPO.class)
                .and(query -> query.and(lq ->
                                lq.eq(MsgPO::getStatus, MsgStatusEnum.INIT.getStatus())
                                        .le(MsgPO::getExpectSendTime, time))
                        .or(lq -> lq.eq(MsgPO::getStatus, MsgStatusEnum.FAIL.getStatus())
                                .eq(MsgPO::getSendRetry, 1)
                                .le(MsgPO::getNextRetryTime, time)));
        qw.orderByAsc(MsgPO::getId);

        //先获取最小的一条记录的id
        MsgPO minMsgPo = this.msgService.findOne(qw);
        if (minMsgPo == null) {
            return;
        }
        this.msgSender.sendRetry(minMsgPo);
        String minMsgId = minMsgPo.getId();

        //循环中继续向后找出id>minMsgId的所有记录，然后投递重试
        while (true) {
            //select * from t_msg where ((status = 0 and expect_send_time<=当前时间+2分钟) or (status = 2 and send_retry = 1 and next_retry_time <= 当前时间 + 2分钟)) and id>#{minMsgId}
            qw = Wrappers.lambdaQuery(MsgPO.class)
                    .and(query -> query.and(lq ->
                                    lq.eq(MsgPO::getStatus, MsgStatusEnum.INIT.getStatus()).le(MsgPO::getExpectSendTime, time))
                            .or(lq -> lq.eq(MsgPO::getStatus, MsgStatusEnum.FAIL.getStatus()).eq(MsgPO::getSendRetry, 1).le(MsgPO::getNextRetryTime, time)));
            qw.gt(MsgPO::getId, minMsgId);
            qw.orderByAsc(MsgPO::getId);
            Page<MsgPO> page = new Page<>();
            page.setCurrent(1);
            page.setSize(500);
            this.msgService.page(page, qw);

            //如果查询出来的为空 || 当前服务要停止了(stop=true)，则退出循环
            if (CollUtils.isEmpty(page.getRecords()) || stop) {
                break;
            }
            //投递重试
            for (MsgPO msgPO : page.getRecords()) {
                this.msgSender.sendRetry(msgPO);
            }

            // minMsgId = 当前列表最后一条消息的id
            minMsgId = page.getRecords().get(page.getRecords().size() - 1).getId();
        }
    }

    @Override
    public void destroy() throws Exception {
        this.stop = true;
    }
}
