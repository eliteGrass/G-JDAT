package com.ms.dts.service.base.controller;

import com.itsoku.common.ResultDto;
import com.itsoku.utils.ResultUtils;
import com.ms.dts.base.model.TccRecordPO;
import com.ms.dts.comm.base.controller.ITccRecordController;
import com.ms.dts.service.base.service.ITccRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>：分布式事务记录 http接口 <br>
 * <b>time</b>：2019-01-23 13:44:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TccRecordController implements ITccRecordController {

    @Autowired
    private ITccRecordService tccRecordService;

    @Override
    public ResultDto<TccRecordPO> insert(@RequestBody TccRecordPO model){
        return ResultUtils.successData(this.tccRecordService.insert(model));
    }


    @Override
    public ResultDto<TccRecordPO> getModelById(@RequestParam("id") String id){
        return ResultUtils.successData(this.tccRecordService.getById(id));
    }


    @Override
    public ResultDto<TccRecordPO> getModelByOrder(@RequestParam("busType") int busType, @RequestParam("busId") String busId){
        return ResultUtils.successData(this.tccRecordService.getModelByOrder(busType, busId));
    }

}