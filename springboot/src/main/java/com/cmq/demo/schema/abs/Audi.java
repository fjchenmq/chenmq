package com.cmq.demo.schema.abs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.apache.coyote.http11.Constants.a;

/**
 * Created by chen.ming.qian on 2020/5/23.
 */
@Service
public class Audi extends CarAbstract {
    @Override
    public void brand() {
        System.out.println("奥迪");
    }
    @Override
    public void price() {
        System.out.println("100w");
    }

}
