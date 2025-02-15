package com.liteGrass.designPatterns.structuralType.proxyMode.staticProxyMode;

/**
 * @Author liteGrass
 * @Date 2025-02-08  21:28
 * @Description
 */
public class RentHouseProxy implements IRentHouse {
    private IRentHouse rentHouse;

    public RentHouseProxy(IRentHouse rentHouse) {
        this.rentHouse = rentHouse;
    }

    @Override
    public void rentHouse() {
        System.out.println("商谈中介费用");
        rentHouse.rentHouse();
        System.out.println("售后");
    }
}
