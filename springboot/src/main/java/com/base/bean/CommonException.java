package com.base.bean;

import com.base.sequence.SysSequenceHelper;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class CommonException extends RuntimeException implements Serializable {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory
        .getLogger(CommonException.class);

    private String failCode;

    private String failMsg = "GetDbdescribe";

    private String stackTraceInfo;

    private Date date;

    private String logModule;

    private String messageH;

    private boolean isThrowable = false;

    //无参数构造函数，防止BSSException序列化报错
    public CommonException() {

    }

    /**
     * 抛业务异常方法
     *
     * @param logModule 模块
     * @param failCode  异常编码
     * @param failMsg   异常信息
     * @param tranId    业务编码
     */
    public static void throwOut(String logModule, String failCode, String failMsg, String tranId) {
        LOGGER.error(failCode, failMsg);
        throw new CommonException(logModule, failCode, failMsg);
    }

    /**
     * 抛业务异常方法
     *
     * @param logModule 模块
     * @param failCode  异常编码
     * @param failMsg   异常信息
     */
    public static void throwOut(String logModule, String failCode, String failMsg) {
        throwOut(logModule, failCode, failMsg, "");
    }

    /**
     * 抛业务异常方法
     *
     * @param logModule
     * @param failCode
     * @param failMsg
     * @param e
     * @param tranId
     */
    public static void throwOut(String logModule, String failCode, String failMsg, Throwable e,
        String tranId) {
        LOGGER.error(failCode, failMsg);

        throw new CommonException(logModule, failCode, failMsg, e);

    }

    /**
     * 抛业务异常方法
     *
     * @param logModule
     * @param failCode
     * @param failMsg
     * @param messageH
     * @param e
     * @param tranId
     */
    public static void throwOut(String logModule, String failCode, String failMsg, String messageH,
        Throwable e, String tranId) {
        LOGGER.error(failCode, failMsg);
        throw new CommonException(logModule, failCode, failMsg, messageH, e);

    }

    /**
     * 抛业务异常方法
     *
     * @param logModule
     * @param failCode
     * @param failMsg
     * @param e
     */
    public static void throwOut(String logModule, String failCode, String failMsg, Throwable e) {
        throwOut(logModule, failCode, failMsg, e, "");

    }

    /**
     * 抛业务异常方法
     *
     * @param logModule
     * @param failCode
     * @param failMsg
     * @param messageH
     * @param e
     */
    public static void throwOut(String logModule, String failCode, String failMsg, String messageH,
        Throwable e) {
        throwOut(logModule, failCode, failMsg, messageH, e, "");

    }

    public boolean isThrowable() {
        return isThrowable;
    }

    public void setThrowable(boolean isThrowable) {
        this.isThrowable = isThrowable;
    }

    public CommonException(String logModule) {
        super();
        this.logModule = logModule;
        date = new Date(System.currentTimeMillis());
    }

    public CommonException(String logModule, String message, Throwable cause) {
        super(message, cause);
        this.logModule = logModule;
        date = new Date(System.currentTimeMillis());
    }

    public CommonException(String logModule, Throwable cause) {
        super(cause);
        this.logModule = logModule;
        date = new Date(System.currentTimeMillis());
    }

    public CommonException(String logModule, String failCode) {
        this.logModule = logModule;
        this.failCode = failCode;
        date = new Date(System.currentTimeMillis());
    }

    public CommonException(String logModule, String failCode, String failMsg) {
        super(failMsg);
        this.logModule = logModule;
        this.failCode = failCode;
        this.failMsg = failMsg;
        date = new Date(System.currentTimeMillis());
    }

    public CommonException(String logModule, String failCode, String failMsg,
        String stackTraceInfo) {
        super(failMsg);
        this.logModule = logModule;
        this.failCode = failCode;
        this.failMsg = failMsg;
        this.stackTraceInfo = stackTraceInfo;
        date = new Date(System.currentTimeMillis());
    }

    //把最底层的exception打出来，added by zsm
    public CommonException(String logModule, String failCode, String failMsg, Throwable cause) {
        super(failMsg, cause);
        this.logModule = logModule;
        this.failCode = failCode;
        this.failMsg = failMsg;
        this.stackTraceInfo = ExceptionUtils.getStackTrace(cause);
        date = new Date(System.currentTimeMillis());
    }

    public CommonException(String logModule, String failCode, String failMsg, String messageH,
        Throwable cause) {
        super(failMsg, cause);
        this.logModule = logModule;
        this.failCode = failCode;
        this.failMsg = failMsg;
        this.stackTraceInfo = ExceptionUtils.getStackTrace(cause);
        date = new Date(System.currentTimeMillis());
        this.messageH = messageH;
    }

    public String getFailCode() {
        return failCode;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public String getStackTraceInfo() {
        return stackTraceInfo;
    }

    public void setFailCode(String failCode) {
        this.failCode = failCode;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public void setStackTraceInfo(String stackTraceInfo) {
        this.stackTraceInfo = stackTraceInfo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLogModule() {
        return logModule;
    }

    public void setLogModule(String logModule) {
        this.logModule = logModule;
    }

    public String getMassageH() {
        return messageH;
    }

    public void setMassageH(String messageH) {
        this.messageH = messageH;
    }

}
