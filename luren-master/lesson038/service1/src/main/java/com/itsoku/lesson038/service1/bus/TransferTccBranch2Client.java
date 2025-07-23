package com.itsoku.lesson038.service1.bus;

import com.itsoku.common.ResultDto;
import com.itsoku.lesson038.service1.dto.TransferTccBranchRequest;
import com.ms.dts.tcc.branch.ITccBranch;
import com.ms.dts.tcc.branch.TccBranchContext;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/26 14:46 <br>
 * <b>author</b>：ready likun_557@162.com
 */
@FeignClient(name = "TransferTccBranch2", url = "http://localhost:9102")
public interface TransferTccBranch2Client extends ITccBranch<TransferTccBranchRequest> {
    @Override
    @PostMapping("/transfer/branch2/try1")
    ResultDto<TccBranchContext<TransferTccBranchRequest>> try1(@RequestBody TccBranchContext<TransferTccBranchRequest> context);

    @PostMapping("/transfer/branch2/confirm")
    @Override
    ResultDto<TccBranchContext<TransferTccBranchRequest>> confirm(@RequestBody TccBranchContext<TransferTccBranchRequest> context);

    @PostMapping("/transfer/branch2/cancel")
    @Override
    ResultDto<TccBranchContext<TransferTccBranchRequest>> cancel(@RequestBody TccBranchContext<TransferTccBranchRequest> context);
}
