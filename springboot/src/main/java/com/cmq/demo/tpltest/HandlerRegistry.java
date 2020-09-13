package com.cmq.demo.tpltest;

public interface HandlerRegistry {

    void register(Class<?> type, NodeHandler nodeHandler);

    void unregister(Class<?> type);

    NodeHandler getHandler(Class<?> type);

    boolean contain(Class<?> type);

}
