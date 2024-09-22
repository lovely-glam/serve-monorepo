package com.lovelyglam.chatsocketserver.model.dto;

import java.time.LocalDateTime;

import com.lovelyglam.chatsocketserver.model.constant.MessageStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {
    private String senderName;
    private String recieveName;
    private String message;
    private LocalDateTime time;
    private MessageStatus status;
}
