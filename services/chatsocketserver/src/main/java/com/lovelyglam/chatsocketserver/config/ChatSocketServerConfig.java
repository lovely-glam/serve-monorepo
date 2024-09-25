package com.lovelyglam.chatsocketserver.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.lovelygram.database.model.entity")
@EnableJpaRepositories(basePackages = "com.lovelygram.database.repository")
@ComponentScan(basePackages = {
    "com.lovelyglam.utils.config",
})
public class ChatSocketServerConfig {
    
}
