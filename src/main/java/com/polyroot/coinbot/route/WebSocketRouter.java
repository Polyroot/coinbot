package com.polyroot.coinbot.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import java.net.URI;

@Configuration
public class WebSocketRouter {

    @Value("${binance.endpoints.stream}")
    private String streamUrl;

    @Autowired
    private WebSocketClient client;
    @Autowired
    private WebSocketHandler webSocketHandler;

    @Bean
    public void wsConnect(){
        client.execute(
                URI.create(streamUrl),
                webSocketHandler
        ).subscribe();
    }

}
