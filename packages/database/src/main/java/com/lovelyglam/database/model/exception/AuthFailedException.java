package com.lovelyglam.database.model.exception;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class AuthFailedException extends BaseException{

    public AuthFailedException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("AUTH-FAILED")
        .content(null)
        .message(message)
        .isSuccess(false)
        .build();
    }
}
