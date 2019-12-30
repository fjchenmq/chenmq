package com.cmq.base;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/12/27.
 *  * 当遇到Page Not Found异常时，需要展示自定义页面或者回到首页
 */
@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {
    /**
     * 进行全局异常的过滤和处理
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
        HttpServletResponse response, Object handler, Exception ex) {

        ModelAndView modelAndView = new ModelAndView();
        //跳转到相应的处理页面
        //modelAndView.addObject("errorMsg", message);
        modelAndView.setViewName("sb/error");

        return modelAndView;
    }
}
