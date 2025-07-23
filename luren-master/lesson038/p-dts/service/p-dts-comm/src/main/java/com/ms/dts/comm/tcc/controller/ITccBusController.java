package com.ms.dts.comm.tcc.controller;

import com.itsoku.common.ResultDto;
import com.itsoku.utils.WebUtils;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/31 12:12 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccBusController {
    String MODULE_NAME = "tcc";
    String CONTROLLER_NAME = "tccBus";
    String UPLOADTCCRESULT_REQUEST_MAPPING = MODULE_NAME + WebUtils.URL_SPLIT + CONTROLLER_NAME + WebUtils.URL_SPLIT + "uploadTccResult";
    String DISPOSENEEDRECOVER_REQUEST_MAPPING = MODULE_NAME + WebUtils.URL_SPLIT + CONTROLLER_NAME + WebUtils.URL_SPLIT + "disposeNeedRecover";
    String RECOVER_REQUEST_MAPPING = MODULE_NAME + WebUtils.URL_SPLIT + CONTROLLER_NAME + WebUtils.URL_SPLIT + "recover/{tccRecordId}";

    /**
     * 上报 tcc处理结果
     *
     * @param tccProcessorResponseDto 数据
     * @return
     * @throws Exception
     */
    @RequestMapping(UPLOADTCCRESULT_REQUEST_MAPPING)
    ResultDto<Void> uploadTccResult(@RequestBody TccProcessorResponseDto tccProcessorResponseDto);

    /**
     * 处理需要补偿的数据
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(DISPOSENEEDRECOVER_REQUEST_MAPPING)
    ResultDto<Void> disposeNeedRecover();

    /**
     * tccRecored补偿
     *
     * @param tccRecordId
     * @return
     * @throws Exception
     */
    @RequestMapping(RECOVER_REQUEST_MAPPING)
    ResultDto<Void> recover(@PathVariable("tccRecordId") String tccRecordId);
}
