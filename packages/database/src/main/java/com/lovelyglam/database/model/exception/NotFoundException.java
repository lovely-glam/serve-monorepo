package com.lovelyglam.database.model.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("NOT_FOUND")
        .message(message)
        .requestTime(LocalDateTime.now())
        .status(HttpStatus.OK)
        .isSuccess(false)
        .content(null)
        .build();
    }
    
}
