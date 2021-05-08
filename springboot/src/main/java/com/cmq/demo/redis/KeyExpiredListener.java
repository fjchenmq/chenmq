package com.cmq.demo.redis;

import redis.clients.jedis.JedisPubSub;

/**
 * Created by chen.ming.qian on 2021/2/2.
 * 监听器
 */

public class KeyExpiredListener extends JedisPubSub {
    public static int count = 0;

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        // System.out.println("onPSubscribe " + pattern + " " + subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        count++;
        System.out.println("onPMessage pattern " + pattern + " :" + channel + " message:" + message);
    }

}

