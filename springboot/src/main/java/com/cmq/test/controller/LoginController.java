package com.cmq.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/12/26.
 */
@Controller
@RequestMapping("")
public class LoginController {
    @RequestMapping(value = {"index", "/"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String index(HttpServletRequest request, HttpServletResponse response) {
        //想成功返回view 需要配置ViewResolver com.cmq.TestApplication.getViewResolver()
        return "/sb/index";
    }

    @RequestMapping(value = "myLogin", method = {RequestMethod.POST, RequestMethod.GET})
    public String login(HttpServletRequest request, HttpServletResponse response) {
        //想成功返回view 需要配置ViewResolver com.cmq.TestApplication.getViewResolver()
        return "/sb/main";
    }

    @RequestMapping(value = "timeout", method = {RequestMethod.POST, RequestMethod.GET})
    public String timeout(HttpServletRequest request, HttpServletResponse response) {
        //想成功返回view 需要配置ViewResolver com.cmq.TestApplication.getViewResolver()
        return "/sb/timeout";
    }

    @RequestMapping(value = "success", method = {RequestMethod.POST, RequestMethod.GET})
    public String success(HttpServletRequest request, HttpServletResponse response) {
        //想成功返回view 需要配置ViewResolver com.cmq.TestApplication.getViewResolver()
        return "/sb/success";
    }

    @RequestMapping(value = "myError", method = {RequestMethod.POST, RequestMethod.GET})
    public String myError(HttpServletRequest request, HttpServletResponse response) {
        //想成功返回view 需要配置ViewResolver com.cmq.TestApplication.getViewResolver()
        return "/sb/error";
    }
}
