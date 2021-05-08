package com.cmq.demo.delayQueue;

/**
 * Created by chen.ming.qian on 2021/1/15.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模拟订单支付，创建订单
 */
public class OrderPay {

    static        String[]         str       = new String[] {"成功", "支付中", "订单初始化"};
    public static Long             orderId   = 1L;
    static        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTime(long time) {
        Date date = new Date(time);
        String currentTime = formatter.format(date);
        return currentTime;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100000; i++) {
            // 创建初始订单
            long createTime = System.currentTimeMillis();
            String currentTime = getTime(createTime);
            Long overTime = createTime + 1;// 1000*10 十秒后超时
            String orderNo = String.valueOf(new Random().nextLong());
            OrderHandleInfo order = new OrderHandleInfo();
            order.setOrderNbr(orderNo);
            order.setExpTime(overTime);
            int random_index = (int) (Math.random() * str.length);
            order.setHandleClassPath(str[random_index]);// 随机分配
            order.setBeginTime(new Date(createTime));
        }
    }

}
