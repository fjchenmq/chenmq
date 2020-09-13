package com.cmq.demo.tpltest;

public interface HierarchicalContext {

    HierarchicalContext getParent();

    void addParent(HierarchicalContext parent);

    public VariableStrategy getTop();
}
