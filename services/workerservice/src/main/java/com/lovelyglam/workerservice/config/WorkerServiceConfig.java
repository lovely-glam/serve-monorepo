package com.lovelyglam.workerservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.lovelyglam.database.model.entity")
@EnableJpaRepositories("com.lovelyglam.database.repository")
@ComponentScan(basePackages = {
    "com.lovelyglam.filecloud"
})
public class WorkerServiceConfig {
    
}
