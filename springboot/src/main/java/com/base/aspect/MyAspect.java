package com.base.aspect;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2019/10/15.
 */
@Aspect
@Component
public class MyAspect {
    // @Pointcut("execution(* com.cmq.bean.*.get*(..))")
    //annotation只能切到方法上的注解 如果是属性切不到
    @Pointcut("@annotation(com.base.aspect.DataMasking) && execution(* com.cmq.bean.*.get*(..))")
    public void getMethod() {
    }

    @Before("getMethod()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        Object object = joinPoint.getTarget();
        System.out.println("pointcut : " + joinPoint.getSignature().getName());
        String propertyName = getPropertyName(joinPoint.getSignature().getName());
        Object value = PropertyUtils.getProperty(object,propertyName);
        PropertyUtils.setProperty(object, propertyName,"我被脱敏了");
        //接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
    }

    public String getPropertyName(String methodName) {
        String name = methodName.substring(3);
        return name.toLowerCase();
    }
}
