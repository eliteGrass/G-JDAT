package com.liteGrass;

import com.liteGrass.designPatterns.structuralType.decoratorMode.MilkDecorator;
import com.liteGrass.designPatterns.structuralType.decoratorMode.OriginalCoffer;
import com.liteGrass.designPatterns.structuralType.decoratorMode.SugarDecorator;

public class Main {
    public static void main(String[] args) {
        OriginalCoffer originalCoffer = new OriginalCoffer();

        MilkDecorator milkDecorator = new MilkDecorator(originalCoffer);
        milkDecorator.makeCoffer("type");

        SugarDecorator sugarDecorator = new SugarDecorator(milkDecorator);
        sugarDecorator.makeCoffer("type");


    }
}