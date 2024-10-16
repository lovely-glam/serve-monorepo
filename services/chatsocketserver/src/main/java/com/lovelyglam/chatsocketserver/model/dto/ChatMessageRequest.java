package com.lovelyglam.chatsocketserver.model.dto;

import com.lovelyglam.chatsocketserver.model.constant.MessageStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    private String role;
    private String message;
    private MessageStatus status;
}
