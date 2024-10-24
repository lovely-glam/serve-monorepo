package com.lovelyglam.database.model.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class UserNotFoundException extends BaseException {

    protected UserNotFoundException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("USER-NOT-FOUND")
        .content(null)
        .requestTime(LocalDateTime.now())
        .status(HttpStatus.OK)
        .message(message)
        .isSuccess(false)
        .build();
    }
}
