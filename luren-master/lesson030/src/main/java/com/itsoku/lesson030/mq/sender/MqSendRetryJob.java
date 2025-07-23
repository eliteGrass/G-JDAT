package com.itsoku.lesson030.mq.sender;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itsoku.lesson030.mq.enums.MsgStatusEnum;
import com.itsoku.lesson030.mq.po.MsgPO;
import com.itsoku.lesson030.mq.service.IMsgService;
import com.itsoku.lesson030.utils.CollUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

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
     * 每20秒执行一次
     */
    @Scheduled(fixedDelay = 20 * 1000)
    public void sendRetry() {
        while (true) {
            /**
             * 查询出需要重试的消息，重新投递
             * select * from t_msg where status = 0 or (status = 2 and send_retry = 1 and next_retry_time <= 当前时间)
             */
            Page<MsgPO> page = new Page<>();
            page.setSize(100);
            page.setCurrent(1);
            LambdaQueryWrapper<MsgPO> qw = Wrappers.lambdaQuery(MsgPO.class)
                    .eq(MsgPO::getStatus, MsgStatusEnum.INIT.getStatus())
                    .or(lq -> lq.eq(MsgPO::getStatus, MsgStatusEnum.FAIL.getStatus())
                            .eq(MsgPO::getSendRetry, 1)
                            .le(MsgPO::getNextRetryTime, LocalDateTime.now()));
            this.msgService.page(page, qw);
            //如果查询出来的为空 || 当前服务要停止了(stop=true)，则退出循环
            if (CollUtils.isEmpty(page.getRecords()) || stop) {
                break;
            }
            //投递重试
            for (MsgPO msgPO : page.getRecords()) {
                this.msgSender.sendRetry(msgPO);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        this.stop = true;
    }
}
