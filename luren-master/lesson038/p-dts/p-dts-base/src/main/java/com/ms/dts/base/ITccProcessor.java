package com.ms.dts.base;

import com.itsoku.common.ResultDto;
import com.ms.dts.tcc.branch.ITccBranchRequest;
import com.ms.dts.tcc.branch.TccBranchContext;
import com.ms.dts.tcc.dto.TccProcessorRequestDto;
import com.ms.dts.tcc.dto.TccProcessorResponseDto;

/**
 * <b>description</b>：tcc事务处理器，内部包含业务处理的入口，tcc事务补偿的入口 <br>
 * <b>time</b>：2018-08-07 19:09:08 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccProcessor<T extends ITccBranchRequest> {
    /**
     * 业务处理，整个业务处理的入口，客户端调用当前方法进行业务处理
     *
     * @param request
     * @return
     * @throws Exception
     */
    ResultDto<TccBranchContext<T>> dispose(T request);

    /**
     * 补偿，对为完毕的tcc事务订单进行补偿操作，由tcc事务服务自动调用
     *
     * @param request
     * @return
     * @throws Exception
     */
    ResultDto<TccProcessorResponseDto> recover(TccProcessorRequestDto request);
}
