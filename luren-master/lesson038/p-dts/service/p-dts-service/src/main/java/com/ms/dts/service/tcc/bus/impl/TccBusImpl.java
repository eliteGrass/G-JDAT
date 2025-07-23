package com.ms.dts.service.tcc.bus.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itsoku.utils.CollUtils;
import com.itsoku.utils.FrameUtils;
import com.ms.dts.base.ResultStatus;
import com.ms.dts.base.model.TccDisposeLogPO;
import com.ms.dts.base.model.TccRecordPO;
import com.ms.dts.service.base.service.ITccDisposeLogService;
import com.ms.dts.service.base.service.ITccRecordService;
import com.ms.dts.service.tcc.bus.ITccBus;
import com.ms.dts.service.tcc.service.ITccService;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/31 11:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
@Slf4j
public class TccBusImpl implements ITccBus {
    @Autowired
    private ITccService tccService;
    @Autowired
    private ITccRecordService tccRecordService;
    @Autowired
    private ITccDisposeLogService tccDisposeLogService;
    @Autowired
    private RestTemplate tccRecoverRestTemplate;

    @Override
    public void uploadTccResult(TccProcessorResponseDto tccProcessorResponseDto){
        this.tccService.uploadTccResult(tccProcessorResponseDto);
    }

    @Override
    public void disposeNeedRecover(){
        while (true) {
            Page<TccRecordPO> page = new Page<>(1, 50);
            LambdaQueryWrapper<TccRecordPO> queryWrapper = Wrappers.lambdaQuery(TccRecordPO.class)
                    .le(TccRecordPO::getNextDisposeTime, FrameUtils.getTime())
                    .eq(TccRecordPO::getStatus, ResultStatus.INIT.getStatus())
                    .ltSql(TccRecordPO::getFailure, "max_failure");
            tccRecordService.page(page, queryWrapper);
            List<TccRecordPO> tccRecordPOList = page.getRecords();
            if (CollUtils.isEmpty(tccRecordPOList)) {
                break;
            }
            for (TccRecordPO tccRecordPO : tccRecordPOList) {
                try {
                    this.recoverIn(tccRecordPO);
                } catch (Exception e) {
                    log.error("tccRecordPO:{}", FrameUtils.json(tccRecordPO));
                    log.error("tcc记录补偿失败：{}", e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void recover(String tccRecordId){
        TccRecordPO tccRecordPO = this.tccRecordService.getById(tccRecordId);
        this.recoverIn(tccRecordPO);
    }

    private void recoverIn(TccRecordPO tccRecordPO){
        if (Objects.nonNull(tccRecordPO) && tccRecordPO.getStatus().equals(ResultStatus.INIT.getStatus())) {
            TccDisposeLogPO tccDisposeLogPO = new TccDisposeLogPO();
            tccDisposeLogPO.setTccRecordId(tccRecordPO.getId());
            tccDisposeLogPO.setStarttime(FrameUtils.getTime());
            tccDisposeLogPO.setAddtime(FrameUtils.getTime());
            try {
                String url = String.format("%s/tcc/tccProcessBus/recover/%s", tccRecordPO.getServiceApplicationName(), tccRecordPO.getId());
                if (log.isInfoEnabled()) {
                    log.info("url:{}", url);
                }
                String requestResultStr = this.tccRecoverRestTemplate.postForObject(url, null, String.class);
                tccDisposeLogPO.setMsg(requestResultStr);
            } catch (Exception e) {
                log.error("tcc补偿异常:{}", e.getMessage(), e);
                if (StringUtils.isBlank(tccDisposeLogPO.getMsg())) {
                    tccDisposeLogPO.setMsg(e.getMessage());
                }
                throw e;
            } finally {
                tccDisposeLogPO.setEndtime(FrameUtils.getTime());
                tccDisposeLogService.save(tccDisposeLogPO);
                tccRecordPO = this.tccRecordService.getById(tccRecordPO.getId());
                if (Objects.nonNull(tccRecordPO) && tccRecordPO.getStatus().equals(ResultStatus.INIT.getStatus())) {
                    tccRecordPO.setFailure(tccRecordPO.getFailure() + 1);
                    tccRecordPO.setNextDisposeTime(FrameUtils.getNextDisposeTime(tccRecordPO.getFailure(), FrameUtils.getTime()));
                    tccRecordPO.setUptime(tccRecordPO.getVersion());
                    if (!this.tccRecordService.updateById(tccRecordPO)) {
                        FrameUtils.throwBaseException("系统繁忙!");
                    }
                }
            }
        }
    }
}
