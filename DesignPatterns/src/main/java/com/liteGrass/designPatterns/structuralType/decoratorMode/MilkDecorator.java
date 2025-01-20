package com.liteGrass.designPatterns.structuralType.decoratorMode;

/**
 * @Author liteGrass
 * @Date 2025-01-20  23:30
 * @Description
 */
public class MilkDecorator extends CofferDecorator {

    public MilkDecorator(ICoffer coffer) {
        super(coffer);
    }

    @Override
    public ICoffer makeCoffer(String type) {
        ICoffer coffer = super.makeCoffer(type);
        System.out.println("加奶");
        return coffer;
    }
}
