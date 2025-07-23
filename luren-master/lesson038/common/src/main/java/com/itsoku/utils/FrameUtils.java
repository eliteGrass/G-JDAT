package com.itsoku.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.itsoku.common.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <b>description</b>：框架工具类 <br>
 * <b>time</b>：2018-07-26 18:16 <br>
 * <b>author</b>： ready likun_557@163.com
 */
public class FrameUtils {
    public static final String PATTERN_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将时间戳转换为制定格式的日期,timemillis不能为空和0
     *
     * @param timemillis
     * @return
     */
    public static String timestampToDateString(Long timemillis) {
        if (timemillis == null || timemillis == 0) {
            return null;
        }
        return DateUtil.format(timestampToDate(timemillis),
                PATTERN_yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 将时间戳转换为日期,timemillis不能为空和0
     *
     * @param second
     * @return
     */
    public static Date timestampToDate(Long second) {
        if (second == null || second == 0) {
            return null;
        }
        return new Date(second * 1000);
    }

    /**
     * 创建arraylist
     *
     * @param args
     * @return
     */
    public static <V> List<V> newArrayList(Object... args) {
        ArrayList list = new ArrayList();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                list.add(args[i]);
            }
        }
        return list;
    }

    /**
     * 创建hashMap
     *
     * @param args
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <K, V> Map<K, V> newHashMap(Object... args) {
        HashMap paramMap = new HashMap();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                paramMap.put(args[i], args[++i]);
            }
        }
        return paramMap;
    }

    /**
     * 创建LinkedHashMap
     *
     * @param args
     * @return
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Object... args) {
        LinkedHashMap paramMap = new LinkedHashMap();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                paramMap.put(args[i], args[++i]);
            }
        }
        return paramMap;
    }

    /**
     * 将对象转换为json字符串
     *
     * @param object 需转换的目标对象
     * @return
     */
    public static String json(Object object) {
        return json(object, false);
    }

    /**
     * 将对象转换为json字符串，可以指定是否格式化
     *
     * @param object 需转换的目标对象
     * @param format 是否格式化
     * @return
     */
    public static String json(Object object, boolean format) {
        if (format) {
            return JSONUtil.toJsonStr(object);
        } else {
            return JSONUtil.toJsonPrettyStr(object);
        }
    }

