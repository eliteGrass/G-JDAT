package com.itsoku.orm;


import com.itsoku.lambda.LambdaUtils;
import com.itsoku.lambda.SFunction;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@ToString
public class Condition implements Serializable {
    /**
     * 条件连接字符串 and or
     */
    private Joint joint = Joint.AND;
    /**
     * 实体对应的属性名称
     */
    private String propertyName;
    /**
     * 查询关系符号
     */
    private Operator operator = Operator.equal;
    /**
     * 条件的值
     */
    private Object value;
    /**
     * 子条件
     */
    private List<Condition> subConditions;

    public Condition() {
    }

    public boolean validate() {
        if (operator == Operator.isNull
                || operator == Operator.isNotNull
                || operator == Operator.blank
                || operator == Operator.notBlank) {
            return true;
        }
        if (value == null) {
            return false;
        }
        if (operator == Operator.in || operator == Operator.notIn || operator == Operator.between || operator == Operator.notBetween) {
            if (value instanceof Collection) {
                Collection<?> collectionValue = (Collection<?>) value;
                //check collection
                for (Object cValue : collectionValue) {
                    if (cValue == null) {
                        return false;
                    }
                }
            } else if (value instanceof String) {
                String[] inValues = StringUtils.split((String) value, ",");
                return inValues.length > 0;
            } else if (value.getClass().isArray()) {
                Object[] values = (Object[]) value;
                //check array values contains null value
                for (Object v : values) {
                    if (v == null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Condition copy() {
        Condition condition = new Condition();
        condition.setValue(this.value);
        condition.setOperator(this.operator);
        condition.setJoint(this.joint);
        condition.setPropertyName(this.propertyName);
        if (this.subConditions != null) {
            condition.setSubConditions(this.subConditions.stream()
                    .map(Condition::copy).collect(Collectors.toList()));
        }
        return condition;
    }

    public Condition(String propertyName, String value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public Condition(String propertyName, Operator operator, Object value) {
        this.propertyName = propertyName;
        this.operator = operator;
        this.value = value;
    }

    public <T> Condition(SFunction<T, ?> lambda, Operator operator, Object value) {
        this(Joint.AND, lambda, operator, value);
    }

    public <T> Condition(Joint joint, SFunction<T, ?> lambda, Operator operator, Object value) {
        this.joint = joint;
        this.propertyName = LambdaUtils.getPropFromLambda(lambda);
        this.operator = operator;
        this.value = value;
    }

    public boolean hasSubCondition() {
        return this.subConditions != null && this.subConditions.size() > 0;
    }

    public Joint getJoint() {
        return joint;
    }

    public void setJoint(Joint joint) {
        this.joint = joint;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Condition> getSubConditions() {
        return subConditions;
    }

    public void setSubConditions(List<Condition> subConditions) {
        this.subConditions = subConditions;
    }
}
