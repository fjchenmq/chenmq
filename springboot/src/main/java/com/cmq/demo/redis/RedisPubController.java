package com.cmq.demo.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by chen.ming.qian on 2021/2/2.
 * http://my:9082/redisPub/pub
 */

@RestController
@RequestMapping("/redisPub")
public class RedisPubController {
    private JedisPool pool = null;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "/sub", method = RequestMethod.GET)
    @ResponseBody
    public String sub() throws Exception {
        if (pool == null) {
            pool = new JedisPool(new JedisPoolConfig(), "localhost");
            Jedis jedis = pool.getResource();
            jedis.psubscribe(new KeyExpiredListener(), "__keyevent@*__:expired");
        }
        return "success";
    }

    @RequestMapping(value = "/pub", method = RequestMethod.GET)
    @ResponseBody
    public String pub() throws Exception {
        int count = MyRedisListener.count;
        for (int i = 0; i < 1000; i++) {

            redisTemplate.opsForValue().set("alarmOrder:" + i, i, 100, TimeUnit.MILLISECONDS);
        }
        return "success";
    }
}
