package com.lovelyglam.email.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class JavaMailInfoConfig {
    @Value("${mail.server.host}")
    private String mailHost;
    @Value("${mail.server.port}")
    private int mailPort;
    @Value("${mail.server.username}")
    private String mailUsername;
    @Value("${mail.server.password}")
    private String mailPassword;
}
