package com.cmq.demo.json2tree;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @param null
 * @author chenmq
 * @version 2020-03-11 15:03:10
 * @description
 * @return
 */
public class Json2TreeUtil {
    public static final String SALES_ORDER       = "/templates/sales-order.json";
    public static final String OPPORTUNITY_ORDER = "/templates/opportunity-order.json";
    private static NodeVo SALES_ORDER_TREE;
    private static NodeVo OPPORTUNITY_ORDER_TREE;
    public static final String              icon        = "oc/template/default/oc/img/tpl-node.png";
    private static      Map<String, String> nodePathMap = new HashMap<>();
    public static final String              SPLIT_CHAR  = ".";

    static {
        SALES_ORDER_TREE = toTree(SALES_ORDER);
        OPPORTUNITY_ORDER_TREE = toTree(OPPORTUNITY_ORDER);
    }

    /**
     * /**
     * 从 JSON 文件读取菜单列表
     */
    public static NodeVo toTree(String file) {
        NodeVo nodeVo = new NodeVo();
        nodePathMap.clear();
        try {
            ClassPathResource resource = new ClassPathResource(file);
            if (resource.exists()) {
                String data = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
                JSONObject jsonObject = JSONObject.fromObject(data);
                String rootPath = "";
                JSONObject requestObject;
                if (!StringUtils.isEmpty(rootPath)) {
                    requestObject = jsonObject.getJSONObject(rootPath);
                } else {
                    requestObject = jsonObject;
                }

                String rootNamePath = "";
                nodeVo = buildNode(nodeVo, rootPath, rootNamePath, "0", null, true, false, null,
                    false);
                nodeVo.setLastChildrenId((Long.parseLong(nodeVo.getId()) + 10) + "");
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
    public static NodeVo buildNode(NodeVo node, String name, String namePath, String id, String pId,
        boolean open, boolean array, String icon, boolean isLeaf) {
        node.setArray(array);
        node.setName(name);
        node.setNamePath(namePath);
        node.setOpen(open);
        node.setId(id + "");
        node.setPId(pId + "");
        node.setIcon(icon);
        node.setIsLeaf(isLeaf);
        return node;

    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-07 22:57:18
     * @description
     */
    public static NodeVo buildNewNode(String name, String namePath, String id, String pId,
        boolean open, boolean isArray, String icon, boolean isLeaf) {
        NodeVo node = new NodeVo();
        node = buildNode(node, name, namePath, id, pId, open, isArray, icon, isLeaf);
        return node;

    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-07 22:57:18
     * @description
     */
    public static NodeVo buildChildrenNode(NodeVo parentNode, String name, String namePath,
        String id, String pId, boolean open, boolean isArray, String icon, boolean isLeaf) {
        NodeVo node = buildNewNode(name, namePath, id, pId, open, isArray, icon, isLeaf);
        parentNode.setLastChildrenId((Long.parseLong(id) + 1) + "");
        parentNode.getChildren().add(node);
        node.setLastChildrenId((Long.parseLong(id) * 100) + "");
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
                String namePath = getNamePath(nodeVo, key);
                if (!isExist(namePath)) {
                    NodeVo childrenNode = buildChildrenNode(nodeVo, key, namePath,
                        nodeVo.getLastChildrenId(), nodeVo.getId(), false, false, null, false);
                    parseJson(nodeVo, childrenNode, (JSONObject) object);
                }
            } else if (object instanceof JSONArray) {
                String namePath = getNamePath(nodeVo, key);
                if (!isExist(namePath)) {
                    NodeVo childrenNode = buildChildrenNode(nodeVo, key, namePath,
                        nodeVo.getLastChildrenId(), nodeVo.getId(), false, true, null, false);
                    JSONArray jsonArray = ((JSONArray) object);
                    int size = jsonArray.size();
                    //暂不考虑数组下 直接挂数组 和数值下只有一个值的情况
                    for (int i = 0; i < size; i++) {
                        JSONObject array = jsonArray.getJSONObject(i);
                        parseJson(nodeVo, childrenNode, array);
                    }
                }
            } else {
                String namePath = getNamePath(nodeVo, key);
                if (!isExist(namePath)) {
                    NodeVo childrenNode = buildChildrenNode(nodeVo, key, namePath,
                        nodeVo.getLastChildrenId(), nodeVo.getId(), false, false, icon, true);
                }
            }
        }
        return nodeVo;
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-27 14:41:44
     * @description
     */
    public static String getNamePath(NodeVo nodeVo, String key) {
        String namePath = "";
        if (StringUtils.isEmpty(nodeVo.getNamePath())) {
            namePath = key;
        } else {
            namePath = nodeVo.getNamePath() + SPLIT_CHAR + key;
        }
        return namePath;
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-13 11:28:03
     * @description 根据选中的ID查询下级节点，并把下级节点拉平成List
     */
    public static List<NodeVo> getTreeNodeChildren(String orderType, Long id, String protocolType) {
        NodeVo rootNode = null;
        //Json2TreeUtil.getOrderTree(orderType, protocolType);
        NodeVo targetNode = null;
        targetNode = findChildren(targetNode, rootNode, id);
        List<NodeVo> nodeList = new ArrayList<>();
        listTreeNode(targetNode, nodeList);
        return nodeList;
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-13 15:58:03
     * @description并把下级节点拉平成List
     */
    public static void listTreeNode(NodeVo pNode, List<NodeVo> nodeList) {
        NodeVo node = buildNewNode(pNode.getName(), pNode.getNamePath(), pNode.getId(),
            pNode.getPId(), pNode.getOpen(), pNode.getArray(), pNode.getIcon(), pNode.getIsLeaf());
        nodeList.add(node);
        if (!pNode.getChildren().isEmpty()) {
            pNode.getChildren().stream().forEach(el -> {
                listTreeNode(el, nodeList);
            });
        }
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-03-13 15:15:18
     * @description
     */
    public static NodeVo findChildren(NodeVo targetNode, NodeVo pNode, Long id) {
        if (pNode.getId().equals(id)) {
            targetNode = pNode;
            return targetNode;
        }
        //if (!pNode.getChildren().isEmpty()) {
        for (NodeVo nodeVo : pNode.getChildren()) {
            if (targetNode == null) {
                //已找到退出递归
                targetNode = findChildren(targetNode, nodeVo, id);
            }
        }
        //}
        return targetNode;
    }

    /**
     * @param namePath
     * @return
     */
    public static boolean isExist(String namePath) {
        if (nodePathMap.containsKey(namePath)) {
            return true;
        }
        nodePathMap.put(namePath, namePath);
        return false;
    }

}
