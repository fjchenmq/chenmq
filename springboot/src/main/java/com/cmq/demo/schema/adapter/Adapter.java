package com.cmq.demo.schema.adapter;

/**
 * Created by Administrator on 2019/11/21.
 */
public class Adapter extends Adaptee implements Target {
    @Override
    public void getKey() {
        System.out.println("开车前得先拿车钥匙");
    }

public static Adapter newInstance() {
    Adapter instance = new Adapter();
    return instance;
}
}
