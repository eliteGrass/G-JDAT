package com.liteGrass.designPatterns.structuralType.proxyMode.jdkProxy;

import com.liteGrass.designPatterns.structuralType.proxyMode.staticProxyMode.IRentHouse;
import com.liteGrass.designPatterns.structuralType.proxyMode.staticProxyMode.RentHouseImpl;

import java.lang.reflect.Proxy;

/**
 * @Author liteGrass
 * @Date 2025-02-08  21:37
 * @Description
 */
public class JdkProxyMainApp {
    public static void main(String[] args) {
        RentHouseImpl rentHouse = new RentHouseImpl();

        /**
         * 第一个参数：类加载器
         * 第二个参数：被代理类实现的接口，可以是多个
         * 第三个参数：自定义InvocationHandler增强接口
         */
        IRentHouse rentHouseProxy = (IRentHouse) Proxy.newProxyInstance(rentHouse.getClass().getClassLoader(), rentHouse.getClass().getInterfaces(),
                new RentHouseHandler(rentHouse));
        rentHouseProxy.rentHouse();
    }
}
