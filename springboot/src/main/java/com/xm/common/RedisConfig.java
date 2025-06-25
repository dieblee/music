package com.xm.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用 String 序列化键
        template.setKeySerializer(new StringRedisSerializer());

        // 使用 JSON 序列化值
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 启用事务支持
        template.setEnableTransactionSupport(true);

        // 初始化参数设置
        template.afterPropertiesSet();

        return template;
    }
}
