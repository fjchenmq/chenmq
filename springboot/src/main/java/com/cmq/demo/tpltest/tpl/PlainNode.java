package com.cmq.demo.tpltest.tpl;

import com.cmq.demo.tpltest.TplContext;

/**
 * 简单类型
 * .
 */
public class PlainNode implements TplNode {

    private String keyExpr;

    private String valueExpr;

    public PlainNode(String keyExpr, String valueExpr) {
        this.keyExpr = keyExpr;
        this.valueExpr = valueExpr;
    }

    @Override
    public void apply(TplContext context) {

    }

    public String getKeyExpr() {
        return keyExpr;
    }

    public String toString() {
        return "key:" + keyExpr + "  " + "value:" + valueExpr;
    }
}
