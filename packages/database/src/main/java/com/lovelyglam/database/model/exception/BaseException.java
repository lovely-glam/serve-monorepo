package com.lovelyglam.database.model.exception;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class BaseException extends RuntimeException {
    protected ResponseObject errorResponse;

    protected BaseException(String message) {
        super(message);
    }

    public ResponseObject getErrorResponse() {
        return errorResponse;
    }
}
