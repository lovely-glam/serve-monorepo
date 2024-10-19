package com.lovelyglam.chatsocketserver.model.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomResponse {
    private BigDecimal id;
    private String userAvatar;
    private String username;
}
