package com.cmq.demo.schema.abs;

/**
 * Created by chen.ming.qian on 2020/5/23.
 */
public class Test {
    public static void  main(String [] args) {
        CarAbstract audi = new Audi();
        audi.brand();
        audi.price();
        audi.color();

        Car bmw = new BMW();
        bmw.brand();
        bmw.price();
        bmw.color();
    }
}
