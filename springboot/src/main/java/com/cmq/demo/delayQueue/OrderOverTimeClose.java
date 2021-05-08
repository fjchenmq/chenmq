package com.cmq.demo.delayQueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * [使用延时队列DelayQueue实现订单超时关闭]
 * [后台守护线程不断的执行检测工作]
 * [双检查模式实现单例模式]
 */
public class OrderOverTimeClose {
    public static AtomicInteger count = new AtomicInteger(0);

    /**
     * @param orderHandleInfo
     */
    public void cancelOrder(OrderHandleInfo orderHandleInfo) {
        if (count.incrementAndGet() >= 10000) {
            System.out.println("取消订单,订单号：" + orderHandleInfo.getOrderNbr());
        }
        // System.out.println("取消订单,订单号：" + orderHandleInfo.getOrderNbr());
    }
}
