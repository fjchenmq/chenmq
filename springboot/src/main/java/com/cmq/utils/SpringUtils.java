package com.cmq.utils;

/**
 * Created by wangzy on 2016/7/11.
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext context; // 声明一个静态变量保存

    public void setApplicationContext(final ApplicationContext ctx) {
        context = ctx;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(final String beanId) {
        return (T) context.getBean(beanId);
    }

    public static <T> T getBean(final String beanId, Object... args) {
        return (T) context.getBean(beanId, args);
    }

    public static <T> Map<String,T> getBeans(final Class<T> clazz) {
        return context.getBeansOfType(clazz);

    }
    public static <T> T getBean(Class clazz) {
        return (T)context.getBean(clazz);
        //return (T) context.getBean(beanId, args);
    }

    public static void postEvent(ApplicationEvent event){
        context.publishEvent(event);
    }
}