    /**
     * 获取list集合中javabean对于的field的值的集合
     *
     * @param list      集合
     * @param fieldName 字段名称
     * @return
     * @throws Exception
     */
    public static <T> List<T> getBeanFieldValues(Collection list, String fieldName){
        if (list != null) {
            List result = FrameUtils.newArrayList();
            for (Object object : list) {
                Field fd = getClassField(fieldName, object.getClass());
                fd.setAccessible(true);
                try {
                    result.add(fd.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            return result;
        }
        return null;
    }


    /**
     * 将一个javabean对象转换为map
     *
     * @param keyFieldMap map的键和javaben的field映射
     * @param javabean
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes"})
    public static Map<Object, Object> javaBeanToMap(Map<String, String> keyFieldMap, Object javabean){
        if (javabean == null) {
            return null;
        } else {
            Map<Object, Object> map = new LinkedHashMap<Object, Object>();
            Class javabeanClass = javabean.getClass();
            if (keyFieldMap != null) {
                Set<Map.Entry<String, String>> set = keyFieldMap.entrySet();
                for (Map.Entry<String, String> entry : set) {

                    Field field = getClassField(entry.getValue(), javabeanClass);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    try {
                        map.put(entry.getKey(), field.get(javabean));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                Field[] fields = getClassAndParentFields(javabeanClass);
                for (Field field : fields) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    try {
                        map.put(field.getName(), field.get(javabean));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return map;
        }
    }

    /**
     * 将一个javabean对象转换为map
     *
     * @param javabean
     * @return
     * @throws Exception
     */
    public static Map javaBeanToMap(Object javabean){

        return javaBeanToMap(null, javabean);
    }

    /**
     * 获取一个class的某个字段，一直追溯到顶级父class对象
     *
     * @param fieldName
     * @param cs
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Field getClassField(String fieldName, Class cs) {
        for (; cs != Object.class; cs = cs.getSuperclass()) {
            try {
                Field field = cs.getDeclaredField(fieldName);
                if (field != null) {
                    return field;
                }
                field = cs.getField(fieldName);
                if (field != null) {
                    return field;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 获取一个class对象的所有属性，包括私有以及所有父类的
     *
     * @param cs
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Field[] getClassAndParentFields(Class cs) {
        List<Field> list = new ArrayList<Field>();
        for (; cs != Object.class; cs = cs.getSuperclass()) {
            Field[] fields = cs.getDeclaredFields();
            for (Field field : fields) {
                list.add(field);
            }
        }
        Field[] fs = new Field[list.size()];
        return list.toArray(fs);
    }

    /**
     * 获取一个类中的某个字段
     *
     * @param cs
     * @param name
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Field getField(Class cs, String name) {
        for (; cs != Object.class; cs = cs.getSuperclass()) {
            try {
                Field field = cs.getDeclaredField(name);
                if (field != null) {
                    return field;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 通过反射获取某个对象属性的值
     *
     * @param target    目标对象
     * @param fieldName 字段
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T getField(Object target, String fieldName){
        Field field = getField(target.getClass(), fieldName);
        if (field != null) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                return (T) field.get(target);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 异常堆栈信息
     *
     * @param throwable
     * @return
     */
    public static String stackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        stringWriter.flush();
        return stringWriter.toString();
    }

    /**
     * 抛出异常
     *
     * @param msg 错误信息
     */
    public static void throwBaseException(String msg) {
        throwBaseException(ResultUtils.ERROR_CODE, null, msg, null);
    }

    /**
     * 抛出异常
     *
     * @param msg     异常信息
     * @param extData 异常扩展信息
     */
    public static void throwBaseException(String msg, Map extData) {
        throwBaseException(ResultUtils.ERROR_CODE, null, msg, extData);
    }

    /**
     * 抛出异常
     *
     * @param code    代码
     * @param subCode 子代码
     * @param msg     错误信息
     */
    public static void throwBaseException(String code, String subCode, String msg, Map extData) {
        throw new BusinessException(code, subCode, msg, extData);
    }

    /**
     * @param @param  date 若date为空，返回当前时间
     * @param @return 设定文件
     * @return Long 返回类型
     * @throws
     * @Title: getTime
     * @Description: 获取日期的时间
     */
    public static Long getTime(Date date) {
        if (date == null) {
            date = Calendar.getInstance().getTime();
        }
        return date.getTime() / 1000L;
    }

    /**
     * 获取1997年到当前时间的秒
     *
     * @return Long
     * @date 2015年11月17日 下午12:53:20
     */
    public static Long getTime() {
        return getTime(Calendar.getInstance().getTime());
    }

    /**
     * 将对象转化为字符串，如果对象为null，则返回null
     *
     * @param obj
     * @return
     */
    public static String convertObjToString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    /**
     * map转换为请求参数
     *
     * @param paramMap map对象
     * @param encoder  是否需要url编码
     * @return
     */
    public static String mapToRequestParam(Map<String, Object> paramMap, boolean encoder) {
        try {
            if (paramMap != null && !paramMap.isEmpty()) {
                StringBuilder s = new StringBuilder();
                s.append("?");
                int i = 0;
                for (Map.Entry<String, Object> param : paramMap.entrySet()) {
                    Object value = param.getValue();
                    if (value != null) {
                        if (value.getClass().isArray()) {
                            Object[] obs = (Object[]) value;
                            for (int j = 0; j < obs.length; j++) {
                                String sv = FrameUtils.convertObjToString(obs[j]);
                                s.append(param.getKey()).append("=").append(encoder && StringUtils.isNotBlank(sv) ? URLEncoder.encode(sv, "UTF-8") : sv);
                                if (j < obs.length - 1) {
                                    s.append("&");
                                }
                            }
                        } else {
                            String sv = FrameUtils.convertObjToString(param.getValue());
                            s.append(param.getKey()).append("=").append(encoder && StringUtils.isNotBlank(sv) ? URLEncoder.encode(sv, "UTF-8") : sv);
                        }
                        if (i < paramMap.size() - 1) {
                            s.append("&");
                        }
                    }
                    i++;
                }
                return s.toString();
            }
            return "";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * list去重
     *
     * @param list
     * @return
     */
    public static <T> List<T> distinctList(List<T> list) {
        if (list == null) {
            return list;
        }
        return new ArrayList<>(new HashSet<>(list));
    }

    /**
     * 将list通过cat连接
     *
     * @param list
     * @param cat
     * @param ismark 是否在两端加引号
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String arrayConcat(List list, String cat, boolean ismark) {
        if (list == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        int len = list.size();
        for (int j = 0; j < len; j++) {
            if (ismark) {
                result.append("'").append(list.get(j)).append("'").append((j != len - 1) ? cat : "");
            } else {
                result.append(list.get(j)).append((j != len - 1) ? cat : "");
            }
        }
        return result.toString();
    }

    /**
     * list转换为String并去重复
     *
     * @param list
     * @return String
     */
    public static String listToString(List<String> list) {
        Set<String> sub = new HashSet<String>();
        for (String temp : list) {
            sub.add(temp);
        }
        return sub.toString().replaceAll("[^0-9,]", "");
    }

    /**
     * 根据失败次数获取处理时间
     *
     * @param failCount
     * @param curtime
     * @return
     */
    public static long getNextDisposeTime(long failCount, long curtime) {
        if (failCount == 0) {
            return curtime + 10;
        } else if (failCount > 0 && failCount <= 5) {
            return curtime + 20;
        } else if (failCount > 5 && failCount <= 20) {
            return curtime + 60;
        } else if (failCount > 20 && failCount <= 40) {
            return curtime + 300;
        } else if (failCount > 40 && failCount <= 60) {
            return curtime + 600;
        } else if (failCount > 60 && failCount <= 80) {
            return curtime + 1800;
        } else if (failCount > 80 && failCount <= 90) {
            return curtime + 3600;
        } else {
            return curtime + 7200;
        }
    }


    /**
     * 获取最小值
     *
     * @param args
     * @return
     */
    public static double getMin(Double... args) {
        double result = Double.MAX_VALUE;
        if (args != null) {

            for (Double arg : args) {
                result = Math.min(result, arg);
            }
        }
        return result;
    }


    /**
     * 判断一个字符串是否是一个数字，最大支持18位
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("-*\\d{0,18}\\.?\\d*");
        return pattern.matcher(str).matches();
    }

    /**
     * 将list集合中以一个field作为key,另一个field作为value，获取map
     *
     * @param list
     * @param fieldKey
     * @param fieldValue
     * @return
     * @throws Exception
     */
    public static Map getBeanFieldMap(Collection list, String fieldKey, String fieldValue){
        if (list != null) {
            Map result = new HashMap();
            for (Object object : list) {
                Field key = getClassField(fieldKey, object.getClass());
                Field value = getClassField(fieldValue, object.getClass());
                key.setAccessible(true);
                value.setAccessible(true);
                try {
                    result.put(key.get(object), value.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            return result;
        }
        return null;
    }

    /**
     * 获取( a-delta, a+delta )区间内的一个随机值
     *
     * @param a
     * @param delta
     * @return
     */
    public static long getRandomLong(long a, long delta) {
        return getRandomLong(a, delta, new Random());
    }

    /**
     * 获取( a-delta, a+delta )区间内的一个随机值
     *
     * @param a
     * @param delta
     * @return
     */
    public static long getRandomLong(long a, long delta, Random random) {
        if (random == null) {
            throwBaseException("请初始化随机数生成器");
        }
        return a + (random.nextInt() % delta);
    }

    /**
     * 将泛型类型的集合转换为指定类型的map
     *
     * @param list
     * @param fileName
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */
    public static <K, V> Map<K, V> convertListToMap(List<V> list, String fileName){
        if (list != null) {
            Map<K, V> result = FrameUtils.newHashMap();
            for (V model : list) {
                Field fd = getClassField(fileName, model.getClass());
                fd.setAccessible(true);
                try {
                    result.put((K) fd.get(model), model);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            return result;
        }

        return null;
    }
}
