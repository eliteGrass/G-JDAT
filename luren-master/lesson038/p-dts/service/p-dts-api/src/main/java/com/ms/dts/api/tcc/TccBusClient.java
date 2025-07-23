package com.ms.dts.api.tcc;

import com.ms.dts.api.ApiConstant;
import com.ms.dts.comm.tcc.controller.ITccBusController;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/31 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@FeignClient(name = "TccBusClient", url = ApiConstant.SERVICE_URL)
public interface TccBusClient extends ITccBusController {
}
