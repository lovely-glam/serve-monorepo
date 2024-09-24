package com.lovelyglam.database.model.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CustomerRegisterResponse (String username, String email, String fullName, LocalDateTime createdDate) {
    
}
