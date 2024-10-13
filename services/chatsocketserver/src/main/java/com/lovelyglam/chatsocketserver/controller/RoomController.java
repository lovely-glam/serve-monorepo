package com.lovelyglam.chatsocketserver.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.chatsocketserver.service.ChatService;
import com.lovelyglam.database.model.dto.response.ResponseObject;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "rooms")
public class RoomController {
    private final ChatService chatService;
    @PostMapping("/{shopId}")
    public ResponseEntity<ResponseObject> initChatBox (@PathVariable BigDecimal shopId) {
        var result = chatService.createChatRoom(shopId);
        return ResponseEntity.ok(
            ResponseObject.builder()
            .code("Create Room Success")
            .content(result)
            .isSuccess(true)
            .message("Create Room Success")
            .status(HttpStatus.OK)
            .requestTime(LocalDateTime.now())
            .build()
        );
    }
}
