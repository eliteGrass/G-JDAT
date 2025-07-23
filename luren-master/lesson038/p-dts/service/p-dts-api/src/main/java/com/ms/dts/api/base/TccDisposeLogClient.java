package com.ms.dts.api.base;

import com.ms.dts.api.ApiConstant;
import com.ms.dts.comm.base.controller.ITccDiposeLogController;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <b>description</b>：tcc分布式事务->补偿日志 相关操作Feign客户端 <br>
 * <b>time</b>：2019-04-13 11:21:49 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@FeignClient(name = "TccDisposeLogClient", url = ApiConstant.SERVICE_URL)
public interface TccDisposeLogClient extends ITccDiposeLogController {
}