package com.lovelyglam.database.model.exception;

import com.lovelyglam.database.model.dto.response.ResponseObject;

public class BaseException extends RuntimeException {
    protected ResponseObject errObject;
}
