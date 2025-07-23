package com.ms.dts.business.service.tcc.controller;

import com.itsoku.common.ResultDto;
import com.ms.dts.business.comm.tcc.controller.ITccProcessBusController;
import com.ms.dts.business.service.tcc.bus.ITccProcessBus;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>：tcc事务记录补偿 <br>
 * <b>time</b>：2019-01-14 21:12 <br>
 * <b>author</b>： ready likun_557@163.com
 */
@RestController
public class TccProcessBusController implements ITccProcessBusController {

    @Autowired
    private ITccProcessBus tccProcessBus;

    @Override
    public ResultDto<TccProcessorResponseDto> recover(@PathVariable("tccRecordId") String tccRecordId){
        return this.tccProcessBus.recover(tccRecordId);
    }
}
