package com.cmq.demo.schema.abs;

/**
 * Created by chen.ming.qian on 2020/5/23.
 */
public interface Car {
    default public void color() {
        System.out.println("default color ");
    }
    default public void price() {
        System.out.println("0元");
    }

    public void brand();
}
