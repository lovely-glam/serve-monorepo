package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends BaseRepository<ChatMessage, String> {
    @Query(value = "SELECT cm FROM chat_messages cm INNER JOIN cm.chatBox WHERE cm.chatBox.id = :roomId")
    Collection<ChatMessage> findByRoomId(@Param(value = "roomId") BigDecimal roomId);
}
