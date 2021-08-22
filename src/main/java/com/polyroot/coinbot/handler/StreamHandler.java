package com.polyroot.coinbot.handler;

import com.polyroot.coinbot.model.dto.WebSocketRequest;
import com.polyroot.coinbot.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StreamHandler {

    private final WebSocketService webSocketService;

    public Mono<ServerResponse> start(ServerRequest request) {

        return request.bodyToMono(WebSocketRequest.class)
                .doOnNext(webSocketRequest -> webSocketService.start(webSocketRequest))
                .then(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just("OK"), String.class));
    }
}
