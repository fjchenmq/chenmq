package com.cmq.demo.freemarker;

import com.cmq.demo.json2tree.BuildFreemarkerMapUtil;
import com.cmq.demo.json2tree.Json2TreeUtil;
import com.cmq.demo.json2tree.NodeVo;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cmq.demo.json2tree.AddUUIDJsonUtil.addUUID;
import static com.cmq.demo.json2tree.BuildFreemarkerMapUtil.buildFreemarkerMap;
import static com.cmq.demo.json2tree.FlatteningJsonUtil.flattening;

/**
 * Created by chen.ming.qian on 2020/8/17.
 */
public class FreeMarkerTest {
    public static void main(String[] args) throws IOException, TemplateException {
        // parseFreeMarkerJavaBean();
        parseFreeMarkerMap();




      /* //IS_FINAL=2
       Map<String,Object> custOrders = new HashMap<>();
        List<JSONObject> orderAttrs = new ArrayList<>();
        custOrders.put("custOrders",orderAttrs);

        JSONObject orderattrJsonObject = new JSONObject();
        String custName ="zhangsan";//从源报文中获取
        String finalValue_custName ="{}";//从源报文中获取
        String custAge="30";//从源报文中获取
        String finalValue_custAge ="{}";//从源报文中获取
        orderattrJsonObject.put("custName",custName);
        orderattrJsonObject.put("custAge",custAge);
        custOrders.put("orderattrJsonObject",orderattrJsonObject);*/

       /* //IS_FINAL=3
        Map<String,Object> custOrders = new HashMap<>();

        List<JSONObject> orderAttrs= new ArrayList<>();
        JSONObject custNameJsonObject = new JSONObject();
        String custName ="zhangsan";//从源报文中获取
        String finalValue_custName ="{\"attrCode\":\"custName\",\"attrName\":\"客户名称\"}";//从源报文中获取
        custNameJsonObject = JSONObject.fromObject(finalValue_custName);
        custNameJsonObject.put("attrValue",custName);
        orderAttrs.add(custNameJsonObject);


        JSONObject custAgeJsonObject = new JSONObject();
        String custAge="30";//从源报文中获取
        String finalValue_custAge ="{\"attrCode\":\"custAge\",\"attrName\":\"年龄\"}";//从源报文中获取
        custAgeJsonObject = JSONObject.fromObject(finalValue_custAge);
        custAgeJsonObject.put("custAge",custAge);
        orderAttrs.add(custAgeJsonObject);

        custOrders.put("custOrders",orderAttrs);*/

        //IS_FINAL=4
        Map<String, Object> custOrders = new HashMap<>();
        List<JSONObject> sourceOrderAttrs = new ArrayList<>();//从源报文中获取
        JSONObject sourceOrderAttr1 = new JSONObject();
        sourceOrderAttr1.put("attrValueS", "值1");
        sourceOrderAttr1.put("attrNameS", "扩展属性1");
        sourceOrderAttr1.put("attrCodeS", "attrCode1");
        sourceOrderAttrs.add(sourceOrderAttr1);

        JSONObject sourceOrderAttr2 = new JSONObject();
        sourceOrderAttr2.put("attrValueS", "值2");
        sourceOrderAttr2.put("attrNameS", "扩展属性2");
        sourceOrderAttr2.put("attrCodeS", "attrCode2");
        sourceOrderAttrs.add(sourceOrderAttr2);
        String finalValue = "{\"attrValue\":\"attrValueS\", \"attrName\":\"attrNameS\" ,\"attrCode\":\"attrCodeS\"}";
        List<JSONObject> orderAttrs = new ArrayList<>();
        sourceOrderAttrs.stream().forEach((item) -> {
            JSONObject orderAttr1 = new JSONObject();
            JSONObject.fromObject(finalValue).forEach((key, sourceKey) -> {
                String value = (String) item.get(sourceKey.toString());
                orderAttr1.put(key, value);
            });
            orderAttrs.add(orderAttr1);
        });
        custOrders.put("custOrders", orderAttrs);

        System.out.println(JSONObject.fromObject(custOrders));

    }

