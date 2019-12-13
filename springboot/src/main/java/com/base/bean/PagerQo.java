package com.base.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * 分页查询条件 包含页数和排序方式字段，对排序方式字段做sql注入拦截
 */
@Data
@Slf4j
public class PagerQo implements Serializable {

    private static final long serialVersionUID = -6550827640887727324L;
    /**
     * 当前页，从1开始
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 排序字段
     */
    private String sortName;

    /**
     * 注入sql语句判断.
     */
    public static Pattern injectPattern = Pattern.compile("\\b(select|and|or|delete|drop|update|from)\\b|;", Pattern.CASE_INSENSITIVE);

    public void setSortName(String sortName){
        if(sortName==null){
            return;
        }
        if(injectPattern.matcher(sortName).find()){
            log.warn("可能是sql注入， sortName={}, 过滤掉这个排序内容",sortName);
            return;
        }
        this.sortName = sortName;
    }
    public String getSortName(){
        if(sortName==null){
            return null;
        }
        if(injectPattern.matcher(sortName).find()){
            log.warn("可能是sql注入， sortName={}, 过滤掉这个排序内容",sortName);
            return null;
        }
        return sortName;
    }
    /**
     * 排序方式，asc， desc
     */
    private String sortOrder;
    public void setSortOrder(String sortOrder){
        if(sortOrder==null){
            return ;
        }
        if(!( "desc".equalsIgnoreCase(sortOrder) || "asc".equalsIgnoreCase(sortOrder))){
            log.warn("排序方向字段值不正确， sortOrder={}, 忽略排序方向",sortOrder);
            return ;
        }
        this.sortOrder = sortOrder;
    }

    public String getSortOrder(){
        if(sortOrder==null){
            return null;
        }
        if(!("desc".equalsIgnoreCase(sortOrder) || "asc".equalsIgnoreCase(sortOrder))){
            log.warn("排序方向字段值不正确， sortOrder={}, 忽略排序方向",sortOrder);
            return null;
        }
        return sortOrder;
    }
}
