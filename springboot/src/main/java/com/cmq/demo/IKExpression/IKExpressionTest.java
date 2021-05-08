package com.cmq.demo.IKExpression;

import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.PreparedExpression;
import org.wltea.expression.datameta.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen.ming.qian on 2021/3/25.
 */
public class IKExpressionTest {
    public static void main(String[] args) {
        //定义表达式
        String expression = "(a > 1 || b < 2) && c < 3 ";
        //给表达式中的变量 [版本] 付上下文的值
        List<Variable> variables = new ArrayList<Variable>();
        variables.add(Variable.createVariable("a", 2));
        variables.add(Variable.createVariable("b", 2));
        variables.add(Variable.createVariable("c", 5));
        //预编译表达式
        PreparedExpression pe = ExpressionEvaluator.preparedCompile(expression, variables);
        //执行表达式
        Object result = pe.execute();
        System.out.println("Result = " + result);

        String arg = "IK Expression";

        //定义表达式
        String exp = "\"Hello World \" + 用户名";
        //给表达式中的变量 "用户名" 付上下文的值
        variables.add(Variable.createVariable("userName", arg));
        //执行表达式
        result = ExpressionEvaluator.evaluate(exp, variables);

        System.out.println("Result = " + result);

        exp ="a==b || b==c";
        //执行表达式
        result = ExpressionEvaluator.evaluate(exp, variables);

        System.out.println("Result = " + result);

        exp="$IN(cc,\"0\")";
        variables.add(Variable.createVariable("cc", "0"));
        result = ExpressionEvaluator.evaluate(exp, variables);

        System.out.println("Result = " + result);


        exp="(value==\"0\")?\"独享\":(value==\"1\")?\"共享\":\"\"";
        variables.add(Variable.createVariable("value", "0"));
        result = ExpressionEvaluator.evaluate(exp, variables);

        System.out.println("Result = " + result);



    }
}
