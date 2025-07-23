package com.ms.dts.service.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.utils.CollUtils;
import com.ms.dts.base.model.TccBranchLogPO;
import com.ms.dts.base.model.TccDisposeLogPO;
import com.ms.dts.service.base.mapper.TccBranchLogMapper;
import com.ms.dts.service.base.service.ITccBranchLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <b>description</b>：分布式事务->分支日志记录业务实现类 <br>
 * <b>time</b>：2019-01-23 13:44:25 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
@Slf4j
public class TccBranchLogServiceImpl extends ServiceImpl<TccBranchLogMapper, TccBranchLogPO> implements ITccBranchLogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByRecordId(String tccRecordId) {
        LambdaQueryWrapper<TccBranchLogPO> queryWrapper = Wrappers.lambdaQuery(TccBranchLogPO.class)
                .eq(TccBranchLogPO::getTccRecordId, tccRecordId);
        return this.baseMapper.delete(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByTccRecordIdList(List<Long> tccRecordIdList){
        if (CollUtils.isEmpty(tccRecordIdList)) {
            return 0;
        }
        LambdaQueryWrapper<TccBranchLogPO> queryWrapper = Wrappers.lambdaQuery(TccBranchLogPO.class)
                .in(TccBranchLogPO::getTccRecordId, tccRecordIdList);
        return this.baseMapper.delete(queryWrapper);
    }

    @Override
    public List<TccBranchLogPO> getListByTccRecordId(String tccRecordId) {
        LambdaQueryWrapper<TccBranchLogPO> queryWrapper = Wrappers.lambdaQuery(TccBranchLogPO.class)
                .eq(TccBranchLogPO::getTccRecordId, tccRecordId)
                .orderByAsc(TccBranchLogPO::getAddtime,TccBranchLogPO::getMethod,TccBranchLogPO::getId);
        return this.baseMapper.selectList(queryWrapper);
    }
}