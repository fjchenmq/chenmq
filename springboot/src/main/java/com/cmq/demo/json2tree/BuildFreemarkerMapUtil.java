package com.cmq.demo.json2tree;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

/**
 * Created by chen.ming.qian on 2020/9/10.
 */
public class BuildFreemarkerMapUtil {
    public static final String SPLIT_CHAR = ".";

    /**
     * @param nodeTree
     * @param nodeTreeJson
     * @return 构造freemarker需要的map
     */
    public static Map<String, Object> buildFreemarkerMap(NodeVo nodeTree,
        Map<String, Object> sourceMap) {
        Map<String, Object> targetValueMap = new HashMap();
        Map<String, Object> tempValueMap = new HashMap();
        buildMap(nodeTree.getChildren().get(0), sourceMap, targetValueMap, tempValueMap);
        return targetValueMap;
    }

    /**
     * 递归构造map
     *
     * @param nodeTree
     * @param nodeTreeJson
     * @param targetValueMap
     * @param tempValueMap
     */
    public static void buildMap(NodeVo nodeTree, Map<String, Object> sourceMap,
        Map<String, Object> targetValueMap, Map<String, Object> tempValueMap) {
        if (isNeedBuild(nodeTree)) {

            if (!nodeTree.getIsLeaf()) {
                //非叶子节点
                processCatalogNode(nodeTree, sourceMap, targetValueMap, tempValueMap);
            } else {
                //叶子节点
                processLeafNode(nodeTree, sourceMap, targetValueMap, tempValueMap);
            }
        }
        if (!CollectionUtils.isEmpty(nodeTree.getChildren())) {
            nodeTree.getChildren().stream().forEach((node) -> {
                buildMap(node, sourceMap, targetValueMap, tempValueMap);
            });
        }

    }

    /**
     * 非叶子节点值需要构造空Map
     *
     * @param nodeTree
     * @param nodeTreeJson
     * @param targetValueMap
     * @param tempValueMap
     */
    public static void processCatalogNode(NodeVo nodeTree, Map<String, Object> sourceMap,
        Map<String, Object> targetValueMap, Map<String, Object> tempValueMap) {
        if (BuildFreemarkerMapUtil.isBuildArray(nodeTree)) {
            processArrayCatalogNode(nodeTree, sourceMap, targetValueMap, tempValueMap);
        } else {
            processNormalCatalogNode(nodeTree, sourceMap, targetValueMap, tempValueMap,
                new HashMap());
        }

    }

    /**
     * 处理普通目录节点
     *
     * @param nodeTree
     * @param nodeTreeJson
     * @param targetValueMap
     * @param tempValueMap
     */
    public static void processNormalCatalogNode(NodeVo nodeTree, Map<String, Object> sourceMap,
        Map<String, Object> targetValueMap, Map<String, Object> tempValueMap, Object value) {
        String parentTempMapKey = getParentTempMapKey(nodeTree.getNamePath());
        String tempMapKey = getTempMapKey(nodeTree.getNamePath());
        String mapKey = getMapKey(nodeTree.getNamePath());
        if (tempValueMap.containsKey(parentTempMapKey)) {
            if (tempValueMap.get(parentTempMapKey) instanceof Map) {
                Map<String, Object> parentMap = (Map<String, Object>) tempValueMap
                    .get(parentTempMapKey);
                parentMap.put(mapKey, value);
            } else if (tempValueMap.get(parentTempMapKey) instanceof List) {
                List<JSONObject> valueList = (List) value;
                List<JSONObject> list = (List) tempValueMap.get(parentTempMapKey);
                list.stream().forEach(item -> {
                    valueList.stream().forEach(valueItem -> {
                        if (valueItem.get(AddUUIDJsonUtil.PUUID)
                            .equals(item.get(AddUUIDJsonUtil.UUID))) {
                            if (item.get(mapKey) == null) {
                                List<JSONObject> tempList = new ArrayList<JSONObject>();
                                tempList.add(valueItem);
                                item.put(mapKey, tempList);
                            } else {
                                List<JSONObject> tempList = (List<JSONObject>) item.get(mapKey);
                                tempList.add(valueItem);
                                item.put(mapKey, tempList);
                            }
                        }
                    });
                });

            }
        } else {
            targetValueMap.put(mapKey, value);
        }
        tempValueMap.put(tempMapKey, value);

    }

