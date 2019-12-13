package com.cmq.demo.schema.proxy;

import javax.sound.midi.Soundbank;

/**
 * Created by Administrator on 2019/11/21.
 */
public class ProxyTest {
    public static void main(String[] args) {
        Car audi = Audi.newInstance();
        Car audiProxy = AudiProxy.newInstance();
        System.out.println(audi.run());

        System.out.println("---------------代理模式---------------");
        //代理 不直接调用类本身 而是通过调用代理类
        System.out.println(audiProxy.run());
    }
}
