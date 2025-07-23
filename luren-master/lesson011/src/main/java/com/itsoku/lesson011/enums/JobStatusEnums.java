package com.itsoku.lesson011.enums;

/**
 * job状态
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/3 0:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public enum JobStatusEnums {
    START(1, "启动"),
    STOP(0, "停止");
    private Integer status;
    private String description;

    JobStatusEnums(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 状态是否有效
     *
     * @param status
     * @return
     */
    public static boolean isValid(Integer status) {
        for (JobStatusEnums item : values()) {
            if (item.getStatus().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
