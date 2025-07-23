package com.ms.dts.service.tcc.service;

import com.ms.dts.tcc.branch.ExceptionDto;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/31 11:40 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccService {
    /**
     * 上报 tcc处理结果
     *
     * @param tccProcessorResponseDto
     * @throws ExceptionDto
     */
    void uploadTccResult(TccProcessorResponseDto tccProcessorResponseDto);
}
