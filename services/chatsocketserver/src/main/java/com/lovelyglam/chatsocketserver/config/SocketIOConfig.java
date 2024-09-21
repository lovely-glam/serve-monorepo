package com.lovelyglam.chatsocketserver.config;

import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoServerOptions;
import io.socket.engineio.server.utils.JsonUtils;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;

@Configuration
public class SocketIOConfig {
    @Bean
    EngineIoServer engineIoServe  () {
        var engine = EngineIoServerOptions.newFromDefault()
            .setCorsHandlingDisabled(true);
        return new EngineIoServer(engine);
    }

    @Bean
    SocketIoServer socketIOServer(EngineIoServer engineSocketServer) {
        var socketServer = new SocketIoServer(engineSocketServer);
        var namespace = socketServer.namespace("/glam-chat");
        namespace.on("connection", args -> {
            var socket = (SocketIoSocket) args[0];
            socket.on("message", messsageListener -> {
                JSONObject j = (JSONObject) messsageListener[0];
            });
        });
        return socketServer;
    }
}
