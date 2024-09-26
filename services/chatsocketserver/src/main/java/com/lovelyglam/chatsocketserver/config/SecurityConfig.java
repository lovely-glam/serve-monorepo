package com.lovelyglam.chatsocketserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;

@Configuration
@EnableWebSecurity
@EnableWebSocketSecurity
public class SecurityConfig {
}
