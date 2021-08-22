package com.polyroot.coinbot.beens;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.polyroot.coinbot.mapper.DepthMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

@Configuration
public class BeenConfiguration {

    @Bean(name = "webSocketClient")
    public WebSocketClient webSocketClient(){
        return new ReactorNettyWebSocketClient();
    }

    @Bean(name = "objectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Bean
    public DepthMapper depthMapper() {
        return Mappers.getMapper(DepthMapper.class);
    }

}
