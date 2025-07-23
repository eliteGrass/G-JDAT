package com.itsoku.lesson024.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:43 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@TableName("t_oper_log_lesson024")
@Data
public class OperLogPO {
    /**
     * id
     */
    private String id;

    /**
     * 操作日志内容
     */
    private String log;

    /**
     * 状态，0：异常，1：正常
     */
    private Integer status;

    /**
     * 请求参数json
     */
    private String paramJson;

    /**
     * 响应结果json
     */
    private String resultJson;

    /**
     * 错误信息(status=0时，记录错误信息)
     */
    private String errorMsg;

    /**
     * 耗时（毫秒）
     */
    private Long costTime;

    /**
     * 操作ip地址
     */
    private String operIp;

    /**
     * 操作ip地址位置
     */
    private String operIpAddress;

    /**
     * 操作人名称
     */
    private String operUserName;

    /**
     * 操作时间
     */
    private LocalDateTime operTime;
}
