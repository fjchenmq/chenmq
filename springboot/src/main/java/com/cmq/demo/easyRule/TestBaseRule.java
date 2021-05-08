package com.cmq.demo.easyRule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.api.Facts;

/**
 * Created by chen.ming.qian on 2021/4/2.
 */
public class TestBaseRule {
    public boolean when(Facts facts) {
        System.out.println("aaaaaaaaa");
        return true;
    }
    public void then(Facts facts) {
        System.out.println("这是一个折扣订单");
        System.out.println(facts);
    }
}
