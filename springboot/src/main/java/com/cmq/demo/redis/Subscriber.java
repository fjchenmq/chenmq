package com.cmq.demo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by chen.ming.qian on 2021/2/2.
 */
public class Subscriber {
    public static void main(String[] args) {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
        Jedis jedis = pool.getResource();
        jedis.psubscribe(new KeyExpiredListener(), "__key*__:*");

        jedis.set("notify", "新浪微博：小叶子一点也不逗");
        jedis.expire("notify", 10);


    }

}
