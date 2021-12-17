package com.polyroot.coinbot.streaming;

import com.polyroot.coinbot.model.dto.IncomingRequestEnvelope;
import com.polyroot.coinbot.streaming.manage.FluxAdaptersManager;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class RequestResponseAdapter {

    @Autowired
    private FluxAdaptersManager<IncomingRequestEnvelope> sinkFluxRequest;

    @Autowired
    private MonoAdaptersManager<ServerResponse> streamMonoResponse;

    public Consumer<String> forwardRequestToFluxSink(ServerRequest request, String sinkName) {

        return requestId -> Mono.just(new IncomingRequestEnvelope(requestId, request))
                .doOnNext(sinkFluxRequest.getSink(sinkName))
                .subscribe();
    }

    public Function<String, Mono<ServerResponse>> getResponseFromMonoStream() {
        return responseId -> streamMonoResponse.getOutput(responseId);
    }

}