    /**
     * 处理数组叶子节点
     *
     * @param nodeTree
     * @param nodeTreeJson
     * @param targetValueMap
     * @param tempValueMap
     */
    public static void processArrayCatalogNode(NodeVo nodeTree, Map<String, Object> sourceMap,
        Map<String, Object> targetValueMap, Map<String, Object> tempValueMap) {
        List<JSONObject> list = new ArrayList();
        Object obj = sourceMap.get(nodeTree.getNamePath());
        obj = obj == null ? FlatteningJsonUtil.newInstance(null, "", "", "") : obj;
        NodeInfo nodeInfo = null;
        if (obj instanceof List) {
            ((List<NodeInfo>) obj).stream().forEach(item -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(AddUUIDJsonUtil.PUUID, item.getPuuid());
                jsonObject.put(AddUUIDJsonUtil.UUID, item.getUuid());
                list.add(jsonObject);
            });
        } else {
            nodeInfo = (NodeInfo) obj;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(AddUUIDJsonUtil.PUUID, nodeInfo.getPuuid());
            jsonObject.put(AddUUIDJsonUtil.UUID, nodeInfo.getUuid());
        }
        processNormalCatalogNode(nodeTree, sourceMap, targetValueMap, tempValueMap, list);

    }

    /**
     * @param nodeTree
     * @param nodeTreeJson
     * @param targetValueMap
     * @param tempValueMap
     */
    public static void processLeafNode(NodeVo nodeTree, Map<String, Object> sourceMap,
        Map<String, Object> targetValueMap, Map<String, Object> tempValueMap) {
        String parentTempMapKey = getParentTempMapKey(nodeTree.getNamePath());
        String tempMapKey = getTempMapKey(nodeTree.getNamePath());
        String mapKey = getMapKey(nodeTree.getNamePath());
        NodeInfo nodeInfo = null;
        if (tempValueMap.get(parentTempMapKey) instanceof List) {
            List<JSONObject> parentVal = (List<JSONObject>) tempValueMap.get(parentTempMapKey);
            if (!sourceMap.containsKey(nodeTree.getNamePath())) {
                //没有找到构建空值
                return;
            }
            Object obj = sourceMap.get(nodeTree.getNamePath());

            if (obj instanceof List) {
                parentVal.stream().forEach(pItem -> {
                    ((List<NodeInfo>) obj).stream().forEach(item -> {
                        if (item.getPuuid().equals(pItem.getString(AddUUIDJsonUtil.PUUID)) && item
                            .getUuid().equals(pItem.getString(AddUUIDJsonUtil.UUID))) {
                            //if(item.getPuuid().equals("3")){
                          //  System.out.println(mapKey + ":" + item.getValue() + ":" + item.getUuid());
                            //}
                            pItem.put(mapKey, item.getValue());
                        }
                    });
                });

            } else {

            }

        } else if (tempValueMap.get(parentTempMapKey) instanceof JSONObject) {

        } else if (tempValueMap.get(parentTempMapKey) instanceof Map) {
            Map<String, Object> parentMap = (Map<String, Object>) tempValueMap
                .get(parentTempMapKey);
            if (!sourceMap.containsKey(nodeTree.getNamePath())) {
                //没有找到构建空值
                parentMap.put(mapKey, "");
                return;
            }
            Object obj = sourceMap.get(nodeTree.getNamePath());
            nodeInfo = (NodeInfo) obj;
            parentMap.put(mapKey, nodeInfo.getValue());
        }

    }

    /**
     * @param nodeTree
     * @param nodeTreeJson
     * @param targetValueMap
     * @param tempValueMap
     */
    public static Map<String, Object> buildValueMap(NodeVo nodeTree, Map<String, Object> sourceMap,
        Map<String, Object> targetValueMap, Map<String, Object> tempValueMap) {
        Map<String, Object> valueMap = new HashMap();
        if (isBuildJsonObject(nodeTree)) {
            valueMap = buildJsonObjectMap(nodeTree, sourceMap, targetValueMap, tempValueMap);
        } else {
            valueMap = buildStringMap(nodeTree, sourceMap, targetValueMap, tempValueMap);
        }
        return valueMap;
    }

    /**
     * @param nodeTree
     * @param nodeTreeJson
     * @param targetValueMap
     * @param tempValueMap
     */
    public static Map<String, Object> buildStringMap(NodeVo nodeTree, Map<String, Object> sourceMap,
        Map<String, Object> targetValueMap, Map<String, Object> tempValueMap) {
        Map<String, Object> valueMap = new HashMap();
        return valueMap;

    }

    /**
     * @param nodeTree
     * @param nodeTreeJson
     * @param targetValueMap
     * @param tempValueMap
     */
    public static Map<String, Object> buildJsonObjectMap(NodeVo nodeTree,
        Map<String, Object> sourceMap, Map<String, Object> targetValueMap,
        Map<String, Object> tempValueMap) {
        Map<String, Object> valueMap = new HashMap();
        return valueMap;
    }

    /**
     * 判断是否需要构建，需要判断节点本身及下级节点，如果节点本身没有配置，但是下级节点有配置，这个节点也需要生成map对象
     *
     * @param node
     * @return
     */
    public static boolean isNeedBuild(NodeVo node) {
        return true;
    }

    /**
     * 是否构建List
     *
     * @param nodeTree
     * @return
     */
    public static boolean isBuildArray(NodeVo node) {
        if (node.getArray() && !node.getIsLeaf()) {
            return true;
        }
        return false;
    }

    /**
     * 是否构建普通map
     *
     * @param nodeTree
     * @return
     */
    public static boolean isBuildMap(NodeVo node) {
        if (!node.getArray()) {
            return true;
        }
        return false;
    }

    /**
     * 是否构建JsonObject对象
     *
     * @param nodeTree
     * @return
     */
    public static boolean isBuildJsonObject(NodeVo node) {
        return false;
    }

    /**
     * @param node
     * @return
     */
    public static String getMapKey(String nodePath) {
        String path = "";
        if (nodePath.lastIndexOf(Json2TreeUtil.SPLIT_CHAR) != -1) {
            path = nodePath
                .substring(nodePath.lastIndexOf(Json2TreeUtil.SPLIT_CHAR) + 1, nodePath.length());
        } else {
            path = nodePath;
        }
        return path;
    }

    /**
     * @param node
     * @return
     */
    public static String getTempMapKey(String nodePath) {
        return nodePath;
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
     * @param node
     * @return
     */
    public static String getJsonPath(String nodePath) {
        return "$." + nodePath;
    }

    public static void main(String[] args) {
    }

}
