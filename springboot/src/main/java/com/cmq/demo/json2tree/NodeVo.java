package com.cmq.demo.json2tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

/**
 * Created by Administrator on 2020/3/7.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeVo implements Serializable {
    private Long id;
    //节点名称
    private String  name;
    //节点全路径
    private String  namePath;
    private Long pId;
    //是否展开-界面展示用
    private Boolean open = false;
    //是否展开-界面展示用
    private String icon;
    //是否数组
    private Boolean      isArray  = false;
    private List<NodeVo> children = new ArrayList<>();
    //最后一个孩子的id
    @JsonIgnore
    private Long lastChildrenId;
}
