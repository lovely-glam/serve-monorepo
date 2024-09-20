package com.lovelyglam.userserver.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.lovelyglam.database.model.entity")
@EnableJpaRepositories("com.lovelyglam.database.repository")
public class UserServiceConfig {
    
}
