package com.lovelyglam.database.model.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class ActionFailedException extends BaseException {

    public ActionFailedException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("ACTION_FAILED")
        .message(message)
        .requestTime(LocalDateTime.now())
        .status(HttpStatus.OK)
        .content(null)
        .isSuccess(false)
        .build();
    }

    public ActionFailedException(String message, String code) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code(code)
        .requestTime(LocalDateTime.now())
        .status(HttpStatus.OK)
        .content(null)
        .message(message)
        .isSuccess(false)
        .build();
    }

    public ActionFailedException(String message, Throwable e) {
        super(e);
        e.printStackTrace();
        this.errorResponse = ResponseObject.builder()
        .code("ACTION_FAILED")
        .requestTime(LocalDateTime.now())
        .status(HttpStatus.OK)
        .content(null)
        .message(message)
        .isSuccess(false)
        .build();
    }

    public ActionFailedException(String message, Throwable e, String code) {
        super(e);
        e.printStackTrace();
        this.errorResponse = ResponseObject.builder()
        .code(code)
        .requestTime(LocalDateTime.now())
        .status(HttpStatus.OK)
        .content(null)
        .message(message)
        .isSuccess(false)
        .build();
    }
    
}
