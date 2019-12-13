package com.cmq.demo.schema.proxy;

import static javafx.scene.input.KeyCode.V;

/**
 * Created by Administrator on 2019/1/21.
 */
public class AudiProxy implements Car{
    @Override
    public String run() {
        refuel();
        return Audi.newInstance().run();
    }

    public void refuel() {
        System.out.println("开之前先加个油");
    }
    public static AudiProxy newInstance() {
        AudiProxy instance = new AudiProxy();
        return instance;
    }
}
