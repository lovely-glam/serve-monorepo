package com.lovelyglam.chatsocketserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.BaseException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.UserNotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class ApplicationExceptionController {
     @ExceptionHandler({
        UserNotFoundException.class,
        AuthFailedException.class,
        NotFoundException.class,
        ActionFailedException.class,
        ValidationFailedException.class
    })
    public ResponseEntity<ResponseObject> bachHoaSiCustomException(BaseException exception) {
        var responseError = exception.getErrorResponse();
        return ResponseEntity.status(HttpStatus.OK).body(responseError);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseObject> accessDeniedException(AccessDeniedException exception) {
        var responseError = ResponseObject.builder()
        .isSuccess(false)
        .message(exception.getMessage())
        .code("AUTH_FAILED")
        .content(null)
        .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseError);
    }
    @ExceptionHandler({
        InvalidKeyException.class,
        ExpiredJwtException.class,
        UnsupportedJwtException.class,
        SignatureException.class,
    })
    public ResponseEntity<ResponseObject> tokenFailedException(JwtException exception) {
        var responseError = ResponseObject.builder()
        .isSuccess(false)
        .message(exception.getMessage())
        .code("AUTH_FAILED")
        .content(null)
        .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseError);
    }
    @ExceptionHandler({
        UsernameNotFoundException.class,
        BadCredentialsException.class,
        LockedException.class,
        DisabledException.class,
        AccountStatusException.class,
        InsufficientAuthenticationException.class
    })
    public ResponseEntity<ResponseObject> authenticationFailedException(AuthenticationException exception) {
        var responseError = ResponseObject.builder()
        .isSuccess(false)
        .message(exception.getMessage())
        .code("AUTH_FAILED")
        .content(null)
        .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject> somethingWrongException(Exception ex) {
        var responseError = ResponseObject.builder()
        .isSuccess(false)
        .message(ex.getMessage())
        .code("SOMETHING_WRONG")
        .content(null)
        .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError);
    }
}
