package com.cmq.demo.json2tree;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cmq.demo.json2tree.AddUUIDJsonUtil.addUUID;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.c;

/**
 * Created by chen.ming.qian on 2020/9/12.
 */
public class FlatteningJsonUtil {

    /**
     * 拉平叶子节点和数组节点的数据
     *
     * @param sourceJsonObject
     * @return
     */
    public static Map<String, Object> flattening(JSONObject jsonObject) {
        Map<String, Object> valueMap = new HashMap();
        Map<String, Object> tempMap = new HashMap();
        parseJson(jsonObject, null, valueMap);

        return valueMap;
    }

    /**
     * @param node
     * @return
     */
    public static String getParentTempMapKey(String nodePath) {
        String path = "";
        if (nodePath.lastIndexOf(Json2TreeUtil.SPLIT_CHAR) != -1) {
            path = nodePath.substring(0, nodePath.lastIndexOf(Json2TreeUtil.SPLIT_CHAR));
        } else {
            path = nodePath;
        }
        return path;
    }

    /**
     * 拉平叶子节点和数组节点的数据
     *
     * @param sourceJsonObject
     * @return
     */
    public static void parseJson(JSONObject sourceJsonObject, NodeInfo parentNode,
        Map<String, Object> valueMap) {
        sourceJsonObject.forEach((key, value) -> {
            if (value instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) value;
                NodeInfo nodeInfo = putMap(jsonObject, key, parentNode, valueMap,null);
                parseJson(jsonObject, nodeInfo, valueMap);
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = ((JSONArray) value);
                String nodePath = parentNode == null ?
                    key :
                    (parentNode.getNodePath() + Json2TreeUtil.SPLIT_CHAR + key);
                NodeInfo nodeInfo = newInstance(null, null, null, nodePath);
                processArray(jsonArray, nodeInfo, valueMap);
            } else {
                if (!key.equals(AddUUIDJsonUtil.UUID) && !key.equals(AddUUIDJsonUtil.PUUID)) {
                    putMap(sourceJsonObject, key, parentNode, valueMap,value);
                }
            }
        });

    }

    /**
     * 重构JsonObject 为每个非叶子数据添加关系ID
     *
     * @param sourceJsonObject
     * @return
     */
    public static void processArray(JSONArray jsonArray, NodeInfo parentNode,
        Map<String, Object> valueMap) {
        jsonArray.stream().forEach(item -> {
            if (item instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) item;
                NodeInfo nodeInfo = putMap(jsonObject, null, parentNode, valueMap,null);
                parseJson(jsonObject, parentNode, valueMap);
            } else if (item instanceof JSONArray) {
                processArray((JSONArray) item, parentNode, valueMap);
            } else {
                //这种格式数组先不考虑
                //[1,2]
            }
        });

    }

    /**
     * @param valueMap
     * @param tempMap
     * @param nodeInfo
     */
    public static NodeInfo putMap(JSONObject jsonObject, String key, NodeInfo parentNode,
        Map<String, Object> valueMap,Object value) {
        String puuid = jsonObject.getString(AddUUIDJsonUtil.PUUID);
        String uuid = jsonObject.getString(AddUUIDJsonUtil.UUID);
        String nodePath = parentNode == null ?
            key :
            (parentNode.getNodePath() + (key == null ? "" : (Json2TreeUtil.SPLIT_CHAR + key)));
        NodeInfo nodeInfo = newInstance(value==null?null:value.toString(), uuid, puuid, nodePath);
        if (!valueMap.containsKey(nodeInfo.getNodePath())) {
            valueMap.put(nodeInfo.getNodePath(), nodeInfo);
        } else {
            //如果有就构造list
            Object temp = valueMap.get(nodeInfo.getNodePath());
            if (temp instanceof List) {
                ((List) temp).add(nodeInfo);
                valueMap.put(nodeInfo.getNodePath(), temp);
            } else {
                List<NodeInfo> list = new ArrayList<NodeInfo>();
                list.add((NodeInfo) temp);
                list.add(nodeInfo);
                valueMap.put(nodeInfo.getNodePath(), list);
            }
        }
        return nodeInfo;
    }

    /**
     * @param value
     * @param uuid
     * @param puuid
     * @param nodePath
     * @return
     */
    public static NodeInfo newInstance(String value, String uuid, String puuid, String nodePath) {
        NodeInfo instance = NodeInfo.newInstance();
        instance.setNodePath(nodePath);
        instance.setPuuid(puuid);
        instance.setUuid(uuid);
        instance.setValue(value);
        return instance;
    }

    public static void main(String[] args) throws Exception {
        String file = "/template/order.json";
        String json = "";
        ClassPathResource resource = new ClassPathResource(file);
        if (resource.exists()) {
            json = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines()
                .collect(Collectors.joining(System.lineSeparator()));
        }
        Map<String, Object> valueMap = new HashMap();
        JSONObject result = addUUID(JSONObject.parseObject(json));
        System.out.println(result);
        valueMap = flattening(result);
        System.out.println(valueMap);
    }
}
