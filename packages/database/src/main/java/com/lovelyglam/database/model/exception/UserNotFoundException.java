package com.lovelyglam.database.model.exception;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class UserNotFoundException extends BaseException {

    protected UserNotFoundException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("USER-NOT-FOUND")
        .content(null)
        .message(message)
        .isSuccess(false)
        .build();
    }
}