    public static final void parseFreeMarkerJavaBean() throws IOException, TemplateException {
        //head.tranId 不是数组 order.orderId 是数组 order.orderAttr.attrName
        /***
         *{    "header": { "reqTime": "2020-02-20", "tranId": "00001"    },
         *  "orders": [ {     "buyer": "买家1",     "orderAttr": [  {      "color": "白色,"  },  {      "size": "35"  }     ],     "orderId": "1" },
         *  {     "buyer": "买家2",     "orderAttr": [  {      "color": "绿色,"  },  {      "size": "38"  }     ],     "orderId": "2" }    ]}
         */

        String tplJson =
            "{\"header\":{\"tranId\":\"${request.head.tranId}\",\"reqTime\": \"${request.head['reqTime']}\" },"
                + "\"orders\":[" + "<#if request?exists && request.orders?exists> "
                + "  <#list request.orders as order>{   "
                + "\"orderId\":\"${order.orderId}\",\"buyer\":\"${order.buyer}\" ,"
                + "\"orderAttr\":[<#list order.orderAttr as orderAttr>"
                + " {<#if orderAttr.attrName='颜色'> \"color\":\"${orderAttr.attrValue},\"  </#if>"
                + "<#if orderAttr.attrName='大小'> \"size\":\"${orderAttr.attrValue}\"  </#if>"
                + "}</#list>] " + "}</#list>" + " </#if> " + "]" + "}";

        String xml =
            "<in> " + "<header> <reqTime>2020-02-20</reqTime> <tranId>00001</tranId> </header> "
                + "<orders> <buyer>买家1</buyer> <orderAttr> <color>白色,</color> </orderAttr> <orderAttr> <size>35</size> </orderAttr> <orderId>1</orderId> </orders>"
                + "<orders> <buyer>买家2</buyer> <orderAttr> <color>绿色,</color> </orderAttr> <orderAttr> <size>38</size> </orderAttr> <orderId>2</orderId> </orders>"
                + "  </in>";
        String tplXml =
            "<in><header><tranId>${request.head.tranId}</tranId><reqTime>${request.head['reqTime']}</reqTime></header>"
                + "<orders>" + "<#if request?exists && request.orders?exists> "
                + "  <#list request.orders as order>"
                + "<orderId>${order.orderId}</orderId><buyer>${order.buyer}</buyer>"
                + "<orderAttr><#list order.orderAttr as orderAttr>"
                + " <#if orderAttr.attrName='颜色'> <color>${orderAttr.attrValue} </color></#if>"
                + "<#if orderAttr.attrName='大小'> <size>${orderAttr.attrValue} </size> </#if>"
                + "</#list></orderAttr> " + "</#list></orders>" + "</#if> " + "</in>";

        //  tplJson ="{\"order\":{" + "<#if request?exists && request.orders?exists> "
        //        + "\"orderId\":\"${request.orders[0].orderId}\",\"buyer\":\"${request.orders[0].buyer}\"" + "</#if>}" + "}";

        StringReader stringReader = new StringReader(tplXml);
        Template template = new Template(null, stringReader, null);

        Map<String, Object> objectMap = new HashMap<String, Object>();
        List orderVos = new ArrayList<OrderVo>();
        OrderVo orderVo1 = new OrderVo();
        orderVo1.setBuyer("买家1");
        orderVo1.setOrderId("1");

        OrderAttrVo orderAttrVo1 = new OrderAttrVo();
        orderAttrVo1.setAttrName("颜色");
        orderAttrVo1.setAttrValue("白色");
        OrderAttrVo orderAttrVo2 = new OrderAttrVo();
        orderAttrVo2.setAttrName("大小");
        orderAttrVo2.setAttrValue("35");
        orderVo1.getOrderAttrList().add(orderAttrVo1);
        orderVo1.getOrderAttrList().add(orderAttrVo2);

        OrderVo orderVo2 = new OrderVo();
        orderVo2.setBuyer("买家2");
        orderVo2.setOrderId("2");
        OrderAttrVo orderAttrVo3 = new OrderAttrVo();
        orderAttrVo3.setAttrName("颜色");
        orderAttrVo3.setAttrValue("绿色");
        OrderAttrVo orderAttrVo4 = new OrderAttrVo();
        orderAttrVo4.setAttrName("大小");
        orderAttrVo4.setAttrValue("38");
        orderVo2.getOrderAttrList().add(orderAttrVo3);
        orderVo2.getOrderAttrList().add(orderAttrVo4);

        orderVos.add(orderVo1);
        orderVos.add(orderVo2);
        RequestVo requestVo = new RequestVo();

        HeadVo headVo = new HeadVo();
        headVo.setReqTime(new Date());
        headVo.setTranId("00001");
        //map.put("head",head);
        requestVo.setHead(headVo);
        requestVo.setOrders(orderVos);

        objectMap.put("request", requestVo);
        //map.put("orders", orderVos);

        StringWriter stringWriter = new StringWriter();

        template.process(objectMap, stringWriter);
        System.out.println(stringWriter.toString());
    }

