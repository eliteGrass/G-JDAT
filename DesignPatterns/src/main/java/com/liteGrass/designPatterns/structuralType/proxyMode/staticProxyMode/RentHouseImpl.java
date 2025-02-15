package com.liteGrass.designPatterns.structuralType.proxyMode.staticProxyMode;

/**
 * @Author liteGrass
 * @Date 2025-02-08  21:28
 * @Description
 */
public class RentHouseImpl implements IRentHouse {
    @Override
    public void rentHouse() {
        System.out.println("租房");
    }
}
