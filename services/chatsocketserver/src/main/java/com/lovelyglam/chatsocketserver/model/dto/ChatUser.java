package com.lovelyglam.chatsocketserver.model.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatUser {
    private BigDecimal id;
    private String username;
    private String role;
}
