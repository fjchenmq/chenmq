package com.cmq.demo.json2tree;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Iterator;
import java.util.UUID;

/**
 * Created by chen.ming.qian on 2020/8/18.
 */
public class BuildJsonTemplateUtil {
    public static final String              SPLIT_CHAR  = ".";

    /**
     * @param null
     * @return 数组只保留一个, 并把报文的节点值生成一个唯一的值
     * @author chenmq
     * @version 2020-08-18 15:06:32
     * @description
     */
    public static final JSONObject emphasisRepeat(JSONObject jsonObject) {
        Iterator<String> it = jsonObject.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object object = jsonObject.get(key);
            if (object instanceof JSONObject) {
                emphasisRepeat((JSONObject) object);
            } else if (object instanceof JSONArray) {
                JSONArray jsonArray = ((JSONArray) object);
                int size = jsonArray.size();
                //如果数组下值结构不一致会有问题 如： "orderAttr": [ {  "color": "白色,"  }, { "size": "35" } ]
                for (int i = size - 1; i >= 0; i--) {
                    if (i > 0) {
                        jsonArray.remove(jsonArray.get(i));
                    }
                }
                //TODO 数组下 直接跟数组 还未测试
                if (!jsonArray.isEmpty() && (jsonArray.get(0) instanceof JSONObject)) {
                    emphasisRepeat((JSONObject) jsonArray.get(0));
                }
            } else {
                //jsonObject.put(key, UUID.randomUUID().toString());
            }
        }
        return jsonObject;
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-08-18 16:45:02
     * @description
     */
    public static final JSONObject buildJsonTemplate(JSONObject jsonObject,String nodePath) {
        Iterator<String> it = jsonObject.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object object = jsonObject.get(key);
            if (object instanceof JSONObject) {
                buildJsonTemplate((JSONObject) object,nodePath);
            } else if (object instanceof JSONArray) {
                JSONArray jsonArray = ((JSONArray) object);
                //经过第一步去重后，数组下都只会有一个元素
                //TODO 数组下 直接跟数组 还未测试
                if (jsonArray.get(0) instanceof JSONObject) {
                    buildJsonTemplate((JSONObject) jsonArray.get(0),nodePath);
                } else {
                    Object value = jsonArray.get(0);
                }
            } else {
                //具体值 开始替换
                buildNodeTemplate(jsonObject, key, "${request.head.tranId}");
            }
        }
        return jsonObject;
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-08-18 17:21:00
     * @description
     */
    public static final JSONObject buildArrayNodeTemplate(JSONObject jsonObject, String key,
        String valueExpress) {
        jsonObject.put(key, valueExpress);
        return jsonObject;
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-08-18 17:21:00
     * @description
     */
    public static final JSONObject buildNodeTemplate(JSONObject jsonObject, String key,
        String valueExpress) {
        jsonObject.put(key, valueExpress);
        return jsonObject;
    }
}
