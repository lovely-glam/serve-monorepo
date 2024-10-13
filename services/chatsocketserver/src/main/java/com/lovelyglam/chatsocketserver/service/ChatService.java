package com.lovelyglam.chatsocketserver.service;

import java.math.BigDecimal;

import com.lovelyglam.chatsocketserver.model.dto.ChatRoom;

public interface ChatService {
    ChatRoom createChatRoom (BigDecimal shopProfileId);
}
