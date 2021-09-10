package com.polyroot.coinbot.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polyroot.coinbot.model.dto.MarketSocketRequestDto;
import com.polyroot.coinbot.service.WebSocketService;
import com.polyroot.coinbot.streaming.manage.FluxAdaptersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WebSocketHandlerImpl implements WebSocketHandler {

    @Autowired
    private FluxAdaptersManager fluxAdaptersManager;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebSocketService webSocketService;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {

        Flux<MarketSocketRequestDto> marketSocketRequestDtoStream =
                fluxAdaptersManager.getStream("market-socket-request-dto");

        return marketSocketRequestDtoStream
                .mapNotNull(marketSocketRequestDto -> marketSocketRequestDto.toString(objectMapper))
                .map(webSocketSession::textMessage)
                .as(webSocketSession::send)
                .and(publisherResponse(webSocketSession))
                .then();
    }

    private Flux<Object> publisherResponse(WebSocketSession session) {
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .transform(webSocketService.getBusinessLogic());
    }

}
