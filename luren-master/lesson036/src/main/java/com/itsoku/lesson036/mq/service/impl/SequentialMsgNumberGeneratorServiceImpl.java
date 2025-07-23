package com.itsoku.lesson036.mq.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson036.common.BusinessExceptionUtils;
import com.itsoku.lesson036.mq.mapper.SequentialMsgNumberGeneratorMapper;
import com.itsoku.lesson036.mq.po.SequentialMsgNumberGeneratorPO;
import com.itsoku.lesson036.mq.service.ISequentialMsgNumberGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class SequentialMsgNumberGeneratorServiceImpl extends ServiceImpl<SequentialMsgNumberGeneratorMapper, SequentialMsgNumberGeneratorPO> implements ISequentialMsgNumberGeneratorService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long get(String groupId) {
        Objects.nonNull(groupId);
        SequentialMsgNumberGeneratorPO po = this.getAndInit(groupId);
        long value = po.getNumbering() + 1;
        po.setNumbering(value);
        boolean update = this.updateById(po);
        if (!update) {
            throw BusinessExceptionUtils.businessException("系统繁忙，请稍后重试");
        }
        return value;
    }

    private SequentialMsgNumberGeneratorPO getAndInit(String groupId) {
        LambdaQueryWrapper<SequentialMsgNumberGeneratorPO> qw = Wrappers.lambdaQuery(SequentialMsgNumberGeneratorPO.class).eq(SequentialMsgNumberGeneratorPO::getGroupId, groupId);
        SequentialMsgNumberGeneratorPO po = this.findOne(qw);
        if (po == null) {
            po = new SequentialMsgNumberGeneratorPO();
            po.setId(IdUtil.fastSimpleUUID());
            po.setGroupId(groupId);
            po.setNumbering(0L);
            po.setVersion(0L);
            this.save(po);
        }
        return po;
    }
}
