package com.liteGrass.designPatterns.structuralType.proxyMode.cglibProxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author liteGrass
 * @Date 2025-02-08  21:46
 * @Description
 */
public class RentHouseInterceptor implements MethodInterceptor {
    
    /**
     * @description: 动态代理
     * @author: liteGrass 
     * @date: 2025/2/8 21:47
     * @param: [o: 被代理对象, method：调用的方法, objects：参数, methodProxy：原始方法]
     * @return: java.lang.Object
     **/
    
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("中介联系商谈费用");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("售后服务");
        return result;
    }
    
}
