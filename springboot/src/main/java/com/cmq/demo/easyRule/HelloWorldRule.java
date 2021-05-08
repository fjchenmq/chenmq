package com.cmq.demo.easyRule;

import com.cmq.demo.IKExpression.IKExpressionUtil;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import java.util.Map;

/**
 * Created by chen.ming.qian on 2021/3/25.
 */
@Rule(name = "Hello World rule", description = "Always say hello world")
public class HelloWorldRule {

    @Condition
    public boolean when(@Fact("expression") String expression,@Fact("params") Map<String,Object> params) throws Exception {
       return (Boolean)IKExpressionUtil.excute(expression,params);
    }
    @Action(order = 1)
    public void then(@Fact("params") Map<String,Object> params) {
        System.out.println(params);
        //params.put("result","执行结果");
    }
    @Action(order = 2)
    public void then2()   {
        System.out.println("then2....");
        //my final actions
    }
}
