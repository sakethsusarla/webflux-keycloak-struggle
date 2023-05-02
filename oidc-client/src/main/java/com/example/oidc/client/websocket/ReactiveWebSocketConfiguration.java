package com.example.oidc.client.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReactiveWebSocketConfiguration {
    private final ReactiveWebSocketHandler reactiveWebSocketHandler;

    @Autowired
    public ReactiveWebSocketConfiguration(@Qualifier("ReactiveWebSocketHandler") ReactiveWebSocketHandler reactiveWebSocketHandler) {
        this.reactiveWebSocketHandler = reactiveWebSocketHandler;
    }

    @Bean
    public HandlerMapping reactiveWebSocketHandlerMapping() {
        final Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("secured-websocket", reactiveWebSocketHandler);

        final SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);

        return handlerMapping;
    }
}
