package com.ms.dts.business.service.tcc.bus.impl;

import com.itsoku.common.ResultDto;
import com.itsoku.utils.FrameUtils;
import com.itsoku.utils.ResultUtils;
import com.ms.dts.api.base.TccRecordClient;
import com.ms.dts.base.ITccProcessor;
import com.ms.dts.base.model.TccRecordPO;
import com.ms.dts.business.service.tcc.bus.ITccProcessBus;
import com.ms.dts.tcc.dto.TccProcessorRequestDto;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * <b>description</b>：业务方tcc分布式事务记录补偿 <br>
 * <b>time</b>：2024/4/28 10:20 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
@Slf4j
public class TccProcessBusImpl implements ITccProcessBus {

    @Autowired(required = false)
    private Map<String, ITccProcessor> tccProcessorMap;
    @Autowired
    private TccRecordClient tccRecordClient;

    @Override
    public ResultDto<TccProcessorResponseDto> recover(String tccRecordId){
        TccRecordPO tccRecordPO = this.tccRecordClient.getModelById(tccRecordId).getSuccessData();
        if (Objects.isNull(tccRecordPO)) {
            FrameUtils.throwBaseException(String.format("TccRecordModel[id=%s]记录不存在", tccRecordId));
        }
        String beanname = tccRecordPO.getBeanname();
        if (StringUtils.isBlank(beanname)) {
            return ResultUtils.error("参数tccRecordModel.beanname不能为空!");
        }
        ITccProcessor processor = this.tccProcessorMap.get(beanname);
        if (processor == null) {
            String msg = String.format("消息处理路由，beanname:[%s]消息处理器未找到!", beanname);
            log.error(msg);
            return ResultUtils.error(msg);
        }
        return processor.recover(TccProcessorRequestDto.builder().tccRecordPO(tccRecordPO).syncUploadResult(true).sync(true).build());
    }
}
