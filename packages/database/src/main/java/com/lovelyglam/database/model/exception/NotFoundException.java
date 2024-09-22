package com.lovelyglam.database.model.exception;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("NOT_FOUND")
        .message(message)
        .isSuccess(false)
        .content(null)
        .build();
    }
    
}
