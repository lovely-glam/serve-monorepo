package com.lovelyglam.database.model.exception;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class ValidationFailedException extends BaseException {

    public ValidationFailedException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("VALIDATION_FAILED")
        .message(message)
        .content(null)
        .isSuccess(false)
        .build();
    }
    
}
