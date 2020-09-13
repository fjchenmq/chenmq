package com.cmq.demo.tpltest.tpl;

import com.cmq.demo.tpltest.TplContext;

/**
 * 生成if节点，关注条件
 */
public class IfNode implements TplNode {

    private String cond;

    private TplNode content;

    public IfNode(String cond, TplNode content) {
        this.cond = cond;
        this.content = content;
    }

    @Override
    public void apply(TplContext context) {

    }
}
