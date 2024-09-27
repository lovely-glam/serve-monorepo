package com.lovelyglam.database.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record BusinessRegisterResponse (
    String username, 
    String name, 
    String avatarUrl, 
    List<String> thumpNails, 
    String description, 
    String email, 
    String address , 
    LocalDateTime createdDate
) {
    
}
