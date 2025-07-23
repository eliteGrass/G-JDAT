package com.itsoku.orm;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public class DataType {
    /**
     * basic types
     */
    public static final int DT_Unknown = 0;
    public static final int DT_byte = 1;
    public static final int DT_short = 2;
    public static final int DT_int = 3;
    public static final int DT_long = 4;
    public static final int DT_float = 5;
    public static final int DT_double = 6;
    public static final int DT_char = 7;
    public static final int DT_boolean = 8;
    public static final int DT_Byte = 10;
    public static final int DT_Short = 11;
    public static final int DT_Integer = 12;
    public static final int DT_Long = 13;
    public static final int DT_Float = 14;
    public static final int DT_Double = 15;
    public static final int DT_Character = 16;
    public static final int DT_Boolean = 17;
    public static final int DT_String = 20;
    public static final int DT_BigInteger = 21;
    public static final int DT_BigDecimal = 22;
    public static final int DT_Date = 23;
    public static final int DT_Time = 24;
    public static final int DT_DateTime = 25;
    public static final int DT_LocalTime = 60;
    public static final int DT_LocalDate = 61;
    public static final int DT_LocalDateTime = 62;
    public static final int DT_ZoneDateTime = 63;

    /**
     * sql types
     */
    public static final int DT_Clob = 26;
    public static final int DT_Blob = 27;

    /**
     * collection types
     */
    public static final int DT_Array = 30;
    public static final int DT_List = 31;
    public static final int DT_Map = 34;
    public static final int DT_Set = 37;

    /**
     * object types
     */
    public static final int DT_Object = 40;
    public static final int DT_Class = 41;
    public static final int DT_Enum = 42;
    public static final int DT_UserDefine = 50;
    private static final Map<String, Integer> dataTypeMap = new HashMap<>();

    public static final DateTimeFormatter yyyyMMddHHmmssSSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter yyyyMMddHHmmssSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
    public static final DateTimeFormatter yyyyMMddHHmmssS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    public static final DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter yyyyMMddHHmm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter yyyyMMddHH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
    public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        dataTypeMap.put("byte", DT_byte);
        dataTypeMap.put("Byte", DT_Byte);
        dataTypeMap.put("short", DT_short);
        dataTypeMap.put("Short", DT_Short);
        dataTypeMap.put("int", DT_int);
        dataTypeMap.put("Integer", DT_Integer);
        dataTypeMap.put("long", DT_long);
        dataTypeMap.put("Long", DT_Long);
        dataTypeMap.put("boolean", DT_boolean);
        dataTypeMap.put("Boolean", DT_Boolean);
        dataTypeMap.put("char", DT_char);
        dataTypeMap.put("Character", DT_Character);
        dataTypeMap.put("float", DT_float);
        dataTypeMap.put("Float", DT_Float);
        dataTypeMap.put("double", DT_double);
        dataTypeMap.put("Double", DT_Double);
        dataTypeMap.put("BigInteger", DT_BigInteger);
        dataTypeMap.put("BigDecimal", DT_BigDecimal);
        dataTypeMap.put("String", DT_String);
        dataTypeMap.put("Date", DT_Date);
        dataTypeMap.put("Time", DT_Time);
        dataTypeMap.put("Timestamp", DT_DateTime);
        dataTypeMap.put("LocalDateTime", DT_LocalDateTime);
        dataTypeMap.put("LocalDate", DT_LocalDate);
        dataTypeMap.put("LocalTime", DT_LocalTime);
        dataTypeMap.put("ZoneDateTime", DT_ZoneDateTime);
        dataTypeMap.put("List", DT_List);
        dataTypeMap.put("ArrayList", DT_List);
        dataTypeMap.put("LinkedList", DT_List);
        dataTypeMap.put("Map", DT_Map);
        dataTypeMap.put("HashMap", DT_Map);
        dataTypeMap.put("Hashtable", DT_Map);
        dataTypeMap.put("Set", DT_Set);
        dataTypeMap.put("HashSet", DT_Set);
        dataTypeMap.put("Object", DT_Object);
        dataTypeMap.put("Class", DT_Class);
        dataTypeMap.put("clob", DT_Clob);
        dataTypeMap.put("blob", DT_Blob);
    }

    private static String deletePrefix(String str, String prefix) {
        if (str.startsWith(prefix)) {
            str = str.substring(prefix.length());
        }
        return str;
    }

    public static String toSimpleType(String typeName) {
        typeName = deletePrefix(typeName, "class ");
        typeName = deletePrefix(typeName, "java.lang.");
        typeName = deletePrefix(typeName, "java.util.");
        typeName = deletePrefix(typeName, "java.sql.");
        typeName = deletePrefix(typeName, "java.math.");
        //增加对localDateTime的支持
        typeName = deletePrefix(typeName, "java.time.");
        return typeName;
    }

    public static int getDataType(Object obj) {
        if (obj == null) {
            return DT_Unknown;
        }
        return getDataType(obj.getClass());
    }

    @SuppressWarnings("unchecked")
    public static int getDataType(Class<?> cls) {
        if (cls == null) {
            return DT_Unknown;
        }
        if (cls.isArray()) {
            return DT_Array;
        }
        if (isEnum(cls)) {
            return DT_Enum;
        }
        String typeName = toSimpleType(cls.getName());
        if (typeName.charAt(0) == '[') {
            return DT_Array;
        }
        Integer iType = dataTypeMap.get(typeName);
        return iType == null ? DT_UserDefine : iType;
    }

    public static boolean isEnum(Class<?> cls) {
        if (cls.isEnum()) {
            return true;
        }
        //TODO enum 类型需要递归向上判断
        Type superclass = cls.getGenericSuperclass();
        if (superclass != null) {
            if (superclass instanceof Class<?>) {
                if (((Class<?>) superclass).isEnum()) {
                    return true;
                }
                return isEnum((Class<?>) superclass);
            }
        }
        return false;
    }

    public static String toUnifyTypeName(String sName) {
        return matchBracket(sName, "<", ">", false);
    }

    public static String getElementTypeName(String collectionTypeName) {
        return getElementTypeName(collectionTypeName, 0);
    }

    public static String getElementTypeName(String collectionTypeName,
                                            int itemIndex) {
        String typeName = toSimpleType(collectionTypeName);
        int iType = getDataType(typeName);
        if (iType == DT_Array) {
            switch (typeName.charAt(1)) {
                case 'B': // byte[]
                    return "byte";
                case 'S': // short[]
                    return "short";
                case 'I': // int[]
                    return "int";
                case 'J': // long[]
                    return "long";
                case 'Z': // boolean[]
                    return "boolean";
                case 'C': // char[]
                    return "char";
                case 'F': // float[]
                    return "float";
                case 'D': // double[]
                    return "double";
                case 'L': // [Ljava.lang.Integer;
                    if (typeName.charAt(typeName.length() - 1) == ';') {
                        return typeName.substring(2, typeName.length() - 1);
                    } else {
                        return typeName.substring(2);
                    }
                case '[': // [[I
                    return typeName.substring(1);
                default:
                    break;
            }
        }

        String str = matchBracket(typeName, "<", ">", true);
        int iLen = str.length();
        for (int i = 0; i <= itemIndex; i++) {
            str = matchBracket(str, "<", ">", false);
            int iLen1 = str.length();
            if (iLen1 == iLen) { // 没有可以切除的部分了
                break;
            } else {
                iLen = iLen1;
            }
        }

        // 需找逗号分隔符
        int iBegin = 0, iEnd = str.length();
        int iPos = 0;
        for (int i = 0; i < itemIndex + 1; i++) {
            iPos = str.indexOf(',', iPos);
            if (iPos == -1) {
                break;
            }

            if (i == itemIndex - 1) {
                iBegin = iPos;
            } else if (i == itemIndex) {
                iEnd = iPos;
            }
        }

        return str.substring(iBegin + 1, iEnd);
    }

    public static int getElementDataType(String collectionTypeName) {
        return getElementDataType(collectionTypeName, 0);
    }

    public static int getElementDataType(String collectionTypeName,
                                         int itemIndex) {
        return getDataType(getElementTypeName(collectionTypeName, itemIndex));
    }
    // public static final int DT_HashSet = 38;

    public static Object toType(Object value, String targetType) {
        int destType = getDataType(targetType);
        return toType(value, destType);
    }

    public static Object toType(Object value, int targetType) {
        int srcType = getDataType(value);
        return toType(value, srcType, targetType);
    }

    public static Object toType(Object value, int srcType, int targetType) {
        srcType = toObjectType(srcType);
        targetType = toObjectType(targetType);
        if (srcType == targetType) {
            return value;
        }

        if (value == null) {
            return null;
        }

        Object retObj = null;
        if (srcType == DT_String) {
            String str = ((String) value).trim();
            if (str.length() < 1 || str.equalsIgnoreCase("null")) {
                return null;
            }
        }

        if (srcType >= DT_byte && srcType <= DT_boolean) {
            srcType += DT_Byte - DT_byte;
        }
        if (targetType >= DT_byte && targetType <= DT_boolean) {
            targetType += DT_Byte - DT_byte;
        }
        //大小类型转换
        if (srcType == targetType) {
            return value;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            switch (targetType) {
                case DT_Byte:
                    retObj = convertToByte(value, srcType);
                    break;
                case DT_Short:
                    retObj = convertToShort(value, srcType);
                    break;
                case DT_Integer:
                    retObj = convertToInteger(value, srcType);
                    break;
                case DT_Long:
                    retObj = convertToLong(value, srcType);
                    break;
                case DT_BigInteger:
                    retObj = convertToBigInteger(value, srcType);
                    break;
                case DT_Float:
                    retObj = convertToFloat(value, srcType);
                    break;
                case DT_BigDecimal:
                    retObj = convertToBigDecimal(value, srcType);
                    break;
                case DT_Double:
                    retObj = convertToDouble(value, srcType);
                    break;
                case DT_Character:
                    retObj = convert2Character(value, srcType);
                    break;
                case DT_Boolean:
                    retObj = convert2Boolean(value, srcType);
                    break;
                case DT_String:
                    retObj = convert2String(value, srcType);
                    break;
                case DT_Date:
                    retObj = convert2Date(value, srcType);
                    break;
                case DT_Time:
                    switch (srcType) {
                        case DT_String:
                            // sdf.applyPattern("HH:mm:ss");
                            // date = sdf.parse((String)value);
                            // retObj = new java.sql.Time(date.getTime());
                            retObj = java.sql.Time.valueOf((String) value);
                            break;
                        case DT_DateTime:
                        case DT_Long:
                        case DT_Integer:
                            Calendar cal = Calendar.getInstance();
                            if (srcType == DT_DateTime) {
                                cal.setTime((Date) value);
                            } else if (srcType == DT_Long) {
                                cal.setTimeInMillis((Long) value);
                            } else {
                                cal.setTimeInMillis((Integer) value);
                            }
                            cal.set(Calendar.YEAR, 0);
                            cal.set(Calendar.MONTH, 0);
                            cal.set(Calendar.DAY_OF_MONTH, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            retObj = new java.sql.Time(cal.getTimeInMillis());
                            break;
                        default:
                            log.debug("can't convert type:" + srcType + " to Time");
                            break;
                    }
                    break;
                case DT_DateTime:
                    switch (srcType) {
                        case DT_String:
                            // date = sdf.parse((String)value);
                            // retObj = new java.sql.Timestamp(date.getTime());
                            retObj = java.sql.Timestamp.valueOf((String) value);
                            break;
                        case DT_Date:
                        case DT_Time:
                        case DT_Long:
                        case DT_Integer:
                            retObj = new java.sql.Timestamp(Long.valueOf(String.valueOf(value)));
                            break;
                        default:
                            log.debug("can't convert type:" + srcType + " to DateTime");
                            break;
                    }
                    break;
                case DT_LocalDate:
                    retObj = convert2LocalDate(srcType, value);
                    break;
                case DT_LocalTime:
                    retObj = convert2LocalTime(srcType, value);
                    break;
                case DT_LocalDateTime:
                    retObj = convert2LocalDateTime(srcType, value);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retObj;
    }

    /**
     * converts a given value to a Character type based on the source type provided as an integer.
     * It uses the Character.toChars() method to convert the value to a character array and returns the first element of the array as a Character type.
     * The source types supported are Byte, Short, Integer, Long, BigInteger, Float, Double, BigDecimal, String, and Boolean. If the source type is not supported, it returns null.
     *
     * @param value   the value to be converted
     * @param srcType the source type of the value
     * @return the converted value as a Character type
     */
    private static Character convert2Character(Object value, int srcType) {
        switch (srcType) {
            case DT_Byte:
                return Character.toChars((Byte) value)[0];
            case DT_Short:
                return Character.toChars((Short) value)[0];
            case DT_Integer:
                return Character.toChars((Integer) value)[0];
            case DT_Long:
                return Character.toChars(((Long) value).intValue())[0];
            case DT_BigInteger:
                return Character.toChars(((BigInteger) value).intValue())[0];
            case DT_Float:
                return Character.toChars(((Float) value).intValue())[0];
            case DT_Double:
                return Character.toChars(((Double) value).intValue())[0];
            case DT_BigDecimal:
                return Character.toChars(((BigDecimal) value).intValue())[0];
            case DT_String:
                return ((String) value).charAt(0);
            case DT_Boolean:
                return (Boolean) value ? 'T' : 'F';
            default:
                return null;
        }
    }

    /**
     * converts the object value to a string based on the source type using a switch statement.
     * There are cases for various data types such as byte, short, integer, long, big integer, float, double, big decimal, character, and boolean, but they are currently commented out.
     * The cases that are currently implemented are for enum, date, time, and date time. For date and time conversions, the method uses SimpleDateFormat to format the date or time in a specific way.
     * If the source type is not recognized, the method simply returns the object value as a string.
     *
     * @param value   the value to be converted
     * @param srcType the source type of the value
     * @return the converted value as a string
     */
    private static String convert2String(Object value, int srcType) {
        switch (srcType) {
//            case DT_Byte:
//                return  ((Byte) value).toString();
//            case DT_Short:
//                return ((Short) value).toString();
//            case DT_Integer:
//                return  ((Integer) value).toString();
//            case DT_Long:
//                return  ((Long) value).toString();
//            case DT_BigInteger:
//                return  ((BigInteger) value).toString();
//            case DT_Float:
//                return  ((Float) value).toString();
//            case DT_Double:
//                return  ((Double) value).toString();
//            case DT_BigDecimal:
//                return  ((BigDecimal) value).toString();
//            case DT_Character:
//                return  ((Character) value).toString();
//            case DT_Boolean:
//                return  ((Boolean) value).toString();
            case DT_Enum:
                return ((Enum<?>) value).name();
            case DT_Date:
                SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
//                if (value instanceof java.sql.Date) {
//                    sdf.applyPattern("yyyy-MM-dd");
//                }
                return dateFmt.format((Date) value);
            case DT_Time:
                SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm:ss");
                return timeFmt.format((Date) value);
            case DT_DateTime:
                SimpleDateFormat dateTimeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return dateTimeFmt.format((Date) value);
            default:
                return value.toString();
        }
    }

    /**
     * converts an input value of various data types to a Boolean data type.
     *
     * @param value   the value to be converted
     * @param srcType the source type of the value
     * @return the converted value as a Boolean type
     */
    private static Boolean convert2Boolean(Object value, int srcType) {
        switch (srcType) {
            case DT_Byte:
                return (Byte) value != 0;
            case DT_Short:
                return (Short) value != 0;
            case DT_Integer:
                return (Integer) value != 0;
            case DT_Long:
                return (Long) value != 0;
            case DT_BigInteger:
                return ((BigInteger) value).longValue() != 0;
            case DT_Float:
                return ((Float) value).intValue() != 0;
            case DT_Double:
                return ((Double) value).intValue() != 0;
            case DT_BigDecimal:
                return ((BigDecimal) value).longValue() != 0;
            case DT_Character:
                return (Character) value == 'T';
            case DT_String:
                String strValue = (String) value;
                if (strValue.equalsIgnoreCase("true")) {
                    return true;
                } else if (strValue.equalsIgnoreCase("t")) {
                    return true;
                } else if (strValue.equalsIgnoreCase("yes")) {
                    return true;
                } else if (strValue.equalsIgnoreCase("y")) {
                    return true;
                } else {
                    return strValue.equalsIgnoreCase("是");
                }
            default:
                return null;
        }
    }

    private static String matchBracket(String str, String matchBegin,
                                       String matchEnd, boolean bGet) {
        int iLen = str.length();
        int iMatchLen = matchBegin.length();
        char cBegin = '[', cEnd = ']';
        int iBeginPos = -1, iEndPos = -1;
        boolean bFindMatch = false;
        int iCount = -1;
        for (int i = 0; i < iLen; i++) {
            char ch = str.charAt(i);
            if (bFindMatch) {
                if (ch == cBegin) {
                    iCount++;
                } else if (ch == cEnd) {
                    iCount--;
                    if (iCount == 0) {
                        iEndPos = i;
                        break;
                    }
                }
            } else {
                for (int k = 0; k < iMatchLen; k++) {
                    cBegin = matchBegin.charAt(k);
                    if (ch == cBegin) {
                        bFindMatch = true;
                        cEnd = matchEnd.charAt(k);
                        iBeginPos = i;
                        iCount = 1;
                        break;
                    }
                }
            }
        }

        if (bFindMatch) { // 找到匹配
            if (bGet) { // 截取匹配括号中的子串
                return str.substring(iBeginPos + 1, iEndPos);
            } else { // 切除匹配括号中的字串
                return str.substring(0, iBeginPos) + str.substring(iEndPos + 1);
            }
        } else { // 未找到匹配
            if (bGet) { // 截取匹配括号中的子串
                return "";
            } else { // 切除匹配括号中的字串
                return str;
            }
        }
    }

    public static boolean isSimpleType(int iType) {
        switch (iType) {
            case DT_byte:
            case DT_short:
            case DT_int:
            case DT_long:
            case DT_float:
            case DT_double:
            case DT_char:
            case DT_boolean:

            case DT_Byte:
            case DT_Short:
            case DT_Integer:
            case DT_Long:
            case DT_BigInteger:
            case DT_Float:
            case DT_Double:
            case DT_BigDecimal:
            case DT_Character:
            case DT_String:
            case DT_Boolean:
            case DT_Date:
            case DT_Time:
            case DT_DateTime:
            case DT_LocalDate:
            case DT_LocalTime:
            case DT_LocalDateTime:
            case DT_ZoneDateTime:
            case DT_Clob:
            case DT_Blob:
                return true;
            default:
                return false;
        }
    }

    public static int toObjectType(int iType) {
        if (iType >= DT_byte && iType <= DT_boolean) {
            iType += DT_Byte - DT_byte;
        }
        return iType;
    }

    /**
     * 判断一个类型是不是集合类型
     *
     * @param dataType 数据类型
     * @return true 集合类型，false 非集合类型
     */
    public static boolean isCollectionType(int dataType) {
        switch (dataType) {
            case DT_Array:
            case DT_List:
            case DT_Set:
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断属性是不是Map类型
     *
     * @param dataType 数据分类
     * @return true：map类型，false非map类型
     */
    public static boolean isMapType(int dataType) {
        return dataType == DT_Map;
    }

    /**
     * converts an input value of various data types to a Byte data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return Byte
     */
    private static Byte convertToByte(Object value, int srcType) {
        switch (srcType) {
            case DT_Short:
                return ((Short) value).byteValue();
            case DT_Integer:
                return ((Integer) value).byteValue();
            case DT_Long:
                return ((Long) value).byteValue();
            case DT_BigInteger:
                return ((BigInteger) value).byteValue();
            case DT_Float:
                return ((Float) value).byteValue();
            case DT_Double:
                return ((Double) value).byteValue();
            case DT_BigDecimal:
                return ((BigDecimal) value).byteValue();
            case DT_Character:
                return Byte.parseByte(((Character) value).toString());
            case DT_String:
                return Byte.parseByte((String) value);
            case DT_Boolean:
                return (byte) ((Boolean) value ? 1 : 0);
            default:
                log.error("can't convert srcType:" + srcType + " to Byte for value:" + value);
                return null;
        }
    }

    /**
     * converts an input value of various data types to a Short data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return Short
     */
    private static Short convertToShort(Object value, int srcType) {
        switch (srcType) {
            case DT_Byte:
                return ((Byte) value).shortValue();
            case DT_Integer:
                return ((Integer) value).shortValue();
            case DT_Long:
                return ((Long) value).shortValue();
            case DT_BigInteger:
                return ((BigInteger) value).shortValue();
            case DT_Float:
                return ((Float) value).shortValue();
            case DT_Double:
                return ((Double) value).shortValue();
            case DT_BigDecimal:
                return ((BigDecimal) value).shortValue();
            case DT_Character:
                return Short.parseShort(((Character) value).toString());
            case DT_String:
                return Short.parseShort((String) value);
            case DT_Boolean:
                return (short) ((Boolean) value ? 1 : 0);
            default:
                log.error("can't convert srcType:" + srcType + " to Short for value:" + value);
                return null;
        }
    }

    /**
     * converts an input value of various data types to a Integer data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return Integer
     */
    private static Integer convertToInteger(Object value, int srcType) {
        switch (srcType) {
            case DT_Byte:
                return ((Byte) value).intValue();
            case DT_Short:
                return ((Short) value).intValue();
            case DT_Long:
                return ((Long) value).intValue();
            case DT_BigInteger:
                return ((BigInteger) value).intValue();
            case DT_Float:
                return ((Float) value).intValue();
            case DT_Double:
                return ((Double) value).intValue();
            case DT_BigDecimal:
                return ((BigDecimal) value).intValue();
            case DT_Character:
                return Integer.parseInt(((Character) value).toString());
            case DT_String:
                String strValue = value.toString().replace(",", "");
                return Integer.parseInt(strValue);
            case DT_Boolean:
                return (int) ((Boolean) value ? 1 : 0);
            case DT_Date:
                return (int) ((Date) value).getTime();
            case DT_Time:
                return (int) ((java.sql.Time) value).getTime();
            case DT_DateTime:
                return (int) ((java.sql.Timestamp) value).getTime();
            default:
                return null;
        }
    }

    /**
     * converts an input value of various data types to a Long data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return Long
     */
    private static Long convertToLong(Object value, int srcType) {
        switch (srcType) {
            case DT_Byte:
                return ((Byte) value).longValue();
            case DT_Short:
                return ((Short) value).longValue();
            case DT_Integer:
                return ((Integer) value).longValue();
            case DT_BigInteger:
                return ((BigInteger) value).longValue();
            case DT_Float:
                return ((Float) value).longValue();
            case DT_Double:
                return ((Double) value).longValue();
            case DT_BigDecimal:
                return ((BigDecimal) value).longValue();
            case DT_Character:
                return Long.parseLong(((Character) value).toString());
            case DT_String:
                String strValue = value.toString().replace(",", "");
                return Long.parseLong(strValue);
            case DT_Boolean:
                return (long) ((Boolean) value ? 1 : 0);
            case DT_Date:
                return ((Date) value).getTime();
            case DT_Time:
                return ((java.sql.Time) value).getTime();
            case DT_DateTime:
                return ((java.sql.Timestamp) value).getTime();
            default:
                return null;
        }
    }

    /**
     * converts an input value of various data types to a BigInteger data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return BigInteger
     */
    private static BigInteger convertToBigInteger(Object value, int srcType) {
        switch (srcType) {
            case DT_Byte:
                return BigInteger.valueOf(((Byte) value).longValue());
            case DT_Short:
                return BigInteger.valueOf(((Short) value).longValue());
            case DT_Integer:
                return BigInteger.valueOf(((Integer) value).longValue());
            case DT_Long:
                return BigInteger.valueOf((Long) value);
            case DT_Float:
                return BigInteger.valueOf(((Float) value).longValue());
            case DT_Double:
                return BigInteger.valueOf(((Double) value).longValue());
            case DT_BigDecimal:
                return BigInteger.valueOf(((BigDecimal) value).longValue());
            case DT_Character:
                return BigInteger.valueOf(Long.parseLong(((Character) value).toString()));
            case DT_String:
                String strValue = value.toString().replace(",", "");
                return BigInteger.valueOf(Long.parseLong(strValue));
            case DT_Boolean:
                return BigInteger.valueOf(((Boolean) value ? 1 : 0));
            case DT_Date:
                return BigInteger.valueOf(((Date) value).getTime());
            case DT_Time:
                return BigInteger.valueOf(((java.sql.Time) value).getTime());
            case DT_DateTime:
                return BigInteger.valueOf(((java.sql.Timestamp) value).getTime());
            default:
                return null;
        }
    }

    /**
     * converts an input value of various data types to a Float data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return Float
     */
    private static Float convertToFloat(Object value, int srcType) {
        switch (srcType) {
            case DT_Byte:
                return ((Byte) value).floatValue();
            case DT_Short:
                return ((Short) value).floatValue();
            case DT_Integer:
                return ((Integer) value).floatValue();
            case DT_Long:
                return ((Long) value).floatValue();
            case DT_BigInteger:
                return ((BigInteger) value).floatValue();
            case DT_Double:
                return ((Double) value).floatValue();
            case DT_BigDecimal:
                return ((BigDecimal) value).floatValue();
            case DT_Character:
                return Float.parseFloat(((Character) value).toString());
            case DT_String:
                String strValue = value.toString().replace(",", "");
                return Float.parseFloat(strValue);
            case DT_Boolean:
                return (float) ((Boolean) value ? 1 : 0);
            default:
                return null;
        }
    }

    /**
     * converts an input value of various data types to a BigDecimal data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return BigDecimal
     */
    private static BigDecimal convertToBigDecimal(Object value, int srcType) {
        switch (srcType) {
            case DT_Byte:
                return BigDecimal.valueOf(((Byte) value).doubleValue());
            case DT_Short:
                return BigDecimal.valueOf(((Short) value).doubleValue());
            case DT_Integer:
                return BigDecimal.valueOf((Integer) value);
            case DT_Long:
                return BigDecimal.valueOf((Long) value);
            case DT_Double:
                return BigDecimal.valueOf((Double) value);
            case DT_Float:
                return BigDecimal.valueOf(((Float) value).doubleValue());
            case DT_Character:
                return BigDecimal.valueOf(Double.parseDouble(((Character) value).toString()));
            case DT_String:
                String strValue = value.toString().replace(",", "");
                return BigDecimal.valueOf(Double.parseDouble(strValue));
            case DT_Boolean:
                return ((Boolean) value ? BigDecimal.ONE : BigDecimal.ZERO);
            default:
                return null;
        }
    }

    /**
     * converts an input value of various data types to a Double data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return Double
     */
    private static Double convertToDouble(Object value, int srcType) {
        switch (srcType) {
            case DT_Byte:
                return ((Byte) value).doubleValue();
            case DT_Short:
                return ((Short) value).doubleValue();
            case DT_Integer:
                return ((Integer) value).doubleValue();
            case DT_Long:
                return ((Long) value).doubleValue();
            case DT_BigInteger:
                return ((BigInteger) value).doubleValue();
            case DT_Float:
                return ((Float) value).doubleValue();
            case DT_BigDecimal:
                return ((BigDecimal) value).doubleValue();
            case DT_Character:
                return Double.parseDouble(((Character) value).toString());
            case DT_String:
                String strValue = value.toString().replace(",", "");
                return Double.parseDouble(strValue);
            case DT_Boolean:
                return (double) ((Boolean) value ? 1 : 0);
            default:
                log.debug("can't convert value:{} to Double", value);
                return null;
        }
    }

    /**
     * converts an input value of various data types to a LocalDate data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return LocalDate
     */
    private static Date convert2Date(Object value, int srcType) throws ParseException {
        switch (srcType) {
            case DT_String:
                if (NumberUtils.isCreatable(value.toString())) {
                    return convert2Date(NumberUtils.toLong((String) value), DT_Long);
                }
                return DateUtils.parseDate(value.toString(),
                        "yyyy-MM-dd HH:mm:ss.SSS"
                        , "yyyy-MM-dd HH:mm:ss"
                        , "yyyy-MM-dd HH:mm"
                        , "yyyy-MM-dd HH"
                        , "yyyy-MM-dd"
                        , "yyyy-MM"
                        , "yyyy");
            case DT_Long:
            case DT_Integer:
            case DT_DateTime:
                Calendar cal = Calendar.getInstance();
                if (srcType == DT_DateTime) {
                    cal.setTime((Date) value);
                } else if (srcType == DT_Long) {
                    cal.setTimeInMillis((Long) value);
                } else {
                    cal.setTimeInMillis((Integer) value);
                }
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                return new java.sql.Date(cal.getTimeInMillis());
            default:
                log.debug("can't convert type:" + srcType + " to Date");
                return null;
        }
    }

    /**
     * converts an input value of various data types to a LocalDate data type.
     *
     * @param value   输入值
     * @param srcType 输入值类型
     * @return LocalDate
     */
    private static LocalDate convert2LocalDate(int srcType, Object value) {
        switch (srcType) {
            case DT_String:
                try {
                    return LocalDate.parse(value.toString(), DateTimeFormatter.ISO_DATE);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDate.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDate.parse(value.toString(), DateTimeFormatter.ISO_OFFSET_DATE);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDate.parse(value.toString(), DateTimeFormatter.ISO_ORDINAL_DATE);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDate.parse(value.toString(), yyyyMMdd);
                } catch (DateTimeParseException dept) {
                    log.warn("can't convert value " + value + " to yyyy-MM-dd HH:mm:ss.SSS");
                }
                return null;
            case DT_Long:
            case DT_long:
                Date dateLong = new Date((Long) value);
                Calendar c1 = Calendar.getInstance();
                c1.setTime(dateLong);
                return LocalDate.of(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
            case DT_BigInteger:
                Date date = new Date(((BigInteger) value).longValue());
                Calendar c2 = Calendar.getInstance();
                c2.setTime(date);
                return LocalDate.of(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH));
            default:
                log.error("can't convert srcType:" + srcType + " to LocalDate for value:" + value);
                return null;
        }
    }

    private static LocalTime convert2LocalTime(int srcType, Object value) {
        switch (srcType) {
            case DT_String:
                try {
                    return LocalTime.parse(value.toString(), DateTimeFormatter.ISO_TIME);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_TIME);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalTime.parse(value.toString(), DateTimeFormatter.ISO_OFFSET_TIME);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalTime.parse(value.toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalTime.parse(value.toString(), DateTimeFormatter.ofPattern("HH:mm:ss"));
                } catch (DateTimeParseException dept) {
                    log.warn("can't convert value " + value + " to HH:mm:ss");
                }
                return null;
            case DT_Long:
            case DT_long:
                Date dateLong = new Date((Long) value);
                Calendar c1 = Calendar.getInstance();
                c1.setTime(dateLong);
                return LocalTime.of(c1.get(Calendar.HOUR), c1.get(Calendar.MINUTE), c1.get(Calendar.SECOND));
            case DT_BigInteger:
                Date date = new Date(((BigInteger) value).longValue());
                Calendar c2 = Calendar.getInstance();
                c2.setTime(date);
                return LocalTime.of(c2.get(Calendar.HOUR), c2.get(Calendar.MINUTE), c2.get(Calendar.SECOND));
            default:
                log.error("can't convert srcType:" + srcType + " to LocalTime for value:" + value);
                return null;
        }
    }

    private static LocalDateTime convert2LocalDateTime(int srcType, Object value) {
        switch (srcType) {
            case DT_String:
                try {
                    return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_DATE_TIME);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), yyyyMMddHHmmssSSS);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), yyyyMMddHHmmssSS);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), yyyyMMddHHmmssS);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), yyyyMMddHHmmss);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), yyyyMMddHHmm);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), yyyyMMddHH);
                } catch (DateTimeParseException ignored) {
                }
                try {
                    return LocalDateTime.parse(value.toString(), yyyyMMdd);
                } catch (DateTimeParseException ignored) {
                }
                log.warn("can't convert value " + value + " to [yyyy-MM-dd HH:mm:ss.SSS,yyyy-MM-dd HH:mm:ss.SS,yyyy-MM-dd HH:mm:ss.S,yyyy-MM-dd HH:mm:ss,yyyy-MM-dd HH:mm,yyyy-MM-dd HH,yyyy-MM-dd]");
                return null;
            case DT_Long:
            case DT_long:
                return convertDateToLDT(new Date((Long) value));
            case DT_BigInteger:
                Date date = new Date(((BigInteger) value).longValue());
                Calendar c2 = Calendar.getInstance();
                c2.setTime(date);
                return LocalDateTime.of(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH),
                        c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), c2.get(Calendar.SECOND));
            default:
                log.error("can't convert srcType:" + srcType + " to LocalDate for value:" + value);
                return null;
        }
    }

    private static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
