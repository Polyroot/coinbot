package com.polyroot.coinbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.polyroot.coinbot.mapper.DtoMapper;
import com.polyroot.coinbot.model.EventType;
import com.polyroot.coinbot.model.document.*;
import com.polyroot.coinbot.model.dto.*;
import com.polyroot.coinbot.repository.*;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.polyroot.coinbot.service.predicate.WebSocketPredicates.*;

@Service
@Slf4j
public class WebSocketService {

    @Autowired
    private DepthRepository depthRepository;
    @Autowired
    private AggTradeRepository aggTradeRepository;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private KLineRepository kLineRepository;
    @Autowired
    private MiniTickerRepository miniTickerRepository;
    @Autowired
    private TickerRepository tickerRepository;
    @Autowired
    private BookTickerRepository bookTickerRepository;
    @Autowired
    private MarkPriceRepository markPriceRepository;
    @Autowired
    private ForceOrderRepository forceOrderRepository;
    @Autowired
    private MarketSocketResponseRepository marketSocketResponseRepository;

    @Autowired
    private DepthServiceImpl depthService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DtoMapper dtoMapper;
    @Autowired
    private MonoAdaptersManager<MarketSocketResponseDto> monoManager;

    @Getter
    private final Function<Flux<String>, Flux<Object>> businessLogic =
            payload -> payload
                    .flatMap(this.payloadToJsonNode)
                    .flatMap(this.jsonNodeToDto)
                    .flatMap(this.addBinanceRule)
                    .doOnNext(this.sendMarketSocketResponseDto)
                    .flatMap(this.dtoToDocument)
                    .doOnNext(this.saveDocumentToDB)
                    .log();

    private final Function<String, Mono<JsonNode>> payloadToJsonNode = payloadAsText ->
         Mono.fromCallable(() -> objectMapper.readTree(payloadAsText))
                 .doOnError(e -> log.debug(e.getLocalizedMessage(), e))
                 .onErrorReturn(NullNode.getInstance());

    private final Function<JsonNode, Flux<Object>> jsonNodeToDto = payloadAsJsonNode -> {

        if (isEvent.test(payloadAsJsonNode)) {

            return Flux.fromArray(EventType.values())
                    .filter(eventTypePredicate(payloadAsJsonNode))
                    .flatMap(eventType -> getMappedDto(payloadAsJsonNode, eventType.getAClass()));

        } else if (isResult.test(payloadAsJsonNode)) {
            return Flux.from(getMappedDto(payloadAsJsonNode, MarketSocketResponseDto.class));
        }

        return Flux.empty();
    };

    private Mono<Object> getMappedDto(JsonNode payloadAsJsonNode, Class aClass) {
        return Mono.fromCallable(() -> objectMapper.treeToValue(payloadAsJsonNode, aClass))
                .doOnError(e -> log.debug(e.getLocalizedMessage(), e))
                .onErrorReturn("error mapping payloadAsJsonNode to dto");
    }

    private final Function<? super Object, ? extends Publisher<?>> addBinanceRule = payloadAsDto -> {

        Mono<Object> dto = Mono.just(payloadAsDto);

        if (payloadAsDto instanceof DepthResponse) return dto
                .cast(DepthResponse.class)
                .doOnNext(depthService::orderBookCorrectlyRule8);

        return dto;
    };

    private final Consumer<Object> sendMarketSocketResponseDto = obj -> {
        if (obj instanceof MarketSocketResponseDto marketSocketResponseDto)
            monoManager.getInput(marketSocketResponseDto.getId().toString())
                    .accept(marketSocketResponseDto);
    };

    private final Function<Object, Mono<Object>> dtoToDocument = payloadAsDto -> {

        Mono<Object> dto = Mono.just(payloadAsDto);

        if (payloadAsDto instanceof MarketSocketResponseDto) return dto
                .cast(MarketSocketResponseDto.class)
                .map(dtoMapper::marketSocketResponseDtoToMarketSocketResponse);
        else if (payloadAsDto instanceof DepthResponse) return dto
                .cast(DepthResponse.class)
                .map(dtoMapper::depthResponseToDepth);
        else if (payloadAsDto instanceof AggTradeResponse) return dto
                .cast(AggTradeResponse.class)
                .map(dtoMapper::aggTradeResponseToAggTrade);
        else if (payloadAsDto instanceof TradeResponse) return dto
                .cast(TradeResponse.class)
                .map(dtoMapper::tradeResponseToTrade);
        else if (payloadAsDto instanceof KLineResponse) return dto
                .cast(KLineResponse.class)
                .map(dtoMapper::kLineResponseToKLine);
        else if (payloadAsDto instanceof MiniTickerResponse) return dto
                .cast(MiniTickerResponse.class)
                .map(dtoMapper::miniTickerResponseToMiniTicker);
        else if (payloadAsDto instanceof TickerResponse) return dto
                .cast(TickerResponse.class)
                .map(dtoMapper::tickerResponseToTicker);
        else if (payloadAsDto instanceof BookTickerResponse) return dto
                .cast(BookTickerResponse.class)
                .map(dtoMapper::bookTickerResponseToBookTicker);
        else if (payloadAsDto instanceof MarkPriceResponse) return dto
                .cast(MarkPriceResponse.class)
                .map(dtoMapper::markPriceResponseToMarkPrice);
        else if (payloadAsDto instanceof ForceOrderResponse) return dto
                .cast(ForceOrderResponse.class)
                .map(dtoMapper::forceOrderResponseToForceOrder);

        return dto;
    };

    private final Consumer<Object> saveDocumentToDB = obj -> {

        Mono<Object> document = Mono.just(obj);

        if (obj instanceof MarketSocketResponse) document
                .cast(MarketSocketResponse.class)
                .flatMap(marketSocketResponseRepository::save)
                .subscribe();
        else if (obj instanceof Depth) document
                .cast(Depth.class)
                .flatMap(depthRepository::save)
                .subscribe();
        else if (obj instanceof AggTrade) document
                .cast(AggTrade.class)
                .flatMap(aggTradeRepository::save)
                .subscribe();
        else if (obj instanceof Trade) document
                .cast(Trade.class)
                .flatMap(tradeRepository::save)
                .subscribe();
        else if (obj instanceof KLine) document
                .cast(KLine.class)
                .flatMap(kLineRepository::save)
                .subscribe();
        else if (obj instanceof MiniTicker) document
                .cast(MiniTicker.class)
                .flatMap(miniTickerRepository::save)
                .subscribe();
        else if (obj instanceof Ticker) document
                .cast(Ticker.class)
                .flatMap(tickerRepository::save)
                .subscribe();
        else if (obj instanceof BookTicker) document
                .cast(BookTicker.class)
                .flatMap(bookTickerRepository::save)
                .subscribe();
        else if (obj instanceof MarkPrice) document
                .cast(MarkPrice.class)
                .flatMap(markPriceRepository::save)
                .subscribe();
        else if (obj instanceof ForceOrder) document
                .cast(ForceOrder.class)
                .flatMap(forceOrderRepository::save)
                .subscribe();

    };
}
