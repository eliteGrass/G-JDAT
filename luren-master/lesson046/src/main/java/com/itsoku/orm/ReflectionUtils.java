package com.itsoku.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/9 19:36 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ReflectionUtils {

    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);
    private static Map<Class, List<Field>> classFiledMap = new ConcurrentHashMap<>(256);
    private static Map<Class, Map<String, PropertyDescriptor>> classPropertyDescriptorMap = new ConcurrentHashMap<>(0);

    /**
     * 直接读取对象属性值, 如果对象有getter方法直接调用getter方法，如果没有getter方法，直接读取属性值
     * 可以取得父类的属性
     *
     * @param object    对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public static Object getValue(Object object, String fieldName) {
        if (object == null) {
            throw new IllegalArgumentException("object is null,can't get value");
        }
        if (fieldName == null) {
            throw new IllegalArgumentException("field name is null,can't get value");
        }
        //首先检查此属性是否有get方法
        Map<String, PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(object.getClass());
        PropertyDescriptor propertyDescriptor = propertyDescriptors.get(fieldName);
        if (propertyDescriptor != null) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                return ReflectionUtils.invokeMethod(object, readMethod.getName(), null, null);
            }
        }
        return ReflectionUtils.getFieldValue(object, fieldName);
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     * 可以设置父类的属性值
     *
     * @param object    对象
     * @param fieldName 属性名
     * @param value     属性值
     */
    public static void setValue(final Object object, final String fieldName, final Object value) {
        if (object == null) {
            return;
        }
        if (fieldName == null) {
            return;
        }
        //首先检查此属性是否有get方法
        Map<String, PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(object.getClass());
        PropertyDescriptor propertyDescriptor = propertyDescriptors.get(fieldName);
        if (propertyDescriptor != null) {
            Method readMethod = propertyDescriptor.getWriteMethod();
            if (readMethod != null) {
                ReflectionUtils.invokeMethod(object, readMethod.getName(), new Class[]{propertyDescriptor.getPropertyType()}, new Object[]{value});
                return;
            }
        }
        setFieldValue(object, fieldName, value);
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     * 可以取得父类的属性
     *
     * @param object    对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        return getFieldValue(object, field);
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     * 可以取得父类的属性
     *
     * @param object 对象
     * @param field  属性名
     * @return 属性值
     */
    public static Object getFieldValue(final Object object, final Field field) {
        makeAccessible(field);
        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e);
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     * 可以设置父类的属性值
     *
     * @param object    对象
     * @param fieldName 属性名
     * @param value     属性值
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        setFieldValue(object, field, value);
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     * 可以设置父类的属性值
     *
     * @param object 对象
     * @param field  属性名
     * @param value  属性值
     */
    public static void setFieldValue(final Object object, final Field field, final Object value) {
        if (field == null) {
            return;
        }
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 可以调用父类的方法
     *
     * @param object         对象
     * @param methodName     方法名
     * @param parameterTypes 方法类型
     * @param parameters     方法参数
     * @return 方法执行结果
     */
    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes, final Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }
        method.setAccessible(true);
        try {
            return method.invoke(object, parameters);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField.
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @param object    对象
     * @param fieldName 属性名
     * @return 对象的Field
     */
    public static Field getDeclaredField(final Object object, final String fieldName) {
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField.
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @param object 对象
     * @return 对象的Field
     */
    public static List<Field> getDeclaredFields(final Object object) {
        return getDeclaredFields(object.getClass());
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField.
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @param clazz 对象类型
     * @return 对象的Field
     */
    public static List<Field> getDeclaredFields(final Class clazz) {
        List<Field> classFieldList = classFiledMap.get(clazz);
        if (classFieldList != null) {
            return classFieldList;
        }
        List<Field> fieldList = new ArrayList<Field>();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    boolean contains = false;
                    for (Field field1 : fieldList) {
                        if (field1.getName().equals(field.getName())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        fieldList.add(field);
                    }
                }
            }
        }
        classFiledMap.put(clazz, fieldList);
        return fieldList;
    }


    /**
     * 循环向上转型, 获取对象的DeclaredField.
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @param clazz 对象类型
     * @return 对象的Field
     */
    public static Map<String, PropertyDescriptor> getPropertyDescriptors(final Class clazz) {
        return classPropertyDescriptorMap.computeIfAbsent(clazz, cls -> {
            Map<String, PropertyDescriptor> propertyDescriptorMap = new ConcurrentHashMap<>();
            for (Class<?> superClass = cls; superClass != Object.class; superClass = superClass.getSuperclass()) {
                try {
                    BeanInfo beanInfo = Introspector.getBeanInfo(superClass);
                    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        propertyDescriptorMap.putIfAbsent(propertyDescriptor.getName(), propertyDescriptor);
                    }
                } catch (IntrospectionException e) {
                    throw new IllegalStateException(e);
                }
            }
            return propertyDescriptorMap;
        });

    }

    /**
     * 循环向上转型, 获取对象的DeclaredField.
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @param clazz     对象类型
     * @param fieldName 属性名
     * @return 对象的Field
     */
    public static Field getDeclaredField(final Class clazz, final String fieldName) {
        return org.springframework.util.ReflectionUtils.findField(clazz, fieldName);
    }

    /**
     * 强行设置Field可访问
     *
     * @param field 属性
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @param object         对象
     * @param methodName     方法名
     * @param parameterTypes 方法参数类型
     * @return 方法
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        return getDeclaredMethod(object.getClass(), methodName, parameterTypes);
    }

    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @param clazz          对象
     * @param methodName     方法名
     * @param parameterTypes 方法参数类型
     * @return 方法
     */
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        return org.springframework.util.ReflectionUtils.findMethod(clazz, methodName, parameterTypes);
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException
                || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException("Reflection Exception.", e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException("Reflection Exception.",
                    ((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }
}
