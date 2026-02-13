package com.liteGrass.designPatterns.structuralType.proxyMode.summary;


import com.liteGrass.designPatterns.structuralType.proxyMode.summary.mapper.MapperCglibInterceptor;
import com.liteGrass.designPatterns.structuralType.proxyMode.summary.mapper.MapperJDKInvocationHandler;
import com.liteGrass.designPatterns.structuralType.proxyMode.summary.mapper.UserMapper;
import com.liteGrass.designPatterns.structuralType.proxyMode.summary.service.UserService;
import com.liteGrass.designPatterns.structuralType.proxyMode.summary.service.UserServiceImpl;
import com.liteGrass.designPatterns.structuralType.proxyMode.summary.service.UserServiceProxy;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * @ClassName: TestMainApp
 * @Author: liteGrass
 * @Date: 2025/11/11 9:16
 * @Description:
 */
public class TestMainApp {

    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "cglib_proxy_classes");
        System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");


        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserMapper.class);
        enhancer.setClassLoader(TestMainApp.class.getClassLoader());
        enhancer.setCallback(new MapperCglibInterceptor());
        UserMapper cglibUserMapper = (UserMapper) enhancer.create();

        UserService userService = new UserServiceProxy(new UserServiceImpl(
                (UserMapper) Proxy.newProxyInstance(UserMapper.class.getClassLoader(), new Class[] {UserMapper.class}, new MapperJDKInvocationHandler())
        ));
        userService.save();
    }

}
