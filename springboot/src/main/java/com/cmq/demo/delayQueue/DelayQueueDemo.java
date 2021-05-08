package com.cmq.demo.delayQueue;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.DelayQueue;

import static com.cmq.demo.delayQueue.OrderPay.getTime;
import static com.cmq.demo.delayQueue.OrderPay.str;
import static org.apache.catalina.security.SecurityUtil.remove;

/**
 * Created by chen.ming.qian on 2021/1/18.
 */
public class DelayQueueDemo {
    private static DelayQueue<OrderHandleInfo> queue = new DelayQueue<OrderHandleInfo>();

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
      /*  Long total =0L;
        for (int i = 0; i < 500000; i++) {
            long createTime = System.currentTimeMillis();

            Long overTime = createTime + 1000*10;// 十秒后超时
            String orderNo = String.valueOf(new Random().nextLong());
            long gcing = Runtime.getRuntime().freeMemory();

            OrderHandleInfo order = new OrderHandleInfo();
            order.setOrderNbr(orderNo);
            order.setExpTime(overTime);
            int random_index = (int) (Math.random() * str.length);
            order.setHandleClassPath(str[random_index]);// 随机分配
            order.setBeginTime(new Date(createTime));
            long gced = Runtime.getRuntime().freeMemory();
            total =total+(gcing-gced);
            queue.add(order);
        }
        System.out.println("占用内存："+(total)/1024/1024+"M");
        Long end =System.currentTimeMillis();
        System.out.println("Put耗时："+(end-start)/1000+"S");

        start = System.currentTimeMillis();
        while (!queue.isEmpty()){
            OrderHandleInfo orderHandleInfo = queue.take();
        }
        end =System.currentTimeMillis();
        System.out.println("Get耗时："+(end-start)/1000+"S");
*/
        OrderHandleInfo order = new OrderHandleInfo();
        order.setOrderNbr("1");
        order.setExpTime(11L);
        queue.add(order);

        OrderHandleInfo order1 = new OrderHandleInfo();
        order1.setOrderNbr("1");
        order1.setExpTime(11L);
        boolean rlt = queue.remove(order1);
        System.out.println(queue);
    }
}
