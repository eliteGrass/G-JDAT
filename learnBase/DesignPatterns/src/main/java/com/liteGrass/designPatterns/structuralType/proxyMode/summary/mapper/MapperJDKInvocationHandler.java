package com.liteGrass.designPatterns.structuralType.proxyMode.summary.mapper;


import cn.hutool.core.annotation.AnnotationUtil;
import com.liteGrass.designPatterns.structuralType.proxyMode.summary.util.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ClassName: MapperHandler
 * @Author: liteGrass
 * @Date: 2025/11/4 16:58
 * @Description: 进行代理
 */
public class MapperJDKInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(MapperJDKInvocationHandler.class);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ConnectionUtils.Connection conn = ConnectionUtils.getConnection();
        Object insertSql = AnnotationUtil.getAnnotationValue(method, Insert.class);
        log.info("执行语句：{}", insertSql);
        return null;
    }


}
