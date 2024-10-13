package com.lovelyglam.chatsocketserver.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
    
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String send (@Payload String message) {
        return "demo";
    }
}
