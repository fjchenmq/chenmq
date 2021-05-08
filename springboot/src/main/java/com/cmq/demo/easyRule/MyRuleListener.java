package com.cmq.demo.easyRule;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;

/**
 * Created by chen.ming.qian on 2021/3/25.
 */
public class MyRuleListener implements RuleListener {
    @Override
    public boolean beforeEvaluate(Rule rule, Facts facts) {
        return true;
    }

    @Override
    public void afterEvaluate(Rule rule, Facts facts, boolean b) {
        System.out.println("---MyRuleListener------afterEvaluate-----");
    }

    @Override
    public void beforeExecute(Rule rule, Facts facts) {
        System.out.println("---MyRuleListener------beforeExecute-----");
    }

    @Override
    public void onSuccess(Rule rule, Facts facts) {
        System.out.println("---MyRuleListener------onSuccess-----");
    }

    @Override
    public void onFailure(Rule rule, Facts facts, Exception e) {

        System.out.println("---MyRuleListener------onFailure-----");
    }
}
