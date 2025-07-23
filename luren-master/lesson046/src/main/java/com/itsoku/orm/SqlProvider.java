package com.itsoku.orm;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * sql 提供者
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SqlProvider {
    private static Logger logger = LoggerFactory.getLogger(SqlProvider.class);
    public static final String BLANK = " ";
    private final AtomicInteger parameterIndex = new AtomicInteger(0);

    /**
     * 获取插入的sql
     *
     * @param parameterObject
     * @return
     */
    public String insert(Object parameterObject) {
        Object po = ((Map) parameterObject).get("po");
        Class<?> poClass = po.getClass();
        Table table = ORMUtils.get(poClass);

        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.insert(table.getTableName());

        Filters.NonNullFilter nonNullFilter = new Filters.NonNullFilter(po);
        List<TableColumn> tableColumnList = table.getTableColumnList().stream().filter(nonNullFilter).collect(Collectors.toList());
        int tableColumnSize = tableColumnList.size();

        for (int i = 0; i < tableColumnSize; i++) {
            TableColumn tableColumn = tableColumnList.get(i);
            sqlBuilder.values(tableColumn.getColumnName(), String.format("#{po.%s}", tableColumn.getJavaName()));
        }
        return sqlBuilder.toString();
    }

    /**
     * 获取批量插入的sql
     *
     * @param parameterObject
     * @return
     */
    public String insertBatch(Object parameterObject) {
        List<?> poList = (List<?>) ((Map) parameterObject).get("poList");
        Class<?> poClass = poList.get(0).getClass();
        Table table = ORMUtils.get(poClass);
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("<script>");
        sqlSb.append("insert into").append(BLANK);
        sqlSb.append(table.getTableName());

        List<TableColumn> tableColumnList = table.getTableColumnList();
        int tableColumnSize = tableColumnList.size();

        sqlSb.append("(");
        for (int i = 0; i < tableColumnSize; i++) {
            TableColumn tableColumn = tableColumnList.get(i);
            sqlSb.append(tableColumn.getColumnName());
            if (i < tableColumnSize - 1) {
                sqlSb.append(",");
            }
        }
        sqlSb.append(")").append(BLANK);

        sqlSb.append("values").append(BLANK);

        sqlSb.append("<foreach collection=\"poList\" separator=\",\" item=\"po\">");
        sqlSb.append("(");
        for (int i = 0; i < tableColumnSize; i++) {
            TableColumn tableColumn = tableColumnList.get(i);
            sqlSb.append(String.format("#{po.%s}", tableColumn.getJavaName()));
            if (i < tableColumnSize - 1) {
                sqlSb.append(",");
            }
        }
        sqlSb.append(")");
        sqlSb.append("</foreach>");
        sqlSb.append("</script>");
        return sqlSb.toString();
    }

    /**
     * 获取更新的sql
     *
     * @param po
     * @param setColumnPredicate
     * @param whereColumnPredicate
     * @return
     */
    public String update(@Param("po") Object po, @Param("setColumnPredicate") Predicate<TableColumn> setColumnPredicate, @Param("whereColumnPredicate") Predicate<TableColumn> whereColumnPredicate) {
        Class<?> poClass = po.getClass();
        Table table = ORMUtils.get(poClass);
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("update").append(BLANK);
        sqlSb.append(table.getTableName()).append(BLANK);
        sqlSb.append("set").append(BLANK);

        //更新的字段
        List<TableColumn> setTableColumnList = table.getTableColumnList().stream().filter(setColumnPredicate).collect(Collectors.toList());
        int setTableColumnSize = setTableColumnList.size();

        for (int i = 0; i < setTableColumnSize; i++) {
            TableColumn tableColumn = setTableColumnList.get(i);
            sqlSb.append(tableColumn.getColumnName()).append(BLANK).append("=").append(BLANK)
                    .append(String.format("#{po.%s}", tableColumn.getJavaName()));
            sqlSb.append(",");
        }

        //版本号
        TableColumn versionColumn = table.getVersionColumn();
        if (versionColumn != null) {
            sqlSb.append(versionColumn.getColumnName())
                    .append(BLANK).append("=").append(BLANK)
                    .append("version + 1");
        }
        if (sqlSb.toString().endsWith(",")) {
            sqlSb.delete(sqlSb.length() - 1, sqlSb.length());
        }

        // 更新条件
        List<TableColumn> whereTableColumnList = table.getTableColumnList().stream().filter(whereColumnPredicate).collect(Collectors.toList());
        int whereTableColumnSize = whereTableColumnList.size();

        if (whereTableColumnSize > 0) {
            sqlSb.append(BLANK).append("where").append(BLANK);
            for (int i = 0; i < whereTableColumnSize; i++) {
                TableColumn tableColumn = whereTableColumnList.get(i);
                sqlSb.append(tableColumn.getColumnName())
                        .append(BLANK).append("=").append(BLANK)
                        .append(String.format("#{po.%s}", tableColumn.getJavaName()));
                if (i < whereTableColumnSize - 1) {
                    sqlSb.append(BLANK).append("and").append(BLANK);
                }
            }
        }
        return sqlSb.toString();
    }

    /**
     * 获取删除的sql
     *
     * @param poClass
     * @param criteria
     * @param map
     * @param <T>
     * @return
     */
    public <T> String deleteByCriteria(@Param("poClass") Class<T> poClass, @Param("criteria") Criteria<T> criteria, @Param("map") Map<String, Object> map) {
        Table table = ORMUtils.get(poClass);
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.delete(table.getTableName());
        buildCondition(criteria, map, table, sqlBuilder);
        return sqlBuilder.toString();
    }

    /**
     * 获取查询的sql
     *
     * @param poClass
     * @param criteria
     * @param map
     * @param <T>
     * @return
     */
    public <T> String findByCriteria(@Param("poClass") Class<T> poClass, @Param("criteria") Criteria<T> criteria, @Param("map") Map<String, Object> map) {
        Table table = ORMUtils.get(poClass);
        SqlBuilder sqlBuilder = new SqlBuilder();
        //查询的列
        PropertySelector selector = criteria != null ? criteria.getSelector() : null;
        table.getTableColumnList().stream().filter(column -> {
            if (selector != null) {
                if (selector.getIncludes() != null) {
                    return selector.getIncludes().contains(column.getJavaName());
                } else if (selector.getExcludes() != null) {
                    return !selector.getExcludes().contains(column.getJavaName());
                }
            }
            return true;
        }).forEach(column -> sqlBuilder.select(String.format("%s as %s", column.getColumnName(), column.getJavaName())));

        sqlBuilder.from(table.getTableName());
        //查询条件
        buildCondition(criteria, map, table, sqlBuilder);
        //构建排序脚本
        buildOrder(criteria, table, sqlBuilder);
        return sqlBuilder.toString();
    }

    private <T> void buildOrder(Criteria<T> criteria, Table table, SqlBuilder sqlBuilder) {
        if (criteria != null && criteria.getSortProperties() != null && !criteria.getSortProperties().isEmpty()) {
            List<SortProperty> sortProperties = criteria.getSortProperties();
            for (SortProperty sortProperty : sortProperties) {
                String propertyName = sortProperty.getPropertyName();
                if (StringUtils.isBlank(propertyName)) {
                    continue;
                }
                TableColumn column = table.getJavaNameRefColumnMap().get(propertyName);
                if (column == null) {
                    throw new RuntimeException("property:" + propertyName + " no found in class:" + table.getPoClass().getName());
                }
                sqlBuilder.orderBy(column.getColumnName() + BLANK + sortProperty.getSort().name());
            }
        }
    }

    /**
     * 获取统计的sql
     *
     * @param poClass
     * @param criteria
     * @param map
     * @param <T>
     * @return
     */
    public <T> String countByCriteria(@Param("poClass") Class<T> poClass, @Param("criteria") Criteria<T> criteria, @Param("map") Map<String, Object> map) {
        Table table = ORMUtils.get(poClass);
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.count();
        sqlBuilder.from(table.getTableName());
        buildCondition(criteria, map, table, sqlBuilder);
        return sqlBuilder.toString();
    }

    private <T> void buildCondition(Criteria<T> criteria, Map<String, Object> map, Table table, SqlBuilder sqlBuilder) {
        if (criteria != null && criteria.getConditions() != null && !criteria.getConditions().isEmpty()) {
            List<Condition> conditions = criteria.getConditions();
            for (Condition condition : conditions) {
                buildCondition(table, map, sqlBuilder, condition);
            }
        }
    }

    public void buildCondition(Table table, Map<String, Object> parameterMap, SqlBuilder sqlBuilder, Condition condition) {
        if (condition.hasSubCondition()) {
            if (condition.getJoint() != null) {
                sqlBuilder.where(condition.getJoint().name() + SqlBuilder.BLANK + "(");
            } else {
                sqlBuilder.where("(");
            }
            for (Condition subCondition : condition.getSubConditions()) {
                buildCondition(table, parameterMap, sqlBuilder, subCondition);
            }
            sqlBuilder.where(")");
        } else {
            buildSingleCondition(table, parameterMap, sqlBuilder, condition);
        }
    }

    private void buildSingleCondition(Table table, Map<String, Object> parameterMap, SqlBuilder sqlBuilder, Condition condition) {
        if (!condition.validate()) {
            throw new RuntimeException("condition:" + condition + " is not valid");
        }
        String propertyName = condition.getPropertyName();
        String joint = (condition.getJoint() == null ? "" : condition.getJoint().name()) + BLANK;
        Object value = condition.getValue();
        TableColumn column = table.getJavaNameRefColumnMap().get(propertyName);
        if (column == null) {
            throw new RuntimeException("property:" + propertyName + " no found in class:" + table.getPoClass().getName());
        }
        String columnName = column.getColumnName();
        String jdbcType = column.getJdbcTypeName();
        int dataType = DataType.getDataType(column.getJavaType());

        switch (condition.getOperator()) {
            case beginWith:
            case notBeginWith:
                String beginWithParamName = generateParameterName();
                sqlBuilder.where(joint + columnName + BLANK + condition.getOperator().value() + BLANK + "#{map." + beginWithParamName + ",jdbcType=" + jdbcType + "}");
                parameterMap.put(beginWithParamName, value + "%");
                break;
            case endWith:
            case notEndWith:
                String endWithParamName = generateParameterName();
                sqlBuilder.where(joint + columnName + BLANK + condition.getOperator().value() + BLANK + "#{map." + endWithParamName + ",jdbcType=" + jdbcType + "}");
                parameterMap.put(endWithParamName, "%" + value);
                break;
            case contains:
            case notContains:
                String containsParameterName = generateParameterName();
                sqlBuilder.where(joint + columnName + BLANK + condition.getOperator().value() + BLANK + "#{map." + containsParameterName + ",jdbcType=" + jdbcType + "}");
                parameterMap.put(containsParameterName, "%" + value + "%");
                break;
            case between:
            case notBetween:
                String betweenParam1 = generateParameterName();
                String betweenParam2 = generateParameterName();
                sqlBuilder.where(joint + columnName + BLANK + condition.getOperator().value() + BLANK + "#{map." + betweenParam1 + ",jdbcType=" + jdbcType + "}" + BLANK
                        + "and" + BLANK + "#{map." + betweenParam2 + ",jdbcType=" + jdbcType + "}");
                if (value instanceof Collection) {
                    int i = 0;
                    for (Object v : (Collection<?>) value) {
                        if (i == 0) {
                            parameterMap.put(betweenParam1, DataType.toType(v, DataType.getDataType(v.getClass()), dataType));
                        }
                        if (i == 1) {
                            parameterMap.put(betweenParam2, DataType.toType(v, DataType.getDataType(v.getClass()), dataType));
                        }
                        i++;
                    }
                } else if (value instanceof String) {
                    String[] betweenValues = StringUtils.split((String) value, ",");
                    parameterMap.put(betweenParam1, DataType.toType(betweenValues[0], DataType.DT_String, dataType));
                    if (betweenValues.length > 1) {
                        parameterMap.put(betweenParam2, DataType.toType(betweenValues[1], DataType.DT_String, dataType));
                    } else {
                        parameterMap.put(betweenParam2, null);
                    }
                } else if (value.getClass().isArray()) {
                    Object[] values = (Object[]) value;
                    for (int i = 0; i < values.length; i++) {
                        Object v = values[i];
                        if (i == 0) {
                            parameterMap.put(betweenParam1, DataType.toType(v, DataType.getDataType(v.getClass()), dataType));
                        }
                        if (i == 1) {
                            parameterMap.put(betweenParam2, DataType.toType(v, DataType.getDataType(v.getClass()), dataType));
                        }
                    }
                }
                break;
            case blank:
            case notBlank:
            case isNull:
            case isNotNull:
                sqlBuilder.where(joint + columnName + BLANK + condition.getOperator().value());
                break;
            case equal:
            case notEqual:
            case lessEqual:
            case lessThan:
            case greaterEqual:
            case greaterThan:
                String eqParamName = generateParameterName();
                sqlBuilder.where(joint + columnName + BLANK + condition.getOperator().value() + BLANK + "#{map." + eqParamName + ",jdbcType=" + jdbcType + "}");
                if (DataType.isEnum(column.getJavaType())) {
                    parameterMap.put(eqParamName, String.valueOf(value));
                } else {
                    parameterMap.put(eqParamName, DataType.toType(value, dataType));
                }
                break;
            case in:
            case notIn:
                if (value instanceof Collection) {
                    Collection<?> collectionValue = (Collection<?>) value;
                    StringBuilder builder = new StringBuilder();
                    builder.append(columnName).append(BLANK).append(condition.getOperator().value());
                    builder.append(BLANK);
                    builder.append("(");
                    for (Object inValue : collectionValue) {
                        String collectionParamName = generateParameterName();
                        builder.append("#{map.").append(collectionParamName).append(",jdbcType=").append(jdbcType).append("}").append(",");
                        parameterMap.put(collectionParamName, inValue);
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    builder.append(")");
                    sqlBuilder.where(joint + builder);
                } else if (value instanceof String) {
                    String[] inValues = StringUtils.split((String) value, ",");
                    StringBuilder builder = new StringBuilder();
                    builder.append(columnName).append(BLANK).append(condition.getOperator().value());
                    builder.append(BLANK);
                    builder.append("(");
                    for (int i = 0; i < inValues.length; i++) {
                        String inValue = inValues[i];
                        String arrayParameterName = generateParameterName();
                        builder.append("#{map.").append(arrayParameterName).append(",jdbcType=").append(jdbcType).append("}").append(",");
                        parameterMap.put(arrayParameterName, inValue);
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    builder.append(")");
                    sqlBuilder.where(joint + builder);
                } else if (value.getClass().isArray()) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(columnName).append(BLANK).append(condition.getOperator().value());
                    builder.append(BLANK);
                    builder.append("(");
                    Object[] values = (Object[]) value;
                    for (int i = 0; i < values.length; i++) {
                        Object inValue = values[i];
                        String inParmaName = generateParameterName();
                        builder.append("#{map.").append(inParmaName).append(",jdbcType=").append(jdbcType).append("}").append(",");
                        parameterMap.put(inParmaName, inValue);
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    builder.append(")");
                    sqlBuilder.where(joint + builder);
                }
                break;
            case custom:
                sqlBuilder.where(joint + columnName + BLANK + value);
                break;
            default:
                break;
        }
    }

    private String generateParameterName() {
        return "p_" + parameterIndex.getAndIncrement();
    }
}
