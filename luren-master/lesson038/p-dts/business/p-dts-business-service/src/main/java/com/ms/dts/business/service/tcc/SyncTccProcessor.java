package com.ms.dts.business.service.tcc;

import com.ms.dts.tcc.branch.ITccBranchRequest;

/**
 * <b>description</b>：同步事务处理器，继承这个处理器可以获取到tcc最终的结果 <br>
 * <b>time</b>：2024/4/25 12:21 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public abstract class SyncTccProcessor<T extends ITccBranchRequest> extends DefaultTccProcessor<T> {
    @Override
    protected boolean isSync(T request) {
        return true;
    }

    @Override
    protected boolean isSyncUploadResult(T request) {
        return true;
    }
}
