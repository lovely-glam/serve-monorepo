package com.lovelyglam.chatsocketserver.model.dto;

import java.time.LocalDateTime;

import com.lovelyglam.chatsocketserver.model.constant.MessageStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private String id;
    private String from;
    private String role;
    private String message;
    private LocalDateTime time;
    private MessageStatus status;
}
