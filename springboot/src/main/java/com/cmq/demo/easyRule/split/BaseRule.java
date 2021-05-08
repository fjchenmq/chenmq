package com.cmq.demo.easyRule.split;

import com.cmq.demo.IKExpression.IKExpressionUtil;
import lombok.Data;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.RuleBuilder;

import java.util.Map;

/**
 * Created by chen.ming.qian on 2021/3/31.
 */
@Data
public abstract class BaseRule {
    String name;
    String description;
    int priority;
    public boolean when(Facts facts) {
        Order order = facts.get("order");
        RulePlan rulePlan = facts.get(this.name);
        Map<String, Object> values = IKExpressionUtil
            .buildValue(order, rulePlan.getRuleConditions());
        Boolean bool = (Boolean) IKExpressionUtil.excute(rulePlan.getExpression(), values);
        return bool;
    }
    public static Rule newRule(BaseRule bizRule) {
        Rule rule = new RuleBuilder()
            .name(bizRule.getName())
            .description(bizRule.getDescription())
            .priority(bizRule.getPriority())
            .when(facts->bizRule.when(facts))
            .then(facts -> {
                bizRule.then(facts);
            })
            .build();
        return rule;
    }

    public abstract void then(Facts facts);

}
