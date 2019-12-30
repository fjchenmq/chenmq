package com.cmq.base;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2019/12/27.
 * 当遇到Page Not Found异常时，需要展示自定义页面或者回到首页
 */
@Component
public class CustomErrorViewResolver implements ErrorViewResolver {
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest httpServletRequest,
        HttpStatus httpStatus, Map<String, Object> map) {
        ModelAndView modelAndView = new ModelAndView();
        //跳转到相应的处理页面
        //modelAndView.addObject("errorMsg", message);
        modelAndView.setViewName("sb/error");
        return modelAndView;
    }
}
