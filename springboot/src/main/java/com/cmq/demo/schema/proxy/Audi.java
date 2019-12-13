package com.cmq.demo.schema.proxy;

/**
 * Created by Administrator on 2019/11/21.
 */
public class Audi implements Car {
    @Override
    public String run() {
        return "开着奥迪在飞驰";
    }
    public static Audi newInstance() {
        Audi instance = new Audi();
        return instance;
    }
}
