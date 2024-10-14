package com.lovelyglam.chatsocketserver.controller;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.lovelyglam.chatsocketserver.model.dto.ChatMessageDto;
import com.lovelyglam.chatsocketserver.service.ChatService;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/messages/{roomId}")
    public ChatMessageDto send (@Payload ChatMessageDto message, @DestinationVariable(value = "roomId") BigDecimal roomId ) {
        return chatService.storeMessage(message, roomId);
    }

    @MessageMapping("/chat-histories/{roomId}")
    public void getMessageHistory(@DestinationVariable(value = "roomId") BigDecimal roomId) {
        Collection<ChatMessageDto> message = chatService.getAllMessageFromRoomId(roomId);
        message.forEach(entity -> {
            simpMessagingTemplate.convertAndSend("/topic/messages" + roomId, message);
        });
    }
}
