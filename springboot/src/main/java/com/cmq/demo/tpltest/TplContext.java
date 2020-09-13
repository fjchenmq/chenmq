package com.cmq.demo.tpltest;

/**
 * 用于保存节点处理结果
 * .
 */
public interface TplContext extends HierarchicalContext {

    public String getTplExpr(String key);

    public void pushContext(String key);

    public void popContext();

    public String getContext();

    public String getContextPath();

    public String createContextVariable();


}
