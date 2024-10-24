package com.lovelyglam.database.model.exception;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class BaseException extends RuntimeException {
    protected ResponseObject errorResponse;

    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(Throwable throwable) {
        super(throwable);
    }

    public ResponseObject getErrorResponse() {
        return errorResponse;
    }
}
