package com.lovelyglam.database.repository;

import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends BaseRepository<ChatMessage, String> {
    
}
