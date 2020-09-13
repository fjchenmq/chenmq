package com.cmq.demo.schema.abs;

import org.springframework.stereotype.Service;

/**
 * Created by chen.ming.qian on 2020/5/23.
 */
@Service
public class BMW implements Car {
    @Override
    public void color() {
        System.out.println("红色");
    }

    @Override
    public void brand() {
        System.out.println("宝马");
    }
}
