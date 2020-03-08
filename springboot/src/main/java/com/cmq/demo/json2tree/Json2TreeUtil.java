package com.cmq.demo.json2tree;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Json2TreeUtil {
    public static final String SALES_ORDER = "/template/sales-order.json";
    private static NodeVo node;
    private static String splitChar = "/";

    static {
        node = toTree(SALES_ORDER);
    }

    /**
     * /**
     * 从 JSON 文件读取菜单列表
     */
    public static NodeVo toTree(String file) {
        NodeVo nodeVo = new NodeVo();
        try {
            ClassPathResource resource = new ClassPathResource(file);
            if (resource.exists()) {
                String data = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
                JSONObject jsonObject = JSONObject.fromObject(data);
                JSONObject requestObject = jsonObject.getJSONObject("contractRoot")
                    .getJSONObject("svcCont").getJSONObject("requestObject");
                nodeVo = buildNode(nodeVo, "requestObject", "requestObject", 0L, null, true, false,
                    null);
                nodeVo.setLastChildrenId(nodeVo.getId() + 10);
                nodeVo = parseJson(null, nodeVo, requestObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodeVo;
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-07 22:57:18
     * @description
     */
    public static NodeVo buildNode(NodeVo node, String name, String namePath, Long id, Long pId,
        boolean open, boolean isArray, String icon) {
        node.setIsArray(isArray);
        node.setName(name);
        node.setNamePath(namePath);
        node.setOpen(open);
        node.setId(id);
        node.setPId(pId);
        node.setIcon(icon);
        return node;

    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-07 22:57:18
     * @description
     */
    public static NodeVo buildNewNode(String name, String namePath, Long id, Long pId, boolean open,
        boolean isArray, String icon) {
        NodeVo node = new NodeVo();
        node = buildNode(node, name, namePath, id, pId, open, isArray, icon);
        return node;

    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-07 22:57:18
     * @description
     */
    public static NodeVo buildChildrenNode(NodeVo parentNode, String name, String namePath, Long id,
        Long pId, boolean open, boolean isArray, String icon) {
        NodeVo node = buildNewNode(name, namePath, id, pId, open, isArray, icon);
        parentNode.setLastChildrenId(id + 1);
        parentNode.getChildren().add(node);
        node.setLastChildrenId(id * 100);
        return node;

    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-07 22:57:12
     * @description
     */
    public static NodeVo parseJson(NodeVo parentNode, NodeVo nodeVo, JSONObject jsonObject) {
        Iterator<String> it = jsonObject.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object object = jsonObject.get(key);
            if (object instanceof JSONObject) {
                NodeVo childrenNode = buildChildrenNode(nodeVo, key,
                    nodeVo.getNamePath() + splitChar + key, nodeVo.getLastChildrenId(),
                    nodeVo.getId(), false, false, null);
                parseJson(nodeVo, childrenNode, (JSONObject) object);
            } else if (object instanceof JSONArray) {
                NodeVo childrenNode = buildChildrenNode(nodeVo, key,
                    nodeVo.getNamePath() + splitChar + key, nodeVo.getLastChildrenId(),
                    nodeVo.getId(), false, true, null);
                JSONArray jsonArray = ((JSONArray) object);
                JSONObject array = jsonArray.getJSONObject(0);
                //暂时不考虑数组下直接跟着数组,或者是单个值的数据情况
                parseJson(nodeVo, childrenNode, array);
            } else {
                NodeVo childrenNode = buildChildrenNode(nodeVo, key,
                    nodeVo.getNamePath() + splitChar + key, nodeVo.getLastChildrenId(),
                    nodeVo.getId(), false, false, null);
            }
        }
        return nodeVo;
    }
}
