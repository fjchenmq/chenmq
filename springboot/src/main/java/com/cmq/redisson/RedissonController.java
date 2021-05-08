package com.cmq.redisson;

import com.cmq.demo.redis.KeyExpiredListener;
import com.cmq.demo.redis.MyRedisListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.cmq.demo.delayQueue.OrderPay.getTime;

/**
 * Created by chen.ming.qian on 2021/2/2.
 * http://my:9082/redisson/put
 */

@RestController
@RequestMapping("/redisson")
public class RedissonController {
    @Resource
    private       RedisTemplate<String, Object> redisTemplate;
    public static OrderHandleQueueConsumer      orderHandleQueueConsumer;

    @RequestMapping(value = "/put", method = RequestMethod.GET)
    @ResponseBody
    public String put() throws Exception {
        String str = "Success";
        long createTime = System.currentTimeMillis();
        Long overTime = createTime + 2;// 1000*10 十秒后超时
        if (orderHandleQueueConsumer == null) {
            orderHandleQueueConsumer = OrderHandleQueueConsumer.getInstance();
        }
        orderHandleQueueConsumer.startConsumerThread();
        for (int i = 0; i < 10; i++) {
            String orderNo = String.valueOf(new Random().nextLong());
            OrderHandleInfo order = new OrderHandleInfo();
            order.setOrderNbr(orderNo);
            order.setBeginTime(createTime);
            order.setHandleClassPath("com.cmq.redisson.OrderOverTimeClose.cancelOrder");
            try {
                orderHandleQueueConsumer
                    .addQueue(order, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /*for (int i = 0; i < 10; i++) {
            *//**
         * 消费线程
         *//*
            orderHandleQueueConsumer.service.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 50; i++) {
                        String orderNo = String.valueOf(new Random().nextLong());
                        OrderHandleInfo order = new OrderHandleInfo();
                        order.setOrderNbr(orderNo);
                        order.setCreateTime(createTime);
                        order.setHandleClassPath("com.cmq.redisson.OrderOverTimeClose.cancelOrder");
                        try {
                            orderHandleQueueConsumer.addQueue(order, 5 * 1000, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }*/

        return "success";
    }

    @RequestMapping(value = "/consume", method = RequestMethod.GET)
    @ResponseBody
    public String consume() throws Exception {
        String str = "Success";
        if (orderHandleQueueConsumer == null) {
            orderHandleQueueConsumer = OrderHandleQueueConsumer.getInstance();
        }
        orderHandleQueueConsumer.startConsumerThread();
        return "success";
    }
}
