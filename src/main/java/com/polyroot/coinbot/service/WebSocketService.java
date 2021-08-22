package com.polyroot.coinbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polyroot.coinbot.mapper.DepthMapper;
import com.polyroot.coinbot.model.document.Depth;
import com.polyroot.coinbot.model.dto.DepthResponse;
import com.polyroot.coinbot.model.dto.WebSocketRequest;
import com.polyroot.coinbot.model.dto.WebSocketResponse;
import com.polyroot.coinbot.repository.DepthRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
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
    private WebSocketClient client;
    @Autowired @Qualifier("objectMapper")
    private ObjectMapper objectMapper;
    @Autowired
    private DepthMapper depthMapper;

    public void start(WebSocketRequest webSocketRequest) {

        client.execute(URI.create("wss://stream.binance.com:9443/ws"),
                       session -> session.send(createRequest(webSocketRequest, session))
                               .thenMany(publisherResponse(session))
                               .then())
                .subscribe();

    }

    private Mono<WebSocketMessage> createRequest(WebSocketRequest webSocketRequest,
                                                 WebSocketSession session) {
        WebSocketMessage webSocketMessage = session.textMessage(webSocketRequest.toString());
        return Mono.just(webSocketMessage);
    }

    private Flux<Object> publisherResponse(WebSocketSession session) {
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(payloadToJsonNode)
                .map(jsonNodeToDto)
                .map(dtoToDocument)
                .doOnNext(saveToDB)
                .log();
    }

    private Consumer<Object> saveToDB = obj -> {
        if (obj instanceof Depth) {
            Depth depth = (Depth) obj;
            depthRepository.save(depth).subscribe();
        }
    };

    private final Function<Object, Object> dtoToDocument = payloadAsDto -> {
        if (payloadAsDto instanceof DepthResponse) {
            DepthResponse depthResponse = (DepthResponse) payloadAsDto;
            return depthMapper.depthResponseToDepth(depthResponse);
        }
        return payloadAsDto;
    };

    private final Function<String, JsonNode> payloadToJsonNode = payloadAsText -> {
        try {
            return objectMapper.readTree(payloadAsText);
        } catch (JsonProcessingException e) {
            log.debug(e.getLocalizedMessage(), e);
        }
        return null;
    };

    private final Function<JsonNode, Object> jsonNodeToDto = payloadAsJsonNode -> {
        try {
            if (isEvent.test(payloadAsJsonNode)) {
                return objectMapper.treeToValue(payloadAsJsonNode, DepthResponse.class);
            } else if (isResult.test(payloadAsJsonNode)) {
                return objectMapper.treeToValue(payloadAsJsonNode, WebSocketResponse.class);
            }
        } catch (JsonProcessingException e) {
            log.debug(e.getLocalizedMessage(), e);
        }
        return null;
    };

}
