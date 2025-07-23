package com.itsoku.lesson038.service1.bus;

import cn.hutool.core.lang.TypeReference;
import com.itsoku.enums.TccBusTypeEnum;
import com.itsoku.lesson038.service1.dto.TransferTccBranchRequest;
import com.ms.dts.business.service.tcc.DefaultTccProcessor;
import com.ms.dts.tcc.branch.TccBranchContext;
import com.ms.dts.tcc.branch.TccBranchOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/16 21:24 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Component
public class TransferTccProcess extends DefaultTccProcessor<TransferTccBranchRequest> {

    @TccBranchOrder(1)
    @Autowired
    private TransferTccBranch1 transferTccBranch1;

    @TccBranchOrder(2)
    @Autowired
    private TransferTccBranch2Client transferTccBranch2;

    @Override
    protected TccBusTypeEnum getBusType() {
        return TccBusTypeEnum.TRANSFER;
    }

    @Override
    public TypeReference<TccBranchContext<TransferTccBranchRequest>> getTccRecordRequestDataTypeReference() {
        return new TypeReference<TccBranchContext<TransferTccBranchRequest>>() {
        };
    }
}
