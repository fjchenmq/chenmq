package com.cmq.base;

import com.base.bean.ErrorResult;
import com.cmq.test.controller.TestController;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常统一处理
 */
@RestControllerAdvice(
    basePackages = "com.ztesoft.bss.oc.web.controller",
    annotations = {RestController.class, Controller.class})
public class EmptyController {
    private static Logger log = LoggerFactory.getLogger(TestController.class);
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
