package com.lovelyglam.database.model.exception;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class ActionFailedException extends BaseException {

    public ActionFailedException(String message) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code("ACTION_FAILED")
        .message(message)
        .content(null)
        .isSuccess(false)
        .build();
    }

    public ActionFailedException(String message, String code) {
        super(message);
        this.errorResponse = ResponseObject.builder()
        .code(code)
        .content(null)
        .message(message)
        .isSuccess(false)
        .build();
    }
    
}
