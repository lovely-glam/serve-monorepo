package com.lovelyglam.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    RedisTemplate<String, Object> redisTemplate (RedisConnectionFactory redisConnectionFactory) {
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
