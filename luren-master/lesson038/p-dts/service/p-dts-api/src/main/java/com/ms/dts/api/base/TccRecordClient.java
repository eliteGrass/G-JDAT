package com.ms.dts.api.base;

import com.ms.dts.api.ApiConstant;
import com.ms.dts.comm.base.controller.ITccRecordController;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <b>description</b>：分布式事务记录 相关操作Feign客户端 <br>
 * <b>time</b>：2019-01-23 13:44:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@FeignClient(name = "TccRecordClient", url = ApiConstant.SERVICE_URL)
public interface TccRecordClient extends ITccRecordController {
}