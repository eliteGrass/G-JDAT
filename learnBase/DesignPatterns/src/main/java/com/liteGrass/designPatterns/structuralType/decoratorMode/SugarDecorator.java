package com.liteGrass.designPatterns.structuralType.decoratorMode;

/**
 * @Author liteGrass
 * @Date 2025-01-20  23:32
 * @Description
 */
public class SugarDecorator extends CofferDecorator {
    public SugarDecorator(ICoffer coffer) {
        super(coffer);
    }

    @Override
    public ICoffer makeCoffer(String type) {
        ICoffer coffer = super.makeCoffer(type);
        System.out.println("加糖");
        return coffer;
    }
}
