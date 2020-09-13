package com.cmq.demo.tpltest;


import java.util.HashMap;
import java.util.Map;

public class AbstractHandlerRegistry implements HandlerRegistry {

    private Map<Class<?>, NodeHandler> registryMapping = new HashMap<>();

    @Override
    public void register(Class<?> type, NodeHandler nodeHandler) {
        registryMapping.put(type, nodeHandler);
    }

    @Override
    public void unregister(Class<?> type) {
        registryMapping.remove(type);
    }

    @Override
    public NodeHandler getHandler(Class<?> type) {
        return registryMapping.get(type);
    }

    @Override
    public boolean contain(Class<?> type) {
        return registryMapping.containsKey(type);
    }
}
