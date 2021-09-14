package com.polyroot.coinbot.service;

import com.polyroot.coinbot.mapper.DtoMapper;
import com.polyroot.coinbot.model.dto.MarketSocketRequestDto;
import com.polyroot.coinbot.repository.MarketSocketRequestRepository;
import com.polyroot.coinbot.streaming.manage.FluxAdaptersManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
public class StreamService {

    @Autowired
    private MarketSocketRequestRepository marketSocketRequestRepository;
    @Autowired
    private DtoMapper dtoMapper;
    @Autowired
    private FluxAdaptersManager fluxAdaptersManager;

    @Getter
    private final Function<Mono<MarketSocketRequestDto>, Mono<MarketSocketRequestDto>> businessLogic =
            marketSocketRequestDto -> {

                Consumer<MarketSocketRequestDto> marketSocketRequestDtoSink =
                        fluxAdaptersManager.getSink("market-socket-request-dto");

                return marketSocketRequestDto
                        .doOnNext(saveMarketSocketRequestToDb())
                        .doOnNext(marketSocketRequestDtoSink);
            };

    private Consumer<MarketSocketRequestDto> saveMarketSocketRequestToDb() {
        return marketSocketRequestDto -> Mono.just(marketSocketRequestDto)
                .map(dtoMapper::marketSocketRequestDtoToMarketSocketRequest)
                .flatMap(marketSocketRequestRepository::save)
                .subscribe();
    }
}
