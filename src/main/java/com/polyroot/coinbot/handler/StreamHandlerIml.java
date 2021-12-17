package com.polyroot.coinbot.handler;

import com.polyroot.coinbot.streaming.RequestResponseAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class StreamHandlerIml {

    @Autowired
    private RequestResponseAdapter adapterRequestResponse;

    public Mono<ServerResponse> test(ServerRequest request) {

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return Mono.fromCallable(() -> UUID.randomUUID().toString())
                .doOnNext(adapterRequestResponse.forwardRequestToFluxSink(request, "test"))
                .flatMap(rqId -> adapterRequestResponse.getResponseFromMonoStream(rqId))
                .switchIfEmpty(notFound);

    }

}
