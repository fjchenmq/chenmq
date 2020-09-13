package com.cmq.demo.tpltest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmq.demo.tpltest.json.JSONHandlerRegistry;
import com.cmq.demo.tpltest.tpl.TplNode;

public class JSONTest {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("{    \"header\": { \"reqTime\": \"2020-02-20\", \"tranId\": \"00001\"    },")
            .append(
                "\"orders\": [ {     \"buyer\": \"买家1\",     \"orderAttr\": [  {      \"color\": \"白色,\"  },  {      \"size\": \"35\"  }     ],     \"orderId\": \"1\" },")
            .append(
                "{     \"buyer\": \"买家2\",     \"orderAttr\": [  {      \"color\": \"绿色,\"  },  {      \"size\": \"38\"  }     ],     \"orderId\": \"2\" }    ]}");

        JSONObject jo = JSON.parseObject(sb.toString());

        HandlerRegistry handlerRegistry = JSONHandlerRegistry.singleton();
        NodeHandler nodeHandler = handlerRegistry.getHandler(jo.getClass());

        if (nodeHandler != null) {
            TplContext tplContext = new DefaultContext("request");
            TplNode tplNode = nodeHandler.handleNode(jo, tplContext);
            System.out.println(tplNode.toString());
        }

        System.out.println(1);
    }
}
