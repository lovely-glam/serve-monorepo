package com.lovelyglam.chatsocketserver.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.lovelyglam.chatsocketserver.model.dto.ChatRoom;
import com.lovelyglam.chatsocketserver.service.ChatService;
import com.lovelyglam.chatsocketserver.utils.AuthUtils;
import com.lovelyglam.database.model.entity.ChatBox;
import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.ChatBoxRepository;
import com.lovelyglam.database.repository.ChatMessageRepository;
import com.lovelyglam.database.repository.ShopAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatBoxRepository chatBoxRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ShopAccountRepository shopAccountRepository;
    private final AuthUtils authUtils;

    public ChatRoom createChatRoom (BigDecimal shopId) {
        ShopAccount shopAccount = shopAccountRepository.findById(shopId).orElseThrow(() -> new NotFoundException("Not found shop with this id"));
        var chatBox = authUtils.getUserAccountFromAuthentication();
        if (chatBox.getRole() == "ROLE_USER") {
            ChatRoom chatRoom = new ChatRoom();
            chatBoxRepository.findChatBoxByShopIdAndUserId(shopAccount.getShopProfile().getId(), chatBox.getId())
            .ifPresentOrElse((entity) -> {
                chatRoom.setId(entity.getId());
                chatRoom.setRoomName(entity.getName());
                chatRoom.setShopId(shopId);
                chatRoom.setUserId(chatBox.getId());
            }, () -> {
                var room = ChatBox.builder()
                .name("Room")
                .shopProfile(shopAccount.getShopProfile())
                .userAccount(null)
                .build();
                var result = chatBoxRepository.save(room);
                chatRoom.setId(result.getId());
                chatRoom.setRoomName(result.getName());
                chatRoom.setShopId(shopId);
                chatRoom.setUserId(result.getId());
            });
            ;
            return chatRoom;
        }else {
            throw new ValidationFailedException("Shop Can't Create Chat Room");
        }
    }
}
