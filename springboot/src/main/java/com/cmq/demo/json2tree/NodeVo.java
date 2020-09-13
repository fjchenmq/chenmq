package com.cmq.demo.json2tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/3/7.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeVo implements Serializable {
    private String id;
    //节点名称
    private String name;
    //节点全路径 即TARGET_NODE_PATH
    private String namePath;
    //对应的源报文节点路径即 NODE_CODE
    private String pId;
    //是否展开-界面展示用
    private Boolean open = false;
    //是否展开-界面展示用
    private String icon;
    //是否数组
    private Boolean      array    = false;
    //@JsonIgnore
    //是否是叶子
    private Boolean      isLeaf   = false;
    private List<NodeVo> children = new ArrayList<>();
    //最后一个孩子的id
    @JsonIgnore
    private String lastChildrenId;

    //是否处理过
    private Boolean isProcessed = false;

    /*
       private String sourceNodeName;
    //对应的源报文节点路径 即NODE_PATH
    private String sourceNodeNamePath;
    private String targetNodeRepeat;
    private String sourceNodeRepeat;
    //源报文节点全路径 即sourceNodeNamePath+sourceNodeName 中间用.号拼接
    private String sourceNodeFullPath;
    private String orderTemplateId;
    //构建map对象的节点对象的key
    private String targetNodeTableCode;
    //构建map对象的节点的key
    private String targetNodeTableFieldCode;*/

}
