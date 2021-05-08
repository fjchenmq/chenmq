package com.cmq.demo.easyRule;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.cmq.demo.easyRule.split.Order;
import com.cmq.demo.easyRule.split.RuleCondition;
import com.cmq.demo.easyRule.split.RulePlan;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen.ming.qian on 2021/3/25.
 */
public class RuleLauncher {
    public static void main(String[] args) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
           // logger.setLevel(Level.ERROR);
        });
        // create rules
        Rules rules = new Rules();

        rules.register(new HelloWorldRule());
        // create a rules engine and fire rules on known facts
        /**
         *
         * Priority：优先级，关于其他规则的规则优先级(在别的文档中看到，如果值越小优先级越高。即：0值优先级高于1值)
         *
         复合规则的三种实现  互斥，全部互斥，依赖，串行
         1、UnitRuleGroup：只要一个不符合，就都不执行了，就是要么都执行，要么都不执行
         2、ActivationRuleGroup：选择第一个，其他的就不执行了，它会触发第一个适用规则，而忽略该组中的其他规则（XOR逻辑,全互斥）。规则首先按组中的自然顺序（默认情况下为优先级）排序。
         3、ConditionalRuleGroup：条件规则组是一个组合规则，其中具有最高优先级的规则作为条件：如果具有最高优先级的规则求值为true，则将触发其余规则 找到其他的符合的rule并执行。

         *skipOnFirstAppliedRule 为true时， 从第一条开始，匹配一条就会跳过后面规则匹配，不匹配则一直往下执行
         skipOnFirstFailedRule 为true时， 如果执行@Action中发生异常就会跳过后面规则匹配
         skipOnFirstNonTriggeredRule 为true时，按照优先级从高到低的顺序进行判断，如果满足当前的规则，则执行相应的操作，直到遇到不满足条件的规则为止，并且也不会剩下的规则进行判断了
         rulePriorityThreshold 大于指定的优先级则不进行匹配
         多条配置可以一起使用。
         */
        RulesEngineParameters parameters = new RulesEngineParameters()
            .skipOnFirstAppliedRule(true);
        RulesEngine rulesEngine = new DefaultRulesEngine(parameters);
        //((DefaultRulesEngine) rulesEngine).registerRuleListener(new MyRuleListener());

        String expression = "(a>1 || b<2) && c>5 ";
        expression = "$IN(d,\"6,7,9\")";
        //expression = "$MATCHES(${d},\"2\")";

        Map<String, Object> params = new HashMap<>();
        params.put("a", 2);
        params.put("b", 2);
        params.put("c", 1);
        params.put("d", "1");
        // create facts
        Facts facts = new Facts();
        facts.put("expression", expression);
        facts.put("params", params);

        //https://blog.csdn.net/ruben95001/article/details/111496433

        RulePlan splitOrderByStoreRule = new RulePlan();
        RuleCondition ruleCondition1 = new RuleCondition();
        ruleCondition1.setVariableName("storeId");
        ruleCondition1.setVariablePath("storeId");
        RuleCondition ruleCondition2 = new RuleCondition();
        ruleCondition2.setVariableName("spuType");
        ruleCondition2.setVariablePath("spuType");
        splitOrderByStoreRule.setExpression("$IN(storeId,\"1\") && $IN(spuType,\"2\")");
        Order order =new Order();
        order.setSpuType("2");
        order.setStoreId("1");
        facts.put("order",order);
        facts.put("splitOrderByStoreRule", splitOrderByStoreRule);
        rulesEngine.fire(rules, facts);

    }
}
