package com.lovelyglam.chatsocketserver.service.impl;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.stereotype.Service;

import com.lovelyglam.chatsocketserver.model.constant.MessageStatus;
import com.lovelyglam.chatsocketserver.model.dto.ChatMessageRequest;
import com.lovelyglam.chatsocketserver.model.dto.ChatMessageResponse;
import com.lovelyglam.chatsocketserver.model.dto.ChatRoom;
import com.lovelyglam.chatsocketserver.model.dto.ChatRoomResponse;
import com.lovelyglam.chatsocketserver.service.ChatService;
import com.lovelyglam.chatsocketserver.utils.AuthUtils;
import com.lovelyglam.database.model.entity.ChatBox;
import com.lovelyglam.database.model.entity.ChatMessage;
import com.lovelyglam.database.model.entity.ShopAccount;
import com.lovelyglam.database.model.exception.ActionFailedException;
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
                var user = authUtils.getUserAccountById(chatBox);
                var room = ChatBox.builder()
                .name("Room")
                .shopProfile(shopAccount.getShopProfile())
                .userAccount(user)
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

    public ChatMessageResponse storeMessage (ChatMessageRequest chatIncome, BigDecimal roomId) {
        try {
            var roomEntity = chatBoxRepository.findById(roomId).orElseThrow();
            var chatUser = authUtils.getUserAccountFromAuthentication();
            var chatMessageEntity = ChatMessage.builder()
                            .chatBox(roomEntity)
                            .from(chatUser.getUsername())
                            .role(chatUser.getRole())
                            .content(chatIncome.getMessage())
                            .build();
            var result = chatMessageRepository.save(chatMessageEntity);
            return ChatMessageResponse.builder()
            .from(result.getFrom())
            .status(MessageStatus.CHATTING)
            .message(result.getContent())
            .role(result.getRole())
            .id(result.getId())
            .time(result.getCreatedDate())
            .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ActionFailedException("Fail to save message", ex);
        }
    }

    @Override
    public Collection<ChatMessageResponse> getAllMessageFromRoomId(BigDecimal roomId) {
        var room = chatBoxRepository.findById(roomId).orElseThrow(() -> new NotFoundException("Not found room with this Id"));
        var user = authUtils.getUserAccountFromAuthentication();
        if (room.getShopProfile().getAccount().getId().equals(user.getId())  || room.getUserAccount().getId().equals(user.getId())) {
            return chatMessageRepository.findByRoomId(roomId).stream().map(entity -> {
                return ChatMessageResponse.builder()
                .id(entity.getId())
                .message(entity.getContent())
                .from(entity.getFrom())
                .role(entity.getRole())
                .time(entity.getCreatedDate())
                .build();
            }).toList();
        } else {
            throw new ValidationFailedException("This room is not your own");
        }
    }

    @Override
    public Collection<ChatRoomResponse> getAllRoomOfShop() {
        var userAuth = authUtils.getUserAccountFromAuthentication();
        if (userAuth.getRole().equals("ROLE_SHOP")) {
            return chatBoxRepository.findChatBoxByShopId(userAuth.getId()).stream().map(entity -> ChatRoomResponse.builder()
            .id(entity.getId())
            .userAvatar(entity.getUserAccount().getAvatarUrl())
            .username(entity.getUserAccount().getFullname())
            .build()).toList();
        } else {
            throw new ValidationFailedException("This method is only made for shop");
        }
    }
}
