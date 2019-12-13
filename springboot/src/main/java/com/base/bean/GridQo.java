package com.base.bean;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 表格查询条件
 */
@Data
public class GridQo extends  PagerQo implements Serializable{
    private static final long serialVersionUID = 6154607167117085659L;
    private Map<String,Object> anyProperties;
    public static GridQo newInstance() {
        GridQo fragment = new GridQo();
        return fragment;
    }
    /**
     * 过滤字段，需要对多个字段过来可以使用逗号分隔
     */
    private String filterCol;
    /**
     * 过滤值
     */
    private String filterVal;

    /**
     * 设置多余的参数信息
     * @param property
     * @param value
     */
    @JsonAnySetter
    public void anySetter(String property, Object value){
        if(anyProperties==null){
            anyProperties = new HashMap<String, Object>();
        }
        anyProperties.put(property, value);
    }
}
