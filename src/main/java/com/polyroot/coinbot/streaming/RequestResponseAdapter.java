package com.polyroot.coinbot.streaming;

import com.fasterxml.jackson.databind.JsonNode;
import com.polyroot.coinbot.model.ServerResp;
import com.polyroot.coinbot.model.dto.FluxRoutedEnvelope;
import com.polyroot.coinbot.model.dto.IncomingRequestEnvelope;
import com.polyroot.coinbot.model.dto.OutgoingResponseEnvelope;
import com.polyroot.coinbot.streaming.manage.FluxAdaptersManager;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
public class RequestResponseAdapter {

    @Autowired
    private FluxAdaptersManager<IncomingRequestEnvelope> sinkFluxRequest;

    @Autowired
    private FluxAdaptersManager<FluxRoutedEnvelope> sinkFluxRoutedEnvelope;

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

    public Consumer<FluxRoutedEnvelope> routeEnvelopeToFlux(){

        return envelope -> Mono.just(envelope)
                .doOnNext(e -> log.info("route to {}", e))
                .doOnNext(sinkFluxRoutedEnvelope.getSink(envelope.getRouteToAdapter()))
                .subscribe();
    }

    public Consumer<OutgoingResponseEnvelope> publishResponseToMono(){
        return responseEnvelope -> responseEnvelope.getResponse()
                .doOnNext(streamMonoResponse.getInput(responseEnvelope.getRqId()))
                .subscribe();
    }

    public Function<FluxRoutedEnvelope, Mono<OutgoingResponseEnvelope>> buildResponses() {

        return fluxRoutedEnvelope ->
                Mono.just(fluxRoutedEnvelope.getValue())
                        .map(body -> {
                            if (body instanceof Throwable) return OutgoingResponseEnvelope.builder()
                                    .rqId(fluxRoutedEnvelope.getKey())
                                    .response(ServerResponse
                                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                            .contentType(MediaType.TEXT_PLAIN)
                                            .body(Mono.just(((Throwable) body).getLocalizedMessage()), String.class))
                                    .build();

                            else return OutgoingResponseEnvelope.builder()
                                    .rqId(fluxRoutedEnvelope.getKey())
                                    .response(ServerResponse
                                            .status(HttpStatus.OK)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .body(Mono.just(ServerResp.SAVE_CJ_OK), ServerResp.class))
                                    .build();
                        })
                        .defaultIfEmpty(OutgoingResponseEnvelope.builder()
                                .rqId(fluxRoutedEnvelope.getKey())
                                .response(ServerResponse
                                        .status(HttpStatus.OK)
                                        .build())
                                .build());
    }

//    public Function<FluxRoutedEnvelope, Mono<OutgoingResponseEnvelope>> buildResponses() {
//
//        return fluxRoutedEnvelope ->
//                Mono.just(fluxRoutedEnvelope.getValue())
//                        .map(body -> {
//                            if (body instanceof Throwable) return OutgoingResponseEnvelope.builder()
//                                    .rqId(fluxRoutedEnvelope.getKey())
//                                    .response(ServerResponse
//                                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                            .contentType(MediaType.TEXT_PLAIN)
//                                            .body(Mono.just(((Throwable) body).getLocalizedMessage()), String.class))
//                                    .build();
//
//                            else return OutgoingResponseEnvelope.builder()
//                                    .rqId(fluxRoutedEnvelope.getKey())
//                                    .response(ServerResponse
//                                            .status(HttpStatus.OK)
//                                            .contentType(MediaType.APPLICATION_JSON)
//                                            .body(Mono.just(body), JsonNode.class))
//                                    .build();
//                        })
//                        .defaultIfEmpty(OutgoingResponseEnvelope.builder()
//                                .rqId(fluxRoutedEnvelope.getKey())
//                                .response(ServerResponse
//                                        .status(HttpStatus.OK)
//                                        .build())
//                                .build());
//    }
}
