package com.itsoku.lesson043.service;

import cn.hutool.core.lang.TypeReference;
import com.itsoku.lesson043.common.BusinessExceptionUtils;
import com.itsoku.lesson043.dto.TransferRequest;
import com.itsoku.lesson043.idempotent.DefaultIdempotentCall;
import com.itsoku.lesson043.idempotent.IdempotentCallRequest;
import com.itsoku.lesson043.idempotent.IdempotentCallResponse;
import com.itsoku.lesson043.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 23:11 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
public class AccountTransferService extends DefaultIdempotentCall<TransferRequest, Void> {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    protected IdempotentCallResponse<Void> disposeLocalBus(IdempotentCallRequest<TransferRequest> request) {
        TransferRequest transferRequest = request.getData();
        //付款方余额减少
        if (this.accountMapper.balanceSubtract(transferRequest.getFromAccountId(), transferRequest.getTransferPrice()) != 1) {
            throw BusinessExceptionUtils.businessException("付款方余额不足");
        }
        //收款方余额增加
        if (this.accountMapper.balanceAdd(transferRequest.getToAccountId(), transferRequest.getTransferPrice()) != 1) {
            throw BusinessExceptionUtils.businessException("更新收款方账户失败");
        }
        return IdempotentCallResponse.success(null);
    }

    @Override
    protected TypeReference<IdempotentCallResponse<Void>> responseType() {
        return new TypeReference<IdempotentCallResponse<Void>>() {
        };
    }
}
