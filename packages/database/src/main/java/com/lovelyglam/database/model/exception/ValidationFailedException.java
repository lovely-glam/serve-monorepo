package com.lovelyglam.database.model.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class ValidationFailedException extends BaseException {

    public ValidationFailedException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("VALIDATION_FAILED")
        .requestTime(LocalDateTime.now())
        .status(HttpStatus.OK)
        .message(message)
        .content(null)
        .isSuccess(false)
        .build();
    }
    
}
