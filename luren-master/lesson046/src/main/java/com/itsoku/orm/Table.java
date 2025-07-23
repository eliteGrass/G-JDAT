package com.itsoku.orm;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/9 19:28 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@ToString
public class Table {
    /**
     * 表名
     */
    private String tableName;
    /**
     * id字段
     */
    private TableColumn idColumn;
    /**
     * version字段
     */
    private TableColumn versionColumn;
    /**
     * 所有字段
     */
    private List<TableColumn> tableColumnList = new ArrayList<>();
    /**
     * 对应的po
     */
    private Class<?> poClass;

    private Map<String,TableColumn> javaNameRefColumnMap;
}
