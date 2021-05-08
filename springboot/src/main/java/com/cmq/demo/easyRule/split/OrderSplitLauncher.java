package com.cmq.demo.easyRule.split;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.jeasy.rules.support.ActivationRuleGroup;
import org.jeasy.rules.support.CompositeRule;
import org.jeasy.rules.support.ConditionalRuleGroup;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by chen.ming.qian on 2021/3/25.
 */
public class OrderSplitLauncher {
    public static void main(String[] args) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            logger.setLevel(Level.ERROR);
        });
        // create rules
        Rules rules = new Rules();
        CompositeRule ruleGroup;
        //组互斥 串行 全部互斥 依赖
        // ruleGroup = new ActivationRuleGroup("互斥", "订单拆分规则");//互斥
        //只要一个不符合，就都不执行了，就是要么都执行，要么都不执行
        //ruleGroup = new UnitRuleGroup("要么都执行，要么都不执行", "订单拆分规则");
        //条件规则组是一个组合规则，其中具有最高优先级的规则作为条件：如果具有最高优先级的规则求值为true，则将触发其余规则 找到其他的符合的rule并执行。
        ruleGroup = new ConditionalRuleGroup("依赖", "订单拆分规则");//priority 越大越优先
        ruleGroup.setPriority(-1);

        BaseRule splitOrderByStoreRule = new SplitOrderByStoreRule();
        splitOrderByStoreRule.setPriority(1);
        splitOrderByStoreRule.setName("splitOrderByStoreRule");
        splitOrderByStoreRule.setDescription("splitOrderByStoreRule");
        ruleGroup.addRule(BaseRule.newRule(splitOrderByStoreRule));

        BaseRule splitOrderBySpuRule = new SplitOrderBySpuRule();
        splitOrderBySpuRule.setPriority(2);
        splitOrderBySpuRule.setName("splitOrderBySpuRule");
        splitOrderBySpuRule.setDescription("splitOrderBySpuRule");
        ruleGroup.addRule(BaseRule.newRule(splitOrderBySpuRule));

        CompositeRule ruleGroup2 = new ActivationRuleGroup("互斥", "订单拆分规则");//priority 越大越优先
        ruleGroup2.setPriority(-2);

        BaseRule splitOrderBySkuRule = new SplitOrderBySkuRule();
        splitOrderBySkuRule.setPriority(3);
        splitOrderBySkuRule.setName("splitOrderBySkuRule");
        splitOrderBySkuRule.setDescription("splitOrderBySkuRule");
        ruleGroup2.addRule(BaseRule.newRule(splitOrderBySkuRule));

        BaseRule splitOrderByOrderTypeRule = new SplitOrderByOrderTypeRule();
        splitOrderByOrderTypeRule.setPriority(4);
        splitOrderByOrderTypeRule.setName("splitOrderByOrderTypeRule");
        splitOrderByOrderTypeRule.setDescription("splitOrderByOrderTypeRule");
        ruleGroup2.addRule(BaseRule.newRule(splitOrderByOrderTypeRule));


        //ruleGroup.addRule(ruleGroup2);

        ConditionalRuleGroup conditionalRuleGroup = new ConditionalRuleGroup("依赖规则组", "订单拆分规则");//priority 越大越优先
        conditionalRuleGroup.addRule(ruleGroup);
        conditionalRuleGroup.addRule(ruleGroup2);//依赖于 ruleGroup ruleGroup2哪个优先的是否执行


        rules.register(conditionalRuleGroup);

        //按优先级顺序执行每个规则  priority 越小越优先
       /* rules.register(BaseRule.newRule(splitOrderByStoreRule));
        rules.register(BaseRule.newRule(splitOrderBySpuRule));
        rules.register(BaseRule.newRule(splitOrderBySkuRule));
        rules.register(BaseRule.newRule(splitOrderByOrderTypeRule));*/

        // create facts
        Facts facts = new Facts();

        Order order = new Order();
        order.setOrderNbr("001");
        order.setStoreId("1");
        order.setSpuType("2");
        order.setSkuType("3");
        order.setOrderType("4");
        facts.put("order", order);
        facts.put("splitOrderByStoreRule", buildSplitOrderByStoreRulePlan());
        facts.put("splitOrderBySpuRule", buildSplitOrderBySpuRulePlan());
        facts.put("splitOrderByOrderTypeRule", buildSplitOrderByOrderTypeRulePlan());
        facts.put("splitOrderBySkuRule", buildSplitOrderBySkuRulePlan());
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);

    }

    public static RulePlan buildSplitOrderByStoreRulePlan() {
        RulePlan splitOrderByStoreRule = new RulePlan();
        RuleCondition ruleCondition1 = new RuleCondition();
        ruleCondition1.setVariableName("storeId");
        ruleCondition1.setVariablePath("storeId");
        RuleCondition ruleCondition2 = new RuleCondition();
        ruleCondition2.setVariableName("spuType");
        ruleCondition2.setVariablePath("spuType");
        splitOrderByStoreRule.getRuleConditions().add(ruleCondition1);
        //splitOrderByStoreRule.getRuleConditions().add(ruleCondition2);
        // splitOrderByStoreRule.setExpression("$IN(storeId,\"1\") && $IN(spuType,\"2\")");
        splitOrderByStoreRule.setExpression("$IN(storeId,\"1\")");
        return splitOrderByStoreRule;
    }

    public static RulePlan buildSplitOrderBySpuRulePlan() {
        RulePlan splitOrderBySpuRule = new RulePlan();
        RuleCondition ruleCondition1 = new RuleCondition();
        ruleCondition1.setVariableName("storeId");
        ruleCondition1.setVariablePath("storeId");
        RuleCondition ruleCondition2 = new RuleCondition();
        ruleCondition2.setVariableName("spuType");
        ruleCondition2.setVariablePath("spuType");
        //splitOrderBySpuRule.getRuleConditions().add(ruleCondition1);
        splitOrderBySpuRule.getRuleConditions().add(ruleCondition2);
        splitOrderBySpuRule.setExpression("$IN(spuType,\"2\")");
        return splitOrderBySpuRule;
    }

    public static RulePlan buildSplitOrderBySkuRulePlan() {
        RulePlan splitOrderBySkuRule = new RulePlan();
        RuleCondition ruleCondition1 = new RuleCondition();
        ruleCondition1.setVariableName("skuType");
        ruleCondition1.setVariablePath("skuType");
        RuleCondition ruleCondition2 = new RuleCondition();
        ruleCondition2.setVariableName("spuType");
        ruleCondition2.setVariablePath("spuType");
        //splitOrderBySpuRule.getRuleConditions().add(ruleCondition1);
        splitOrderBySkuRule.getRuleConditions().add(ruleCondition1);
        splitOrderBySkuRule.setExpression("$IN(skuType,\"3\")");
        return splitOrderBySkuRule;
    }

    public static RulePlan buildSplitOrderByOrderTypeRulePlan() {
        RulePlan splitOrderByOrderTypeRule = new RulePlan();
        RuleCondition ruleCondition1 = new RuleCondition();
        ruleCondition1.setVariableName("orderType");
        ruleCondition1.setVariablePath("orderType");
        RuleCondition ruleCondition2 = new RuleCondition();
        ruleCondition2.setVariableName("spuType");
        ruleCondition2.setVariablePath("spuType");
        //splitOrderBySpuRule.getRuleConditions().add(ruleCondition1);
        splitOrderByOrderTypeRule.getRuleConditions().add(ruleCondition1);
        splitOrderByOrderTypeRule.setExpression("$IN(orderType,\"4\")");
        return splitOrderByOrderTypeRule;
    }
}
