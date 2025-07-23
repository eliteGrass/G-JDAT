package com.ms.dts.service.tcc.controller;

import com.itsoku.common.ResultDto;
import com.itsoku.utils.ResultUtils;
import com.ms.dts.comm.tcc.controller.ITccBusController;
import com.ms.dts.service.tcc.bus.ITccBus;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/31 11:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TccBusController implements ITccBusController {
    @Autowired
    private ITccBus tccBus;

    @Override
    public ResultDto<Void> uploadTccResult(@RequestBody TccProcessorResponseDto tccProcessorResponseDto){
        this.tccBus.uploadTccResult(tccProcessorResponseDto);
        return ResultUtils.success();
    }

    @Override
    public ResultDto<Void> disposeNeedRecover(){
        this.tccBus.disposeNeedRecover();
        return ResultUtils.success();
    }

    @Override
    public ResultDto<Void> recover(@PathVariable("tccRecordId") String tccRecordId){
        this.tccBus.recover(tccRecordId);
        return ResultUtils.success();
    }
}