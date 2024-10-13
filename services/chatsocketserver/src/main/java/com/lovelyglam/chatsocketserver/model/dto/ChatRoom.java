package com.lovelyglam.chatsocketserver.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    private BigDecimal id;
    private BigDecimal shopId;
    private BigDecimal userId;
    private String roomName;
}