    public static final void parseFreeMarkerMap() throws IOException, TemplateException {
        String tplJson = "{"
            + "\"head\":{\"tranId\":\"${request.head.tranId}\",\"reqTime\": \"${request.head['reqTime']}\" },"
            + "\"orders\":[" + "<#if request?exists && request.orders?exists> "
            + "  <#list request.orders as order>{   "
            + "\"orderId\":\"${order.orderId}\",\"buyer\":\"${order.buyer}\" ," + "\"orderAttr\":["
            + "<#list order.orderAttr as orderAttr>" + " { " + "<#if  orderAttr.orderAttr2?exists> "
            + ",\"orderAttr2\":[<#list orderAttr.orderAttr2 as orderAttr2>"
            + "{\"attrValue\":\"${orderAttr2.attrValue}\", \"attrName\":\"${orderAttr2.attrName}\", \"attrCode\":\"${orderAttr2.attrCode}\" "

            + "},</#list>] </#if>"

            + ",\"attrValue\":\"${orderAttr.attrValue}\", \"attrName\":\"${orderAttr.attrName}\", \"attrCode\":\"${orderAttr.attrCode}\" }"
            + ",</#list>" + "] " + "},</#list>"//最后一个逗号要考虑怎么处理
            + " </#if> " + "]" + "}";

        StringReader stringReader = new StringReader(tplJson);

        Map map = new HashMap();
        Map request = new HashMap();
        map.put("request", request);

        Map header = new HashMap();
        header.put("tranId", "00001");
        header.put("reqTime", "2020-02-20");

        request.put("head", header);

        List<JSONObject> orders = new ArrayList<>();
        JSONObject orderJsonObject1 = new JSONObject();
        orderJsonObject1.put("orderId", 1);
        orderJsonObject1.put("buyer", "买家");

        List<JSONObject> orderAttrs = new ArrayList<>();
        JSONObject orderAttrsJSONObject1 = new JSONObject();
        orderAttrsJSONObject1.put("attrValue", "36");
        orderAttrsJSONObject1.put("attrName", "大小");
        orderAttrsJSONObject1.put("attrCode", "size");
        JSONObject orderAttrsJSONObject2 = new JSONObject();
        orderAttrsJSONObject2.put("attrValue", "白色");
        orderAttrsJSONObject2.put("attrName", "颜色");
        orderAttrsJSONObject2.put("attrCode", "color");
        orderAttrs.add(orderAttrsJSONObject1);
        orderAttrs.add(orderAttrsJSONObject2);

        orderJsonObject1.put("orderAttr", orderAttrs);

        JSONObject orderJsonObject2 = new JSONObject();
        orderJsonObject2.put("orderId", 2);
        orderJsonObject2.put("buyer", "买家2");

        List<JSONObject> orderAttrs2 = new ArrayList<>();
        JSONObject orderAttrsJSONObject3 = new JSONObject();
        orderAttrsJSONObject3.put("attrValue", "38");
        orderAttrsJSONObject3.put("attrName", "大小");
        orderAttrsJSONObject3.put("attrCode", "size");
        JSONObject orderAttrsJSONObject4 = new JSONObject();
        orderAttrsJSONObject4.put("attrValue", "绿色");
        orderAttrsJSONObject4.put("attrName", "颜色");
        orderAttrsJSONObject4.put("attrCode", "color");
        orderAttrs2.add(orderAttrsJSONObject3);
        orderAttrs2.add(orderAttrsJSONObject4);
        orderJsonObject2.put("orderAttr", orderAttrs2);

        orders.add(orderJsonObject1);
        orders.add(orderJsonObject2);

        request.put("orders", orders);

        JSONObject jsonObject = JSONObject.fromObject(map);

        System.out.println(jsonObject);

        String file = "/template/order.json";
        NodeVo nodeTree = Json2TreeUtil.toTree(file);
        JSONObject nodeTreeJson = JSONObject.fromObject(nodeTree);

        String orderJson = "";
        ClassPathResource resource = new ClassPathResource(file);
        if (resource.exists()) {
            orderJson = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines()
                .collect(Collectors.joining(System.lineSeparator()));
        }
        StringWriter stringWriter = null;
        Long t1 = System.currentTimeMillis();
        int loop = 1000;
        Template template = new Template(null, stringReader, null);

        for (int i = 0; i < loop; i++) {
            Map<String, Object> valueMap = new HashMap();
            com.alibaba.fastjson.JSONObject result = addUUID(
                com.alibaba.fastjson.JSONObject.parseObject(orderJson));
            valueMap = flattening(result);

            Map convertedMap = BuildFreemarkerMapUtil.buildFreemarkerMap(nodeTree, valueMap);

           // System.out.println(nodeTreeJson);

            stringWriter = new StringWriter();
            //template.process(map, stringWriter);
            template.process(convertedMap, stringWriter);
        }
        System.out.println("cost:" + ((System.currentTimeMillis() - t1) / loop));

        System.out.println(111111);
        System.out.println(stringWriter.toString());
        System.out.println(111111);
    }
}
