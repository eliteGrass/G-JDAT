package com.itsoku.lesson041.lock;

import com.itsoku.lesson041.common.BusinessExceptionUtils;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/25 13:12 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Aspect
@Slf4j
public class LockAspect {

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private ExpressionParser expressionParser = new SpelExpressionParser();

    private DistributeLock distributeLock;

    public LockAspect(DistributeLock distributeLock) {
        this.distributeLock = distributeLock;
    }

    /**
     * 环绕通知，会拦截所有标注有 @Lock 注解的方法
     *
     * @param pjp
     * @param lock
     * @return
     */
    @Around("@annotation(lock)")
    public Object around(ProceedingJoinPoint pjp, Lock lock) throws InterruptedException {
        //1、校验lock信息
        this.validate(lock);
        //2、获取锁名称
        String lockName = this.getLockName(pjp, lock);

        //3、加锁并执行业务
        R r = R.builder().build();
        boolean lockResult;
        // 上锁时间为-1，则锁会自动续期，否则使用 leaseTime 作为锁的持有时间
        if (lock.leaseTime() == -1) {
            lockResult = this.distributeLock.tryLockRun(lockName, this.runnable(pjp, r), lock.waitTime(), lock.timeUnit());
        } else {
            lockResult = this.distributeLock.tryLockRun(lockName, this.runnable(pjp, r), lock.waitTime(), lock.leaseTime(), lock.timeUnit());
        }
        //4、上锁失败，抛出异常
        if (!lockResult) {
            throw BusinessExceptionUtils.businessException(lock.message());
        }
        //5、返回结果
        return r.getResult();
    }

    public Runnable runnable(ProceedingJoinPoint pjp, R r) {
        return () -> {
            try {
                Object obj = pjp.proceed();
                r.setResult(obj);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Data
    @Builder
    private static class R {
        private Object result;
    }

    /**
     * 验证 @Lock 注解
     *
     * @param lock
     */
    private void validate(Lock lock) {
        if (StringUtils.isBlank(lock.lockName())) {
            throw BusinessExceptionUtils.businessException("lockName must not be blank");
        }

        if (lock.waitTime() <= 0) {
            throw BusinessExceptionUtils.businessException("waitTime > 0");
        }

        if (!(lock.leaseTime() == -1 || lock.leaseTime() > 0)) {
            throw BusinessExceptionUtils.businessException("leaseTime=-1 || leaseTime>0");
        }

        if (lock.timeUnit() == null) {
            throw BusinessExceptionUtils.businessException("timeUnit not null");
        }
        if (StringUtils.isBlank(lock.message())) {
            throw BusinessExceptionUtils.businessException("message must not be blank");
        }
    }

    /**
     * 解析spel表达式得到最终的 lockName
     *
     * @param pjp
     * @param lock
     * @return
     */
    private String getLockName(ProceedingJoinPoint pjp, Lock lock) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, pjp.getArgs(), parameterNameDiscoverer);
        Expression expression = this.expressionParser.parseExpression(lock.lockName());
        return expression.getValue(context, String.class);
    }
}
