package com.liteGrass.designPatterns.structuralType.decoratorMode;

/**
 * @Author liteGrass
 * @Date 2025-01-20  23:27
 * @Description 装饰抽象类
 */
public abstract class CofferDecorator implements ICoffer {

    private ICoffer coffer;

    public CofferDecorator(ICoffer coffer) {
        this.coffer = coffer;
    }

    @Override
    public ICoffer makeCoffer(String type) {
        return coffer.makeCoffer(type);
    }
}
