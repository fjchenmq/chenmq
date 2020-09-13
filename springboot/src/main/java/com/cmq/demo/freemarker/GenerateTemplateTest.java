package com.cmq.demo.freemarker;

import com.cmq.demo.json2tree.BuildJsonTemplateUtil;
import com.cmq.demo.json2tree.Json2TreeUtil;
import com.cmq.demo.json2tree.NodeVo;
import freemarker.template.TemplateException;
import net.sf.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Created by chen.ming.qian on 2020/8/17.
 */
public class GenerateTemplateTest {
    public static void main(String[] args) throws IOException, TemplateException {
        String xml = "<in> <header> <reqTime>2020-02-20</reqTime> <tranId>00001</tranId> </header> <orders> <buyer>买家1</buyer> <orderAttr> <color>白色,</color> </orderAttr> <orderAttr> <size>35</size> </orderAttr> <orderId>1</orderId> </orders> <orders> <buyer>买家2</buyer> <orderAttr> <color>绿色,</color> </orderAttr> <orderAttr> <size>38</size> </orderAttr> <orderId>2</orderId> </orders>   </in>";
        String file = "/template/order.json";
        String json = "";
        ClassPathResource resource = new ClassPathResource(file);
        if (resource.exists()) {
            json = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines()
                .collect(Collectors.joining(System.lineSeparator()));
        }
        System.out.println(json);

        //入参 上级没填 不允许填下级
        int targetArrayOrder = 1;//目标是否数组 1是 0否
        int sourceArrayOrder = 1;//源是否数组 1是 0否
        String targetNodePathOrder = "orders.orderId";
        String sourceNodePathOrder = "request.orders.orderId";

        NodeVo nodeTree = Json2TreeUtil.toTree("/template/order.json");
        JSONObject jsonObject = JSONObject.fromObject(nodeTree);
        System.out.println(jsonObject);

        JSONObject jsonObject1 = JSONObject.fromObject(json);
        jsonObject1 = BuildJsonTemplateUtil.emphasisRepeat(jsonObject1);
        //System.out.println(jsonObject1);

        jsonObject1 = BuildJsonTemplateUtil.buildJsonTemplate(jsonObject1,"");
        System.out.println(jsonObject1);


    }
}
