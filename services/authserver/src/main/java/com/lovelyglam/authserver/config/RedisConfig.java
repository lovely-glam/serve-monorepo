package com.lovelyglam.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    RedisTemplate<Long, Object> redisTemplate (RedisConnectionFactory redisConnectionFactory) {
        var template = new RedisTemplate<Long, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
