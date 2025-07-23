package com.itsoku.orm;

import com.itsoku.lambda.LambdaUtils;
import com.itsoku.lambda.SFunction;
import com.itsoku.utils.CollUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Criteria 构建器
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class CriteriaBuilder<S> {

    private Criteria<S> criteria;
    private Class<S> sClass;

    public static <S> CriteriaBuilder<S> from(Class<S> clazz) {
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder();
        criteriaBuilder.criteria = Criteria.from(clazz);
        criteriaBuilder.sClass = clazz;
        return criteriaBuilder;
    }

    public CriteriaBuilder<S> includes(SFunction<S, ?>... functions) {
        this.criteria.includes(functions);
        return this;
    }

    public CriteriaBuilder<S> excludes(SFunction<S, ?>... functions) {
        this.criteria.excludes(functions);
        return this;
    }

    public CriteriaBuilder<S> beginWithIfPresent(Joint joint, SFunction<S, ?> lambda, String value) {
        if (StringUtils.isNotBlank(value)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.beginWith, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> notBeginWithIfPresent(Joint joint, SFunction<S, ?> lambda, String value) {
        if (StringUtils.isNotBlank(value)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.notBeginWith, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> containsIfPresent(Joint joint, SFunction<S, ?> lambda, String value) {
        if (StringUtils.isNotBlank(value)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.contains, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> contains(SFunction<S, ?> lambda, String value) {
        return containsIfPresent(Joint.AND, lambda, value);
    }

    public CriteriaBuilder<S> notContainsIfPresent(Joint joint, SFunction<S, ?> lambda, String value) {
        if (StringUtils.isNotBlank(value)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.notContains, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> endWithIfPresent(Joint joint, SFunction<S, ?> lambda, String value) {
        if (StringUtils.isNotBlank(value)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.endWith, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> notEndWithIfPresent(Joint joint, SFunction<S, ?> lambda, String value) {
        if (StringUtils.isNotBlank(value)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.notEndWith, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> betweenIfPresent(Joint joint, SFunction<S, ?> lambda, Object start, Object end) {
        if (start != null && end != null) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.between, new Object[]{start, end});
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> notBetweenIfPresent(Joint joint, SFunction<S, ?> lambda, Object start, Object end) {
        if (start != null && end != null) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.notBetween, new Object[]{start, end});
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> blank(Joint joint, SFunction<S, ?> lambda) {
        Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.blank, null);
        condition.setJoint(joint);
        criteria.getConditions().add(condition);
        return this;
    }

    public CriteriaBuilder<S> notBlank(Joint joint, SFunction<S, ?> lambda) {
        Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.notBlank, null);
        condition.setJoint(joint);
        criteria.getConditions().add(condition);
        return this;
    }

    public CriteriaBuilder<S> isIfPresent(Joint joint, SFunction<S, ?> lambda, Object value) {
        if (value != null) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.equal, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> isIfPresent(SFunction<S, ?> lambda, Object value) {
        return isIfPresent(Joint.AND, lambda, value);
    }

    public CriteriaBuilder<S> is(Joint joint, SFunction<S, ?> lambda, Object value) {
        Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.equal, value);
        condition.setJoint(joint);
        criteria.getConditions().add(condition);
        return this;
    }

    public CriteriaBuilder<S> is(SFunction<S, ?> lambda, Object value) {
        return is(Joint.AND, lambda, value);
    }

    public CriteriaBuilder<S> isNotIfPresent(Joint joint, SFunction<S, ?> lambda, Object value) {
        if (value != null) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.notEqual, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> isNot(Joint joint, SFunction<S, ?> lambda, Object value) {
        Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.notEqual, value);
        condition.setJoint(joint);
        criteria.getConditions().add(condition);
        return this;
    }

    public CriteriaBuilder<S> isNot(SFunction<S, ?> lambda, Object value) {
        return isNot(Joint.AND, lambda, value);
    }

    public CriteriaBuilder<S> greaterThanIfPresent(Joint joint, SFunction<S, ?> lambda, Object value) {
        if (value != null) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.greaterThan, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> greaterThan(SFunction<S, ?> lambda, Object value) {
        return greaterThanIfPresent(Joint.AND, lambda, value);
    }

    public CriteriaBuilder<S> greaterEqualIfPresent(Joint joint, SFunction<S, ?> lambda, Object value) {
        if (value != null) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.greaterEqual, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> greaterEqual(SFunction<S, ?> lambda, Object value) {
        return greaterEqualIfPresent(Joint.AND, lambda, value);
    }

    public CriteriaBuilder<S> lessEqualIfPresent(Joint joint, SFunction<S, ?> lambda, Object value) {
        if (value != null) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.lessEqual, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> lessThanIfPresent(Joint joint, SFunction<S, ?> lambda, Object value) {
        if (value != null) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.lessThan, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> lessThan(SFunction<S, ?> lambda, Object value) {
        return lessThanIfPresent(Joint.AND, lambda, value);
    }

    public CriteriaBuilder<S> isNull(Joint joint, SFunction<S, ?> lambda) {
        Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.isNull, null);
        condition.setJoint(joint);
        criteria.getConditions().add(condition);
        return this;
    }

    public CriteriaBuilder<S> isNotNull(Joint joint, SFunction<S, ?> lambda) {
        Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.isNotNull, null);
        condition.setJoint(joint);
        criteria.getConditions().add(condition);
        return this;
    }

    public CriteriaBuilder<S> inIfPresent(Joint joint, SFunction<S, ?> lambda, Collection<?> values) {
        if (CollUtils.isNotEmpty(values)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.in, values);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> inIfPresent(SFunction<S, ?> lambda, Collection<?> values) {
        return inIfPresent(Joint.AND, lambda, values);
    }

    public CriteriaBuilder<S> in(Joint joint, SFunction<S, ?> lambda, Collection<?> values) {
        Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.in, values);
        condition.setJoint(joint);
        criteria.getConditions().add(condition);
        return this;
    }

    public CriteriaBuilder<S> in(SFunction<S, ?> lambda, Collection<?> values) {
        return in(Joint.AND, lambda, values);
    }

    public CriteriaBuilder<S> notInIfPresent(Joint joint, SFunction<S, ?> lambda, Collection<?> values) {
        if (CollUtils.isNotEmpty(values)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.notIn, values);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> custom(Joint joint, String value) {
        if (StringUtils.isNotBlank(value)) {
            Condition condition = new Condition("", Operator.custom, value);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> inIfPresent(Joint joint, SFunction<S, ?> lambda, Object[] values) {
        if (ArrayUtils.isNotEmpty(values)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.in, values);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> notInIfPresent(Joint joint, SFunction<S, ?> lambda, Object[] values) {
        if (ArrayUtils.isNotEmpty(values)) {
            Condition condition = new Condition(LambdaUtils.getPropFromLambda(lambda), Operator.notIn, values);
            condition.setJoint(joint);
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> and(CriteriaBuilder<S> other) {
        if (other != null && CollUtils.isNotEmpty(other.build().getConditions())) {
            Condition condition = new Condition();
            condition.setJoint(Joint.AND);
            if (condition.getSubConditions() == null) {
                condition.setSubConditions(CollUtils.newArrayList());
            }
            condition.getSubConditions().addAll(other.build().getConditions());
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> or(CriteriaBuilder<S> other) {
        if (other != null && CollUtils.isNotEmpty(other.build().getConditions())) {
            Condition condition = new Condition();
            condition.setJoint(Joint.OR);
            if (condition.getSubConditions() == null) {
                condition.setSubConditions(CollUtils.newArrayList());
            }
            condition.getSubConditions().addAll(other.build().getConditions());
            criteria.getConditions().add(condition);
        }
        return this;
    }

    public CriteriaBuilder<S> asc(SFunction<S, ?>... lambda) {
        this.criteria.asc(lambda);
        return this;
    }

    public CriteriaBuilder<S> desc(SFunction<S, ?>... lambda) {
        this.criteria.desc(lambda);
        return this;
    }

    public Criteria<S> build() {
        return this.criteria;
    }

}
