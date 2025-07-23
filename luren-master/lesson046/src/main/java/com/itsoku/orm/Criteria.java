package com.itsoku.orm;

import com.itsoku.lambda.LambdaUtils;
import com.itsoku.lambda.SFunction;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
@ToString
@Slf4j
public class Criteria<T> implements Serializable {
    /**
     * 属性选择器 本次查询包含哪些属性，忽略哪些属性
     */
    private PropertySelector selector;
    /**
     * 过滤条件 支持级联属性
     */
    private List<Condition> conditions;
    /**
     * 排序的属性
     */
    private List<SortProperty> sortProperties;

    /**
     * 复制一个新的查询构建器，与原来的值形同
     *
     * @return 新的查询构建器
     */
    public Criteria<T> copy() {
        Criteria<T> that = new Criteria<>();
        if (this.selector != null) {
            PropertySelector thatSelector = new PropertySelector();
            if (this.selector.getIncludes() != null) {
                thatSelector.include(this.selector.getIncludes().toArray(new String[0]));
            }
            if (this.selector.getExcludes() != null) {
                thatSelector.exclude(this.selector.getExcludes().toArray(new String[0]));
            }
            that.selector = thatSelector;
        }
        that.conditions = this.conditions.stream().map(Condition::copy).collect(Collectors.toList());
        if (this.sortProperties != null) {
            that.sortProperties = this.sortProperties.stream().map(sortProperty -> {
                return new SortProperty(sortProperty.getPropertyName(), sortProperty.getSort());
            }).collect(Collectors.toList());
        }
        return that;
    }

    private Criteria() {
        this.conditions = new ArrayList<>();
    }

