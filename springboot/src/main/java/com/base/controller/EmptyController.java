package com.base.controller;

import com.base.bean.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 空操作的Controller基类.
 */
@Slf4j
public class EmptyController {
    @Autowired(required = false)

    /**
     * 未知异常.
     *
     * @param request http请求对象
     * @param ex      异常信息
     * @return ErrorResult
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult exp(HttpServletRequest request, Throwable ex) {
        log.warn(request.getRequestURI(), ex);
        ErrorResult resp = new ErrorResult();
        resp.setCode("500");
        resp.setType(1);
        String msg = ex.getMessage();
        if (msg != null && msg.length() > 200) {
            resp.setMessage("未知异常");
            resp.setStack(msg);
        } else {
            resp.setMessage(msg);
            resp.setStack(ExceptionUtils.getStackTrace(ex));
        }
        
        return resp;
    }

    /**
     * 没有找到数据异常.
     *
     * @param request http请求对象
     * @param ex      异常信息
     * @return ErrorResult
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResult exp(HttpServletRequest request, RuntimeException ex) {
        log.warn(request.getRequestURI(), ex);
        ErrorResult resp = new ErrorResult();
        resp.setCode("404");
        resp.setType(1);
        resp.setMessage(ex.getMessage());
        return resp;
    }


}
