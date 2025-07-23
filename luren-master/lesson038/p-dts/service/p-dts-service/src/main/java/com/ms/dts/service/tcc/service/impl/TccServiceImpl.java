package com.ms.dts.service.tcc.service.impl;

import com.itsoku.utils.FrameUtils;
import com.ms.dts.base.ResultStatus;
import com.ms.dts.base.model.TccBranchLogPO;
import com.ms.dts.base.model.TccRecordPO;
import com.ms.dts.service.base.service.ITccBranchLogService;
import com.ms.dts.service.base.service.ITccRecordService;
import com.ms.dts.service.tcc.service.ITccService;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/31 11:40 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class TccServiceImpl implements ITccService {

    @Autowired
    private ITccRecordService tccRecordService;
    @Autowired
    private ITccBranchLogService tccBranchLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadTccResult(TccProcessorResponseDto tccProcessorResponseDto){
        String tccRecordId = tccProcessorResponseDto.getTccRecordId();
        TccRecordPO tccRecordPO = this.tccRecordService.getById(tccRecordId);
        if (tccRecordPO == null) {
            return;
        } else if (!ResultStatus.isInit(tccRecordPO.getStatus())) {
            return;
        }
        if (tccProcessorResponseDto.getFinish()) {
            tccRecordPO.setStatus(tccProcessorResponseDto.getResultStatus().getStatus());
            tccRecordPO.setUptime(FrameUtils.getTime());
            if (!this.tccRecordService.updateById(tccRecordPO)) {
                FrameUtils.throwBaseException("系统繁忙!");
            }
        }
        this.tccBranchLogService.deleteByRecordId(tccRecordId);
        List<TccBranchLogPO> tccBranchLogPOList = tccProcessorResponseDto.getTccBranchLogPOList();
        if (!CollectionUtils.isEmpty(tccBranchLogPOList)) {
            tccBranchLogPOList.forEach(item -> {
                item.setId(null);
            });
        }
        this.tccBranchLogService.saveBatch(tccBranchLogPOList);
    }
}
