package com.liteGrass.designPatterns.structuralType.proxyMode.summary.mapper;


import cn.hutool.core.annotation.AnnotationUtil;
import com.liteGrass.designPatterns.structuralType.proxyMode.summary.util.ConnectionUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName: MapperCglibInteceptor
 * @Author: liteGrass
 * @Date: 2025/11/11 16:28
 * @Description: Cglib代理
 */
@Slf4j
public class MapperCglibInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        ConnectionUtils.Connection conn = ConnectionUtils.getConnection();
        Object insertSql = AnnotationUtil.getAnnotationValue(method, Insert.class);
        log.info("执行语句：{}", insertSql);
        return null;
    }
}
