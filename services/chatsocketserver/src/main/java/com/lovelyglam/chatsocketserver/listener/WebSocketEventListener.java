package com.lovelyglam.chatsocketserver.listener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.lovelyglam.chatsocketserver.model.constant.MessageStatus;
import com.lovelyglam.chatsocketserver.model.dto.ChatMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messTemplate;
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {

    }

    @EventListener
    public void handleWebSocketDisconnectListener (SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setStatus(MessageStatus.LEAVE);
            chatMessage.setSenderName(username);
            messTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
