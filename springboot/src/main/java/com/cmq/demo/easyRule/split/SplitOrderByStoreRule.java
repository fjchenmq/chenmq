package com.cmq.demo.easyRule.split;

import org.jeasy.rules.api.Facts;

/**
 * Created by chen.ming.qian on 2021/3/30.
 */
public class SplitOrderByStoreRule extends BaseRule {
    public void then(Facts facts) {
        System.out.println("根据商户拆分");
        //params.put("result","执行结果");
    }
}
