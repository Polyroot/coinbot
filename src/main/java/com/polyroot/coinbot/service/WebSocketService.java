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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.polyroot.coinbot.service.Predicates.*;

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
    private ObjectMapper objectMapper;
    @Autowired
    private DtoMapper dtoMapper;
    @Autowired
    private MonoAdaptersManager monoManager;

    @Getter
    private final Function<Flux<String>, Flux<Object>> businessLogic =
            payload -> payload
                    .flatMap(this.payloadToJsonNode)
                    .flatMap(this.jsonNodeToDto)
                    .doOnNext(this.sendMarketSocketResponseDto)
                    .map(this.dtoToDocument)
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


    private final Consumer<Object> sendMarketSocketResponseDto = obj -> {
        if (obj instanceof MarketSocketResponseDto) {
            MarketSocketResponseDto marketSocketResponseDto = (MarketSocketResponseDto) obj;
            monoManager.getInput(marketSocketResponseDto.getId().toString()).accept(marketSocketResponseDto);
        }
    };

    private final Function<Object, Object> dtoToDocument = payloadAsDto -> {
        if (payloadAsDto instanceof MarketSocketResponseDto) {
            MarketSocketResponseDto marketSocketResponseDto = (MarketSocketResponseDto) payloadAsDto;
            return dtoMapper.marketSocketResponseDtoToMarketSocketResponse(marketSocketResponseDto);
        } else if (payloadAsDto instanceof DepthResponse) {
            DepthResponse depthResponse = (DepthResponse) payloadAsDto;
            return dtoMapper.depthResponseToDepth(depthResponse);
        } else if (payloadAsDto instanceof AggTradeResponse) {
            AggTradeResponse aggTradeResponse = (AggTradeResponse) payloadAsDto;
            return dtoMapper.aggTradeResponseToAggTrade(aggTradeResponse);
        } else if (payloadAsDto instanceof TradeResponse) {
            TradeResponse tradeResponse = (TradeResponse) payloadAsDto;
            return dtoMapper.tradeResponseToTrade(tradeResponse);
        } else if (payloadAsDto instanceof KLineResponse) {
            KLineResponse kLineResponse = (KLineResponse) payloadAsDto;
            return dtoMapper.kLineResponseToKLine(kLineResponse);
        } else if (payloadAsDto instanceof MiniTickerResponse) {
            MiniTickerResponse miniTickerResponse = (MiniTickerResponse) payloadAsDto;
            return dtoMapper.miniTickerResponseToMiniTicker(miniTickerResponse);
        } else if (payloadAsDto instanceof TickerResponse) {
            TickerResponse tickerResponse = (TickerResponse) payloadAsDto;
            return dtoMapper.tickerResponseToTicker(tickerResponse);
        } else if (payloadAsDto instanceof BookTickerResponse) {
            BookTickerResponse bookTickerResponse = (BookTickerResponse) payloadAsDto;
            return dtoMapper.bookTickerResponseToBookTicker(bookTickerResponse);
        } else if (payloadAsDto instanceof MarkPriceResponse) {
            MarkPriceResponse markPriceResponse = (MarkPriceResponse) payloadAsDto;
            return dtoMapper.markPriceResponseToMarkPrice(markPriceResponse);
        } else if (payloadAsDto instanceof ForceOrderResponse) {
            ForceOrderResponse forceOrderResponse = (ForceOrderResponse) payloadAsDto;
            return dtoMapper.forceOrderResponseToForceOrder(forceOrderResponse);
        }
        return payloadAsDto;
    };

    private final Consumer<Object> saveDocumentToDB = obj -> {
        if (obj instanceof MarketSocketResponse) {
            MarketSocketResponse marketSocketResponse = (MarketSocketResponse) obj;
            marketSocketResponseRepository.save(marketSocketResponse).subscribe();
        } else if (obj instanceof Depth) {
            Depth depth = (Depth) obj;
            depthRepository.save(depth).subscribe();
        } else if (obj instanceof AggTrade) {
            AggTrade aggTrade = (AggTrade) obj;
            aggTradeRepository.save(aggTrade).subscribe();
        } else if (obj instanceof Trade) {
            Trade trade = (Trade) obj;
            tradeRepository.save(trade).subscribe();
        } else if (obj instanceof KLine) {
            KLine kLine = (KLine) obj;
            kLineRepository.save(kLine).subscribe();
        } else if (obj instanceof MiniTicker) {
            MiniTicker miniTicker = (MiniTicker) obj;
            miniTickerRepository.save(miniTicker).subscribe();
        } else if (obj instanceof Ticker) {
            Ticker ticker = (Ticker) obj;
            tickerRepository.save(ticker).subscribe();
        } else if (obj instanceof BookTicker) {
            BookTicker bookTicker = (BookTicker) obj;
            bookTickerRepository.save(bookTicker).subscribe();
        } else if (obj instanceof MarkPrice) {
            MarkPrice markPrice = (MarkPrice) obj;
            markPriceRepository.save(markPrice).subscribe();
        } else if (obj instanceof ForceOrder) {
            ForceOrder forceOrder = (ForceOrder) obj;
            forceOrderRepository.save(forceOrder).subscribe();
        }

    };
}
