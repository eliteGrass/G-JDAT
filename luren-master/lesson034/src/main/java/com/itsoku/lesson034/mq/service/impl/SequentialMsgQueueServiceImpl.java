package com.itsoku.lesson034.mq.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson034.mq.mapper.SequentialMsgQueueMapper;
import com.itsoku.lesson034.mq.po.SequentialMsgQueuePO;
import com.itsoku.lesson034.mq.service.ISequentialMsgQueueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class SequentialMsgQueueServiceImpl extends ServiceImpl<SequentialMsgQueueMapper, SequentialMsgQueuePO> implements ISequentialMsgQueueService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SequentialMsgQueuePO push(String groupId, Long numbering, String queueName, String msgJson) {
        SequentialMsgQueuePO po = this.get(groupId, numbering, queueName);
        if (po != null) {
            return po;
        }
        po = new SequentialMsgQueuePO();
        po.setId(IdUtil.fastSimpleUUID());
        po.setGroupId(groupId);
        po.setNumbering(numbering);
        po.setQueueName(queueName);
        po.setMsgJson(msgJson);
        this.save(po);
        return po;
    }

    private SequentialMsgQueuePO get(String groupId, Long numbering, String queueName) {
        LambdaQueryWrapper<SequentialMsgQueuePO> qw = Wrappers.lambdaQuery(SequentialMsgQueuePO.class)
                .eq(SequentialMsgQueuePO::getGroupId, groupId)
                .eq(SequentialMsgQueuePO::getNumbering, numbering)
                .eq(SequentialMsgQueuePO::getQueueName, queueName);
        return this.findOne(qw);
    }

    @Override
    public SequentialMsgQueuePO getFirst(String groupId, String queueName) {
        LambdaQueryWrapper<SequentialMsgQueuePO> qw = Wrappers.lambdaQuery(SequentialMsgQueuePO.class)
                .eq(SequentialMsgQueuePO::getGroupId, groupId)
                .eq(SequentialMsgQueuePO::getQueueName, queueName)
                .orderByAsc(SequentialMsgQueuePO::getNumbering);
        return this.findOne(qw);
    }

    @Override
    public void delete(String id) {
        this.removeById(id);
    }
}
