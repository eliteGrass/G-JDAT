package com.itsoku.lesson016;

public class XxxService {

    /**
     * 这个方法是对外暴露的接口
     *
     * @param request
     */
    public void execute(M1Request request) {
        //m1方法中会产生Obj1,Obj2
        this.m1(request);

        //下面m2方法中要用到m1方法中产生的Obj1、Obj2，而m2本身又会返回obj3
        Obj1 obj1 = null;
        Obj2 obj2 = null;
        Obj3 obj3 = this.m2(obj1, obj2);

        //m3方法中又会用到4个参数：request、obj1、obj2、obj3
        this.m3(request, obj1, obj2, obj3);
    }

    private void m1(M1Request request) {
        System.out.println(request);

        //这里会产生2个对象
        Obj1 obj1 = new Obj1();
        Obj2 obj2 = new Obj2();
    }

    private Obj3 m2(Obj1 obj1, Obj2 obj2) {
        System.out.println(obj1);
        System.out.println(obj2);

        //这里需要用到 obj1,obj2
        Obj3 obj3 = new Obj3();
        return obj3;
    }

    private void m3(M1Request request, Obj1 obj1, Obj2 obj2, Obj3 obj3) {
        System.out.println(request);
        System.out.println(obj1);
        System.out.println(obj2);
        System.out.println(obj3);
    }

}