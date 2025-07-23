package com.itsoku.lesson024.log;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsoku.lesson024.ip.IpAddressUtils;
import com.itsoku.lesson024.ip.IpUtils;
import com.itsoku.lesson024.po.OperLogPO;
import com.itsoku.lesson024.service.IOperLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 在返回值中填充traceId，用于方便排查错误
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/24 11:31 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Aspect
@Order
@Component
public class OperLogAspect {
    @Autowired
    private IOperLogService operLogService;
    @Autowired
    private IUserNameProvider userNameProvider;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * 环绕通知，拦截Controller中所有方法上标注有 @OperLog注解的方法，记录日志
     *
     * @param joinPoint
     * @param operLog
     * @return
     * @throws Throwable
     */
    @Around("@annotation(operLog) && execution(* com.itsoku..*Controller.*(..))")
    public Object around(ProceedingJoinPoint joinPoint, OperLog operLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable error = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            error = e;
            throw e;
        } finally {
            this.log(joinPoint, operLog, result, error, startTime);
        }
        return result;
    }

    private void log(ProceedingJoinPoint joinPoint, OperLog operLog, Object result, Throwable error, long startTime) throws JsonProcessingException {
        OperLogPO operLogPO = new OperLogPO();
        //日志id
        operLogPO.setId(IdUtil.fastSimpleUUID());
        //日志信息，直接从注解中获取
        operLogPO.setLog(operLog.log());
        //接口参数，json格式
        operLogPO.setParamJson(jsonString(getParamMap(joinPoint)));
        //返回值，json格式
        operLogPO.setResultJson(jsonString(result));
        //状态，0：异常，1：正常，error不为空表示有异常
        operLogPO.setStatus(error != null ? 0 : 1);
        //记录异常信息
        if (error != null) {
            operLogPO.setErrorMsg(ExceptionUtil.stacktraceToString(error));
        }
        operLogPO.setCostTime(System.currentTimeMillis() - startTime);
        //操作ip
        String operIp = IpUtils.getIpAddr();
        operLogPO.setOperIp(operIp);
        //根据ip获取ip归属地
        operLogPO.setOperIpAddress(IpAddressUtils.getRegion(operIp));
        //通过userNameProvider获取用户名，userNameProvider可以自己去实现
        operLogPO.setOperUserName(this.userNameProvider.getUserName());
        //操作时间
        operLogPO.setOperTime(LocalDateTime.now());
        //写入日志
        this.operLogService.save(operLogPO);
    }

    private String jsonString(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        return mapper.writeValueAsString(obj);
    }

    private Map<String, Object> getParamMap(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取所有要打印的参数，丢到map中，key为参数名称，value为参数的值，然后会将这个map以json格式输出
        Map<String, Object> paramMap = new LinkedHashMap<>();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            //参数名称
            String parameterName = parameterNames[i];
            //参数值
            Object parameterValue = args[i];
            //将其放入到map中，稍后会以json格式输出
            paramMap.put(parameterName, parameterValue);
        }
        return paramMap;
    }

}
