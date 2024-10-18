package com.lovelyglam.database.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CustomerRegisterResponse (BigDecimal id, String username, String email, String fullName, boolean isActive, LocalDateTime createdDate) {
    
}
