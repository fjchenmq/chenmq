package com.cmq.demo.easyRule.split;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen.ming.qian on 2021/3/31.
 */
@Data
public class RulePlan {
    private String expression;
    private List<RuleCondition> ruleConditions = new ArrayList<>();
}
