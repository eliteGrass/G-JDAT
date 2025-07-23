package com.itsoku.lesson016;

public class XxxService3 {

    /**
     * 这个方法是对外暴露的接口
     *
     * @param request
     */
    public void execute(M1Request request) {
        XxxContext context = new XxxContext();
        //m1方法中会产生Obj1,Obj2
        this.m1(context);

        //下面m2方法中要用到m1方法中产生的Obj1、Obj2
        this.m2(context);

        //m3方法中又会用到4个参数：request、obj1、obj2、obj3
        this.m3(context);

        // 下面还有其他业务方法，内部也会产生一些对象，后续一些方法可能需要用到这些对象，有没有更好的解决方案呢？
        this.otherMethod(context);
    }

    private void m1(XxxContext context) {
        System.out.println(context.getRequest());
        Obj1 obj1 = new Obj1();
        Obj2 obj2 = new Obj2();

        context.setObj1(obj1);
        context.setObj2(obj2);
    }

    private void m2(XxxContext context) {
        //这里需要用到 obj1,obj2
        System.out.println(context.getObj1());
        System.out.println(context.getObj2());

        Obj3 obj3 = new Obj3();
        context.setObj3(obj3);
    }

    private void m3(XxxContext context) {
        System.out.println(context.getRequest());
        System.out.println(context.getObj1());
        System.out.println(context.getObj2());
        System.out.println(context.getObj3());
    }

    private void otherMethod(XxxContext context) {

    }

}