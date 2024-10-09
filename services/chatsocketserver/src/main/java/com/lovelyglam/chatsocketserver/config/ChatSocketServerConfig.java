package com.lovelyglam.chatsocketserver.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EntityScan(basePackages = "com.lovelygram.database.model.entity")
@EnableJpaRepositories(basePackages = "com.lovelygram.database.repository")
@ComponentScan(basePackages = {
    "com.lovelyglam.utils.config.CorsConfig",
})
@EnableWebSocketMessageBroker
public class ChatSocketServerConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints (StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

}
