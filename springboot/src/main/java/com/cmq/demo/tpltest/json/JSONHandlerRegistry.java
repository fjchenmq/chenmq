package com.cmq.demo.tpltest.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmq.demo.tpltest.AbstractHandlerRegistry;
import com.cmq.demo.tpltest.HandlerRegistry;

public class JSONHandlerRegistry extends AbstractHandlerRegistry {

    public static HandlerRegistry singleton() {
        return new JSONHandlerRegistry();
    }

    private JSONHandlerRegistry() {
        register(JSONObject.class, new JSONObjectHandler());
        register(JSONArray.class, new JSONArrayHandler());
    }
}
