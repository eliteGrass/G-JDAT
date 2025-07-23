package com.itsoku.lesson053.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * 集合工具类
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/30 12:31 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class CollUtils {

    private CollUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 创建 ArrayList
     *
     * @param args
     * @return
     */
    public static <E> List<E> newArrayList(E... args) {
        return new ArrayList(Arrays.asList(args));
    }

    /**
     * 指定容量，创建 ArrayList
     *
     * @param initialCapacity
     * @return
     */
    public static <E> List<E> newArrayListCapacity(int initialCapacity) {
        return new ArrayList(initialCapacity);
    }

    /**
     * 创建HashSet
     *
     * @param args
     * @return
     */
    public static <E> Set<E> newHashSet(E... args) {
        return new HashSet<>(Arrays.asList(args));
    }

    /**
     * 创建LinkedHashSet
     *
     * @param args
     * @return
     */
    public static <E> Set<E> newLinkedHashSet(E... args) {
        return new LinkedHashSet<>(Arrays.asList(args));
    }

    /**
     * 创建hashMap
     *
     * @param args
     * @return
     */
    public static <K, V> Map<K, V> newHashMap(Object... args) {
        HashMap paramMap = new HashMap();
        if (args != null) {
            if (args.length % 2 == 0) {
                throw new RuntimeException("The length must be a multiple of 2");
            }
            int size = args.length / 2;
            for (int i = 0; i < size; i++) {
                paramMap.put(args[2 * i], args[2 * i + 1]);
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
            if (args.length % 2 == 0) {
                throw new RuntimeException("The length must be a multiple of 2");
            }
            int size = args.length / 2;
            for (int i = 0; i < size; i++) {
                paramMap.put(args[2 * i], args[2 * i + 1]);
            }
        }
        return paramMap;
    }

    /**
     * 判断map是否是为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断map是否不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    /**
     * 判断集合是否是为空
     *
     * @param coll
     * @return
     */
    public static boolean isEmpty(Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 判断集合是否不为空
     *
     * @param coll
     * @return
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * 集合去重
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> Collection<T> distinct(Collection<T> list) {
        return isEmpty(list) ? list : distinct(list, item -> item);
    }

    /**
     * 去重，然后把集合中的元素转换为另外的元素
     *
     * @param from
     * @param func
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> distinct(Collection<T> from, Function<T, R> func) {
        return distinct(from, func, t -> true);
    }

    /**
     * 去重，然后把集合中的元素转换为另外的元素 (from->filter->func->distinct->list)
     *
     * @param from
     * @param func
     * @param filter
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> distinct(Collection<T> from, Function<T, R> func, Predicate<T> filter) {
        if (isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(filter).map(func).distinct().collect(Collectors.toList());
    }

    /**
     * 将一个集合转换为另外一个集合
     *
     * @param from
     * @param func
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func) {
        if (isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().map(func).collect(Collectors.toList());
    }

    /**
     * 将一个集合转换为另外一个集合：from->filter->list
     *
     * @param from
     * @param func
     * @param filter
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        if (isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(filter).map(func).collect(Collectors.toList());
    }

    /**
     * 将集合转换为set
     *
     * @param from
     * @param func
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func) {
        if (isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().map(func).collect(Collectors.toSet());
    }

    /**
     * 将集合转换为set：from->filter->list
     *
     * @param from
     * @param func
     * @param filter
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        if (isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().filter(filter).map(func).collect(Collectors.toSet());
    }

    /**
     * 转换为 Set
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> Set<T> convertToSet(Collection<T> list) {
        if (list == null) {
            return null;
        }
        if (Set.class.isInstance(list)) {
            return (Set<T>) list;
        }
        return list.stream().collect(Collectors.toSet());
    }

    /**
     * 转换为 List
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> convertToList(Collection<T> list) {
        if (list == null) {
            return null;
        }
        if (List.class.isInstance(list)) {
            return (List<T>) list;
        }
        return list.stream().collect(Collectors.toList());
    }

    /**
     * 将集合转换为map
     *
     * @param from
     * @param keyFunc
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, Function.identity());
    }

    /**
     * 将集合转换为map
     *
     * @param from
     * @param keyFunc
     * @param supplier
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc, Supplier<? extends Map<K, T>> supplier) {
        if (isEmpty(from)) {
            return supplier.get();
        }
        return convertMap(from, keyFunc, Function.identity(), supplier);
    }

    /**
     * 将集合转换为map
     *
     * @param from
     * @param keyFunc
     * @param valueFunc
     * @param <T>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1);
    }

    /**
     * 将集合转换为map
     *
     * @param from
     * @param keyFunc       key转换器
     * @param valueFunc     value转换器
     * @param mergeFunction key重复时value处理策略
     * @param <T>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, valueFunc, mergeFunction, HashMap::new);
    }

    /**
     * 将集合转换为map
     *
     * @param from
     * @param keyFunc
     * @param valueFunc
     * @param supplier
     * @param <T>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, Supplier<? extends Map<K, V>> supplier) {
        if (isEmpty(from)) {
            return supplier.get();
        }
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1, supplier);
    }

    /**
     * 将集合转换为map
     *
     * @param from
     * @param keyFunc
     * @param valueFunc
     * @param mergeFunction
     * @param supplier
     * @param <T>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction, Supplier<? extends Map<K, V>> supplier) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.toMap(keyFunc, valueFunc, mergeFunction, supplier));
    }

    /**
     * 集合分组
     *
     * @param from
     * @param keyFunc
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> Map<K, List<T>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping(t -> t, Collectors.toList())));
    }

    /**
     * 集合分组
     *
     * @param from
     * @param keyFunc
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K, V> Map<K, List<V>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream()
                .collect(Collectors.groupingBy(keyFunc, Collectors.mapping(valueFunc, Collectors.toList())));
    }

    /**
     * 多个集合都不为空返回true
     *
     * @param values
     * @return
     */
    public static boolean allNotEmpty(final Collection<?>... values) {
        if (values == null) {
            return false;
        }
        for (Collection val : values) {
            if (isEmpty(val)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 多个集合都为空，返回true
     *
     * @param values
     * @return
     */
    public static boolean allEmpty(final Collection<?>... values) {
        return !anyNotEmpty(values);
    }

    /**
     * 多个集合中，任意一个不为空，则返回true
     *
     * @param values
     * @return
     */
    public static boolean anyNotEmpty(final Collection<?>... values) {
        return firstNotEmpty(values) != null;
    }

    /**
     * 多个集合中，任意一个为空，则返回true
     *
     * @param values
     * @return
     */
    public static boolean anyEmpty(final Collection<?>... values) {
        return !allNotEmpty(values);
    }

    /**
     * 多个集合中，返回第一个不为空的集合
     *
     * @param values
     * @return
     */
    public static Collection<?> firstNotEmpty(final Collection<?>... values) {
        if (values != null) {
            for (final Collection val : values) {
                if (isNotEmpty(val)) {
                    return val;
                }
            }
        }
        return null;
    }

    /**
     * 返回集合中，第一个元素，若集合为null || 空，则返回null
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T getFirst(Collection<T> list) {
        return isNotEmpty(list) ? list.iterator().next() : null;
    }

    /**
     * 返回第一个元素的某个属性，若集合为null || 空，则返回null
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T, R> R getFirst(Collection<T> list, Function<T, R> fun) {
        T first = getFirst(list);
        return first == null ? null : fun.apply(first);
    }

    /**
     * 消费第一个元素
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> void first(Collection<T> list, Consumer<T> consumer) {
        T first = getFirst(list);
        if (null != first) {
            consumer.accept(first);
        }
    }

    /**
     * 根据 key 获取值
     *
     * @param map
     * @param key
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V get(Map<K, V> map, K key) {
        return map != null ? map.get(key) : null;
    }


    /**
     * list 中是否包含对象
     *
     * @param list
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> boolean contain(Collection<T> list, T obj) {
        return CollUtils.isNotEmpty(list) && list.contains(obj);
    }

    /**
     * 集合批处理，比如集合中有1000个元素，每次处理 100 个元素，则通过10次会将集合中的元素处理完毕
     *
     * @param collection 集合
     * @param batchSize  每批次处理多少个元素
     * @param consumer   消费者
     * @param <T>
     */
    public static <T> void batch(Collection<T> collection, int batchSize, Consumer<List<T>> consumer) {
        if (isEmpty(collection)) {
            return;
        }
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Illegal batchSize: " +
                    batchSize);
        }
        if (consumer == null) {
            throw new IllegalArgumentException("consumer not null");
        }
        int size = size(collection);
        int batch = size % batchSize == 0 ? size / batchSize : size / batchSize + 1;
        List<T> list = newArrayListCapacity(batch);
        int index = 0;
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            list.add(item);
            index++;
            if (index % batchSize == 0) {
                consumer.accept(list);
                list.clear();
            }
        }
        if (!list.isEmpty()) {
            consumer.accept(list);
        }
    }

    /**
     * 获取集合对象大小
     *
     * @param object
     * @return
     */
    public static int size(final Object object) {
        if (object == null) {
            return 0;
        }
        int total = 0;
        if (object instanceof Map<?, ?>) {
            total = ((Map<?, ?>) object).size();
        } else if (object instanceof Collection<?>) {
            total = ((Collection<?>) object).size();
        } else if (object instanceof Iterable<?>) {
            Iterator iterator = ((Iterable) object).iterator();
            int size = 0;
            if (iterator != null) {
                while (iterator.hasNext()) {
                    iterator.next();
                    size++;
                }
            }
            return size;
        } else if (object instanceof Object[]) {
            total = ((Object[]) object).length;
        } else if (object instanceof Iterator<?>) {
            Iterator iterator = (Iterator) object;
            if (iterator != null) {
                while (iterator.hasNext()) {
                    iterator.next();
                    total++;
                }
            }
        } else if (object instanceof Enumeration<?>) {
            final Enumeration<?> it = (Enumeration<?>) object;
            while (it.hasMoreElements()) {
                total++;
                it.nextElement();
            }
        } else {
            try {
                total = Array.getLength(object);
            } catch (final IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
            }
        }
        return total;
    }

    /**
     * 判断两个{@link Collection} 是否元素和顺序相同，返回{@code true}的条件是：
     * <ul>
     *     <li>两个{@link Collection}必须长度相同</li>
     *     <li>两个{@link Collection}元素相同index的对象必须equals，满足{@link Objects#equals(Object, Object)}</li>
     * </ul>
     * 此方法来自Apache-Commons-Collections4。
     *
     * @param list1 列表1
     * @param list2 列表2
     * @return 是否相同
     */
    public static boolean isEqualOrderList(final Collection<?> list1, final Collection<?> list2) {
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        final Iterator<?> it1 = list1.iterator();
        final Iterator<?> it2 = list2.iterator();
        Object obj1;
        Object obj2;
        while (it1.hasNext() && it2.hasNext()) {
            obj1 = it1.next();
            obj2 = it2.next();

            if (false == Objects.equals(obj1, obj2)) {
                return false;
            }
        }

        // 当两个Iterable长度不一致时返回false
        return false == (it1.hasNext() || it2.hasNext());
    }

    /**
     * 判断两个集合的元素是否一样
     *
     * @param a
     * @param b
     * @param orderEqual 元素顺序是否也要一样？
     * @return
     */
    public static boolean isEqualCollection(final Collection<?> a, final Collection<?> b, boolean orderEqual) {
        if (orderEqual) {
            return isEqualOrderList(a, b);
        }

        if (a.size() != b.size()) {
            return false;
        } else {
            Map mapa = getCardinalityMap(a);
            Map mapb = getCardinalityMap(b);
            if (mapa.size() != mapb.size()) {
                return false;
            } else {
                Iterator it = mapa.keySet().iterator();
                while (it.hasNext()) {
                    Object obj = it.next();
                    if (getFreq(obj, mapa) != getFreq(obj, mapb)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private static final Integer INTEGER_ONE = new Integer(1);

    /**
     * 获取集合中每个元素出现的次数
     *
     * @param coll
     * @return 元素->出现的次数
     */
    private static Map getCardinalityMap(final Collection coll) {
        Map count = new HashMap();
        for (Iterator it = coll.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            Integer c = (Integer) (count.get(obj));
            if (c == null) {
                count.put(obj, INTEGER_ONE);
            } else {
                count.put(obj, new Integer(c.intValue() + 1));
            }
        }
        return count;
    }

    private static int getFreq(final Object obj, final Map freqMap) {
        Integer count = (Integer) freqMap.get(obj);
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

}