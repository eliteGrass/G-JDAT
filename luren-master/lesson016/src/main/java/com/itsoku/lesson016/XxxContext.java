package com.itsoku.lesson016;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/10 13:58 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class XxxContext {
    private  M1Request request;
    private Obj1 obj1;
    private Obj2 obj2;
    private Obj3 obj3;

    public M1Request getRequest() {
        return request;
    }

    public void setRequest(M1Request request) {
        this.request = request;
    }

    public Obj1 getObj1() {
        return obj1;
    }

    public void setObj1(Obj1 obj1) {
        this.obj1 = obj1;
    }

    public Obj2 getObj2() {
        return obj2;
    }

    public void setObj2(Obj2 obj2) {
        this.obj2 = obj2;
    }

    public Obj3 getObj3() {
        return obj3;
    }

    public void setObj3(Obj3 obj3) {
        this.obj3 = obj3;
    }
}
