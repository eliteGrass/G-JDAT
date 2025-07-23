package com.ms.dts.service.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.utils.CollUtils;
import com.ms.dts.base.model.TccBranchLogPO;
import com.ms.dts.base.model.TccDisposeLogPO;
import com.ms.dts.service.base.mapper.TccDisposeLogMapper;
import com.ms.dts.service.base.service.ITccDisposeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <b>description</b>：tcc分布式事务->补偿日志业务实现类 <br>
 * <b>time</b>：2019-04-13 11:21:49 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
@Slf4j
public class TccDisposeLogServiceImpl extends ServiceImpl<TccDisposeLogMapper, TccDisposeLogPO> implements ITccDisposeLogService {

    @Override
    public int deleteByTccRecordIdList(List<Long> tccRecordIdList){
        if (CollUtils.isEmpty(tccRecordIdList)) {
            return 0;
        }
        LambdaQueryWrapper<TccDisposeLogPO> queryWrapper = Wrappers.lambdaQuery(TccDisposeLogPO.class)
                .in(TccDisposeLogPO::getTccRecordId, tccRecordIdList);
        return this.baseMapper.delete(queryWrapper);
    }

    @Override
    public List<TccDisposeLogPO> getListByTccRecordId(String tccRecordId) {
        LambdaQueryWrapper<TccDisposeLogPO> queryWrapper = Wrappers.lambdaQuery(TccDisposeLogPO.class)
                .eq(TccDisposeLogPO::getTccRecordId, tccRecordId);
        return this.baseMapper.selectList(queryWrapper);
    }
}