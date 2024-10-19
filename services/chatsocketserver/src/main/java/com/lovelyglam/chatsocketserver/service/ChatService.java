package com.lovelyglam.chatsocketserver.service;

import java.math.BigDecimal;
import java.util.Collection;

import com.lovelyglam.chatsocketserver.model.dto.ChatMessageRequest;
import com.lovelyglam.chatsocketserver.model.dto.ChatMessageResponse;
import com.lovelyglam.chatsocketserver.model.dto.ChatRoom;
import com.lovelyglam.chatsocketserver.model.dto.ChatRoomResponse;

public interface ChatService {
    ChatRoom createChatRoom (BigDecimal shopProfileId);
    ChatMessageResponse storeMessage (ChatMessageRequest chatIncome, BigDecimal roomId);
    Collection<ChatMessageResponse> getAllMessageFromRoomId (BigDecimal roomId);
    Collection<ChatRoomResponse> getAllRoomOfShop();
}
