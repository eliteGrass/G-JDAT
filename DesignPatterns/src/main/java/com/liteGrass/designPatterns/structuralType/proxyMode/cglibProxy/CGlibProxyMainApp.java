package com.liteGrass.designPatterns.structuralType.proxyMode.cglibProxy;

import com.liteGrass.designPatterns.structuralType.proxyMode.staticProxyMode.IRentHouse;
import com.liteGrass.designPatterns.structuralType.proxyMode.staticProxyMode.RentHouseImpl;
import net.sf.cglib.proxy.Enhancer;

/**
 * @Author liteGrass
 * @Date 2025-02-08  21:50
 * @Description
 */
public class CGlibProxyMainApp {
    public static void main(String[] args) {
        // 创建代理增强类
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(RentHouseImpl.class.getClassLoader());
        // 设置被代理的类
        enhancer.setSuperclass(RentHouseImpl.class);
        // 设置方法拦截器
        enhancer.setCallback(new RentHouseInterceptor());
        //创建
        IRentHouse rentHouseProxy = (IRentHouse) enhancer.create();
        rentHouseProxy.rentHouse();
    }
}
