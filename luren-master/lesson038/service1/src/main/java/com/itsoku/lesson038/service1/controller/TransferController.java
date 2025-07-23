package com.itsoku.lesson038.service1.controller;

import com.itsoku.common.ResultDto;
import com.itsoku.lesson038.service1.bus.TransferTccProcess;
import com.itsoku.lesson038.service1.dto.TransferRequest;
import com.itsoku.lesson038.service1.dto.TransferTccBranchRequest;
import com.itsoku.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class TransferController {

    @Autowired
    private TransferTccProcess transferTccProcess;

    @RequestMapping("/transfer")
    public ResultDto<Boolean> transfer(@RequestBody TransferRequest request){
        TransferTccBranchRequest tccBranchRequest = TransferTccBranchRequest.builder().request(request).build();
        this.transferTccProcess.dispose(tccBranchRequest);
        return ResultUtils.successData(Boolean.TRUE);
    }
}
