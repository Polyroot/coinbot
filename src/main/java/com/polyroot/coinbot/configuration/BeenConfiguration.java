package com.polyroot.coinbot.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.polyroot.coinbot.mapper.DepthMapper;
import com.polyroot.coinbot.mapper.MarketSocketMapper;
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
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
    }

    @Bean
    public DepthMapper depthMapper() {
        return Mappers.getMapper(DepthMapper.class);
    }

    @Bean
    public MarketSocketMapper marketSocketMapper() {
        return Mappers.getMapper(MarketSocketMapper.class);
    }

}
