package com.cmq.demo.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by chen.ming.qian on 2021/1/14.
 */
@Configuration
public class MyRedisConfig {

    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
    private ApplicationContext     applicationContext;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 连接工厂
        template.setConnectionFactory(factory);
        // 使用Jackson2JsonRedisSerializer
        Jackson2JsonRedisSerializer<Object> jacksonSeial = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        // 使用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        // 设置hash序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        return template;
    }
   @Bean
    public MyRedisListener registerListener() {
        RedisMessageListenerContainer redisMessageListenerContainer = applicationContext.
            getBean(RedisMessageListenerContainer.class);
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        MyRedisListener listener = new MyRedisListener();
        redisMessageListenerContainer
            .addMessageListener(listener, new PatternTopic("__keyevent@*__:expired"));
        return listener;
    }

}
