package com.polyroot.coinbot.handler;

import com.polyroot.coinbot.model.dto.MarketSocketRequestDto;
import com.polyroot.coinbot.model.dto.MarketSocketResponseDto;
import com.polyroot.coinbot.service.WebSocketService;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class StreamHandler {


    @Autowired
    private WebSocketService webSocketService;
    @Autowired @Qualifier("MonoWireManager")
    private MonoAdaptersManager monoManager;


    public Mono<ServerResponse> start(ServerRequest request) {

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return request.bodyToMono(MarketSocketRequestDto.class)
                .doOnNext(webSocketService::saveMarketSocketRequestToDb)
                .doOnNext(webSocketService::wsConnect)
                .flatMap(marketSocketRequestDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(monoManager.getOutput(marketSocketRequestDto.getId().toString()), MarketSocketResponseDto.class))
                .switchIfEmpty(notFound);

    }


}
