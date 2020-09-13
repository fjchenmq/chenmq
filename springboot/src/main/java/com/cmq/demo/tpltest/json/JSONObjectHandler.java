package com.cmq.demo.tpltest.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmq.demo.tpltest.HandlerRegistry;
import com.cmq.demo.tpltest.NodeHandler;
import com.cmq.demo.tpltest.TplContext;
import com.cmq.demo.tpltest.tpl.MultiNode;
import com.cmq.demo.tpltest.tpl.PlainNode;
import com.cmq.demo.tpltest.tpl.TplNode;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * fastjson对复杂对象会转换成JSONObject，对于列表会转换成JSONArray，对于简单的key/value结构会作为Map节点的元素
 */
public class JSONObjectHandler implements JSONNodeHandler<JSONObject> {

    @Override
    public TplNode handleNode(JSONObject node, TplContext context) {
        // 将JSONObject转换成MulitNode，每个属性转化成TplNode
        // 根据value的类型进行转换
        // 如果是JSONArray，通过JSONArrayHandler进行处理
        // 如果是JSONObject,通过JSONObjectHandler处理
        // 否则转换成PlainNode即可
        if (node.size() > 0) {
            MultiNode multiNode = new MultiNode();
            Set<Map.Entry<String, Object>> entrySet = node.entrySet();

            Iterator<Map.Entry<String, Object>> iter = entrySet.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();

                String key = entry.getKey();
                Object value = entry.getValue();

                if (value != null) {
                    NodeHandler nodeHandler = getHandlerRegistry().getHandler(value.getClass());
                    if (nodeHandler != null) {

                        context.pushContext(key);
                        TplNode tplNode = nodeHandler.handleNode((JSON) value, context);
                        context.popContext();

                        if (tplNode != null) {
                            multiNode.addNode(tplNode);
                            continue;
                        }
                    }
                }

                PlainNode plainNode = new PlainNode(key, context.getTplExpr(key));
                multiNode.addNode(plainNode);
            }

            return multiNode;
        }


        return null;
    }

    private HandlerRegistry getHandlerRegistry() {
        return JSONHandlerRegistry.singleton();
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();




        sb.append("{    \"header\": { \"reqTime\": \"2020-02-20\", \"tranId\": \"00001\"    },")
            .append("\"orders\": [ {     \"buyer\": \"买家1\",     \"orderAttr\": [  {      \"color\": \"白色,\"  },  {      \"size\": \"35\"  }     ],     \"orderId\": \"1\" },")
            .append("{     \"buyer\": \"买家2\",     \"orderAttr\": [  {      \"color\": \"绿色,\"  },  {      \"size\": \"38\"  }     ],     \"orderId\": \"2\" }    ]}");

        JSONObject jo = JSON.parseObject(sb.toString());

        System.out.println(1);

    }
}
