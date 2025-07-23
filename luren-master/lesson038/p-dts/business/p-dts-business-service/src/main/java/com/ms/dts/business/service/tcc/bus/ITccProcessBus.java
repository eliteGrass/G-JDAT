package com.ms.dts.business.service.tcc.bus;

import com.itsoku.common.ResultDto;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;

/**
 * <b>description</b>：tcc事务补偿处理 <br>
 * <b>time</b>：2024/4/28 10:18 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccProcessBus {
    /**
     * 补偿处理
     *
     * @param tccRecordId tcc分布式事务记录id
     * @return
     * @throws Exception
     */
    ResultDto<TccProcessorResponseDto> recover(String tccRecordId);
}
