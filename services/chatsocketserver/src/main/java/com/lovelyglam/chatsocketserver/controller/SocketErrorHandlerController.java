package com.lovelyglam.chatsocketserver.controller;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

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
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SocketErrorHandlerController {
    private final SimpMessagingTemplate messagingTemplate; 
    @MessageExceptionHandler({ 
        UserNotFoundException.class, 
        AuthFailedException.class, 
        NotFoundException.class, 
        ActionFailedException.class, 
        ValidationFailedException.class 
    })
    public void handleBaseException(BaseException exception, Message<?> message) {
        sendErrorResponse(exception.getErrorResponse());
    }

    @MessageExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException exception, Message<?> message) {
        ResponseObject responseError = ResponseObject.builder()
            .isSuccess(false)
            .message(exception.getMessage())
            .code("AUTH_FAILED")
            .content(null)
            .build();
        sendErrorResponse(responseError);
    }

    @MessageExceptionHandler({
        InvalidKeyException.class,
        ExpiredJwtException.class,
        UnsupportedJwtException.class,
        SignatureException.class
    })
    public void handleJwtException(JwtException exception, Message<?> message) {
        ResponseObject responseError = ResponseObject.builder()
            .isSuccess(false)
            .message(exception.getMessage())
            .code("AUTH_FAILED")
            .content(null)
            .build();
        sendErrorResponse(responseError);
    }

    @MessageExceptionHandler({
        UsernameNotFoundException.class,
        BadCredentialsException.class,
        LockedException.class,
        DisabledException.class,
        AccountStatusException.class,
        InsufficientAuthenticationException.class
    })
    public void handleAuthenticationException(AuthenticationException exception, Message<?> message) {
        ResponseObject responseError = ResponseObject.builder()
            .isSuccess(false)
            .message(exception.getMessage())
            .code("AUTH_FAILED")
            .content(null)
            .build();
        sendErrorResponse(responseError);
    }

    @MessageExceptionHandler(Exception.class)
    public void handleGenericException(Exception ex, Message<?> message) {
        ResponseObject responseError = ResponseObject.builder()
            .isSuccess(false)
            .message(ex.getMessage())
            .code("SOMETHING_WRONG")
            .content(null)
            .build();
        sendErrorResponse(responseError);
    }

    private void sendErrorResponse(ResponseObject responseError) {
        messagingTemplate.convertAndSend("/topic/errors", responseError);
    }
}
