package com.polyroot.coinbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.polyroot.coinbot.mapper.DepthMapper;
import com.polyroot.coinbot.mapper.MarketSocketMapper;
import com.polyroot.coinbot.model.document.Depth;
import com.polyroot.coinbot.model.document.MarketSocketResponse;
import com.polyroot.coinbot.model.dto.DepthResponse;
import com.polyroot.coinbot.model.dto.MarketSocketRequestDto;
import com.polyroot.coinbot.model.dto.MarketSocketResponseDto;
import com.polyroot.coinbot.repository.DepthRepository;
import com.polyroot.coinbot.repository.MarketSocketRequestRepository;
import com.polyroot.coinbot.repository.MarketSocketResponseRepository;
import com.polyroot.coinbot.streaming.manage.FluxAdaptersManager;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.polyroot.coinbot.service.Predicates.isEvent;
import static com.polyroot.coinbot.service.Predicates.isResult;

@Component
@Slf4j
public class WebSocketService {

    @Autowired
    private DepthRepository depthRepository;
    @Autowired
    private MarketSocketRequestRepository marketSocketRequestRepository;
    @Autowired
    private MarketSocketResponseRepository marketSocketResponseRepository;
    @Autowired @Qualifier("webSocketClient")
    private WebSocketClient client;
    @Autowired @Qualifier("objectMapper")
    private ObjectMapper objectMapper;
    @Autowired
    private DepthMapper depthMapper;
    @Autowired
    private MarketSocketMapper marketMapper;
    @Autowired @Qualifier("MonoWireManager")
    private MonoAdaptersManager monoManager;

    @Value("${binance.endpoints.stream}")
    private String streamUrl;

    @Bean
    public void wsConnect(){
        client.execute(
                URI.create(streamUrl),
                getWebSocketHandler()
        ).subscribe();
    }

    @Autowired @Qualifier("FluxManager")
    private FluxAdaptersManager fluxAdaptersManager;

    private WebSocketHandler getWebSocketHandler() {

        Flux<MarketSocketRequestDto> marketSocketRequestDtoStream =
                fluxAdaptersManager.getStream("market-socket-request-dto");

        return session -> marketSocketRequestDtoStream
                .mapNotNull(marketSocketRequestDto -> marketSocketRequestDto.toString(objectMapper))
                .map(session::textMessage)
                .as(session::send)
                .and(publisherResponse(session))
                .then();
    }

    private Flux<Object> publisherResponse(WebSocketSession session) {
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(payloadToJsonNode)
                .mapNotNull(jsonNodeToDto)
                .doOnNext(sendMarketSocketResponseDto)
                .map(dtoToDocument)
                .doOnNext(saveDocumentToDB)
                .log();
    }

    private final Consumer<Object> sendMarketSocketResponseDto = obj -> {
        if (obj instanceof MarketSocketResponseDto) {
            MarketSocketResponseDto marketSocketResponseDto = (MarketSocketResponseDto) obj;
            monoManager.getInput(marketSocketResponseDto.getId().toString()).accept(marketSocketResponseDto);
        }
    };

    private final Consumer<Object> saveDocumentToDB = obj -> {
        if (obj instanceof Depth) {
            Depth depth = (Depth) obj;
            depthRepository.save(depth).subscribe();
        } else if (obj instanceof MarketSocketResponse) {
            MarketSocketResponse marketSocketResponse = (MarketSocketResponse) obj;
            marketSocketResponseRepository.save(marketSocketResponse).subscribe();
        }
    };

    private final Function<Object, Object> dtoToDocument = payloadAsDto -> {
        if (payloadAsDto instanceof DepthResponse) {
            DepthResponse depthResponse = (DepthResponse) payloadAsDto;
            return depthMapper.depthResponseToDepth(depthResponse);
        } else if (payloadAsDto instanceof MarketSocketResponseDto) {
            MarketSocketResponseDto marketSocketResponseDto = (MarketSocketResponseDto) payloadAsDto;
            return marketMapper.marketSocketResponseDtoToMarketSocketResponse(marketSocketResponseDto);
        }
        return payloadAsDto;
    };

    private final Function<String, Mono<JsonNode>> payloadToJsonNode = payloadAsText ->
         Mono.fromCallable(() -> objectMapper.readTree(payloadAsText))
                 .doOnError(e -> log.debug(e.getLocalizedMessage(), e))
                 .onErrorReturn(NullNode.getInstance());

    private final Function<JsonNode, Object> jsonNodeToDto = payloadAsJsonNode -> {
        try {
            if (isEvent.test(payloadAsJsonNode)) {
                return objectMapper.treeToValue(payloadAsJsonNode, DepthResponse.class);
            } else if (isResult.test(payloadAsJsonNode)) {
                return objectMapper.treeToValue(payloadAsJsonNode, MarketSocketResponseDto.class);
            }
        } catch (JsonProcessingException e) {
            log.debug(e.getLocalizedMessage(), e);
        }
        return null;
    };

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
                .map(marketMapper::marketSocketRequestDtoToMarketSocketRequest)
                .flatMap(marketSocketRequestRepository::save)
                .subscribe();
    }
}
