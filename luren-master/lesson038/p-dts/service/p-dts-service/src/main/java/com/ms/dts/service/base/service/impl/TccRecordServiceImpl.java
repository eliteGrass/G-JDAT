package com.ms.dts.service.base.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ms.dts.base.model.TccRecordPO;
import com.ms.dts.service.base.mapper.TccRecordMapper;
import com.ms.dts.service.base.service.ITccRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <b>description</b>：分布式事务记录业务实现类 <br>
 * <b>time</b>：2019-01-23 13:44:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
@Slf4j
public class TccRecordServiceImpl extends ServiceImpl<TccRecordMapper, TccRecordPO> implements ITccRecordService {


    @Override
    public TccRecordPO getModelByOrder(int busType, String busId) {
        LambdaQueryWrapper<TccRecordPO> queryWrapper = Wrappers.lambdaQuery(TccRecordPO.class)
                .eq(TccRecordPO::getBusType, busType)
                .eq(TccRecordPO::getBusId, busId);
        return this.findOne(queryWrapper);
    }

    @Override
    public TccRecordPO insert(TccRecordPO model) {
        model.setId(IdUtil.fastSimpleUUID());
        this.save(model);
        return model;
    }
}