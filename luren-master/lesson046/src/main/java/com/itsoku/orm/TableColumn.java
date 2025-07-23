package com.itsoku.orm;

import lombok.Data;
import lombok.ToString;

import java.sql.JDBCType;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/9 19:29 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@ToString
public class TableColumn {
    /**
     * 列名
     */
    private String columnName;

    /**
     * The java.sql.Types type
     */
    private JDBCType jdbcType;

    /**
     * The sql typename. provided by JDBC driver
     *
     * @see JDBCType#getName()
     */
    private String jdbcTypeName;

    /**
     * java字段名称
     */
    private String javaName;

    /**
     * java类型
     */
    private Class<?> javaType;

    /**
     * 是否是主键？标注有@TableId注解，则为true
     *
     * @see com.itsoku.orm.annotation.TableId
     */
    private boolean isPk;

    /**
     * 主键类型
     */
    private IdType idType;

    /**
     * 是否是version字段，标注有@Version注解，则为true
     *
     * @see com.itsoku.orm.annotation.Version
     */
    private boolean isVersioned;
}
