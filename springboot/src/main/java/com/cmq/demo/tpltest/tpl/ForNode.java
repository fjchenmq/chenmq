package com.cmq.demo.tpltest.tpl;

import com.cmq.demo.tpltest.TplContext;

/**
 * 对于for节点，需要关注变量,值表达式
 */
public class ForNode implements TplNode {

    /**
     * 变量
     * .
     */

    private String variable;

    // 这种设计不好，不如转换成if parentValue != null + for value as variable
//    // 父值表达式
//    private String pValueExpr;

    /**
     * 值表达式
     * .
     */

    private String valueExpr;

    /**
     * 循环体
     * .
     */
    private TplNode content;

    public ForNode(String variable, String valueExpr, TplNode content) {
        this.content = content;
        this.valueExpr = valueExpr;
        this.variable = variable;
    }

    public String getValueExpr() {
        return this.valueExpr;
    }


    @Override
    public void apply(TplContext context) {

    }
}
