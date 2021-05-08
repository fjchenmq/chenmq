package com.cmq.demo.redis;

import com.sun.javafx.logging.PulseLogger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chen.ming.qian on 2021/1/14.
 */
@Component
public class MyRedisListener implements MessageListener {
    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    final public static String CANCEL_ORDER = "cancelOrder";
    final public static String ALARM_ORDER  = "alarmOrder";

    public static int count = 0;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        count++;
        String key = message.toString();
        String opType = key.substring(0, key.indexOf(":"));
        String orderId = key.substring(key.indexOf(":") + 1, key.length());
        switch (opType) {
            case CANCEL_ORDER:
                break;
            case ALARM_ORDER:
                break;
            default:
                ;
        }
        if (Integer.valueOf(orderId) > 995) {
            System.out.println("count:" + count + "订单告警...订单号=" + orderId);
        }
    }
}
