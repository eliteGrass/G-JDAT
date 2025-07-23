package com.itsoku.lesson038.service2.dto;

import com.ms.dts.tcc.branch.TccBranchRequest;
import lombok.*;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/16 21:25 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferTccBranchRequest extends TccBranchRequest {
    TransferRequest request;
}
