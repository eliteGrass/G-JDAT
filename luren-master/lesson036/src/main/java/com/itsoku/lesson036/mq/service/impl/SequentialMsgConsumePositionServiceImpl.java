package com.itsoku.lesson036.mq.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson036.mq.mapper.SequentialMsgConsumeInfoMapper;
import com.itsoku.lesson036.mq.po.SequentialMsgConsumePositionPO;
import com.itsoku.lesson036.mq.service.ISequentialMsgConsumePositionService;
import org.springframework.stereotype.Service;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class SequentialMsgConsumePositionServiceImpl extends ServiceImpl<SequentialMsgConsumeInfoMapper, SequentialMsgConsumePositionPO> implements ISequentialMsgConsumePositionService {

    @Override
    public SequentialMsgConsumePositionPO getAndCreate(String groupId, String queueName) {
        LambdaQueryWrapper<SequentialMsgConsumePositionPO> queryWrapper = Wrappers.lambdaQuery(SequentialMsgConsumePositionPO.class)
                .eq(SequentialMsgConsumePositionPO::getGroupId, groupId)
                .eq(SequentialMsgConsumePositionPO::getQueueName, queueName);
        SequentialMsgConsumePositionPO po = this.findOne(queryWrapper);
        if (po == null) {
            po = new SequentialMsgConsumePositionPO();
            po.setId(IdUtil.fastSimpleUUID());
            po.setGroupId(groupId);
            po.setQueueName(queueName);
            po.setConsumeNumbering(0L);
            this.save(po);
        }
        return po;
    }
}
