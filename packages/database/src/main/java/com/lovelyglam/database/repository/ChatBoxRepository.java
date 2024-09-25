package com.lovelyglam.database.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.ChatBox;

@Repository
public interface ChatBoxRepository extends BaseRepository<ChatBox, BigDecimal> {
    
}
