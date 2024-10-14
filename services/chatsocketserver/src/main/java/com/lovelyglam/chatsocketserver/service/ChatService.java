package com.lovelyglam.chatsocketserver.service;

import java.math.BigDecimal;
import java.util.Collection;

import com.lovelyglam.chatsocketserver.model.dto.ChatMessageDto;
import com.lovelyglam.chatsocketserver.model.dto.ChatRoom;

public interface ChatService {
    ChatRoom createChatRoom (BigDecimal shopProfileId);
    ChatMessageDto storeMessage (ChatMessageDto chatIncome, BigDecimal roomId);
    Collection<ChatMessageDto> getAllMessageFromRoomId (BigDecimal roomId);
}
