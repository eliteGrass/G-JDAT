package com.liteGrass.designPatterns.structuralType.proxyMode.jdkProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author liteGrass
 * @Date 2025-02-08  21:32
 * @Description
 */
public class RentHouseHandler implements InvocationHandler {

    /**
     * 被代理对象
     */
    private Object target;

    public RentHouseHandler(Object target) {
        this.target = target;
    }

    /**
     * @description: 代理
     * @author: liteGrass
     * @date: 2025/2/8 21:33
     * @param: [proxy:生成的代理对象, method：代理对象调用的方法, args：方法的参数信息]
     * @return: java.lang.Object
     **/

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("中介联系商谈费用");
        Object result = method.invoke(target, args);
        System.out.println("售后服务");
        return result;
    }
}
