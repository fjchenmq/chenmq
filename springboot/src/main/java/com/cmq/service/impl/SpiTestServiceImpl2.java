package com.cmq.service.impl;

import com.cmq.service.SpiTestService;

/**
 * Created by chenmq on 2018/11/2.
 */
public class SpiTestServiceImpl2 implements SpiTestService {
    @Override
    public void helloSpi(String name) {
        System.out.println(" hello spi2 "+name);
    }
}
