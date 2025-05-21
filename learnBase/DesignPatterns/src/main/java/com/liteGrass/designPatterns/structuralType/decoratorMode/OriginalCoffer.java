package com.liteGrass.designPatterns.structuralType.decoratorMode;

/**
 * @Author liteGrass
 * @Date 2025-01-20  23:26
 * @Description
 */
public class OriginalCoffer implements ICoffer {
    @Override
    public ICoffer makeCoffer(String type) {
        System.out.println("生产原味咖啡");
        return new OriginalCoffer();
    }
}
