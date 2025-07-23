package com.itsoku.orm;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/9 22:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SqlBuilder {
    public static final String BLANK = " ";
    private static final int UNSET = -1;
    private String table;
    private final List<String> selectList = new ArrayList<>();
    private final List<String> whereList = new ArrayList<>();
    private final List<String> orderByList = new ArrayList<>();
    private final List<String> updateList = new ArrayList<>();
    private final List<String> insertColumnList = new ArrayList<>();
    private final List<String> insertParamList = new ArrayList<>();
    private int limit = UNSET;
    private int offset = UNSET;
    private StatementType statementType;

    public SqlBuilder() {
    }

    public SqlBuilder(StatementType statementType) {
        this.statementType = statementType;
    }

    public enum StatementType {
        /**
         * 新增语句
         */
        insert,
        /**
         * 删除语句
         */
        delete,
        /**
         * 查询语句
         */
        select,
        /**
         * 更新语句
         */
        update,
        /**
         * 统计语句
         */
        count
    }

    public SqlBuilder select(String... selects) {
        this.statementType = StatementType.select;
        for (String select : selects) {
            selectList.add(select);
        }
        return this;
    }

    public SqlBuilder count() {
        this.statementType = StatementType.count;
        return this;
    }

    public SqlBuilder from(String fromClause) {
        this.table = fromClause;
        return this;
    }

    public SqlBuilder insert(String table) {
        this.statementType = StatementType.insert;
        this.table = table;
        return this;
    }

    public SqlBuilder values(String column, String param) {
        this.insertColumnList.add(column);
        this.insertParamList.add(param);
        return this;
    }

    public SqlBuilder clearSelect() {
        this.selectList.clear();
        return this;
    }

    public SqlBuilder clearOrder() {
        this.orderByList.clear();
        return this;
    }

    public SqlBuilder where(String clause) {
        this.whereList.add(clause);
        return this;
    }

    public SqlBuilder orderBy(String order) {
        this.orderByList.add(order);
        return this;
    }

    public SqlBuilder update(String tableName) {
        this.table = tableName;
        this.statementType = StatementType.update;
        return this;
    }

    public SqlBuilder set(String... columns) {
        for (String column : columns) {
            this.updateList.add(column);
        }
        return this;
    }

    public SqlBuilder delete(String tableName) {
        this.table = BLANK + tableName + BLANK;
        this.statementType = StatementType.delete;
        return this;
    }

    public SqlBuilder limit(int n) {
        this.limit = n;
        return this;
    }

    public SqlBuilder offset(int n) {
        this.offset = n;
        return this;
    }

    public List<String> getUpdateList() {
        return updateList;
    }

    public SqlBuilder copy() {
        SqlBuilder builder = new SqlBuilder();
        builder.table = this.table;
        builder.selectList.addAll(selectList);
        builder.whereList.addAll(whereList);
        builder.orderByList.addAll(orderByList);
        builder.updateList.addAll(updateList);
        builder.statementType = this.statementType;
        builder.insertColumnList.addAll(this.insertColumnList);
        builder.insertParamList.addAll(this.insertParamList);
        return builder;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        switch (statementType) {
            case insert:
                builder.append("insert into").append(BLANK).append(table).append(BLANK).append("(");
                boolean first = true;
                for (String s : insertColumnList) {
                    if (!first) {
                        builder.append(BLANK);
                        first = false;
                    }
                    builder.append(s).append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append(")").append(BLANK).append("values").append(BLANK).append("(");
                first = true;
                for (String s : insertParamList) {
                    if (!first) {
                        builder.append(BLANK);
                        first = false;
                    }
                    builder.append(s).append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append(")");
                break;
            case delete:
                builder.append("delete from").append(BLANK).append(table).append(BLANK);
                buildWhereClause(builder);
                break;
            case select:
                builder.append("select").append(BLANK);
                for (int i = 0; i < selectList.size(); i++) {
                    String select = selectList.get(i);
                    if (i > 0) {
                        builder.append(",");
                    }
                    builder.append(select);
                }
                builder.append(BLANK).append("from").append(BLANK).append(table).append(BLANK);
                buildWhereClause(builder);
                if (CollectionUtils.isNotEmpty(orderByList)) {
                    builder.append(BLANK).append("order by").append(BLANK);
                    builder.append(String.join(",", orderByList));
                }
                if (this.limit != UNSET) {
                    builder.append(BLANK).append("limit").append(BLANK).append(limit);
                }
                if (this.offset != UNSET) {
                    builder.append(BLANK).append("offset").append(BLANK).append(offset);
                }
                break;
            case count:
                builder.append("select").append(BLANK);
                builder.append("count(1)");
                builder.append(BLANK).append("from").append(BLANK).append(table).append(BLANK);
                buildWhereClause(builder);
                if (CollectionUtils.isNotEmpty(orderByList)) {
                    builder.append(BLANK).append("order by").append(BLANK);
                    builder.append(String.join(",", orderByList));
                }
                if (this.limit != UNSET) {
                    builder.append(BLANK).append("limit").append(BLANK).append(limit);
                }
                if (this.offset != UNSET) {
                    builder.append(BLANK).append("offset").append(BLANK).append(offset);
                }
                break;
            case update:
                builder.append("update").append(BLANK).append(table).append(BLANK).append("set").append(BLANK);
                for (int i = 0; i < updateList.size(); i++) {
                    String update = updateList.get(i);
                    if (i > 0) {
                        builder.append(",");
                    }
                    builder.append(update);
                }
                buildWhereClause(builder);
                break;
            default:
                break;
        }

        return builder.toString();
    }

    private void buildWhereClause(StringBuilder sql) {
        if (CollectionUtils.isNotEmpty(whereList)) {
            sql.append(BLANK).append("where").append(BLANK);
            for (int i = 0; i < whereList.size(); i++) {
                String where = whereList.get(i);
                if (i == 0) {
                    String whereToInsert = where.trim();
                    if (StringUtils.startsWithIgnoreCase(whereToInsert, "and")) {
                        sql.append(BLANK).append(whereToInsert.substring(3));
                    } else if (StringUtils.startsWithIgnoreCase(whereToInsert, "or")) {
                        sql.append(BLANK).append(whereToInsert.substring(2));
                    } else {
                        sql.append(BLANK).append(where);
                    }
                } else {
                    String previous = whereList.get(i - 1);
                    String previousTrim = previous.trim();
                    if (StringUtils.endsWithIgnoreCase(previousTrim, "(") || StringUtils.endsWithIgnoreCase(previousTrim, "where")) {
                        String whereToTrim = where.trim();
                        if (StringUtils.startsWithIgnoreCase(whereToTrim, "and")) {
                            sql.append(BLANK).append(whereToTrim.substring(3));
                        } else if (StringUtils.startsWithIgnoreCase(whereToTrim, "or")) {
                            sql.append(BLANK).append(whereToTrim.substring(2));
                        } else if (StringUtils.equalsIgnoreCase(whereToTrim, ")")) {
                            sql.append(BLANK).append("1=1").append(BLANK).append(where);
                        } else {
                            sql.append(BLANK).append(where);
                        }
                    } else {
                        sql.append(BLANK).append(where);
                    }
                }
            }
        }
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public List<String> getSelectList() {
        return selectList;
    }

    public String getTable() {
        return table;
    }

    public List<String> getWhereList() {
        return whereList;
    }

    public List<String> getOrderByList() {
        return orderByList;
    }

    public List<String> getInsertColumnList() {
        return insertColumnList;
    }

    public List<String> getInsertParamList() {
        return insertParamList;
    }

}
