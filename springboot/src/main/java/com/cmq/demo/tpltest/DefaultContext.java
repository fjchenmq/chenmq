package com.cmq.demo.tpltest;

import org.apache.commons.lang.StringUtils;

import java.util.Stack;

public class DefaultContext implements TplContext {

    /**
     * 顶级context是没有parent的
     * .
     */
    private HierarchicalContext parent = null;

    private Stack<String> contextStack = new Stack<>();

    private VariableStrategy variableStrategy = new DefaultVariableStrategy();

    public DefaultContext(String initContext) {
        pushContext(initContext);
    }

    public DefaultContext() {
    }

    @Override
    public String getTplExpr(String key) {
        if (contextStack.size() > 0) {
            return this.getContextPath() + "." + key;
        }

        return key;
    }

    @Override
    public void pushContext(String key) {
        contextStack.push(key);
    }

    @Override
    public void popContext() {
        contextStack.pop();
    }

    @Override
    public String getContext() {
        return contextStack.peek();
    }

    @Override
    public String getContextPath() {
        StringBuilder sb = new StringBuilder();

        if (contextStack.size() > 0) {
            sb.append(contextStack.elementAt(0));
        }

        if (contextStack.size() > 1) {
            for (int i = 1; i < contextStack.size(); i++) {
                sb.append(".")
                        .append(contextStack.elementAt(i));
            }
        }

        return sb.toString();
    }

    @Override
    public String createContextVariable() {
        String context = getContext();

        if (!StringUtils.isEmpty(context)) {
            return this.getTop().getVariable(context);
        }
        return null;
    }

    @Override
    public HierarchicalContext getParent() {
        return this.parent;
    }

    @Override
    public void addParent(HierarchicalContext parent) {
        this.parent = parent;
    }

    @Override
    public VariableStrategy getTop() {
        if (this.getParent() != null) {
            return this.getParent().getTop();
        }
        else {
            return this.variableStrategy;
        }

    }

    public String toString() {
        return getContextPath();
    }
}
