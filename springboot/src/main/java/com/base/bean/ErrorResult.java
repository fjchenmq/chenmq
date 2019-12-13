package com.base.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 */
@Data
public class ErrorResult implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 错误类型.
     * type=0 业务异常，type=1系统异常.
     */
    private Integer type;
    /**
     * 错误代码.
     */
    private String code;
    /**
     * 错误描述信息.
     */
    private String message;
    /**
     * 错误模块编码.
     */
    private String logModule;
    /**
     * 详细信息.
     */
    private String stack;
    /**
     * 错误时间.
     */
    private Date date;
    
    /**
     * 错误提示小标题.
     */
    private String messageH;
}
