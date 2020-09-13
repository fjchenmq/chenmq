package com.cmq.demo.schema.abs;

/**
 * Created by chen.ming.qian on 2020/5/23.
 */
public abstract class CarAbstract {
    public void  color(){
        System.out.println("default color ");
    };
    public void  price(){
        System.out.println("0元");
    };
    public  abstract void brand();
}
