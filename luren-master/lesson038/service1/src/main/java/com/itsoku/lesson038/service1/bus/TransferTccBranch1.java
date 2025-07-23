package com.itsoku.lesson038.service1.bus;

import cn.hutool.core.lang.TypeReference;
import com.itsoku.common.ResultDto;
import com.itsoku.lesson038.service1.dto.TransferRequest;
import com.itsoku.lesson038.service1.dto.TransferTccBranchRequest;
import com.itsoku.lesson038.service1.po.AccountPO;
import com.itsoku.lesson038.service1.service.IAccountService;
import com.itsoku.utils.FrameUtils;
import com.itsoku.utils.ResultUtils;
import com.ms.dts.tcc.branch.ITccBranchBus;
import com.ms.dts.tcc.branch.TccBranchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/16 21:29 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class TransferTccBranch1 implements ITccBranchBus<TransferTccBranchRequest> {
    @Autowired
    private IAccountService accountService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<TccBranchContext<TransferTccBranchRequest>> try1(TccBranchContext<TransferTccBranchRequest> context){
        AccountPO fromAccountPO = this.accountService.getById(context.getRequest().getRequest().getFromAccountId());
        if (fromAccountPO == null) {
            FrameUtils.throwBaseException("付款方账号不存在!");
        }
        //先将余额转到冻结金额，余额-price，冻结金额+price
        TransferRequest transferRequest = context.getRequest().getRequest();
        accountService.frozen(transferRequest.getFromAccountId(), transferRequest.getTransferPrice());
        return ResultUtils.successData(context);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<TccBranchContext<TransferTccBranchRequest>> confirm(TccBranchContext<TransferTccBranchRequest> context){
        //冻结金额-price
        TransferRequest transferRequest = context.getRequest().getRequest();
        accountService.frozenSubtract(transferRequest.getFromAccountId(), transferRequest.getTransferPrice());
        return ResultUtils.successData(context);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<TccBranchContext<TransferTccBranchRequest>> cancel(TccBranchContext<TransferTccBranchRequest> context){
        //失败了，将钱从冻结转到余额，余额+price，冻结金额-price
        TransferRequest transferRequest = context.getRequest().getRequest();
        accountService.unFrozen(transferRequest.getFromAccountId(), transferRequest.getTransferPrice());
        return ResultUtils.successData(context);
    }

    @Override
    public TypeReference<ResultDto<TccBranchContext<TransferTccBranchRequest>>> getTccBranchContextTypeReference() {
        return new TypeReference<ResultDto<TccBranchContext<TransferTccBranchRequest>>>() {
        };
    }
}
