package com.cmq.demo.tpltest.tpl;

import com.cmq.demo.tpltest.TplContext;

/**
 * 特化的if节点，条件就是判断属性是否存在
 * TODO 这里需要考虑下，因为exits已经是方言的内容了，所以不应该作为if的子节点
 */
public class ExistsNode implements TplNode {

    private String valueExpr;

    private TplNode content;

    public ExistsNode(String valueExpr, TplNode content) {
        this.valueExpr = valueExpr;
        this.content = content;
    }

    @Override
    public void apply(TplContext context) {

    }
}
