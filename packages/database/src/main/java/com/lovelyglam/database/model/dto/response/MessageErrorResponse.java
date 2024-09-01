package com.lovelyglam.database.model.dto.response;

import org.springframework.http.HttpStatus;

public record MessageErrorResponse(String error, HttpStatus status, String ... args) {
    
}
