package com.ms.dts.comm.base.controller;

import com.itsoku.common.ResultDto;
import com.itsoku.utils.WebUtils;
import com.ms.dts.base.model.TccRecordPO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <b>description</b>：分布式事务记录 http接口 <br>
 * <b>time</b>：2019-01-23 13:44:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccRecordController {

    String MODULE_NAME = "base";
    String CONTROLLER_NAME = "tccRecord";
    String INSERT_REQUEST_MAPPING = MODULE_NAME + WebUtils.URL_SPLIT + CONTROLLER_NAME + WebUtils.URL_SPLIT + "insert";
    String UPDATE_REQUEST_MAPPING = MODULE_NAME + WebUtils.URL_SPLIT + CONTROLLER_NAME + WebUtils.URL_SPLIT + "update";

    String GETMODELBYID_REQUEST_MAPPING = MODULE_NAME + WebUtils.URL_SPLIT + CONTROLLER_NAME + WebUtils.URL_SPLIT + "getModelById";

    String GETMODELBYORDER_REQUEST_MAPPING = MODULE_NAME + WebUtils.URL_SPLIT + CONTROLLER_NAME + WebUtils.URL_SPLIT + "getModelByOrder";

    /**
     * 插入
     *
     * @param model 数据
     * @return
     * @throws Exception
     */
    @RequestMapping(INSERT_REQUEST_MAPPING)
    ResultDto<TccRecordPO> insert(@RequestBody TccRecordPO model);

    /**
     * 根据对象id查询数据
     *
     * @param id 对象id
     * @return 返回id对应的对象
     */
    @RequestMapping(GETMODELBYID_REQUEST_MAPPING)
    ResultDto<TccRecordPO> getModelById(@RequestParam("id") String id);


    /**
     * 获取对象
     *
     * @param busType 订单类型
     * @param busId   订单id
     * @return
     * @throws Exception
     */
    @RequestMapping(GETMODELBYORDER_REQUEST_MAPPING)
    ResultDto<TccRecordPO> getModelByOrder(@RequestParam("busType") int busType, @RequestParam("busId") String busId);

}