    private void criterion(Condition... condition) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        if (condition != null && condition.length > 0) {
            conditions.addAll(Arrays.asList(condition));
        }
    }

    private void sort(SortProperty sortProperty) {
        if (sortProperty == null) {
            return;
        }
        if (this.sortProperties == null) {
            this.sortProperties = new ArrayList<>();
        }
        sortProperties.add(sortProperty);
    }

    /**
     * 从一个泛型类构建一个空的查询 构建器
     *
     * @param clazz 类
     * @param <S>   对象的泛型类
     * @return 查询构建器
     */
    public static <S> Criteria<S> from(Class<S> clazz) {
        return new Criteria<>();
    }

    public static <S, R> ConditionSpec<S, R> from(SFunction<S, R> lamda) {
        return new Criteria<S>().and(lamda);
    }

    public static <S> Criteria<S> empty() {
        return new Criteria<>();
    }

    /**
     * 指定本次查询需要查询哪些属性，如果不指定，将查询所有属性，包括关联对象的所有属性
     *
     * @param functions 当前对象T的属性的getter方法
     * @return 新的查询构建器
     */
    public Criteria<T> includes(SFunction<T, ?>... functions) {
        if (functions == null || functions.length == 0) {
            return this;
        }
        if (this.selector == null) {
            this.selector = new PropertySelector();
        }
        for (SFunction<T, ?> function : functions) {
            String prop = LambdaUtils.getPropFromLambda(function);
            this.selector.include(prop);
        }
        return this;
    }

    /**
     * 指定本次查询需要忽略哪些属性，如果不指定，将查询所有属性，包括关联对象的所有属性
     *
     * @param functions 当前对象T的属性(?)的getter方法
     * @return 新的查询构建器
     */
    public Criteria<T> excludes(SFunction<T, ?>... functions) {
        if (functions == null || functions.length == 0) {
            return this;
        }
        if (this.selector == null) {
            this.selector = new PropertySelector();
        }
        for (SFunction<T, ?> function : functions) {
            String prop = LambdaUtils.getPropFromLambda(function);
            this.selector.exclude(prop);
        }
        return this;
    }

    /**
     * 指定本次查询的升序排序属性，如果不指定，将不排序
     *
     * @param lambda 当前对象T的属性(?)的getter方法
     * @return 新的查询构建器
     */
    public Criteria<T> asc(SFunction<T, ?>... lambda) {
        if (lambda == null || lambda.length == 0) {
            return this;
        }
        for (SFunction<T, ?> function : lambda) {
            String prop = LambdaUtils.getPropFromLambda(function);
            this.sort(new SortProperty(prop, Sort.ASC));
        }
        return this;
    }

    /**
     * 指定本次查询的降序排序属性，如果不指定，将不排序
     *
     * @param lambda 当前对象T的属性(?)的getter方法
     * @return 新的查询构建器
     */
    public Criteria<T> desc(SFunction<T, ?>... lambda) {
        if (lambda == null || lambda.length == 0) {
            return this;
        }
        for (SFunction<T, ?> function : lambda) {
            String prop = LambdaUtils.getPropFromLambda(function);
            this.sort(new SortProperty(prop, Sort.DESC));
        }
        return this;
    }

    public <R> ConditionSpec<T, R> where(SFunction<T, R> lambda) {
        return and(lambda);
    }

    public <R> ConditionSpec<T, R> and(SFunction<T, R> lambda) {
        String prop = LambdaUtils.getPropFromLambda(lambda);
        return new ConditionSpec<>(this, Joint.AND, prop);
    }

    public <R> ConditionSpec<T, R> or(SFunction<T, R> lambda) {
        String prop = LambdaUtils.getPropFromLambda(lambda);
        return new ConditionSpec<>(this, Joint.OR, prop);
    }

    /**
     * 条件构造类
     *
     * @param <T> 对象类型
     */
    public static class ConditionSpec<T, R> {
        /**
         * 对象属性名
         */
        private String property;
        /**
         * 关联的criteria
         */
        private Criteria<T> criteria;
        /**
         * 条件的连接符
         */
        private Joint joint;

        public ConditionSpec(Criteria<T> criteria, Joint joint, String property) {
            this.property = property;
            this.criteria = criteria;
            this.joint = joint;
        }

        public Criteria<T> beginWith(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.beginWith, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> notBeginWith(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.notBeginWith, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> contains(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.contains, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> notContains(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.notContains, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> endWith(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.endWith, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> notEndWith(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.notEndWith, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> between(@NonNull R start, @NonNull R end) {
            validateNonNull(start);
            validateNonNull(end);
            Condition condition = new Condition(property, Operator.between, new Object[]{start, end});
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> notBetween(@NonNull R start, @NonNull R end) {
            validateNonNull(start);
            validateNonNull(end);
            Condition condition = new Condition(property, Operator.notBetween, new Object[]{start, end});
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> blank() {
            Condition condition = new Condition(property, Operator.blank, null);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> notBlank() {
            Condition condition = new Condition(property, Operator.notBlank, null);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> is(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.equal, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> isNot(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.notEqual, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> greaterThan(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.greaterThan, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> greaterEqual(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.greaterEqual, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> lessEqual(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.lessEqual, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> lessThan(@NonNull R value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.lessThan, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> isNull() {
            Condition condition = new Condition(property, Operator.isNull, null);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> isNotNull() {
            Condition condition = new Condition(property, Operator.isNotNull, null);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> in(@NonNull Collection<R> values) {
            validateNonNull(values);
            Condition condition = new Condition(property, Operator.in, values);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> notIn(@NonNull Collection<R> values) {
            validateNonNull(values);
            Condition condition = new Condition(property, Operator.notIn, values);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> custom(@NonNull String value) {
            validateNonNull(value);
            Condition condition = new Condition(property, Operator.custom, value);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> in(@NonNull R[] values) {
            validateNonNull(values);
            Condition condition = new Condition(property, Operator.in, values);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }

        public Criteria<T> notIn(@NonNull R[] values) {
            validateNonNull(values);
            Condition condition = new Condition(property, Operator.notIn, values);
            condition.setJoint(joint);
            criteria.criterion(condition);
            return criteria;
        }
    }


    private static void validateNonNull(Object value) {
        if (value == null) {
            throw new IllegalStateException("value" + value + " is null");
        }
        if (value instanceof Collection<?>) {
            if (((Collection<?>) value).isEmpty()) {
                throw new IllegalStateException("value" + value + " is empty collection");
            }
            ((Collection<?>) value).forEach(Criteria::validateNonNull);
        }
        if (value.getClass().isArray()) {
            //check arrays
            String simpleName = value.getClass().getSimpleName();
            switch (simpleName) {
                case "int[]": {
                    int[] values = (int[]) value;
                    if (values.length == 0) {
                        throw new IllegalStateException("value" + value + " is empty array");
                    }
                    break;
                }
                case "long[]": {
                    long[] values = (long[]) value;
                    if (values.length == 0) {
                        throw new IllegalStateException("value" + value + " is empty array");
                    }
                    break;
                }
                case "char[]": {
                    char[] values = (char[]) value;
                    if (values.length == 0) {
                        throw new IllegalStateException("value" + value + " is empty array");
                    }
                    break;
                }
                case "double[]": {
                    double[] values = (double[]) value;
                    if (values.length == 0) {
                        throw new IllegalStateException("value" + value + " is empty array");
                    }
                    break;
                }
                case "byte[]": {
                    byte[] values = (byte[]) value;
                    if (values.length == 0) {
                        throw new IllegalStateException("value" + value + " is empty array");
                    }
                    break;
                }
                case "short[]": {
                    short[] values = (short[]) value;
                    if (values.length == 0) {
                        throw new IllegalStateException("value" + value + " is empty array");
                    }
                    break;
                }
                case "boolean[]": {
                    boolean[] values = (boolean[]) value;
                    if (values.length == 0) {
                        throw new IllegalStateException("value" + value + " is empty array");
                    }
                    break;
                }
                case "float[]": {
                    float[] values = (float[]) value;
                    if (values.length == 0) {
                        throw new IllegalStateException("value" + value + " is empty array");
                    }
                    break;
                }
                default: {
                    Object[] values = (Object[]) value;
                    if (values.length == 0) {
                        throw new IllegalStateException("value" + value + " is empty array");
                    }
                    break;
                }
            }
        }
    }
}
