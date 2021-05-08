package com.cmq.demo.delayQueue;

import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chen.ming.qian on 2021/1/20.
 */
public class OrderHandleQueueConsumer {
    private volatile static OrderHandleQueueConsumer orderHandleQueueConsumer = null;
    public static           ExecutorService          service                  = Executors
        .newFixedThreadPool(10);

    /**
     * 单例模式，双检查锁模式，在并发环境下对象只被初始化一次
     */
    public static OrderHandleQueueConsumer getInstance() {
        if (orderHandleQueueConsumer == null) {
            synchronized (OrderHandleQueueConsumer.class) {
                orderHandleQueueConsumer = new OrderHandleQueueConsumer();
                /**
                 * 消费线程
                 */
                Thread consumerThread;
                consumerThread = new Thread(() -> execute());
                consumerThread.setName("订单处理队列-->");
                consumerThread.start();

            }
        }
        return orderHandleQueueConsumer;
    }

    /**
     * 创建空延时队列
     */
    private static DelayQueue<OrderHandleInfo> handleQueue   = new DelayQueue<OrderHandleInfo>();
    private static Map<String, Method>         methodCache   = new HashMap();
    private static Map<String, Object>         instanceCache = new HashMap();

    /**
     * 读取延时队列，关闭超时订单
     */
    private static void execute() {
        boolean isStop = false;
        try {
            while (!isStop) {
                if (!handleQueue.isEmpty()) {
                    //从队列里获取超时的订单
                    OrderHandleInfo orderHandleInfo = handleQueue.take();
                    Runnable run = new Runnable() {
                        @Override
                        public void run() {
                            String handleClassPath = orderHandleInfo.getHandleClassPath();
                            String classPath = OrderHandleQueueConsumer
                                .getClassPath(handleClassPath);
                            String methodName = OrderHandleQueueConsumer
                                .getMethodName(handleClassPath);
                            try {
                                Method method = null;
                                Object instance = null;
                                if (methodCache.containsKey(handleClassPath)) {
                                    method = methodCache.get(handleClassPath);
                                    instance = instanceCache.get(handleClassPath);
                                } else {
                                    Class cls = Class.forName(classPath);
                                    method = cls.getMethod(methodName, OrderHandleInfo.class);
                                    methodCache.put(handleClassPath, method);
                                    instance = cls.newInstance();
                                    instanceCache.put(handleClassPath, instance);
                                }
                                method.invoke(instance, orderHandleInfo);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    service.execute(run);
                    Thread.sleep(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 获取类路径
     *
     * @param handleClassPath
     * @return
     */
    public static String getClassPath(String handleClassPath) {
        if (StringUtils.isEmpty(handleClassPath) || handleClassPath.indexOf(".") == -1) {
            return null;
        }
        return handleClassPath.substring(0, handleClassPath.lastIndexOf("."));

    }

    /**
     * 获取方法
     *
     * @param handleClassPath
     * @return
     */
    public static String getMethodName(String handleClassPath) {
        if (StringUtils.isEmpty(handleClassPath) || handleClassPath.indexOf(".") == -1) {
            return null;
        }
        return handleClassPath
            .substring(handleClassPath.lastIndexOf(".") + 1, handleClassPath.length());

    }

    /**
     * 插入订单到超时队列中
     */
    public void putHandleQueue(OrderHandleInfo orderHandleInfo) {
        handleQueue.add(orderHandleInfo);
    }
}
