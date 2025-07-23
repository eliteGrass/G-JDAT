package com.itsoku.lesson047.tenant;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/12 11:45 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public final class TenantContextHolder {
    public static final ThreadLocal<String> TENANT_ID_TL = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        TENANT_ID_TL.set(tenantId);
    }

    public static void clean() {
        TENANT_ID_TL.remove();
    }

    public static String getCurrentTenantId() {
        return TENANT_ID_TL.get();
    }
}
