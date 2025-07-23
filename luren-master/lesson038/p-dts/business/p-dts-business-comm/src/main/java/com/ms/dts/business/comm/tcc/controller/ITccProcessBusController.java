package com.ms.dts.business.comm.tcc.controller;

import com.itsoku.common.ResultDto;
import com.itsoku.utils.WebUtils;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <b>description</b>：tcc补偿处理器 <br>
 * <b>time</b>：2019-01-14 21:06 <br>
 * <b>author</b>： ready likun_557@163.com
 */
public interface ITccProcessBusController {

    String MODULE_NAME = "tcc";
    String CONTROLLER_NAME = "tccProcessBus";
    String RECOVER_REQUEST_MAPPING = MODULE_NAME + WebUtils.URL_SPLIT + CONTROLLER_NAME + WebUtils.URL_SPLIT + "recover/{tccRecordId}";

    /**
     * tcc补偿处理
     *
     * @param tccRecordId tcc分布式事务记录
     * @return
     * @throws Exception
     */
    @RequestMapping(RECOVER_REQUEST_MAPPING)
    ResultDto<TccProcessorResponseDto> recover(@PathVariable("tccRecordId") String tccRecordId);

}
