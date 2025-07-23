package com.ms.dts.business.service.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.utils.CollUtils;
import com.ms.dts.base.model.TccBusBranchLogPO;
import com.ms.dts.business.service.base.mapper.TccBusBranchLogMapper;
import com.ms.dts.business.service.base.service.ITccBusBranchLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <b>description</b>：业务库中分支事务日志记录业务实现类 <br>
 * <b>time</b>：2019-01-24 16:43:51 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
@Slf4j
public class TccBusBranchLogServiceImpl extends ServiceImpl<TccBusBranchLogMapper, TccBusBranchLogPO> implements ITccBusBranchLogService {


    @Override
    public TccBusBranchLogPO get(String tccRecordId, String classname, int method){
        LambdaQueryWrapper<TccBusBranchLogPO> queryWrapper = Wrappers.lambdaQuery(TccBusBranchLogPO.class)
                .eq(TccBusBranchLogPO::getTccRecordId, tccRecordId)
                .eq(TccBusBranchLogPO::getClassname, classname)
                .eq(TccBusBranchLogPO::getMethod, method);
        return this.findOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByTccRecordIdList(List<String> tccRecordIdList){
        if (CollUtils.isEmpty(tccRecordIdList)) {
            return 0;
        }
        LambdaQueryWrapper<TccBusBranchLogPO> queryWrapper = Wrappers.lambdaQuery(TccBusBranchLogPO.class)
                .in(TccBusBranchLogPO::getTccRecordId, tccRecordIdList);
        return this.baseMapper.delete(queryWrapper);
    }
}