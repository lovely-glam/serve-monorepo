package com.lovelyglam.chatsocketserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatSocketServerConfig implements WebSocketMessageBrokerConfigurer {
    
}
