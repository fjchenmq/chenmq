package com.cmq.demo.IKExpression;

import com.cmq.demo.easyRule.split.RuleCondition;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlException;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.PreparedExpression;
import org.wltea.expression.datameta.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen.ming.qian on 2021/3/25.
 */
public class IKExpressionUtil {

    public static final String left  = "${";
    public static final String right = "}";

    public static Object excute(String expression, Map<String, Object> params) {
        Object result;
        List<Variable> variables = new ArrayList<Variable>();
        List<String> variableNames = new ArrayList<>();
        /*parseExpression(expression, variableNames);
        String runExpression = expression.replaceAll("\\$\\{", "");
        runExpression = runExpression.replaceAll("\\}", "");
        //给表达式中的变量 [版本] 付上下文的值
        variableNames.stream().forEach(variableName -> {
            variables.add(Variable.createVariable(variableName, params.get(variableName)));
        });*/
        params.keySet().stream().forEach(key -> {
            variables.add(Variable.createVariable(key, params.get(key)));
        });

        //预编译表达式
        PreparedExpression pe = ExpressionEvaluator.preparedCompile(expression, variables);
        //执行表达式
        result = pe.execute();
        return result;
    }

    /**
     * @param expression
     * @param rlt
     */
    public static void parseExpression(String expression, List<String> variableNames) {
        if (expression.indexOf(left) == -1 || expression.indexOf(right) == -1) {
            return;
        }
        String variableName = expression
            .substring(expression.indexOf(left) + 2, expression.indexOf(right));
        String remainStr = expression.substring(expression.indexOf(right) + 1, expression.length());
        variableNames.add(variableName);
        parseExpression(remainStr, variableNames);
    }

    /**
     * @param rootObject
     * @param valuesPath
     * @return
     */
    public static Map<String, Object> buildValue(Object rootObject,
        List<RuleCondition> ruleConditions ) {
        Map<String, Object> params = new HashMap();

        // 构建一个OgnlContext对象
        OgnlContext context = new OgnlContext();
        context.put("order", rootObject);
        // 将员工设置为根对象
        context.setRoot(rootObject);
        ruleConditions.forEach((ruleCondition) -> {
            try {
                // 构建Ognl表达式的树状表示,用来获取
                Object expression = Ognl.parseExpression("#order." + ruleCondition.getVariablePath());
                Object value = Ognl.getValue(expression, context, context.getRoot());
                params.put(ruleCondition.getVariableName(), value);
            } catch (OgnlException e) {
                e.printStackTrace();
            }
        });

        return params;
    }

    public static void main(String[] args) {
        List<String> variableNames = new ArrayList<>();
        String expression = "(a>1 || b<2) && c>3 ";
        expression = "$IN(d,\"2,3,4\")";
        //expression = "$MATCHES(${d},\"2\")";

        Map<String, Object> params = new HashMap<>();
        params.put("a", 2);
        params.put("b", 2);
        params.put("c", 4);
        params.put("d", "32");
        Object rlt = excute(expression, params);
        System.out.println(rlt);
    }
}
