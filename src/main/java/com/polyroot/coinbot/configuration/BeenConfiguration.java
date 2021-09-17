package com.polyroot.coinbot.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.polyroot.coinbot.mapper.DtoMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class BeenConfiguration {

    @Bean
    public WebSocketClient webSocketClient(){
        return new ReactorNettyWebSocketClient();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")))
                .registerModule(new JavaTimeModule());
    }

    @Bean
    public DtoMapper depthMapper() {
        return Mappers.getMapper(DtoMapper.class);
    }

}
