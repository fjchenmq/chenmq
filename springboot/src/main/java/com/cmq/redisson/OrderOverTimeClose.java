package com.cmq.redisson;

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
        System.out.println(
            "取消订单,订单号：" + orderHandleInfo.getOrderNbr() + "时间差：" + (System.currentTimeMillis()
                - orderHandleInfo.getBeginTime()));
        // System.out.println("取消订单,订单号：" + orderHandleInfo.getOrderNbr());
    }
}
