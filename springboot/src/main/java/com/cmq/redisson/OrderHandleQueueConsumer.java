package com.cmq.redisson;

import com.cmq.utils.SpringUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by chen.ming.qian on 2021/1/20.
 */
public class OrderHandleQueueConsumer {
    private volatile static OrderHandleQueueConsumer        orderHandleQueueConsumer = null;
    public static          ExecutorService                 service                  = Executors
        .newFixedThreadPool(30);
    public static          String                          QUEUE_NAME               = "orderHandleQueue";
    /**
     * 创建空延时队列
     */
    private static         Map<String, Method>             methodCache              = new HashMap();
    private static         Map<String, Object>             instanceCache            = new HashMap();
    private                RBlockingQueue<OrderHandleInfo> blockingQueue            = null;
    private                RDelayedQueue<OrderHandleInfo>  delayedQueue             = null;

    /**
     * 初始化消费者实例，单例模式，双检查锁模式，在并发环境下对象只被初始化一次
     */
    public static OrderHandleQueueConsumer getInstance() {
        if (orderHandleQueueConsumer == null) {
            RedissonClient redissonClient = SpringUtils.getBean(RedissonClient.class);
            synchronized (OrderHandleQueueConsumer.class) {
                orderHandleQueueConsumer = new OrderHandleQueueConsumer();
                orderHandleQueueConsumer.blockingQueue = redissonClient
                    .getBlockingQueue(QUEUE_NAME);
                orderHandleQueueConsumer.delayedQueue = redissonClient
                    .getDelayedQueue(orderHandleQueueConsumer.blockingQueue);
            }
        }
        return orderHandleQueueConsumer;
    }






    /**
     * 启动消费线程
     */
    public void startConsumerThread() {
        for (int i = 0; i < 1; i++) {
            /**
             * 消费线程
             */
            service.execute(new Runnable() {
                @Override
                public void run() {
                    execute();
                }
            });
        }
    }

    /**
     * 读取延时队列，进行订单处理
     */
    private void execute() {
        boolean isStop = false;
        try {
            while (!isStop) {
                if (!orderHandleQueueConsumer.blockingQueue.isEmpty()) {
                    //从队列里获取超时的订单,不能用pool pool是马上消费
                    OrderHandleInfo orderHandleInfo = orderHandleQueueConsumer.blockingQueue.take();
                    if (orderHandleInfo == null) {
                        return;
                    }
                    String handleClassPath = orderHandleInfo.getHandleClassPath();
                    String classPath = OrderHandleQueueConsumer.getClassPath(handleClassPath);
                    String methodName = OrderHandleQueueConsumer.getMethodName(handleClassPath);
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
                Thread.sleep(1000);
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
     * 添加到延时队列
     *
     * @param t        DTO传输类
     * @param delay    时间数量
     * @param timeUnit 时间单位
     * @param <T>      泛型
     */
    public void addQueue(OrderHandleInfo orderHandleInfo, long delay, TimeUnit timeUnit)
        throws Exception {
        // orderHandleQueueConsumer.blockingQueue.offer(orderHandleInfo, delay, timeUnit);
        orderHandleQueueConsumer.delayedQueue.offer(orderHandleInfo, delay, timeUnit);
        // 不要调用下面的方法,否者会导致消费不及时
        //delayedQueue.destroy();
    }

}
