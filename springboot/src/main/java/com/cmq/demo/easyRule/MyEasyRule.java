package com.cmq.demo.easyRule;

import com.cmq.demo.freemarker.OrderVo;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.support.ActivationRuleGroup;

/**
 * Created by chen.ming.qian on 2021/3/26.
 */
public class MyEasyRule extends ActivationRuleGroup {
 public static void  main(String [] args) {
     //定义事实数据

     ActivationRuleGroup activationRuleGroup = new ActivationRuleGroup("order_type_rule","订单类型规则");
     //普通类型
     Rule ordinaryRule = new RuleBuilder()
         .name("ordinary_order_rule")
         .description("普通订单类型")
         .when(vo -> vo.get("type").equals(1))
         .then(vo -> System.out.println("这是一个普通订单"))
         .build();
     activationRuleGroup.addRule(ordinaryRule);
     //折扣类型
     TestBaseRule testBaseRule = new TestBaseRule();
     Rule discountRule = new RuleBuilder()
         .name("discount_order_rule")
         .description("折扣订单类型")
         .when(facts->testBaseRule.when(facts))
         .then(facts -> {
             testBaseRule.then(facts);
         })
         .build();
     activationRuleGroup.addRule(discountRule);
     //注册
     Rules rules = new Rules();
     rules.register(activationRuleGroup);
     //启动点火
     RulesEngine rulesEngine = new DefaultRulesEngine();
     Facts facts = new Facts();
     facts.put("type", 1);
     facts.put("mode", 1);
     rulesEngine.fire(rules, facts);
 }
}
