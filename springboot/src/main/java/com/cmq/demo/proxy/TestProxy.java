package com.cmq.demo.proxy;

/**
 * Created by Administrator on 2019/11/20.
 */
public class TestProxy {
    public static void main(String[] args) {
        ProxyTestService testService = new ProxyTestServiceImpl();

        ProxyTestService jdkProxyTestService = (ProxyTestService) ProxyFactory
            .newJdkLogProxyInstance(testService);

        ProxyTestService cglibProxyTestService = (ProxyTestService) ProxyFactory
            .newCglibLogProxyInstance(ProxyTestServiceImpl.class);


        testService.printAge("chenmq");
        testService.printAge("33");

        System.out.println("---------jdk proxy invoke---------------");
        jdkProxyTestService.printAge("chenmq");
        jdkProxyTestService.printAge("33");

        System.out.println("---------cglib proxy invoke---------------");
        cglibProxyTestService.printAge("chenmq");
        cglibProxyTestService.printAge("33");
    }
}
