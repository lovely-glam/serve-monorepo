package com.lovelyglam.workerservice.config;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.lovelyglam.workerservice.model.BookingAppointment;

@Configuration
public class RedisConfig {
    @Bean
    RedisTemplate<String, Object> redisCommonTemplate (RedisConnectionFactory redisConnectionFactory) {
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
    @Bean
    RedisTemplate<Date, List<BookingAppointment>> redisBookingQueueTemplate  (RedisConnectionFactory redisConnectionFactory) {
        var template = new RedisTemplate<Date, List<BookingAppointment>>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
    
}
