package com.lovelyglam.database.model.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class AuthFailedException extends BaseException{

    public AuthFailedException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("AUTH-FAILED")
        .requestTime(LocalDateTime.now())
        .status(HttpStatus.UNAUTHORIZED)
        .content(null)
        .message(message)
        .isSuccess(false)
        .build();
    }
}
