package com.cmq.demo.proxy;

import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2019/11/20.
 */
public class ProxyFactory {
    static Object jdcLogProxy;
    static Object cglibLogProxy;
    //绑定关系，也就是关联到哪个接口（与具体的实现类绑定）的哪些方法将被调用时，执行invoke方法。

    /**
     * jdk创建对象的速度远大于cglib，这是由于cglib创建对象时需要操作字节码。
     * cglib执行速度略大于jdk，所以比较适合单例模式。另外由于CGLIB的大部分类是直接对Java字节码进行操作，
     * 这样生成的类会在Java的永久堆中。如果动态代理操作过多，容易造成永久堆满，触发OutOfMemory异常。
     * spring默认使用jdk动态代理，如果类没有接口，则使用cglib。
     */
    public static Object newJdkLogProxyInstance(Object targetObject) {
        //该方法用于为指定类装载器、一组接口及调用处理器生成动态代理类实例
        //第一个参数指定产生代理对象的类加载器，需要将其指定为和目标对象同一个类加载器
        //第二个参数要实现和目标对象一样的接口，所以只需要拿到目标对象的实现接口
        //第三个参数表明这些被拦截的方法在被拦截时需要执行哪个InvocationHandler的invoke方法
        //根据传入的目标返回一个代理对象
        if (jdcLogProxy == null) {
            jdcLogProxy = Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(), new JdkLogProxy(targetObject));
        }
        return jdcLogProxy;
    }

    public  static  Object newCglibLogProxyInstance(Class<?> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new CglibLogProxy());
        if (cglibLogProxy == null) {
            cglibLogProxy = enhancer.create();
        }
        return cglibLogProxy;
    }
}
