package com.polyroot.coinbot.handler;

import com.polyroot.coinbot.model.dto.MarketSocketRequestDto;
import com.polyroot.coinbot.model.dto.MarketSocketResponseDto;
import com.polyroot.coinbot.service.StreamService;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class StreamHandlerIml {

    @Autowired
    private StreamService streamService;
    @Autowired
    private MonoAdaptersManager<MarketSocketResponseDto> monoManager;

    public Mono<ServerResponse> start(ServerRequest request) {

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return request.bodyToMono(MarketSocketRequestDto.class)
                .transform(streamService.getBusinessLogic())
                .flatMap(marketSocketRequestDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(getMarketSocketResponseDto(marketSocketRequestDto), MarketSocketResponseDto.class))
                .switchIfEmpty(notFound);

    }

    private Mono<MarketSocketResponseDto> getMarketSocketResponseDto(MarketSocketRequestDto marketSocketRequestDto) {
        return monoManager.getOutput(marketSocketRequestDto.getId().toString());
    }


}
