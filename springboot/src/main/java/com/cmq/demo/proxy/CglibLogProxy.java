package com.cmq.demo.proxy;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2019/11/20.
 */
public class CglibLogProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
        throws Throwable {
        System.out.println("add a log before by cglib proxy");

        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("add a log after by jdk proxy");
        return result;

    }
}
