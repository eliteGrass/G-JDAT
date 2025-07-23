package com.itsoku.orm;

import com.itsoku.lesson046.po.AccountPO;
import com.itsoku.orm.annotation.TableField;
import com.itsoku.orm.annotation.TableId;
import com.itsoku.orm.annotation.Temporal;
import com.itsoku.orm.annotation.Version;
import com.itsoku.utils.CollUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ORMUtils {
    private static final Set<Class> VERSION_TYPE = new HashSet<>();

    static {
        VERSION_TYPE.add(int.class);
        VERSION_TYPE.add(long.class);
        VERSION_TYPE.add(Integer.class);
        VERSION_TYPE.add(Long.class);
    }

    private static final Map<Class<?>, Table> tableMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        AccountPO accountPO = AccountPO.builder().version(1L).build();
        setNewVersion(accountPO);
    }

    public static void setNewVersion(Object po) {
        Class<?> poClass = po.getClass();
        Table table = get(poClass);
        TableColumn versionColumn = table.getVersionColumn();
        if (versionColumn == null) {
            throw new RuntimeException(poClass.getName() + "中缺少 @Version 字段");
        }
        Object version = ReflectionUtils.getValue(po, versionColumn.getJavaName());
        if (version instanceof Integer) {
            Integer newVersion = (Integer) version + 1;
            ReflectionUtils.setValue(po, versionColumn.getJavaName(), newVersion);
        } else if (version instanceof Long) {
            Long newVersion = (Long) version + 1;
            ReflectionUtils.setValue(po, versionColumn.getJavaName(), newVersion);
        }
    }

    public static Table get(Class<?> cls) {
        return tableMap.computeIfAbsent(cls, cs -> {
            com.itsoku.orm.annotation.Table tableAnno = cls.getAnnotation(com.itsoku.orm.annotation.Table.class);
            if (tableAnno == null) {
                throw new RuntimeException("目标类：" + cls.getName() + " 没有 @com.itsoku.orm.annotation.Table 注解");
            }
            String tableName = tableAnno.value();
            Table table = new Table();
            table.setPoClass(cs);
            table.setTableName(tableName);
            table.setTableColumnList(getTableColumnList(cls));
            table.setIdColumn(getIdColumn(table));
            table.setVersionColumn(getVersionColumn(table));
            table.setJavaNameRefColumnMap(CollUtils.convertMap(table.getTableColumnList(), TableColumn::getJavaName));
            return table;
        });
    }

    private static TableColumn getIdColumn(Table table) {
        return table.getTableColumnList().stream().filter(TableColumn::isPk).findFirst().orElse(null);
    }

    private static TableColumn getVersionColumn(Table table) {
        TableColumn versionColumn = table.getTableColumnList().stream().filter(TableColumn::isVersioned).findFirst().orElse(null);
        return versionColumn;
    }

    private static List<TableColumn> getTableColumnList(Class<?> cls) {
        List<TableColumn> tableColumnList = new ArrayList<>();
        List<Field> tableFiledList = ReflectionUtils.getDeclaredFields(cls);
        for (Field field : tableFiledList) {
            TableColumn tableColumn = getTableColumn(field);
            tableColumnList.add(tableColumn);
        }
        //必须有一个主键
        long pkCount = tableColumnList.stream().filter(column -> column.isPk()).count();
        if (pkCount != 1) {
            throw new RuntimeException("目标类：" + cls.getName() + "必须有一个 @" + TableId.class.getName() + " 注解");
        }
        //对字段排序下，主键放到最前面
        return tableColumnList.stream().sorted(Comparator.comparing(tableColumn -> tableColumn.isPk() ? -1 : 1)).collect(Collectors.toList());
    }

    private static TableColumn getTableColumn(Field field) {
        TableColumn tableColumn = new TableColumn();
        tableColumn.setJavaName(field.getName());
        tableColumn.setJavaType(field.getType());
        JDBCType jdbcType = getJdbcType(field);
        tableColumn.setJdbcType(jdbcType);
        tableColumn.setJdbcTypeName(jdbcType.getName());

        TableField tableFieldAnno = field.getAnnotation(TableField.class);
        if (tableFieldAnno != null) {
            tableColumn.setColumnName(tableFieldAnno.value());
        } else {
            tableColumn.setColumnName(ORMUtils.camelToUnderline(field.getName()));
        }

        TableId tableIdAnno = field.getAnnotation(TableId.class);
        if (tableIdAnno != null) {
            tableColumn.setPk(true);
            tableColumn.setIdType(tableIdAnno.type());
        }

        Version version = field.getAnnotation(Version.class);
        if (version != null) {
            if (!VERSION_TYPE.contains(tableColumn.getJavaType())) {
                throw new RuntimeException("@Version字段类型可选：" + VERSION_TYPE.stream().map(Class::getName).collect(Collectors.joining()));
            }
            tableColumn.setVersioned(true);
        }
        return tableColumn;
    }


    public static String camelToUnderline(String s) {
        if (s == null || "".equals(s.trim())) {
            return "";
        }
        int len = s.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append("_");
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String underlineToCamel(String s) {
        String[] strs = s.toLowerCase().split("_");
        StringBuilder result = new StringBuilder();
        String preStr = "";
        for (int i = 0; i < strs.length; i++) {
            if (preStr.length() == 1) {
                result.append(strs[i]);
            } else {
                result.append(StringUtils.capitalize(strs[i]));
            }
            preStr = strs[i];
        }
        return result.toString();
    }

    public static JDBCType getJdbcType(Field field) {
        Class<?> fieldType = field.getType();
        int dataType = DataType.getDataType(fieldType);
        if (DataType.isSimpleType(dataType)) {
            switch (dataType) {
                case DataType.DT_Byte:
                case DataType.DT_short:
                case DataType.DT_byte:
                    return JDBCType.TINYINT;
                case DataType.DT_int:
                case DataType.DT_Integer:
                    return JDBCType.INTEGER;
                case DataType.DT_Long:
                case DataType.DT_long:
                case DataType.DT_BigInteger:
                    return JDBCType.BIGINT;
                case DataType.DT_Double:
                case DataType.DT_double:
                    return JDBCType.DOUBLE;
                case DataType.DT_Float:
                case DataType.DT_float:
                    return JDBCType.FLOAT;
                case DataType.DT_Character:
                case DataType.DT_char:
                case DataType.DT_String:
                    return JDBCType.VARCHAR;
                case DataType.DT_Date:
                case DataType.DT_DateTime:
                case DataType.DT_Time:
                    Temporal temporal = field.getAnnotation(Temporal.class);
                    if (temporal != null) {
                        switch (temporal.value()) {
                            case DATE:
                                return JDBCType.DATE;
                            case TIME:
                                return JDBCType.TIME;
                            case TIMESTAMP:
                                return JDBCType.TIMESTAMP;
                            default:
                                return JDBCType.TIMESTAMP;
                        }
                    } else {
                        return JDBCType.TIMESTAMP;
                    }
                case DataType.DT_LocalDate:
                    return JDBCType.DATE;
                case DataType.DT_LocalTime:
                    return JDBCType.TIMESTAMP;
                case DataType.DT_LocalDateTime:
                    return JDBCType.TIMESTAMP;
                case DataType.DT_Boolean:
                case DataType.DT_boolean:
                    return JDBCType.TINYINT;
                case DataType.DT_BigDecimal:
                    return JDBCType.DECIMAL;
                default:
                    return JDBCType.VARCHAR;
            }
        } else if (dataType == DataType.DT_Enum) {
            return JDBCType.VARCHAR;
        } else {
            return JDBCType.BLOB;
        }
    }


}
