package com.cmq.demo.easyRule;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;

/**
 * Created by chen.ming.qian on 2021/3/25.
 */
public class FizzBuzzMainWithRule {
    public static void main(String[] args) {
        // create a rules engine
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        RulesEngine fizzBuzzEngine = new DefaultRulesEngine(parameters);
        // create rules
        Rules rules = new Rules();
        rules.register(new ZzRuleClass.FizzRule());
        rules.register(new ZzRuleClass.BuzzRule());
        rules.register(new ZzRuleClass.NonFizzBuzzRule());
        // fire rules
        /**
         *如果一个数字可以被5整除，则输出“fizz”；
         如果一个数字可以被7整除，则输出“buzz”；
         如果一个数字不满足以上2个条件，则输出这个数字本身。
         */
        Facts facts = new Facts();
        for (int i = 1; i <= 36; i++) {
            facts.put("number", i);
            fizzBuzzEngine.fire(rules, facts);
        }
    }
}
