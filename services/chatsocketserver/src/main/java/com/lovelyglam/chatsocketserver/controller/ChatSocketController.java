package com.lovelyglam.chatsocketserver.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import io.socket.engineio.server.EngineIoServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatSocketController implements HandshakeInterceptor , WebSocketHandler {
    private final EngineIoServer engineIoServer;

    @RequestMapping
    public void httpHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        engineIoServer.handleRequest(request, response);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afterConnectionEstablished'");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleMessage'");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleTransportError'");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afterConnectionClosed'");
    }

    @Override
    public boolean supportsPartialMessages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supportsPartialMessages'");
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'beforeHandshake'");
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            @Nullable Exception exception) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afterHandshake'");
    }
    
    
}
