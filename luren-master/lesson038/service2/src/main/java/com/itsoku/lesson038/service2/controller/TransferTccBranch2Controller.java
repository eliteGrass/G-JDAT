package com.itsoku.lesson038.service2.controller;

import com.itsoku.common.ResultDto;
import com.itsoku.lesson038.service2.bus.TransferTccBranch2;
import com.itsoku.lesson038.service2.dto.TransferTccBranchRequest;
import com.ms.dts.tcc.branch.ITccBranch;
import com.ms.dts.tcc.branch.TccBranchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/26 14:46 <br>
 * <b>author</b>：ready likun_557@162.com
 */
@RestController
public class TransferTccBranch2Controller implements ITccBranch<TransferTccBranchRequest> {
    @Autowired
    private TransferTccBranch2 transferTccBranch2;

    @Override
    @PostMapping("/transfer/branch2/try1")
    public ResultDto<TccBranchContext<TransferTccBranchRequest>> try1(@RequestBody TccBranchContext<TransferTccBranchRequest> context){
        return this.transferTccBranch2.try1(context);
    }

    @PostMapping("/transfer/branch2/confirm")
    @Override
    public ResultDto<TccBranchContext<TransferTccBranchRequest>> confirm(@RequestBody TccBranchContext<TransferTccBranchRequest> context){
        return this.transferTccBranch2.confirm(context);
    }

    @PostMapping("/transfer/branch2/cancel")
    @Override
    public ResultDto<TccBranchContext<TransferTccBranchRequest>> cancel(@RequestBody TccBranchContext<TransferTccBranchRequest> context){
        return this.transferTccBranch2.cancel(context);
    }
}
