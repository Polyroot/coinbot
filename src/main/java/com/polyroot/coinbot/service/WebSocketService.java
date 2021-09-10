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
import com.polyroot.coinbot.model.dto.MarketSocketResponseDto;
import com.polyroot.coinbot.repository.DepthRepository;
import com.polyroot.coinbot.repository.MarketSocketResponseRepository;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.polyroot.coinbot.service.Predicates.isEvent;
import static com.polyroot.coinbot.service.Predicates.isResult;

@Service
@Slf4j
public class WebSocketService {

    @Autowired
    private DepthRepository depthRepository;
    @Autowired
    private MarketSocketResponseRepository marketSocketResponseRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DepthMapper depthMapper;
    @Autowired
    private MarketSocketMapper marketMapper;
    @Autowired
    private MonoAdaptersManager monoManager;

    @Getter
    private final Function<Flux<String>, Flux<Object>> businessLogic =
            payload -> payload
                    .flatMap(this.payloadToJsonNode)
                    .mapNotNull(this.jsonNodeToDto)
                    .doOnNext(this.sendMarketSocketResponseDto)
                    .map(this.dtoToDocument)
                    .doOnNext(this.saveDocumentToDB)
                    .log();

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

    private final Consumer<Object> sendMarketSocketResponseDto = obj -> {
        if (obj instanceof MarketSocketResponseDto) {
            MarketSocketResponseDto marketSocketResponseDto = (MarketSocketResponseDto) obj;
            monoManager.getInput(marketSocketResponseDto.getId().toString()).accept(marketSocketResponseDto);
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

    private final Consumer<Object> saveDocumentToDB = obj -> {
        if (obj instanceof Depth) {
            Depth depth = (Depth) obj;
            depthRepository.save(depth).subscribe();
        } else if (obj instanceof MarketSocketResponse) {
            MarketSocketResponse marketSocketResponse = (MarketSocketResponse) obj;
            marketSocketResponseRepository.save(marketSocketResponse).subscribe();
        }
    };
}
